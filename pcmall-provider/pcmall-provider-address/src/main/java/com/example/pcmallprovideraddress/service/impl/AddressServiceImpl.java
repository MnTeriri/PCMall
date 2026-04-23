package com.example.pcmallprovideraddress.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.Address;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallprovideraddress.dao.IAddressDao;
import com.example.pcmallprovideraddress.service.IAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements IAddressService {

    private final IAddressDao addressDao;

    @Override
    public Address searchAddressById(Integer id) {
        return addressDao.selectById(id);
    }

    @Override
    public List<Address> searchAddressList(String uid) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<Address>()
                .eq("uid", uid)
                .orderByDesc("is_default")
                .orderByDesc("id");
        return addressDao.selectList(queryWrapper);
    }

    @Override
    public Address searchDefaultAddress(String uid) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<Address>()
                .eq("uid", uid)
                .eq("is_default", 1);
        return addressDao.selectOne(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addAddress(Address address) {
        //如果该地址被设置成默认
        if (address.getIsDefault() == 1) {
            addressDao.cleanUserDefaultAddress(address.getUid());//清除原默认地址
        }
        if (addressDao.insert(address) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAddress(Address address) {
        address.setUpdateTime(LocalDateTime.now());
        //如果该地址被设置成默认
        if (address.getIsDefault() == 1) {
            addressDao.cleanUserDefaultAddress(address.getUid());//清除原默认地址
        }
        if (addressDao.updateById(address) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void deleteAddress(Integer id) {
        if (addressDao.deleteById(id) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }
}
