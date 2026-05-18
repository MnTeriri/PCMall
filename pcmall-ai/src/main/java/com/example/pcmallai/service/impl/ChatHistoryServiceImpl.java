package com.example.pcmallai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pcmallai.dao.IChatHistoryDao;
import com.example.pcmallai.service.IChatHistoryService;
import com.example.pcmallcommon.model.ChatHistory;
import com.example.pcmallcommon.model.vo.ChatHistoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatHistoryServiceImpl implements IChatHistoryService {
    private final IChatHistoryDao chatHistoryDao;

    @Override
    public void insertChatHistory(ChatHistory chatHistory) {
        chatHistoryDao.insert(chatHistory);
    }

    @Override
    public void deleteChatHistoryByMemoryId(String memoryId) {
        chatHistoryDao.delete(new QueryWrapper<ChatHistory>().eq("memory_id", memoryId));
    }

    @Override
    public List<ChatHistoryVO> listConversation(String memoryId) {
        QueryWrapper<ChatHistory> queryWrapper = new QueryWrapper<ChatHistory>()
                .eq("memory_id", memoryId)
                .in("type", ChatHistory.ChatHistoryType.USER, ChatHistory.ChatHistoryType.AI)
                .orderByDesc("id");
        return chatHistoryDao.selectList(queryWrapper)
                .stream()
                .map(history -> new ChatHistoryVO()
                        .setRole(history.getType() == ChatHistory.ChatHistoryType.USER ? "USER" : "AI")
                        .setContent(history.getContent())
                        .setCreateTime(history.getCreateTime()))
                .toList();
    }

    @Override
    public List<ChatHistory> listMemoryWindow(String memoryId, Integer maxMessages) {
        QueryWrapper<ChatHistory> queryWrapper = new QueryWrapper<ChatHistory>()
                .eq("memory_id", memoryId)
                .eq("type", ChatHistory.ChatHistoryType.SYSTEM);

        List<ChatHistory> chatMemory = chatHistoryDao.selectList(queryWrapper);
        if (chatMemory == null || chatMemory.isEmpty()) {
            return chatMemory;
        }

        queryWrapper = new QueryWrapper<ChatHistory>()
                .eq("memory_id", memoryId)
                .notIn("type", ChatHistory.ChatHistoryType.SYSTEM, ChatHistory.ChatHistoryType.USER)
                .orderByDesc("id")
                .last("LIMIT " + (maxMessages - 1));
        List<ChatHistory> recent = chatHistoryDao.selectList(queryWrapper);
        chatMemory.addAll(recent.reversed());

        return chatMemory;
    }

    @Override
    public Long searchLastMsgIndexByMemoryId(String memoryId) {
        return chatHistoryDao.searchLastMsgIndexByMemoryId(memoryId);
    }
}
