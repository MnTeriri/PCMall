package com.example.pcmallprovideraddress.service.impl;

import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.Address;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallprovideraddress.dao.IOrderAddressDao;
import com.example.pcmallprovideraddress.service.IOrderAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderAddressServiceImpl implements IOrderAddressService {
    @Autowired
    private IOrderAddressDao orderAddressDao;

    public OrderAddressServiceImpl() {
        log.debug("创建Service对象：{}", this);
    }

    @Override
    public Address searchOrderAddress(String oid) {
        return orderAddressDao.searchOrderAddress(oid);
    }

    @Override
    public void addOrderAddress(String oid, Address address) {
        if (orderAddressDao.addOrderAddress(oid, address) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void deleteOrderAddress(String oid) {
        if (orderAddressDao.deleteOrderAddress(oid) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }
}
