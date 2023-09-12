package com.vjiki.pdf.gpt.chat.controller;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.vjiki.pdf.gpt.chat.exception.FailedToProceedException;
import com.vjiki.pdf.gpt.chat.model.converter.PdfConverterRequest;
import com.vjiki.pdf.gpt.chat.model.converter.PdfConverterResponse;
import com.vjiki.pdf.gpt.chat.service.ProcessorService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping(value = "/pdf-converter")
public class PdfConverterController {

  private static final Logger LOGGER = LoggerFactory.getLogger(PdfConverterController.class);
  private final ProcessorService processorService;

  public PdfConverterController(
      ProcessorService processorService) {
    this.processorService = processorService;
  }

  @PostMapping(value = "/convert-pdfs", produces = MediaType.APPLICATION_JSON_VALUE)
  public PdfConverterResponse convertPdfs(@RequestBody PdfConverterRequest pdfConverterRequest) {
    if (CollectionUtils.isEmpty(pdfConverterRequest.getPdfs())) {
      LOGGER.info("pdf links input is null or empty");
      throw new IllegalArgumentException("pdf links input is null or empty");
    }

    if (CollectionUtils.isEmpty(pdfConverterRequest.getEmailsTo())) {
      LOGGER.info("emails list input is null or empty");
      throw new IllegalArgumentException("emails list input is null or empty");
    }

    LOGGER.info("pdfs to be converted size: " + pdfConverterRequest.getPdfs().size());
    Optional<PdfConverterResponse> optionalResponse = Optional.empty();
    try {
      optionalResponse = processorService.proceed(pdfConverterRequest.getPdfs(), pdfConverterRequest.getEmailsTo());
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }

    PdfConverterResponse response = optionalResponse.orElseThrow(() -> {
      LOGGER.info(String.valueOf(NO_CONTENT));
      throw new FailedToProceedException(String.format("Could not covert pdfs %s.", pdfConverterRequest.getPdfs()));
    });
    LOGGER.info("converting pdfs size: " + response.getConvertedPdfs().size() + " done!");

    return response;
  }
}
