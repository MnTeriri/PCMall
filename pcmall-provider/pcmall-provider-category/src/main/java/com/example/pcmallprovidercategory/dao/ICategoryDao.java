package com.example.pcmallprovidercategory.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryDao extends BaseMapper<CategoryEntity> {
}
