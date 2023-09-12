package com.vjiki.pdf.gpt.chat.model.converter;

import java.util.List;
import lombok.Data;

@Data
public class PdfConverterRequest {
  private List<String> pdfs;
  private List<String> emailsTo;

}
