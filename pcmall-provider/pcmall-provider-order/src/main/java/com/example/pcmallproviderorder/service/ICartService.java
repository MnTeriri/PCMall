package com.example.pcmallproviderorder.service;

import com.example.pcmallcommon.model.Cart;

import java.util.List;

public interface ICartService {
    List<Cart> searchCartList(String uid, Integer currentPage, Integer pageSize);

    List<Cart> searchSelectCartList(String uid);

    Long getTotalCount(String uid);

    void addCart(Cart cart);

    void addCartCount(Cart cart);

    void subCartCount(Cart cart);

    void selectCart(Cart cart);

    void selectAllCart(Cart cart);

    void deleteCart(Integer id);
}
