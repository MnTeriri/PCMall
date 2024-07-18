package com.example.pcmallproviderorder.dao;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallproviderorder.cache.OrderInfoCache;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheNamespace(implementation = OrderInfoCache.class)
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
    List<Goods> searchOrderGoods(String oid);
}
