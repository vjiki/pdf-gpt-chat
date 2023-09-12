package com.vjiki.pdf.gpt.chat.service.converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class DownloadService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);
  private final Path root = Paths.get("./uploads");

  public String downloadFile(String url) {
    LOGGER.info("downloading pdf file with url: " + url);

    String filename = url.substring(url.lastIndexOf("/") + 1);

    Path path = Paths.get("uploads/" + filename);

    WebClient webClient = WebClient.builder().baseUrl(url).build();

    Flux<DataBuffer> dataBufferFlux = webClient.get().accept(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL)
        .retrieve().bodyToFlux(DataBuffer.class);

    DataBufferUtils.write(dataBufferFlux, path, StandardOpenOption.CREATE).block();

    LOGGER.info("downloading pdf file with url: " + url + " to local filename: " + path + " done!");
    return path.toString();
  }

  private void init() {
    LOGGER.info("Initializing root dir: " + root);
    try {
      Files.createDirectory(root);
    } catch (IOException e) {
      LOGGER.error("Initializing root dir failed: " + e.getMessage());
    }
  }

  public void clean() {
    System.gc();
    deleteAll();
    init();
  }

  private void deleteAll() {
    LOGGER.info("Deleting all files in root dir: " + root);
    try {
      FileSystemUtils.deleteRecursively(root);
    } catch (IOException e) {
      LOGGER.error("Deleting all files in root dir: " + e.getMessage());
    }
    LOGGER.info("Deleting all files in root dir: " + root + " done");
  }
}
