package com.example.pcmallprovidercart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.Brand;
import com.example.pcmallcommon.model.Cart;
import com.example.pcmallcommon.model.Category;
import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallprovidercart.client.GoodsClient;
import com.example.pcmallprovidercart.dao.ICartDao;
import com.example.pcmallprovidercart.service.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Cart searchCartById(Integer id) {
        Cart cart = cartDao.selectById(id);
        if (cart == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);
        }
        Goods goods = goodsClient.searchGoodsById(cart.getGid(), true, true).getData();
        cart.setGoods(goods);
        return cart;
    }

    @Override
    public List<Cart> searchCartList(String uid, Integer currentPage, Integer pageSize, CartSearchType searchType) {
        if (searchType == CartSearchType.ALL) {
            QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>()
                    .eq("uid", uid)
                    .orderByDesc("id");
            Page<Cart> page = new Page<>(currentPage, pageSize);
            return cartDao.selectPage(page, queryWrapper).getRecords();
        } else if (searchType == CartSearchType.SELECT) {
            QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>()
                    .eq("uid", uid)
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
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>()
                .eq("uid", cart.getUid())
                .eq("gid", cart.getGid());
        Cart data = cartDao.selectOne(queryWrapper);
        Goods goods = goodsClient.searchGoodsById(cart.getGid(), true, true).getData();
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
        if (data == null) {
            //如果没添加过
            if (cartDao.insert(cart) != 1) {
                throw new SystemException(ResponseCode.ERROR);
            }
        } else {
            if (data.getCount() >= goods.getCount()) {
                throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);
            }
            data.setCount(data.getCount() + 1);
            if (cartDao.updateById(data) != 1) {
                throw new SystemException(ResponseCode.ERROR);
            }
        }
    }

    @Override
    public void updateCart(Cart cart) {

    }

    @Override
    public void addCartCount(Cart cart) {
        Cart data = searchCartById(cart.getId());
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
        cart.setCount(data.getCount() + 1);
        if (cartDao.updateById(cart) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void subCartCount(Cart cart) {
        Cart data = searchCartById(cart.getId());
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
        cart.setCount(data.getCount() - 1);
        if (cartDao.updateById(cart) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void selectCart(Cart cart) {
        Cart data = searchCartById(cart.getId());
        Goods goods = data.getGoods();
        Brand brand = goods.getBrand();
        Category category = goods.getCategory();
        if (brand.getIsDelete() == 1 ||
                category.getIsDelete() == 1 ||
                goods.getStatus() != 0 ||
                goods.getIsDelete() == 1) {
            throw new SystemException(ResponseCode.CART_GOODS_ERROR);
        }
        if (cartDao.updateById(cart) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void selectAllCart(Cart cart) {
        if (!cartDao.updateAllCartSelected(cart.getUid(), cart.getIsSelect())) {
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
