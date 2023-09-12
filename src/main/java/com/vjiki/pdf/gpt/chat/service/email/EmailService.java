package com.vjiki.pdf.gpt.chat.service.email;

import com.vjiki.pdf.gpt.chat.model.converter.ConvertedItem;
import com.vjiki.pdf.gpt.chat.model.converter.PdfConverterResponse;
import com.vjiki.pdf.gpt.chat.service.converter.DownloadService;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);
  private final Clock clock;
  private final JavaMailSender emailSender;

  public EmailService(Clock clock, JavaMailSender emailSender) {
    this.clock = clock;
    this.emailSender = emailSender;
  }

  private void send(
      List<String> emailsTo, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("email@gmail.com");
    message.setTo(emailsTo.toArray(String[]::new));
    message.setSubject(subject);
    message.setText(text);
    try {
      LOGGER.info("sending email to: " + message.getFrom());
      emailSender.send(message);
      LOGGER.info("sending email to: " + message.getFrom() + " done!");
    } catch (MailException e) {
      LOGGER.error("Error when sending the email: " + e.getMessage());
    }
  }

  public void send(List<String> emailsTo, PdfConverterResponse pdfConverterResponse) {
    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    Date date = new Date(clock.millis());
    String subject = "Hello from PDF Chat! News for " + formatter.format(date);

    send(emailsTo, subject, getFormattedText(pdfConverterResponse));
  }

  public String getFormattedText(PdfConverterResponse pdfConverterResponse) {


    StringBuilder sb = new StringBuilder();
    for (ConvertedItem convertedPdf : pdfConverterResponse.getConvertedPdfs()) {
      sb.append("The new version of document is available:\n")
          .append(convertedPdf.getUrl())
          .append("\n\n")
          .append("The short summary from GPT chart is:\n")
          .append(convertedPdf.getChatGPTResponse().getChoices().get(0).getMessage().getContent())
          .append("\n");
    }

    return """
            Hello there!
            
            Here you can find the short information about new available documents:
            
            %s
            
            BR,
            PDF GPT Chat Notificator
            """.formatted(sb.toString());
  }
}