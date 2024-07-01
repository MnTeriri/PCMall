package com.example.pcmallproviderpayment.dao;

import com.example.pcmallcommon.model.Address;
import com.example.pcmallproviderpayment.cache.OrderInfoCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@CacheNamespace(implementation = OrderInfoCache.class)
public interface IOrderAddressDao {
    @Select("SELECT province, city, district, address_detail, receiver_name, phone FROM order_address WHERE oid=#{oid}")
    Address searchOrderAddress(String oid);
}
