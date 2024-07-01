package com.example.pcmallproviderpayment.service;

import com.example.pcmallcommon.model.Address;

import java.util.List;

public interface IAddressService {
    List<Address> searchAddressList(String uid);

    Address searchDefaultAddress(String uid);

    void addAddress(Address address);

    void updateAddress(Address address);

    void deleteAddress(Integer id);
}
