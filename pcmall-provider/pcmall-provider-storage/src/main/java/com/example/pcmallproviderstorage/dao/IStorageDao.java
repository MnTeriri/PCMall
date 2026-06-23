package com.example.pcmallproviderstorage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.entity.StorageEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface IStorageDao extends BaseMapper<StorageEntity> {
}
