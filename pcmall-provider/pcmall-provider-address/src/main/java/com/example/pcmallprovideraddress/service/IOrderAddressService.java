package com.example.pcmallprovideraddress.service;

import com.example.pcmallcommon.model.Address;

public interface IOrderAddressService {
    Address searchOrderAddress(String oid);

    void addOrderAddress(String oid, Address address);

    void deleteOrderAddress(String oid);
}
