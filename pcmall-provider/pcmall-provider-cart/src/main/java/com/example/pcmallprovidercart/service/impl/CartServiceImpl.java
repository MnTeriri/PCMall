package com.example.pcmallprovidercart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pcmallcommon.client.GoodsClient;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.Brand;
import com.example.pcmallcommon.model.Cart;
import com.example.pcmallcommon.model.Category;
import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallprovidercart.dao.ICartDao;
import com.example.pcmallprovidercart.service.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private ICartDao cartDao;
    @Autowired
    private GoodsClient goodsClient;

    public CartServiceImpl() {
        log.debug("创建Service对象：{}", this);
    }

    @Override
    public Cart searchCartById(Map<String, Boolean> aspectRule, Integer id) {
        return cartDao.selectById(id);
    }

    @Override
    public List<Cart> searchCartList(Map<String, Boolean> aspectRule, CartSearchType searchType, Map<String, Object> searchValue) {
        if (searchType == CartSearchType.ALL) {
            QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>()
                    .eq("uid", searchValue.get("uid"))
                    .orderByDesc("id");
            Page<Cart> page = new Page<>((Integer) searchValue.get("currentPage"), (Integer) searchValue.get("pageSize"));
            return cartDao.selectPage(page, queryWrapper).getRecords();
        } else if (searchType == CartSearchType.SELECT) {
            QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>()
                    .eq("uid", searchValue.get("uid"))
                    .eq("is_select", 1)
                    .orderByDesc("id");
            return cartDao.selectList(queryWrapper);
        }
        return List.of();
    }

    @Override
    public Long getTotalCount(String uid) {
        return cartDao.selectCount(new QueryWrapper<Cart>().eq("uid", uid));
    }

    @Override
    public void addCart(Cart cart) {
        Goods goods = goodsClient.searchGoodsById(new HashMap<>() {{
            put("isSearchCategory", true);
            put("isSearchBrand", true);
        }}, cart.getGid()).getData();
        Brand brand = goods.getBrand();
        Category category = goods.getCategory();
        if (brand.getIsDelete() == 1 || category.getIsDelete() == 1 || goods.getIsDelete() == 1) {
            throw new SystemException(ResponseCode.CART_GOODS_ERROR);//商品状态异常
        }
        if (goods.getStatus() == 1) {
            throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);//商品缺货
        }
        if (goods.getStatus() == 2) {
            throw new SystemException(ResponseCode.GOODS_OFF_SHELF_ERROR);//商品下架
        }
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>()
                .eq("uid", cart.getUid())
                .eq("gid", cart.getGid());
        Cart data = cartDao.selectOne(queryWrapper);//查询是否有购物车信息
        if (data == null) {
            //如果没添加过
            if (cartDao.insert(cart) != 1) {
                throw new SystemException(ResponseCode.ERROR);
            }
        } else {
            //如果添加过
            if (data.getCount() >= goods.getCount()) {//如果商品库存不足
                throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);//商品缺货
            }
            data.setCount(data.getCount() + 1);
            if (cartDao.updateById(data) != 1) {
                throw new SystemException(ResponseCode.ERROR);
            }
        }
    }

    @Override
    public void updateCart(Cart cart) {
        if (cartDao.updateById(cart) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void addCartCount(Integer id) {
        Cart data = ((ICartService) AopContext.currentProxy()).searchCartById(null, id);
        if (data == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);
        }
        Goods goods = data.getGoods();
        Brand brand = goods.getBrand();
        Category category = goods.getCategory();
        if (brand.getIsDelete() == 1 || category.getIsDelete() == 1 || goods.getIsDelete() == 1) {
            throw new SystemException(ResponseCode.CART_GOODS_ERROR);
        }
        if (goods.getStatus() == 1) {
            throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);
        }
        if (goods.getStatus() == 2) {
            throw new SystemException(ResponseCode.GOODS_OFF_SHELF_ERROR);
        }
        if (data.getCount() >= goods.getCount()) {
            throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);
        }
        Cart cart = new Cart().setId(id).setCount(data.getCount() + 1);
        updateCart(cart);
    }

    @Override
    public void subCartCount(Integer id) {
        Cart data = ((ICartService) AopContext.currentProxy()).searchCartById(null, id);
        if (data == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);
        }
        Goods goods = data.getGoods();
        Brand brand = goods.getBrand();
        Category category = goods.getCategory();
        if (brand.getIsDelete() == 1 || category.getIsDelete() == 1 || goods.getIsDelete() == 1) {
            throw new SystemException(ResponseCode.CART_GOODS_ERROR);
        }
        if (goods.getStatus() == 1) {
            throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);
        }
        if (goods.getStatus() == 2) {
            throw new SystemException(ResponseCode.GOODS_OFF_SHELF_ERROR);
        }
        if (data.getCount() <= 1) {
            throw new SystemException(ResponseCode.CART_MIN_COUNT_ERROR);
        }
        Cart cart = new Cart().setId(id).setCount(data.getCount() - 1);
        updateCart(cart);
    }

    @Override
    public void selectCart(Integer id, Integer isSelect) {
        Cart data = ((ICartService) AopContext.currentProxy()).searchCartById(null, id);
        if (data == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);
        }
        Goods goods = data.getGoods();
        Brand brand = goods.getBrand();
        Category category = goods.getCategory();
        if (brand.getIsDelete() == 1 ||
                category.getIsDelete() == 1 ||
                goods.getStatus() != 0 ||
                goods.getIsDelete() == 1) {
            throw new SystemException(ResponseCode.CART_GOODS_ERROR);
        }
        Cart cart = new Cart().setId(id).setIsSelect(isSelect);
        updateCart(cart);
    }

    @Override
    public void selectAllCart(String uid, Integer isSelect) {
        if (!cartDao.updateAllCartSelected(uid, isSelect)) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void deleteCart(Integer id) {
        if (cartDao.deleteById(id) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

}
