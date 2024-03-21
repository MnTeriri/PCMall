package com.example.pcmallproviderpayment.service;

import com.example.pcmallcommon.model.Address;

import java.util.List;

public interface IAddressService {
    public Address searchDefaultAddress(String uid);

    public List<Address> searchAddressByUid(String uid);

    public void addAddress(Address address);

    public void updateAddress(Address address);

    public void updateAddressByUid(Address address);

    public void deleteAddress(Address address);
}
