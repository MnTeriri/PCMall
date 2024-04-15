package com.example.pcmallproviderpayment.dao;

import com.example.pcmallcommon.model.Address;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderAddressDao {
    @Select("SELECT province, city, district, address_detail, receiver_name, phone FROM order_address WHERE oid=#{oid}")
    public Address searchOrderAddress(String oid);
}
