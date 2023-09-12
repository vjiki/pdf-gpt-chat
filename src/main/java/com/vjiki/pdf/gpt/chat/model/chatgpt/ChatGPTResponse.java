package com.vjiki.pdf.gpt.chat.model.chatgpt;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class ChatGPTResponse implements Serializable {

  private String id;
  private String object;
  private String model;
  private Long created;
  private List<Choice> choices;
  private Usage usage;
}
