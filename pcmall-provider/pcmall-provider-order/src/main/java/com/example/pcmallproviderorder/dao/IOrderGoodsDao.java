package com.example.pcmallproviderorder.dao;

import com.example.pcmallcommon.model.Goods;
import com.example.pcmallproviderorder.cache.OrderInfoCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@CacheNamespace(implementation = OrderInfoCache.class)
public interface IOrderGoodsDao {
    @Select("SELECT goods.id, goods.cid, goods.bid, goods.gname, goods.image, goods.description," +
            "order_goods.count, order_goods.price, order_goods.discount " +
            "FROM order_goods INNER JOIN goods ON order_goods.gid = goods.id " +
            "WHERE oid=#{oid}")
    List<Goods> searchOrderGoods(String oid);

    @Insert("INSERT INTO order_goods(oid, gid, count, price, discount) VALUES (#{oid}, #{gid}, #{count}, #{price}, #{discount});")
    Integer insertOrderGoods(String oid, Integer gid, Integer count, BigDecimal price, BigDecimal discount);
}
