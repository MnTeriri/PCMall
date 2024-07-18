package com.example.pcmallproviderorder.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.Category;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryDao extends BaseMapper<Category> {
    @Select("SELECT * FROM category WHERE id=#{id}")
    Category searchCategory(Integer id);
}
