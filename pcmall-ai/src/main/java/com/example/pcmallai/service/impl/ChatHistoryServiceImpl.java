package com.example.pcmallai.service.impl;

import com.example.pcmallai.dao.IChatHistoryDao;
import com.example.pcmallai.service.IChatHistoryService;
import com.example.pcmallcommon.model.ChatHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatHistoryServiceImpl implements IChatHistoryService {
    private final IChatHistoryDao chatHistoryDao;

    @Override
    public void insertChatHistory(ChatHistory chatHistory) {
        chatHistoryDao.insert(chatHistory);
    }
}
