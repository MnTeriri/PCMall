package com.example.pcmallprovideraddress.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.Address;
import com.example.pcmallprovideraddress.cache.OrderAddressCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@CacheNamespace(implementation = OrderAddressCache.class)
public interface IOrderAddressDao extends BaseMapper<Address> {
    @Select("SELECT province, city, district, address_detail, receiver_name, phone " +
            "FROM order_address WHERE oid=#{oid}")
    Address searchOrderAddress(String oid);

    @Insert("INSERT INTO order_address(oid, province, city, district, address_detail, receiver_name, phone) " +
            "VALUE (#{oid},#{province},#{city},#{district},#{address_detail},#{receiver_name},#{phone})")
    Integer addOrderAddress(String oid, Address address);

    @Delete("DELETE FROM order_address WHERE oid=#{oid}")
    Integer deleteOrderAddress(String oid);
}
