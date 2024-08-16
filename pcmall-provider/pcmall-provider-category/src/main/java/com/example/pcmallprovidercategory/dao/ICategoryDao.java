package com.example.pcmallprovidercategory.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryDao extends BaseMapper<Category> {
}
