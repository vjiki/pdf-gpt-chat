package com.vjiki.pdf.gpt.chat.model.chatgpt;

import java.io.Serializable;
import lombok.Data;

@Data
public class ChatRequest implements Serializable {

  private String question;
}
