package com.example.pcmallprovidercart.service;

import com.example.pcmallcommon.model.dto.Cart;

import java.util.List;

public interface ICartService {
    Cart searchCartById(Integer id);

    List<Cart> searchAllCart(String uid, Integer currentPage, Integer pageSize);

    List<Cart> searchSelectCart(String uid, Boolean isSearchGoods);

    Long getTotalCount(String uid);

    void addCart(Cart cart);

    void updateCart(Cart cart);

    void deleteCart(Integer id);

    void addCartCount(Integer id);

    void subCartCount(Integer id);

    void selectCart(Integer id, Integer isSelect);

    void selectAllCart(String uid, Integer isSelect);

    /**
     * ALL：搜索全部购物车信息，使用分页<br>
     * SELECT：搜索已选中的购物车信息，不使用分页
     */
    enum CartSearchType {
        ALL, SELECT
    }
}
