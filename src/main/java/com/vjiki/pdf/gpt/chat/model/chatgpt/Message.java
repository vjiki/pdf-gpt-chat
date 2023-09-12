package com.vjiki.pdf.gpt.chat.model.chatgpt;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message implements Serializable {

  private String role;
  private String content;
}
