package com.vjiki.pdf.gpt.chat.service.converter;

import com.vjiki.pdf.gpt.chat.model.chatgpt.ChatRequest;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PdfConverterService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PdfConverterService.class);

  public ChatRequest generateTextStringFromPDF(String pdfFileName) {
    COSDocument cosDoc;
    PDDocument pdDoc;
    ChatRequest chatRequest = new ChatRequest();
    LOGGER.info("generating text for: " + pdfFileName);

    try {
      File f = new File(pdfFileName);

      String parsedText;
      PDFParser parser = new PDFParser(new RandomAccessFile(f, "r"));

      parser.parse();

      cosDoc = parser.getDocument();

      PDFTextStripper pdfStripper = new PDFTextStripper();
      pdDoc = new PDDocument(cosDoc);

      parsedText = pdfStripper.getText(pdDoc);
      // TODO: change the length
      chatRequest.setQuestion(
          "write short summary of the following text: " + parsedText.trim()
              .replaceAll("[\\r]", "")
              .replaceAll("[\\n]", "")
              .replace("...","")
              .replace("  ", " "));

      if (cosDoc != null) {
        cosDoc.close();
      }
      pdDoc.close();

      LOGGER.info("generating text for: " + pdfFileName + " done!");
    } catch (IOException e) {
      LOGGER.error("Failed to parse pdf: " + pdfFileName + " exception: " + e);
      chatRequest.setQuestion("who is the prettiest girl in the world?");
      LOGGER.info("Failed to generate text: " + e);
    }
    return chatRequest;
  }
}