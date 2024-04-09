package com.example.pcmallproviderpayment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pcmallcommon.model.Address;
import com.example.pcmallproviderpayment.dao.IAddressDao;
import com.example.pcmallproviderpayment.service.IAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private IAddressDao addressDao;

    public AddressServiceImpl() {
        log.debug("创建Service对象：AddressServiceImpl");
    }

    @Override
    public List<Address> searchAddressList(String uid) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<Address>()
                .eq("uid", uid)
                .eq("is_delete", 0);
        return addressDao.selectList(queryWrapper);
    }

    @Override
    public Address searchDefaultAddress(String uid) {
        return null;
    }


    @Override
    public void addAddress(Address address) {

    }

    @Override
    public void updateAddress(Address address) {

    }

    @Override
    public void updateAddressByUid(Address address) {

    }

    @Override
    public void deleteAddress(Address address) {

    }
}
