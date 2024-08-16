package com.example.pcmallprovidercart.aspect;

import com.example.pcmallcommon.client.GoodsClient;
import com.example.pcmallcommon.model.Cart;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Aspect
@Component
public class CartServiceAspect {
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public CartServiceAspect() {
        log.debug("创建切面类对象：{}", this);
    }

    @Around("execution(* com.example.pcmallprovidercart.service.ICartService.searchCartById(..))")
    public Object searchCartAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("进入切面");
        log.debug("方法参数：{}", Arrays.toString(joinPoint.getArgs()));
        log.debug("方法签名：{}", joinPoint.getSignature());
        log.debug("执行。。。。");
        Cart result = (Cart) joinPoint.proceed();
        log.debug("结果类名：{}", result.getClass());
        log.debug("结果：{}", result);
        if (result == null) {
            return null;
        }
        CompletableFuture<Void> completableFuture = CompletableFuture
                .supplyAsync(() -> goodsClient.searchGoodsById(new HashMap<>() {{
                    put("isSearchBrand", true);
                    put("isSearchCategory", true);
                }}, result.getGid()).getData(), threadPoolTaskExecutor)
                .thenAccept(result::setGoods);
        completableFuture.join();
        log.debug("增强后结果：{}", result);
        return result;
    }

    @Around("execution(* com.example.pcmallprovidercart.service.ICartService.searchCartList(..))")
    public Object searchCartListAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("进入切面");
        log.debug("方法参数：{}", Arrays.toString(joinPoint.getArgs()));
        log.debug("方法签名：{}", joinPoint.getSignature());
        log.debug("执行。。。。");
        Object result = joinPoint.proceed();
        log.debug("结果类名：{}", result.getClass());
        log.debug("结果：{}", result);
        List<Cart> list = (List<Cart>) result;
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Cart cart : list) {
            //设置异步任务
            CompletableFuture<Void> completableFuture = CompletableFuture
                    .supplyAsync(() -> goodsClient.searchGoodsById(new HashMap<>() {{
                        put("isSearchBrand", true);
                        put("isSearchCategory", true);
                    }}, cart.getGid()).getData(), threadPoolTaskExecutor)
                    .thenAccept(cart::setGoods);
            futures.add(completableFuture);
        }
        //等待所有异步任务执行完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.debug("增强后结果：{}", list);
        return list;
    }
}
