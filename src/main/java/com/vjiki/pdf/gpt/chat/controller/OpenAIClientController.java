package com.vjiki.pdf.gpt.chat.controller;

import com.vjiki.pdf.gpt.chat.model.chatgpt.ChatGPTResponse;
import com.vjiki.pdf.gpt.chat.model.chatgpt.ChatRequest;
import com.vjiki.pdf.gpt.chat.service.chatgpt.OpenAIClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class OpenAIClientController {

  private final OpenAIClientService openAIClientService;

  @PostMapping(value = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ChatGPTResponse chat(@RequestBody ChatRequest chatRequest) {
    return openAIClientService.chat(chatRequest);
  }
}
