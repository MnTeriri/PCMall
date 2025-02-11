package com.example.pcmallproviderorder.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pcmallcommon.client.AddressClient;
import com.example.pcmallcommon.client.CartClient;
import com.example.pcmallcommon.client.GoodsClient;
import com.example.pcmallcommon.client.StorageClient;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.*;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallproviderorder.dao.IOrderAddressDao;
import com.example.pcmallproviderorder.dao.IOrderDao;
import com.example.pcmallproviderorder.dao.IOrderGoodsDao;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class SeataOrderServiceImpl extends OrderServiceImpl {
    @Autowired
    private AddressClient addressClient;
    @Autowired
    private CartClient cartClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private StorageClient storageClient;
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private IOrderAddressDao orderAddressDao;
    @Autowired
    private IOrderGoodsDao orderGoodsDao;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public SeataOrderServiceImpl() {
        log.debug("创建Service对象：{}", this);
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public String createOrder(String uid, Integer aid) {
        String oid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + RandomUtil.randomNumbers(6);
        while (orderDao.selectCount(new QueryWrapper<Order>().eq("oid", oid)) != 0) {//如果生成的订单号存在，则重新生成，直到不存在
            oid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + RandomUtil.randomNumbers(6);
        }

        CompletableFuture<List<Cart>> cartListFuture = CompletableFuture.supplyAsync(() -> cartClient.searchSelectCart(uid, false, false, false).getData(), threadPoolTaskExecutor);
        CompletableFuture<Address> addressFuture = CompletableFuture.supplyAsync(() -> addressClient.searchAddressById(aid).getData(), threadPoolTaskExecutor);
        CompletableFuture.allOf(cartListFuture, addressFuture).join();//等待所有异步任务执行完成

        List<Cart> cartList = cartListFuture.join();//已选中的购物车信息
        Address address = addressFuture.join();//选择的地址信息
        BigDecimal totalPrice = new BigDecimal("0");//总价格

        if (cartList.isEmpty()) {
            throw new SystemException(ResponseCode.CART_EMPTY_ERROR);//购物车为空
        }
        for (Cart cart : cartList) {
            Goods goods = goodsClient.searchGoodsById(cart.getGid(), false, false).getData();
            if (goods.getStatus() != 0) {
                throw new SystemException(ResponseCode.CART_GOODS_ERROR);//购物车商品状态异常
            }
            if (goods.getCount() >= cart.getCount()) {
                Storage storage = new Storage().setGid(cart.getGid()).setUid(uid).setCount(cart.getCount()).setStatus(2);
                storageClient.outboundDelivery(storage);
            } else {
                throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);//商品缺货
            }
            //添加订单商品信息
            if (orderGoodsDao.insertOrderGoods(oid, cart.getGid(), cart.getCount(), goods.getPrice(), goods.getDiscount()) != 1) {
                throw new SystemException(ResponseCode.ERROR);
            }
            cartClient.deleteCart(cart.getId());//删除购物车信息
            BigDecimal temp = NumberUtil.mul(cart.getCount(), goods.getPrice(), goods.getDiscount());
            totalPrice = totalPrice.add(temp);//计算总价格
        }
        //添加订单地址信息
        if (orderAddressDao.addOrderAddress(oid, address) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
        //添加订单信息
        if (orderDao.insert(new Order().setOid(oid).setUid(uid).setPrice(totalPrice)) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
        //创建定时任务，15分钟自动关闭订单
        setSchedulerTask(oid);
        return oid;
    }
}
