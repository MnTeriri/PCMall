package com.example.pcmallai.controller;

import com.example.pcmallai.service.IChatHistoryService;
import com.example.pcmallcommon.model.vo.ChatHistoryVO;
import com.example.pcmallcommon.response.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ai/history")
@RequiredArgsConstructor
@Tag(name = "历史聊天记录接口")
public class ChatHistoryController {
    private final IChatHistoryService chatHistoryService;

    @Operation(summary = "搜索历史聊天")
    @GetMapping("/listConversation")
    public ResponseResult<List<ChatHistoryVO>> listConversation(String memoryId) {
        return ResponseResult.ok(chatHistoryService.listConversation(memoryId));
    }
}
