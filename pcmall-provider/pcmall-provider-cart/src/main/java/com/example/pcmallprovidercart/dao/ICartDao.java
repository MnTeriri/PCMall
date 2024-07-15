package com.example.pcmallprovidercart.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.Cart;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartDao extends BaseMapper<Cart> {
    @Update("UPDATE cart INNER JOIN goods ON cart.gid = goods.id " +
            "SET cart.is_select=#{isSelect} " +
            "WHERE cart.uid=#{uid} AND goods.status=0 AND goods.count>=cart.count")
    boolean updateAllCartSelected(String uid, Integer isSelect);
}
