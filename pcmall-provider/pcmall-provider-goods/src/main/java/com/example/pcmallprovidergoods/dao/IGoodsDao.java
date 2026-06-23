package com.example.pcmallprovidergoods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.ai.GoodsAiSearchRequest;
import com.example.pcmallcommon.model.entity.GoodsEntity;
import com.example.pcmallprovidergoods.provider.GoodsSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGoodsDao extends BaseMapper<GoodsEntity> {
    @Select("SELECT * FROM goods WHERE id=#{id} FOR UPDATE;")
    GoodsEntity searchGoodsForUpdate(Integer id);

    @Select("SELECT goods.* FROM goods " +
            "INNER JOIN category ON goods.cid = category.id " +
            "INNER JOIN brand ON goods.bid = brand.id " +
            "WHERE goods.status=0 AND goods.is_delete=0 " +
            "AND (" +
                "gname LIKE CONCAT('%', #{searchValue}, '%')" +
                "OR description LIKE CONCAT('%', #{searchValue}, '%')" +
                "OR category.cname LIKE CONCAT('%', #{searchValue}, '%')" +
                "OR brand.bname LIKE CONCAT('%', #{searchValue}, '%')" +
            ") " +
            "LIMIT #{start},#{pageSize};")
    List<GoodsEntity> searchGoodsList(String searchValue, Integer start, Integer pageSize);

    @Select("SELECT * FROM goods " +
            "WHERE cid=#{cid} AND bid=#{bid} AND status=0 AND is_delete=0 " +
            "LIMIT #{start},#{pageSize};")
    List<GoodsEntity> searchGoodsByCidAndBid(Integer cid, Integer bid, Integer start, Integer pageSize);

    @SelectProvider(type = GoodsSqlProvider.class, method = "aiSearchSql")
    List<GoodsEntity> searchGoodsByAiIntent(@Param("request") GoodsAiSearchRequest request);

    @Select("SELECT COUNT(goods.id) FROM goods " +
            "INNER JOIN category ON goods.cid = category.id " +
            "INNER JOIN brand ON goods.bid = brand.id " +
            "WHERE goods.status=0 AND goods.is_delete=0 " +
            "AND (" +
                "gname LIKE CONCAT('%', #{searchValue}, '%')" +
                "OR description LIKE CONCAT('%', #{searchValue}, '%')" +
                "OR category.cname LIKE CONCAT('%', #{searchValue}, '%')" +
                "OR brand.bname LIKE CONCAT('%', #{searchValue}, '%')" +
            ");")
    Long getRecordsFiltered(String searchValue);
}
