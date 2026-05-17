package com.example.pcmallai.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pcmallcommon.model.ChatHistory;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatHistoryDao extends BaseMapper<ChatHistory> {

    @Select("SELECT MAX(msg_index) FROM ai_chat_history WHERE memory_id=#{memoryId} AND msg_index>=0")
    Long searchLastMsgIndexByMemoryId(String memoryId);
}
