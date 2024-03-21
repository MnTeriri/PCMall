package com.example.pcmallproviderpayment.dao;

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
    @Select("SELECT * FROM goods WHERE id=#{id};")
    public Goods searchGoods(Integer id);
}
