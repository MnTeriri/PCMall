package com.example.pcmallai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 商品向量化初始化进度（持久化到 Redis）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEmbedProgress {

    private ProgressStatus status = ProgressStatus.IDLE;
    private Long totalCount = 0L;             // 商品总数
    private Long processedCount = 0L;         // 已处理数量（已入库向量数）
    private Integer currentPage = 0;          // 当前页码
    private LocalDateTime startTime;          // 开始时间
    private LocalDateTime endTime;            // 结束时间
    private String errorMessage;              // 失败信息

    public enum ProgressStatus {
        IDLE,       // 未启动
        RUNNING,    // 运行中
        COMPLETED,  // 已完成
        FAILED      // 失败
    }
}
