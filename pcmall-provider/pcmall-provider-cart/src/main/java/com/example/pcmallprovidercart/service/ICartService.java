package com.example.pcmallprovidercart.service;

import com.example.pcmallcommon.model.Cart;

import java.util.List;

public interface ICartService {
    Cart searchCartById(Integer id);

    List<Cart> searchCartList(String uid, Integer currentPage, Integer pageSize, CartSearchType searchType);

    Long getTotalCount(String uid);

    void addCart(Cart cart);

    void updateCart(Cart cart);

    void deleteCart(Integer id);

    void addCartCount(Cart cart);

    void subCartCount(Cart cart);

    void selectCart(Cart cart);

    void selectAllCart(Cart cart);

    /**
     * ALL：搜索全部购物车信息，使用分页<br>
     * SELECT：搜索已选中的购物车信息，不使用分页
     */
    enum CartSearchType {
        ALL, SELECT
    }
}
