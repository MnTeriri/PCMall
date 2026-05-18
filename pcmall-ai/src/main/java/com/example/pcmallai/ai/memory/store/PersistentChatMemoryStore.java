package com.example.pcmallai.ai.memory.store;

import com.example.pcmallai.service.IChatHistoryService;
import com.example.pcmallcommon.model.ChatHistory;
import dev.langchain4j.data.message.*;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@RequiredArgsConstructor
public class PersistentChatMemoryStore implements ChatMemoryStore {

    private final ChatMemoryStore redisMemoryStore;
    private final IChatHistoryService chatHistoryService;
    private final Integer maxMessages;
    private final ConcurrentMap<Object, Long> counter = new ConcurrentHashMap<>();

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        // 1、先搜 Redis
        List<ChatMessage> messages = redisMemoryStore.getMessages(memoryId);
        if (messages.isEmpty()) {
            // 2、Redis 为空，搜 MySQL
            log.debug("Redis 历史记忆数据为空，尝试搜索 MySQL，memoryId = {}", memoryId);
            List<ChatHistory> memoryWindow = chatHistoryService.listMemoryWindow(memoryId.toString(), maxMessages);
            if (memoryWindow == null || memoryWindow.isEmpty()) {
                // 3、MySQL 为空，是新对话
                log.debug("MySQL 历史记忆数据为空，此轮对话为新对话，memoryId = {}", memoryId);
                return new ArrayList<>();
            } else {
                // 4、MySQL 不为空，保存到 Redis
                messages = memoryWindow
                        .stream()
                        .map(this::toChatMessage)
                        .toList();
                redisMemoryStore.updateMessages(memoryId, messages);
                log.debug("MySQL 历史记忆数据为不为空，恢复记忆，memoryId = {}, messages = {}", memoryId, messages.size());
            }
        }
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

    private ChatMessage toChatMessage(ChatHistory chatHistory) {
        return ChatMessageDeserializer.messageFromJson(chatHistory.getRawJson());
    }
}
