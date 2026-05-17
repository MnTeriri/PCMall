package com.example.pcmallai.ai.memory.store;

import com.example.pcmallai.service.IChatHistoryService;
import com.example.pcmallcommon.model.ChatHistory;
import dev.langchain4j.data.message.*;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@RequiredArgsConstructor
public class PersistentChatMemoryStore implements ChatMemoryStore {

    private final ChatMemoryStore redisMemoryStore;
    private final IChatHistoryService chatHistoryService;
    private final ConcurrentMap<Object, Long> counter = new ConcurrentHashMap<>();

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        List<ChatMessage> messages = redisMemoryStore.getMessages(memoryId);
        log.debug("getMessages: memoryId = {}, messages = {}", memoryId, messages.size());
        return messages;
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        log.debug("updateMessages: memoryId = {}, messages = {}", memoryId, messages.size());
        if (counter.containsKey(memoryId)) {
            counter.put(memoryId, counter.get(memoryId) + 1);
        } else {
            Long msgIndex = chatHistoryService.searchLastMsgIndexByMemoryId(memoryId.toString());
            if (msgIndex != null) {
                counter.put(memoryId, msgIndex + 1);
            } else {
                counter.put(memoryId, 0L);
            }
        }
        // 存 Redis（最大只到窗口容量）
        redisMemoryStore.updateMessages(memoryId, messages);

        // 存 MySQL（增量存储）
        ChatHistory chatHistory = toChatHistory(memoryId, messages.getLast());
        chatHistoryService.insertChatHistory(chatHistory);
        log.debug("MySQL 增量追加: memoryId = {} , type = {}, msg_index = {}", memoryId, chatHistory.getType(), counter.get(memoryId));
    }

    @Override
    public void deleteMessages(Object memoryId) {
        log.debug("deleteMessages: memoryId = {}", memoryId);
        redisMemoryStore.deleteMessages(memoryId);
        chatHistoryService.deleteChatHistoryByMemoryId(memoryId.toString());
        counter.remove(memoryId);
    }

    private ChatHistory toChatHistory(Object memoryId, ChatMessage chatMessage) {
        switch (chatMessage) {
            case SystemMessage systemMessage -> {
                return new ChatHistory()
                        .setMemoryId(memoryId.toString())
                        .setMsgIndex(counter.get(memoryId))
                        .setType(ChatHistory.ChatHistoryType.SYSTEM)
                        .setContent(systemMessage.text())
                        .setRawJson(ChatMessageSerializer.messageToJson(systemMessage));
            }
            case UserMessage userMessage -> {
                return new ChatHistory()
                        .setMemoryId(memoryId.toString())
                        .setMsgIndex(counter.get(memoryId))
                        .setType(ChatHistory.ChatHistoryType.PACK_USER)
                        .setContent(userMessage.contents().toString())
                        .setRawJson(ChatMessageSerializer.messageToJson(userMessage));
            }
            case AiMessage aiMessage -> {
                return new ChatHistory()
                        .setMemoryId(memoryId.toString())
                        .setMsgIndex(counter.get(memoryId))
                        .setType(ChatHistory.ChatHistoryType.AI)
                        .setContent(aiMessage.text())
                        .setRawJson(ChatMessageSerializer.messageToJson(aiMessage));
            }
            default -> throw new IllegalStateException("Unexpected value: " + chatMessage);
        }
    }
}
