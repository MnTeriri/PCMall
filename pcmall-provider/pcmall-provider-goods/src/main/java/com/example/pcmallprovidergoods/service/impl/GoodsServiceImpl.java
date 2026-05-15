package com.example.pcmallprovidergoods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pcmallcommon.client.BrandClient;
import com.example.pcmallcommon.client.CategoryClient;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.model.dto.GoodsAiSearchRequest;
import com.example.pcmallcommon.model.message.GoodsChangeMessage;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallprovidergoods.annotation.EnableExtraSearch;
import com.example.pcmallprovidergoods.dao.IGoodsDao;
import com.example.pcmallprovidergoods.service.IGoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.seata.spring.annotation.GlobalLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements IGoodsService {
    @Value("${rocketmq.topic}")
    private String topic;

    private final IGoodsDao goodsDao;

    private final BrandClient brandClient;

    private final CategoryClient categoryClient;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final RocketMQTemplate rocketMQTemplate;

    @Override
    public Goods searchGoodsById(Integer id, Boolean isSearchCategory, Boolean isSearchBrand) {
        Goods goods = goodsDao.selectById(id);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        if (isSearchCategory) {
            CompletableFuture<Void> categoryFuture = CompletableFuture
                    .supplyAsync(() -> categoryClient.searchCategoryById(goods.getCid()).getData(), threadPoolTaskExecutor)
                    .thenAccept(goods::setCategory);
            futures.add(categoryFuture);
        }
        if (isSearchBrand) {
            CompletableFuture<Void> brandFuture = CompletableFuture
                    .supplyAsync(() -> brandClient.searchBrandById(goods.getBid()).getData(), threadPoolTaskExecutor)
                    .thenAccept(goods::setBrand);
            futures.add(brandFuture);
        }

        //等待所有异步任务执行完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return goods;
    }

    @EnableExtraSearch
    @Override
    public List<Goods> searchAllGoods(Integer currentPage, Integer pageSize) {
        Page<Goods> page = new Page<>(currentPage, pageSize);
        return goodsDao.selectPage(page, null).getRecords();
    }

    @EnableExtraSearch
    @Override
    public List<Goods> searchGoodsByValue(String searchValue, Integer currentPage, Integer pageSize) {
        return goodsDao.searchGoodsList(searchValue, (currentPage - 1) * pageSize, pageSize);
    }

    @EnableExtraSearch
    @Override
    public List<Goods> searchGoodsByCidAndBid(Integer bid, Integer cid, Integer currentPage, Integer pageSize) {
        return goodsDao.searchGoodsByCidAndBid(cid, bid, (currentPage - 1) * pageSize, pageSize);
    }

    @EnableExtraSearch
    @Override
    public List<Goods> searchGoodsByAiIntent(GoodsAiSearchRequest aiSearchRequest) {
        return goodsDao.searchGoodsByAiIntent(aiSearchRequest);
    }

    @Override
    public Long getTotalCount() {
        return goodsDao.selectCount(null);
    }

    @Override
    public Long getTotalCountByValue(String searchValue) {
        return goodsDao.getRecordsFiltered(searchValue);
    }

    @Override
    public Long getTotalCountByCidAndBid(Integer bid, Integer cid) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<Goods>()
                .eq("cid", cid)
                .eq("bid", bid)
                .eq("status", 0)
                .eq("is_delete", 0);
        return goodsDao.selectCount(queryWrapper);
    }

    @Transactional
    @Override
    public void addGoods(Goods goods) {
        if (goodsDao.insert(goods) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
        // insert 后 goods.getId() 已回填，再查一次拿 category/brand
        Goods data = searchGoodsById(goods.getId(), true, true);
        GoodsChangeMessage message = new GoodsChangeMessage()
                .setGoodsId(data.getId())
                .setAction(GoodsChangeMessage.Action.ADD)
                .setGoods(data)
                .setTimestamp(System.currentTimeMillis());
        sendMessage(message);
    }

    @Transactional
    @Override
    public void updateGoods(Goods goods) {
        //更新商品信息到数据库
        doUpdateGoods(goods);
        //获取最新数据
        Goods data = searchGoodsById(goods.getId(), true, true);
        //发送更新消息
        GoodsChangeMessage message = new GoodsChangeMessage()
                .setGoodsId(data.getId())
                .setAction(GoodsChangeMessage.Action.UPDATE)
                .setGoods(data)
                .setTimestamp(System.currentTimeMillis());
        sendMessage(message);
    }

    @Transactional
    @Override
    public void updateGoodsStatus(Goods goods) {
        Goods data = searchGoodsById(goods.getId(),false,false);
        if (goods.getStatus() == Goods.GoodsState.NORMAL && data.getCount() == 0) {
            //上架操作如果商品没货，设置为缺货
            goods.setStatus(Goods.GoodsState.OUT_OF_STOCK);
        }
        doUpdateGoods(goods);

        data = searchGoodsById(goods.getId(), true, true);
        //发送更新消息
        GoodsChangeMessage message = new GoodsChangeMessage()
                .setGoodsId(data.getId())
                .setAction(GoodsChangeMessage.Action.STATUS_CHANGE)
                .setGoods(data)
                .setTimestamp(System.currentTimeMillis());
        sendMessage(message);
    }

    @Transactional
    @Override
    public void deleteGoods(Integer id) {
        Goods goods = new Goods().setId(id).setIsDelete(1);
        doUpdateGoods(goods);

        //发送更新消息
        GoodsChangeMessage message = new GoodsChangeMessage()
                .setGoodsId(id)
                .setAction(GoodsChangeMessage.Action.DELETE)
                .setGoods(null)
                .setTimestamp(System.currentTimeMillis());
        sendMessage(message);
    }

    @Transactional
    @Override
    public void recoverGoods(Integer id) {
        Goods goods = new Goods().setId(id).setIsDelete(0);
        doUpdateGoods(goods);

        //获取最新数据
        Goods data = searchGoodsById(id, true, true);
        //发送更新消息
        GoodsChangeMessage message = new GoodsChangeMessage()
                .setGoodsId(id)
                .setAction(GoodsChangeMessage.Action.RECOVER)
                .setGoods(data)
                .setTimestamp(System.currentTimeMillis());
        sendMessage(message);
    }

    @GlobalLock(lockRetryInterval = 50, lockRetryTimes = 1000)
    @Transactional
    @Override
    public void addGoodsCount(Integer id, Integer count) {
        Goods goods = goodsDao.searchGoodsForUpdate(id);
        if (goods == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);//不存在该商品
        }

        Goods.GoodsState oldStatus = goods.getStatus();

        if (goods.getStatus() == Goods.GoodsState.OUT_OF_STOCK) {
            goods.setStatus(Goods.GoodsState.NORMAL);//如果商品状态为缺货，设置为正常
        }
        goods.setCount(goods.getCount() + count);
        doUpdateGoods(goods);

        // 仅状态变化时通知（count 不影响知识库文本）
        if (goods.getStatus() != oldStatus) {
            Goods data = searchGoodsById(id, true, true);
            //发送更新消息
            GoodsChangeMessage message = new GoodsChangeMessage()
                    .setGoodsId(data.getId())
                    .setAction(GoodsChangeMessage.Action.STATUS_CHANGE)
                    .setGoods(data)
                    .setTimestamp(System.currentTimeMillis());
            sendMessage(message);
        }
    }

    @GlobalLock(lockRetryInterval = 50, lockRetryTimes = 1000)
    @Transactional
    @Override
    public void divGoodsCount(Integer id, Integer count) {
        Goods goods = goodsDao.searchGoodsForUpdate(id);
        if (goods == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);//不存在该商品
        }
        if (goods.getCount() < count) {
            throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);//库存不足
        }

        Goods.GoodsState oldStatus = goods.getStatus();

        goods.setCount(goods.getCount() - count);
        if (goods.getCount() == 0) {
            goods.setStatus(Goods.GoodsState.OUT_OF_STOCK);//如果商品数量为0，设置状态为缺货
        }
        doUpdateGoods(goods);

        // 仅状态变化时通知（count 不影响知识库文本）
        if (goods.getStatus() != oldStatus) {
            Goods data = searchGoodsById(id, true, true);
            //发送更新消息
            GoodsChangeMessage message = new GoodsChangeMessage()
                    .setGoodsId(data.getId())
                    .setAction(GoodsChangeMessage.Action.STATUS_CHANGE)
                    .setGoods(data)
                    .setTimestamp(System.currentTimeMillis());
            sendMessage(message);
        }
    }

    /**
     * 纯 DB 更新：设置 updateTime 后执行 updateById，不发 MQ。
     */
    private void doUpdateGoods(Goods goods) {
        goods.setUpdateTime(LocalDateTime.now());
        if (goodsDao.updateById(goods) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    private void sendMessage(GoodsChangeMessage message) {
        try {
            rocketMQTemplate.convertAndSend(topic, message);
            log.debug("MQ 已发送: goodsId={}, action={}, message={}", message.getGoodsId(), message.getAction(), message);
        } catch (Exception e) {
            log.error("MQ 发送失败: goodsId={}, action={}", message.getGoodsId(), message.getAction(), e);
            throw new SystemException(ResponseCode.ERROR);
        }
    }
}
