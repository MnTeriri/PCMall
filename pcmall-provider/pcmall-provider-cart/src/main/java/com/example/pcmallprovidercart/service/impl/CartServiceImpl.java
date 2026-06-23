package com.example.pcmallprovidercart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pcmallcommon.client.GoodsClient;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.dto.Brand;
import com.example.pcmallcommon.model.dto.Cart;
import com.example.pcmallcommon.model.dto.Category;
import com.example.pcmallcommon.model.dto.Goods;
import com.example.pcmallcommon.model.entity.CartEntity;
import com.example.pcmallcommon.model.mapper.CartMapper;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallprovidercart.dao.ICartDao;
import com.example.pcmallprovidercart.service.ICartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final ICartDao cartDao;

    private final GoodsClient goodsClient;

    @Override
    public Cart searchCartById(Integer id) {
        CartEntity entity = cartDao.selectById(id);
        return CartMapper.INSTANCE.toDto(entity);
    }

    @Override
    public List<Cart> searchAllCart(String uid, Integer currentPage, Integer pageSize) {
        QueryWrapper<CartEntity> queryWrapper = new QueryWrapper<CartEntity>()
                .eq("uid", uid)
                .orderByDesc("id");
        Page<CartEntity> page = new Page<>(currentPage, pageSize);
        return CartMapper.INSTANCE.toDtoList(cartDao.selectPage(page, queryWrapper).getRecords());
    }

    @Override
    public List<Cart> searchSelectCart(String uid, Boolean isSearchGoods) {
        QueryWrapper<CartEntity> queryWrapper = new QueryWrapper<CartEntity>()
                .eq("uid", uid)
                .eq("is_select", 1)
                .orderByDesc("id");
        return CartMapper.INSTANCE.toDtoList(cartDao.selectList(queryWrapper));
    }

    @Override
    public Long getTotalCount(String uid) {
        return cartDao.selectCount(new QueryWrapper<CartEntity>().eq("uid", uid));
    }

    @Override
    public void addCart(Cart cart) {
        Goods goods = goodsClient.searchGoodsById(cart.getGid(), true, true).getData();
        Brand brand = goods.getBrand();
        Category category = goods.getCategory();
        if (brand.getIsDelete() == 1 || category.getIsDelete() == 1 || goods.getIsDelete() == 1) {
            throw new SystemException(ResponseCode.CART_GOODS_ERROR);//商品状态异常
        }
        if (goods.getStatus() == Goods.GoodsState.OUT_OF_STOCK) {
            throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);//商品缺货
        }
        if (goods.getStatus() == Goods.GoodsState.OFF_SHELF) {
            throw new SystemException(ResponseCode.GOODS_OFF_SHELF_ERROR);//商品下架
        }
        QueryWrapper<CartEntity> queryWrapper = new QueryWrapper<CartEntity>()
                .eq("uid", cart.getUid())
                .eq("gid", cart.getGid());
        CartEntity data = cartDao.selectOne(queryWrapper);
        if (data == null) {
            CartEntity entity = CartMapper.INSTANCE.toEntity(cart);
            if (cartDao.insert(entity) != 1) {
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
        CartEntity entity = CartMapper.INSTANCE.toEntity(cart);
        if (cartDao.updateById(entity) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void addCartCount(Integer id) {
        Cart data = ((ICartService) AopContext.currentProxy()).searchCartById(id);
        if (data == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);
        }
        Goods goods = data.getGoods();
        Brand brand = goods.getBrand();
        Category category = goods.getCategory();
        if (brand.getIsDelete() == 1 || category.getIsDelete() == 1 || goods.getIsDelete() == 1) {
            throw new SystemException(ResponseCode.CART_GOODS_ERROR);
        }
        if (goods.getStatus() == Goods.GoodsState.OUT_OF_STOCK) {
            throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);
        }
        if (goods.getStatus() == Goods.GoodsState.OFF_SHELF) {
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
        Cart data = ((ICartService) AopContext.currentProxy()).searchCartById(id);
        if (data == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);
        }
        Goods goods = data.getGoods();
        Brand brand = goods.getBrand();
        Category category = goods.getCategory();
        if (brand.getIsDelete() == 1 || category.getIsDelete() == 1 || goods.getIsDelete() == 1) {
            throw new SystemException(ResponseCode.CART_GOODS_ERROR);
        }
        if (goods.getStatus() == Goods.GoodsState.OUT_OF_STOCK) {
            throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);
        }
        if (goods.getStatus() == Goods.GoodsState.OFF_SHELF) {
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
        Cart data = ((ICartService) AopContext.currentProxy()).searchCartById(id);
        if (data == null) {
            throw new SystemException(ResponseCode.ENTITY_NOT_FOUND);
        }
        Goods goods = data.getGoods();
        Brand brand = goods.getBrand();
        Category category = goods.getCategory();
        if (brand.getIsDelete() == 1 ||
                category.getIsDelete() == 1 ||
                goods.getStatus() != Goods.GoodsState.NORMAL ||
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
