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
    @Select("SELECT * FROM goods LIMIT #{start},#{pageSize}")
    @Results({
            @Result(property = "cid", column = "cid", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "bid", column = "bid", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "category",column = "cid",one = @One(select = "com.example.pcmallprovidergoods.dao.ICategoryDao.selectCategoryById")),
            @Result(property = "brand",column = "bid",one = @One(select = "com.example.pcmallprovidergoods.dao.IBrandDao.searchBrandById"))
    })
    public List<Goods> getGoodsList(Integer start, Integer pageSize);
}
