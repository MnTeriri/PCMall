package com.example.pcmallai.service;

import com.example.pcmallcommon.model.ChatHistory;
import com.example.pcmallcommon.model.vo.ChatHistoryVO;

import java.util.List;

public interface IChatHistoryService {
    void insertChatHistory(ChatHistory chatHistory);

    void deleteChatHistoryByMemoryId(String memoryId);

    List<ChatHistoryVO> listConversation(String memoryId);//用户对话记录（type = USER / AI）

    List<ChatHistory> listMemoryWindow(String memoryId);//Redis 恢复用的记忆窗口（type = SYSTEM / PACK_USER / AI）

    Long searchLastMsgIndexByMemoryId(String memoryId);
}
