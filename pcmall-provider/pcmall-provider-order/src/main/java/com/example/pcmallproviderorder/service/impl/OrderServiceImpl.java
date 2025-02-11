package com.example.pcmallproviderorder.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.pcmallcommon.exception.SystemException;
import com.example.pcmallcommon.model.Order;
import com.example.pcmallcommon.response.ResponseCode;
import com.example.pcmallproviderorder.dao.IOrderDao;
import com.example.pcmallproviderorder.job.OrderJob;
import com.example.pcmallproviderorder.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    public OrderServiceImpl() {
        log.debug("创建Service对象：{}", this);
    }

    @Override
    public List<Order> searchOrderList(String searchValue, String uid, Integer type, Integer currentPage, Integer pageSize) {
        return orderDao.searchOrderList(searchValue, uid, type, (currentPage - 1) * pageSize, pageSize);
    }

    @Override
    public Long getRecordsFiltered(String searchValue, String uid, Integer type) {
        return orderDao.getRecordsFiltered(searchValue, uid, type);
    }

    @Override
    public String createOrder(String uid, Integer aid) {
        String oid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + RandomUtil.randomNumbers(6);
        while (orderDao.selectCount(new QueryWrapper<Order>().eq("oid", oid)) != 0) {//如果生成的订单号存在，则重新生成，直到不存在
            oid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + RandomUtil.randomNumbers(6);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("oid", oid);
        data.put("uid", uid);
        data.put("aid", aid);
        orderDao.createOrder(data);//执行存储过程
        Integer result = (Integer) data.get("result");//获取输出参数
        if (result == -4) {
            throw new SystemException(ResponseCode.ERROR);
        } else if (result == -3) {
            throw new SystemException(ResponseCode.CART_EMPTY_ERROR);
        } else if (result == -2) {
            throw new SystemException(ResponseCode.CART_GOODS_ERROR);
        } else if (result == -1) {
            throw new SystemException(ResponseCode.GOODS_NOT_ENOUGH_ERROR);
        } else if (result == 1) {
            //创建定时任务，15分钟自动关闭订单
            setSchedulerTask(oid);
        }
        return oid;
    }

    protected void setSchedulerTask(String oid) {
        JobDetail jobDetail = JobBuilder.newJob(OrderJob.class)
                .withIdentity(oid, "orderGroup")
                .usingJobData("orderOid", oid)
                .build();
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(15);
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(oid, "orderGroup")
                .startAt(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .build();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.scheduleJob(jobDetail, trigger);//添加订单定时任务
            log.debug("订单定时任务{}添加成功", scheduler);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void payOrder(String oid) {
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<Order>()
                .set("pay_time", LocalDateTime.now())
                .set("status", 1)
                .eq("oid", oid);
        if (orderDao.update(updateWrapper) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
        try {//删除对应订单的定时任务
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.unscheduleJob(new TriggerKey(oid, "orderGroup"));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendOrder(String oid) {
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<Order>()
                .set("send_time", LocalDateTime.now())
                .set("status", 2)
                .eq("oid", oid);
        if (orderDao.update(updateWrapper) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void finishOrder(String oid) {
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<Order>()
                .set("finish_time", LocalDateTime.now())
                .set("status", 3)
                .eq("oid", oid);
        if (orderDao.update(updateWrapper) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public void refundOrder(String oid) {
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<Order>()
                .set("status", 5)
                .eq("oid", oid);
        if (orderDao.update(updateWrapper) != 1) {
            throw new SystemException(ResponseCode.ERROR);
        }
    }

    @Override
    public Integer cancelOrder(String oid, Integer status) {
        return cancelOrderByProcedure(oid, status);
    }

    @Override
    public Integer cancelOrderByProcedure(String oid, Integer status) {
        try {//删除对应订单的定时任务
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.unscheduleJob(new TriggerKey(oid, "orderGroup"));
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("oid", oid);
        data.put("status", status);
        orderDao.cancelOrder(data);
        return (Integer) data.get("result");
    }
}
