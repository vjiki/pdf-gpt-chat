package com.vjiki.pdf.gpt.chat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;

class ReadPdfFileUnitTest {

  @Test
  public void givenSamplePdf_whenUsingApachePdfBox_thenCompareOutput() throws IOException {
    String expectedText = "Hello World!\n";

    File file = new File("src/test/resources/sample.pdf");
    PDDocument document = PDDocument.load(file);

    PDFTextStripper stripper = new PDFTextStripper();

    String text = stripper.getText(document);

    document.close();

    assertEquals(expectedText.trim(), text.trim());
  }

  @Test
  public void givenSamplePdf_whenUsingiTextPdf_thenCompareOutput() throws IOException {
    String expectedText = "Hello World!";

    PdfReader reader = new PdfReader("src/test/resources/sample.pdf");
    int pages = reader.getNumberOfPages();
    StringBuilder text = new StringBuilder();

    for (int i = 1; i <= pages; i++) {

      text.append(PdfTextExtractor.getTextFromPage(reader, i));

    }
    reader.close();
    assertEquals(expectedText, text.toString());

  }

}