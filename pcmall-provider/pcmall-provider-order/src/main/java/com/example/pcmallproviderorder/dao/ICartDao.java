package com.example.pcmallproviderorder.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.Cart;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartDao extends BaseMapper<Cart> {
    @Select("SELECT * FROM cart WHERE uid=#{uid} ORDER BY id DESC LIMIT #{start},#{pageSize};")
    @Results({
            @Result(property = "gid", column = "gid", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "goods", column = "gid", one = @One(select = "com.example.pcmallproviderpayment.dao.IGoodsDao.searchGoods"))
    })
    List<Cart> searchCartList(String uid, Integer start, Integer pageSize);

    @Select("SELECT * FROM cart WHERE uid=#{uid} AND is_select=1 ORDER BY id DESC;")
    @Results({
            @Result(property = "gid", column = "gid", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "goods", column = "gid", one = @One(select = "com.example.pcmallproviderpayment.dao.IGoodsDao.searchGoods"))
    })
    List<Cart> searchSelectCartList(String uid);

    @Select("SELECT * FROM cart WHERE id=#{id};")
    @Results({
            @Result(property = "gid", column = "gid", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "goods", column = "gid", one = @One(select = "com.example.pcmallproviderpayment.dao.IGoodsDao.searchGoods"))
    })
    Cart searchCart(Integer id);

    /**
     * 对购物车全选或者取消全选时，需要过滤掉
     * 1、书籍状态不为正常状态
     * 2、书籍数量比购物车数量少的
     * 这些购物车信息是异常信息
     */
    @Update("UPDATE cart INNER JOIN goods ON cart.gid = goods.id " +
            "SET cart.is_select=#{isSelect} " +
            "WHERE cart.uid=#{uid} AND goods.status=0 AND goods.count>=cart.count")
    boolean updateAllCartSelected(String uid, Integer isSelect);
}
