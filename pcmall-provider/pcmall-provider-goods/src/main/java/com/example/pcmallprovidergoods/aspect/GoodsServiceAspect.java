package com.example.pcmallprovidergoods.aspect;

import com.example.pcmallcommon.client.BrandClient;
import com.example.pcmallcommon.client.CategoryClient;
import com.example.pcmallcommon.model.Goods;
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
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@Aspect
public class GoodsServiceAspect {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public GoodsServiceAspect() {
        log.debug("创建切面类对象：{}", this);
    }

    @Around("execution(* com.example.pcmallprovidergoods.service.IGoodsService.searchGoodsById(java.util.Map,..))")
    public Object searchGoodsAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("进入切面");
        Object[] args = joinPoint.getArgs();
        log.debug("方法参数：{}", Arrays.toString(args));
        log.debug("方法签名：{}", joinPoint.getSignature());
        log.debug("执行。。。。");
        Goods result = (Goods) joinPoint.proceed();
        if (result == null) {
            return null;
        }
        log.debug("结果类名：{}", result.getClass());
        log.debug("结果：{}", result);
        if (args[0] != null) {
            Map<String, Boolean> aspectRule = (Map<String, Boolean>) args[0];
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            if (aspectRule.get("isSearchBrand")) {
                CompletableFuture<Void> brandFuture = CompletableFuture
                        .supplyAsync(() -> brandClient.searchBrandById(result.getBid()).getData(), threadPoolTaskExecutor)
                        .thenAccept(result::setBrand);
                futures.add(brandFuture);
            }
            if (aspectRule.get("isSearchCategory")) {
                CompletableFuture<Void> categoryFuture = CompletableFuture
                        .supplyAsync(() -> categoryClient.searchCategoryById(result.getCid()).getData(), threadPoolTaskExecutor)
                        .thenAccept(result::setCategory);
                futures.add(categoryFuture);
            }
            //等待所有异步任务执行完成
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            log.debug("增强后结果：{}", result);
        }
        return result;
    }

    @Around("execution(java.util.List com.example.pcmallprovidergoods.service.IGoodsService.searchGoodsList(java.util.Map,..))")
    public Object searchGoodsListAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("进入切面");
        Object[] args = joinPoint.getArgs();
        log.debug("方法参数：{}", Arrays.toString(args));
        log.debug("方法签名：{}", joinPoint.getSignature());
        log.debug("执行。。。。");
        List<Goods> result = (List<Goods>) joinPoint.proceed();
        if (result == null) {
            return null;
        }
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Goods goods : result) {
            CompletableFuture<Void> brandFuture = CompletableFuture
                    .supplyAsync(() -> brandClient.searchBrandById(goods.getBid()).getData(), threadPoolTaskExecutor)
                    .thenAccept(goods::setBrand);
            futures.add(brandFuture);
            CompletableFuture<Void> categoryFuture = CompletableFuture
                    .supplyAsync(() -> categoryClient.searchCategoryById(goods.getCid()).getData(), threadPoolTaskExecutor)
                    .thenAccept(goods::setCategory);
            futures.add(categoryFuture);
        }
        //等待所有异步任务执行完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.debug("增强后结果：{}", result);
        return result;
    }
}
