package com.example.pcmallproviderpayment.service;

import com.example.pcmallcommon.model.Cart;

import java.util.List;

public interface ICartService {
    public List<Cart> searchCartList(String uid, Integer currentPage, Integer pageSize);

    public List<Cart> searchSelectCartList(String uid);

    public Long getTotalCount(String uid);

    public void addCart(Cart cart);

    public void addCartCount(Cart cart);

    public void subCartCount(Cart cart);

    public void selectCart(Cart cart);

    public void selectAllCart(Cart cart);

    public void deleteCart(Integer id);
}
