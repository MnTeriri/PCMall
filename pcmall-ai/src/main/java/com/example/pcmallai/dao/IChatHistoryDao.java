package com.example.pcmallai.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.ChatHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatHistoryDao extends BaseMapper<ChatHistory> {
}
