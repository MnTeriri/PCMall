package com.example.pcmallprovidergoods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.Goods;
import org.springframework.stereotype.Repository;

@Repository
public interface IGoodsDao extends BaseMapper<Goods> {
}
