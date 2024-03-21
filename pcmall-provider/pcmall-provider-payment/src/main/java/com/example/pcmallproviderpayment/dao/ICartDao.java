package com.example.pcmallproviderpayment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.Cart;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface ICartDao extends BaseMapper<Cart> {
    @Select("SELECT * FROM cart WHERE uid=#{uid} ORDER BY id DESC LIMIT #{start},#{pageSize};")
    @Results({
            @Result(property = "gid", column = "gid", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "goods", column = "gid", one = @One(select = "com.example.pcmallproviderpayment.dao.IGoodsDao.searchGoods"))
    })
    public List<Cart> searchCartByUid(String uid, Integer start, Integer pageSize);

    @Select("SELECT * FROM cart WHERE id=#{id};")
    @Results({
            @Result(property = "gid", column = "gid", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "goods", column = "gid", one = @One(select = "com.example.pcmallproviderpayment.dao.IGoodsDao.searchGoods"))
    })
    public Cart searchCart(Integer id);
}
