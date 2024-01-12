package com.example.pcmalluserservice;

import com.example.pcmalluserservice.dao.IUserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PcmallUserServiceApplicationTests {
    @Autowired
    private IUserDao userDao;

    @Test
    void contextLoads() {
        System.out.println(userDao.selectList(null));
    }

}
