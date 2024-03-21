package com.example.pcmallproviderpayment.service;

import com.example.pcmallcommon.model.Cart;

import java.util.List;

public interface ICartService {
    public List<Cart> searchCartByUid(String uid, Integer currentPage, Integer pageSize);

    public Long getTotalCount(String uid);

    public void addCart(Cart cart);

    public void addCartCount(Cart cart);

    public void subCartCount(Cart cart);

    public void selectCart(Cart cart);

    public void deleteCart(Cart cart);
}
