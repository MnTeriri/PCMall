package com.example.pcmallprovidergoods.aspect;

import com.example.pcmallcommon.client.BrandClient;
import com.example.pcmallcommon.client.CategoryClient;
import com.example.pcmallcommon.model.Goods;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class GoodsServiceAspect {

    private final BrandClient brandClient;

    private final CategoryClient categoryClient;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Around("execution(* com.example.pcmallprovidergoods.service.IGoodsService.searchGoodsById(..))")
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
        log.debug("结果：{}", result);
        Boolean isSearchCategory = (Boolean) args[1];
        Boolean isSearchBrand = (Boolean) args[2];
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        if (isSearchCategory) {
            CompletableFuture<Void> categoryFuture = CompletableFuture
                    .supplyAsync(() -> categoryClient.searchCategoryById(result.getCid()).getData(), threadPoolTaskExecutor)
                    .thenAccept(result::setCategory);
            futures.add(categoryFuture);
        }
        if (isSearchBrand) {
            CompletableFuture<Void> brandFuture = CompletableFuture
                    .supplyAsync(() -> brandClient.searchBrandById(result.getBid()).getData(), threadPoolTaskExecutor)
                    .thenAccept(result::setBrand);
            futures.add(brandFuture);
        }
        //等待所有异步任务执行完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.debug("增强完成");
        return result;
    }

    @Around("@annotation(com.example.pcmallprovidergoods.annotation.EnableExtraSearch)")
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
        log.debug("结果：{}", result);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Goods goods : result) {
            CompletableFuture<Void> categoryFuture = CompletableFuture
                    .supplyAsync(() -> categoryClient.searchCategoryById(goods.getCid()).getData(), threadPoolTaskExecutor)
                    .thenAccept(goods::setCategory);
            futures.add(categoryFuture);
            CompletableFuture<Void> brandFuture = CompletableFuture
                    .supplyAsync(() -> brandClient.searchBrandById(goods.getBid()).getData(), threadPoolTaskExecutor)
                    .thenAccept(goods::setBrand);
            futures.add(brandFuture);
        }
        //等待所有异步任务执行完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.debug("增强完成");
        return result;
    }
}
