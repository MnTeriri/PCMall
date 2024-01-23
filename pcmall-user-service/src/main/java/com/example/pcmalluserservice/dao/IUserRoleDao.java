package com.example.pcmalluserservice.dao;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRoleDao {
    @Select("SELECT role.name FROM role INNER JOIN user_role " +
            "ON role.id = user_role.rid " +
            "WHERE user_role.uid=#{uid}")
    List<String> findUserRole(String uid);
}
