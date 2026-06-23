package com.example.pcmallprovideraddress.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.dto.Address;
import com.example.pcmallcommon.model.entity.AddressEntity;
import com.example.pcmallcommon.model.mapper.AddressMapper;
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
        AddressEntity data = addressDao.selectById(id);
        return AddressMapper.INSTANCE.toDto(data);
    }

    @Override
    public List<Address> searchAddressList(String uid) {
        QueryWrapper<AddressEntity> queryWrapper = new QueryWrapper<AddressEntity>()
                .eq("uid", uid)
                .orderByDesc("is_default")
                .orderByDesc("id");
        List<AddressEntity> list = addressDao.selectList(queryWrapper);
        return AddressMapper.INSTANCE.toDtoList(list);
    }

    @Override
    public Address searchDefaultAddress(String uid) {
        QueryWrapper<AddressEntity> queryWrapper = new QueryWrapper<AddressEntity>()
                .eq("uid", uid)
                .eq("is_default", 1);
        AddressEntity data = addressDao.selectOne(queryWrapper);
        return AddressMapper.INSTANCE.toDto(data);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addAddress(Address address) {
        //如果该地址被设置成默认
        if (address.getIsDefault() == 1) {
            addressDao.cleanUserDefaultAddress(address.getUid());//清除原默认地址
        }

        AddressEntity entity = AddressMapper.INSTANCE.toEntity(address);
        if (addressDao.insert(entity) != 1) {
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

        AddressEntity entity = AddressMapper.INSTANCE.toEntity(address);
        if (addressDao.updateById(entity) != 1) {
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
