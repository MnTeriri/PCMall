package com.example.pcmallproviderpayment.service.impl;

import com.example.pcmallproviderpayment.dao.IAddressDao;
import com.example.pcmallproviderpayment.service.IAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private IAddressDao addressDao;

    public AddressServiceImpl() {
        log.debug("创建Service对象：AddressServiceImpl");
    }
}
