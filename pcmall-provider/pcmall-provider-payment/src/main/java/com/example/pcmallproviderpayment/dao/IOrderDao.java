package com.example.pcmallproviderpayment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.Order;
import com.example.pcmallproviderpayment.provider.OrderSqlProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderDao extends BaseMapper<Order> {
//    @SelectProvider(type = OrderSqlProvider.class, method = "searchOrderListSql")
    public List<Order> searchOrderList();

    @SelectProvider(type = OrderSqlProvider.class, method = "getRecordsFilteredSql")
    public Long getRecordsFiltered(@Param("uid") String uid, @Param("type") Integer type);
}
