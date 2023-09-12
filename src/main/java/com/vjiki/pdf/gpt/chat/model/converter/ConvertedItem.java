package com.vjiki.pdf.gpt.chat.model.converter;

import com.vjiki.pdf.gpt.chat.model.chatgpt.ChatGPTResponse;
import lombok.Data;

@Data
public class ConvertedItem {
  private String url;
  private ChatGPTResponse chatGPTResponse;
}
