package com.vjiki.pdf.gpt.chat.service;


import com.vjiki.pdf.gpt.chat.model.chatgpt.ChatGPTResponse;
import com.vjiki.pdf.gpt.chat.model.chatgpt.ChatRequest;
import com.vjiki.pdf.gpt.chat.model.converter.ConvertedItem;
import com.vjiki.pdf.gpt.chat.model.converter.PdfConverterResponse;
import com.vjiki.pdf.gpt.chat.service.chatgpt.OpenAIClientService;
import com.vjiki.pdf.gpt.chat.service.converter.DownloadService;
import com.vjiki.pdf.gpt.chat.service.converter.PdfConverterService;
import com.vjiki.pdf.gpt.chat.service.email.EmailService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessorService {
  private static final Logger LOGGER = LoggerFactory.getLogger(PdfConverterService.class);
  private final PdfConverterService pdfConverterService;
  private final DownloadService downloadService;
  private final OpenAIClientService openAIClientService;
  private final EmailService emailService;


  public ProcessorService(PdfConverterService pdfConverterService, DownloadService downloadService,
      OpenAIClientService openAIClientService, EmailService emailService) {
    this.pdfConverterService = pdfConverterService;
    this.downloadService = downloadService;
    this.openAIClientService = openAIClientService;
    this.emailService = emailService;
    // TODO: how to do this correctly?
    downloadService.clean();
  }

  public Optional<PdfConverterResponse> proceed(List<String> urls, List<String> emailsTo) {
    PdfConverterResponse response = new PdfConverterResponse(new ArrayList<>());
    List<ConvertedItem> pdfOutputList = new ArrayList<>();

    LOGGER.info("proceeding: " + urls);

    urls.stream().parallel().forEach(url -> {
      String filename = downloadService.downloadFile(url);
      ChatRequest chatRequest = pdfConverterService.generateTextStringFromPDF(filename);
      ChatGPTResponse chatGPTResponse = openAIClientService.chat(chatRequest);
      ConvertedItem convertedItem = new ConvertedItem();
      convertedItem.setUrl(url);
      convertedItem.setChatGPTResponse(chatGPTResponse);
      pdfOutputList.add(convertedItem);
    });

    response.setConvertedPdfs(pdfOutputList);

    emailService.send(emailsTo, response);
    downloadService.clean();

    LOGGER.info("proceeding: " + urls + " done!");
    return Optional.of(response);
  }
}
