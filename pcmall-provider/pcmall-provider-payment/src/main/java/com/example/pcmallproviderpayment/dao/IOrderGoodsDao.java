package com.example.pcmallproviderpayment.dao;

import com.example.pcmallcommon.model.Goods;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderGoodsDao {
    @Select("SELECT goods.id, goods.cid, goods.bid, goods.gname, goods.image, goods.description," +
            "order_goods.count, order_goods.price, order_goods.discount " +
            "FROM order_goods INNER JOIN goods ON order_goods.gid = goods.id " +
            "WHERE oid=#{oid}")
    @Results({
            @Result(property = "cid", column = "cid", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "bid", column = "bid", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "category", column = "cid", one = @One(select = "com.example.pcmallproviderpayment.dao.ICategoryDao.searchCategory")),
            @Result(property = "brand", column = "bid", one = @One(select = "com.example.pcmallproviderpayment.dao.IBrandDao.searchBrand"))
    })
    public List<Goods> searchOrderGoods(String oid);
}
