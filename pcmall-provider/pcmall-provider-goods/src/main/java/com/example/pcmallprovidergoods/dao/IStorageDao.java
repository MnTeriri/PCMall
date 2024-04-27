package com.example.pcmallprovidergoods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.Storage;
import org.springframework.stereotype.Repository;

@Repository
//@CacheNamespace(implementation = StorageCache.class)
public interface IStorageDao extends BaseMapper<Storage> {

}
