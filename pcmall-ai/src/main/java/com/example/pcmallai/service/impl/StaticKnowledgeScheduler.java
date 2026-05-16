package com.example.pcmallai.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StaticKnowledgeScheduler implements ApplicationRunner {

    @Value("${rag.static.refresh-on-startup:true}")
    private Boolean fullRefreshOnStartup;

    private final StaticKnowledgeService staticKnowledgeService;

    @Override
    public void run(@NonNull ApplicationArguments args) {
        if (fullRefreshOnStartup) {
            log.info("启动时触发静态知识库全量刷新（异步执行）");
            staticKnowledgeService.fullRefreshDocument();
        }
    }

    @Scheduled(cron = "${rag.static.refresh-cron}")
    public void scheduledRefresh() {
        log.debug("定时触发静态知识库增量刷新");
        staticKnowledgeService.refreshDocument();
    }
}