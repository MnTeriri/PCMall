package com.example.pcmallproviderorder.dao;

import com.example.pcmallcommon.model.Address;
import com.example.pcmallproviderorder.cache.OrderInfoCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@CacheNamespace(implementation = OrderInfoCache.class)
public interface IOrderAddressDao {
    @Select("SELECT province, city, district, address_detail, receiver_name, phone " +
            "FROM order_address WHERE oid=#{oid}")
    Address searchOrderAddress(String oid);

    @Insert("INSERT INTO order_address(oid, province, city, district, address_detail, receiver_name, phone) " +
            "VALUE (#{oid},#{address.province},#{address.city},#{address.district},#{address.addressDetail},#{address.receiverName},#{address.phone})")
    Integer addOrderAddress(String oid, Address address);
}