package com.example.pcmallproviderpayment.provider;

import org.apache.ibatis.annotations.Param;

public class OrderSqlProvider {
    public String getRecordsFilteredSql(@Param("uid") String uid, @Param("type") Integer type) {
        String sql = "SELECT COUNT(*) FROM `order` WHERE uid=#{uid} ";
        if (type != -1) {//-1为查全部订单，其余的type数值对应status
            sql += "AND status=#{type} ";
        }
        return sql;
    }
}
