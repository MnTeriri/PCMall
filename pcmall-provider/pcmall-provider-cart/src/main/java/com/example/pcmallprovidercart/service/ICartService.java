package com.example.pcmallprovidercart.service;

import com.example.pcmallcommon.model.Cart;

import java.util.List;
import java.util.Map;

public interface ICartService {
    /**
     * aspectRule：null时不增强，（isSearchGoods：是否搜索商品）、（isSearchCategory：是否搜索商品分类）、（isSearchBrand：是否搜索商品品牌）<br>
     */
    Cart searchCartById(Map<String, Boolean> aspectRule, Integer id);

    /**
     * aspectRule：null时不增强，（isSearchGoods：是否搜索商品）、（isSearchCategory：是否搜索商品分类）、（isSearchBrand：是否搜索商品品牌）<br>
     * searchType：搜索类型枚举<br>
     * searchValue：（uid(String)：用户ID）、（currentPage(Integer)：当前页数）、（pageSize(Integer)：页面大小）
     */
    List<Cart> searchCartList(Map<String, Boolean> aspectRule, CartSearchType searchType, Map<String, Object> searchValue);

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
