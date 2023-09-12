package com.vjiki.pdf.gpt.chat.model.converter;

import java.util.List;
import lombok.Data;

@Data
public class PdfConverterResponse {
  private List<ConvertedItem> convertedPdfs;

  public PdfConverterResponse(
      List<ConvertedItem> convertedCodes) {
    this.convertedPdfs = convertedCodes;
  }
}
