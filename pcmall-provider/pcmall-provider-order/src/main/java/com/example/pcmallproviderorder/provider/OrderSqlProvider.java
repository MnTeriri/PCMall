package com.example.pcmallproviderorder.provider;

import org.apache.ibatis.annotations.Param;

public class OrderSqlProvider {
    public String searchOrderListSql(
            @Param("searchValue") String searchValue,
            @Param("uid") String uid,
            @Param("type") Integer type,
            @Param("start") Integer start,
            @Param("pageSize") Integer pageSize) {
        String sql = "SELECT * FROM `order` WHERE oid LIKE CONCAT('%', #{searchValue}, '%')";
        if (!"".equals(uid)) {//有uid则查对应uid，没有则全查
            sql += " AND uid = #{uid}";
        }
        if (type != -1) {//-1为查全部订单，其余的type数值对应status
            sql += " AND status = #{type}";
        }
        sql += " ORDER BY id DESC LIMIT #{start},#{pageSize};";
        return sql;
    }

    public String getRecordsFilteredSql(
            @Param("searchValue") String searchValue,
            @Param("uid") String uid,
            @Param("type") Integer type) {
        String sql = "SELECT COUNT(*) FROM `order` WHERE oid LIKE CONCAT('%', #{searchValue}, '%')";
        if (!"".equals(uid)) {//有uid则查对应uid，没有则全查
            sql += " AND uid = #{uid}";
        }
        if (type != -1) {//-1为查全部订单，其余的type数值对应status
            sql += " AND status = #{type}";
        }
        return sql;
    }
}
