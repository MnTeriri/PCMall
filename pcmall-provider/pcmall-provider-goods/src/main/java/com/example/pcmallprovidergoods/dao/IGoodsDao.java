package com.example.pcmallprovidergoods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.Goods;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGoodsDao extends BaseMapper<Goods> {
    @Select("SELECT * FROM goods WHERE id=#{id} FOR UPDATE;")
    Goods searchGoodsForUpdate(Integer id);

    @Select("SELECT goods.* FROM goods " +
            "INNER JOIN category ON goods.cid = category.id " +
            "INNER JOIN brand ON goods.bid = brand.id " +
            "WHERE status=0 AND goods.is_delete=0 " +
            "AND (" +
                "gname LIKE CONCAT('%', #{searchValue}, '%')" +
                "OR description LIKE CONCAT('%', #{searchValue}, '%')" +
                "OR category.cname LIKE CONCAT('%', #{searchValue}, '%')" +
                "OR brand.bname LIKE CONCAT('%', #{searchValue}, '%')" +
            ") " +
            "LIMIT #{start},#{pageSize};")
    List<Goods> searchGoodsList(String searchValue, Integer start, Integer pageSize);

    @Select("SELECT * FROM goods " +
            "WHERE cid=#{cid} AND bid=#{bid} AND status=0 AND is_delete=0 " +
            "LIMIT #{start},#{pageSize};")
    List<Goods> searchGoodsByCidAndBid(Integer cid, Integer bid, Integer start, Integer pageSize);

    @Select("SELECT COUNT(goods.id) FROM goods " +
            "INNER JOIN category ON goods.cid = category.id " +
            "INNER JOIN brand ON goods.bid = brand.id " +
            "WHERE status=0 AND goods.is_delete=0 " +
            "AND (" +
                "gname LIKE CONCAT('%', #{searchValue}, '%')" +
                "OR description LIKE CONCAT('%', #{searchValue}, '%')" +
                "OR category.cname LIKE CONCAT('%', #{searchValue}, '%')" +
                "OR brand.bname LIKE CONCAT('%', #{searchValue}, '%')" +
            ");")
    Long getRecordsFiltered(String searchValue);
}
