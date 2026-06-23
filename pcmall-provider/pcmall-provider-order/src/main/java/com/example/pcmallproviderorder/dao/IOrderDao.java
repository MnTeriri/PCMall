package com.example.pcmallproviderorder.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.entity.OrderEntity;
import com.example.pcmallproviderorder.provider.OrderSqlProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IOrderDao extends BaseMapper<OrderEntity> {
    @SelectProvider(type = OrderSqlProvider.class, method = "searchOrderListSql")
    @Results({
            @Result(property = "oid", column = "oid", javaType = String.class, jdbcType = JdbcType.CHAR),
            @Result(property = "goodsList", column = "oid", many = @Many(select = "com.example.pcmallproviderorder.dao.IOrderGoodsDao.searchOrderGoods")),
            @Result(property = "address", column = "oid", one = @One(select = "com.example.pcmallproviderorder.dao.IOrderAddressDao.searchOrderAddress"))
    })
    List<OrderEntity> searchOrderList(
            @Param("searchValue") String searchValue,
            @Param("uid") String uid,
            @Param("type") Integer type,
            @Param("start") Integer start,
            @Param("pageSize") Integer pageSize);

    @SelectProvider(type = OrderSqlProvider.class, method = "getRecordsFilteredSql")
    Long getRecordsFiltered(
            @Param("searchValue") String searchValue,
            @Param("uid") String uid,
            @Param("type") Integer type);

    /**
     * @param data 存储过程输出参数(data["result"])：1成功、-4SQL语句出错、-3购物车为空、-2商品缺货下架或删除、-1商品数量不够
     */
    @Update("{CALL create_order(#{oid,mode=IN},#{uid,mode=IN},#{aid,mode=IN},#{result, mode=OUT, jdbcType=INTEGER})}")
    @Options(statementType = StatementType.CALLABLE)
    void createOrder(Map<String, Object> data);

    /**
     * @param data 存储过程输出参数(data["result"])：1成功、-1无该订单、-2SQL语句出错 status只有两个值（4交易取消、6退货完成）
     */
    @Update("{CALL cancel_order(#{oid,mode=IN},#{status, mode=IN},#{result, mode=OUT, jdbcType=INTEGER})}")
    @Options(statementType = StatementType.CALLABLE)
    void cancelOrder(Map<String, Object> data);

}
