package com.example.pcmallprovideraddress.service;

import com.example.pcmallcommon.model.dto.Address;

import java.util.List;

public interface IAddressService {
    Address searchAddressById(Integer id);

    List<Address> searchAddressList(String uid);

    Address searchDefaultAddress(String uid);

    void addAddress(Address address);

    void updateAddress(Address address);

    void deleteAddress(Integer id);
}
