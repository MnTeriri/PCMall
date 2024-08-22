package com.example.pcmallproviderorder.aspect;

import com.example.pcmallcommon.client.BrandClient;
import com.example.pcmallcommon.client.CategoryClient;
import com.example.pcmallcommon.model.Goods;
import com.example.pcmallcommon.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@Aspect
public class OrderServiceAspect {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Around("execution(java.util.List<com.example.pcmallcommon.model.Order> com.example.pcmallproviderorder.service.IOrderService.searchOrderList(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("进入切面");
        Object[] args = joinPoint.getArgs();
        log.debug("方法参数：{}", Arrays.toString(args));
        log.debug("方法签名：{}", joinPoint.getSignature());
        log.debug("执行。。。。");
        List<Order> result = (List<Order>) joinPoint.proceed();
        if (result == null) {
            return null;
        }
        log.debug("结果类名：{}", result.getClass());
        log.debug("结果：{}", result);
        for (Order order : result) {
            List<Goods> goodsList = order.getGoodsList();
            for (Goods goods : goodsList) {
                List<CompletableFuture<Void>> futures = new ArrayList<>();
                CompletableFuture<Void> brandFuture = CompletableFuture
                        .supplyAsync(() -> brandClient.searchBrandById(goods.getBid()).getData(), threadPoolTaskExecutor)
                        .thenAccept(goods::setBrand);
                futures.add(brandFuture);

                CompletableFuture<Void> categoryFuture = CompletableFuture
                        .supplyAsync(() -> categoryClient.searchCategoryById(goods.getCid()).getData(), threadPoolTaskExecutor)
                        .thenAccept(goods::setCategory);
                futures.add(categoryFuture);

                //等待所有异步任务执行完成
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            }
        }
        log.debug("增强后结果：{}", result);
        return result;
    }
}
