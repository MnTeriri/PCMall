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
    }

    @Override
    public void updateCart(Cart cart) {

    }

    @Override
    public void addCartCount(Cart cart) {
    }

    @Override
    public void subCartCount(Cart cart) {
    }

    @Override
    public void selectCart(Cart cart) {
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
