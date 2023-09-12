package com.vjiki.pdf.gpt.chat.openaiclient;

import com.vjiki.pdf.gpt.chat.model.chatgpt.ChatGPTRequest;
import com.vjiki.pdf.gpt.chat.model.chatgpt.ChatGPTResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "openai-service",
    url = "${openai-service.urls.base-url}",
    configuration = OpenAIClientConfig.class
)
public interface OpenAIClient {

  @PostMapping(value = "${openai-service.urls.chat-url}", headers = {"Content-Type=application/json"})
  ChatGPTResponse chat(@RequestBody ChatGPTRequest chatGPTRequest);
}
