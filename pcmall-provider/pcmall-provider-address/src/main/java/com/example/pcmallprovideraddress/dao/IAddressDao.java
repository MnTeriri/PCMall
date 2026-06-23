package com.example.pcmallprovideraddress.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.entity.AddressEntity;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface IAddressDao extends BaseMapper<AddressEntity> {
    @Update("UPDATE address SET is_default=0,update_time=NOW() WHERE uid=#{uid} AND is_default=1;")
    Integer cleanUserDefaultAddress(String uid);
}
