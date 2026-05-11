package com.example.pcmallproviderorder.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pcmallcommon.model.Order;
import com.example.pcmallproviderorder.dao.IOrderDao;
import com.example.pcmallproviderorder.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderJob implements Job {

    @Autowired
    private IOrderDao orderDao;

    @Qualifier("orderServiceImpl")
    @Autowired
    private IOrderService orderService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String oid = jobDataMap.getString("orderOid");//获取创建订单的订单号
        Order order = orderDao.selectOne(new QueryWrapper<Order>().eq("oid", oid));
        if (order.getStatus() == Order.OrderState.PENDING_PAYMENT) {//如果订单未付款
            Integer result = orderService.cancelOrder(oid, 4);//取消订单
            if (result == 1) {
                log.debug("当前时间：{}，订单：{}付款超时，被取消！", jobExecutionContext.getFireTime(), oid);
            } else {
                log.debug("订单：{}，取消订单执行出错！", oid);
            }
        } else {
            log.debug("订单：{}，订单已付款或已取消！", oid);
        }
    }
}
