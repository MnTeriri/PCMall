package com.example.pcmallproviderorder.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.Brand;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface IBrandDao extends BaseMapper<Brand> {
    @Select("SELECT * FROM brand WHERE id=#{id}")
    Brand searchBrand(Integer id);
}
