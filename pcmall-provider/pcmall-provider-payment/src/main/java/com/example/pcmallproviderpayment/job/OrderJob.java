package com.example.pcmallproviderpayment.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pcmallcommon.model.Order;
import com.example.pcmallproviderpayment.dao.IOrderDao;
import com.example.pcmallproviderpayment.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class OrderJob implements Job {
    @Autowired
    private IOrderDao orderDao;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String oid = jobDataMap.getString("orderOid");//获取创建订单的订单号
        Order order = orderDao.selectOne(new QueryWrapper<Order>().eq("oid", oid));
//        if (order.getStatus() == 0) {//如果订单未付款
//            Map<String, Object> data = new HashMap<>();
//            data.put("oid", order.getOid());
//            data.put("status", 4);
//            orderDao.cancelOrder(data);//取消订单
//            Integer result = (Integer) data.get("result");
//            if (result == 1) {
//                log.debug("当前时间：{}，订单：{}付款超时，被取消！", jobExecutionContext.getFireTime(), oid);
//            } else {
//                log.debug("订单：{}，取消订单执行出错！", oid);
//            }
//        } else {
//            log.debug("订单：{}，订单已付款或已取消！", oid);
//        }
    }
}
