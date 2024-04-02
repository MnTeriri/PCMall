package com.example.pcmallproviderpayment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.Brand;
import com.example.pcmallcommon.model.Cart;
import com.example.pcmallcommon.model.Category;
import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallproviderpayment.dao.ICartDao;
import com.example.pcmallproviderpayment.dao.IGoodsDao;
import com.example.pcmallproviderpayment.service.ICartService;
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
    private IGoodsDao goodsDao;

    public CartServiceImpl() {
        log.debug("创建Service对象：CartServiceImpl");
    }

    @Override
    public List<Cart> searchCartByUid(String uid, Integer currentPage, Integer pageSize) {
        return cartDao.searchCartByUid(uid, (currentPage - 1) * pageSize, pageSize);
    }

    @Override
    public Long getTotalCount(String uid) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>().eq("uid", uid);
        return cartDao.selectCount(queryWrapper);
    }

    @Override
    public void addCart(Cart cart) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>()
                .eq("uid", cart.getUid())
                .eq("gid", cart.getGid());
        Cart data = cartDao.selectOne(queryWrapper);
        Goods goods = goodsDao.searchGoods(cart.getGid());
        Brand brand = goods.getBrand();
        Category category = goods.getCategory();
        if (brand.getIsDelete() == 1 || category.getIsDelete() == 1) {
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
    public void addCartCount(Cart cart) {
        Cart data = cartDao.searchCart(cart.getId());
        if (data == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);
        }
        Goods goods = data.getGoods();
        if (data.getCount() >= goods.getCount()) {
            throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);
        }
        cart.setCount(data.getCount() + 1);
        if (cartDao.updateById(cart
        ) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void subCartCount(Cart cart) {
        Cart data = cartDao.searchCart(cart.getId());
        if (data == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);
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
        Cart data = cartDao.searchCart(cart.getId());
        if (data == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);
        }
        Goods goods = data.getGoods();
        if (goods.getStatus() != 0 || goods.getIsDelete() == 1) {
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
    public void deleteCart(Cart cart) {
        if (cartDao.deleteById(cart) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }
}
