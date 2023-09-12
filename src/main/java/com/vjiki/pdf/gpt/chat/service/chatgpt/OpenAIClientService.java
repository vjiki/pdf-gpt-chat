package com.vjiki.pdf.gpt.chat.service.chatgpt;

import com.vjiki.pdf.gpt.chat.model.chatgpt.ChatGPTResponse;
import com.vjiki.pdf.gpt.chat.model.chatgpt.ChatRequest;
import com.vjiki.pdf.gpt.chat.model.chatgpt.ChatGPTRequest;
import com.vjiki.pdf.gpt.chat.model.chatgpt.Message;
import com.vjiki.pdf.gpt.chat.openaiclient.OpenAIClient;
import com.vjiki.pdf.gpt.chat.openaiclient.OpenAIClientConfig;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAIClientService {

  private final OpenAIClient openAIClient;
  private final OpenAIClientConfig openAIClientConfig;

  private final static String ROLE_USER = "user";

  public ChatGPTResponse chat(ChatRequest chatRequest) {
    Message message = Message.builder()
        .role(ROLE_USER)
        .content(chatRequest.getQuestion())
        .build();

    ChatGPTRequest chatGPTRequest = ChatGPTRequest.builder()
        .model(openAIClientConfig.getModel())
        .messages(Collections.singletonList(message))
        .build();

    return openAIClient.chat(chatGPTRequest);
  }
}
