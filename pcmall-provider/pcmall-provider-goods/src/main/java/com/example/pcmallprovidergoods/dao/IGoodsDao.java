package com.example.pcmallprovidergoods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.Goods;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGoodsDao extends BaseMapper<Goods> {
    @Select("SELECT * FROM goods WHERE status=0 AND is_delete=0 " +
            "AND (gname LIKE CONCAT('%', #{searchValue}, '%') OR description LIKE CONCAT('%', #{searchValue}, '%') " +
            "OR bid IN (SELECT id FROM brand WHERE bname LIKE CONCAT('%', #{searchValue}, '%'))" +
            "OR cid IN (SELECT id FROM category WHERE category.cname LIKE CONCAT('%', #{searchValue}, '%'))) " +
            "LIMIT #{start},#{pageSize};")
    List<Goods> searchGoodsList(String searchValue, Integer start, Integer pageSize);

    @Select("SELECT * FROM goods " +
            "WHERE cid=#{cid} AND bid=#{bid} AND status=0 AND is_delete=0 " +
            "LIMIT #{start},#{pageSize};")
    List<Goods> searchGoodsByCidAndBid(Integer cid, Integer bid, Integer start, Integer pageSize);

    @Select("SELECT COUNT(*) FROM goods WHERE status=0 AND is_delete=0 " +
            "AND (gname LIKE CONCAT('%', #{searchValue}, '%') OR description LIKE CONCAT('%', #{searchValue}, '%') " +
            "OR bid IN (SELECT id FROM brand WHERE bname LIKE CONCAT('%', #{searchValue}, '%'))" +
            "OR cid IN (SELECT id FROM category WHERE category.cname LIKE CONCAT('%', #{searchValue}, '%')));")
    Long getRecordsFiltered(String searchValue);
}
