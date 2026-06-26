package com.example.pcmallai.service;

import com.example.pcmallai.model.GoodsEmbedProgress;

/**
 * 向量知识库通用接口。
 * 定义所有知识库（商品向量、静态文档向量）共有的生命周期操作。
 */
public interface IKnowledgeService {

    /**
     * 全量刷新：清空当前向量集合，从原始数据源全量重建。
     * 异步执行，进度可通过 {@link #getProgress()} 查询，通过 {@link #stopRefresh()} 请求终止。
     */
    void fullRefresh();

    /**
     * 增量刷新：仅处理自上次刷新以来发生变更的数据，避免全量重建。
     * <p>
     * 动态商品知识库：用于 RocketMQ 增量更新
     * 静态文档知识库：扫描 new 目录中的新文件，删除同名旧向量后重新 Embedding 写入，最后移至 old 目录。
     */
    void incrementalRefresh();

    void incrementalRefresh(Object data);

    /**
     * 请求停止正在执行的 {@link #fullRefresh()} 异步任务。
     * 仅标记停止信号，下一批次检查时生效，不强制中断线程。
     */
    void stopRefresh();

    /**
     * 清空向量集合中所有数据。原始数据源（DB / 文件）不受影响。
     */
    void clearAll();

    /**
     * 按标识符删除单条向量记录。
     *
     * @param key 商品侧为 goodsId 字符串（如 "42"），静态文档侧为文件名（如 "cpu-buying-guide.md"）
     */
    void deleteItem(String key);

    /**
     * 查询当前全量刷新任务的进度。无任务运行时返回 IDLE 状态。
     */
    GoodsEmbedProgress getProgress();

    /**
     * 设置全量刷新任务的进度。
     */
    void setProgress(GoodsEmbedProgress progress);
}
