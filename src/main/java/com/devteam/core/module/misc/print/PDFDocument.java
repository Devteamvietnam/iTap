package com.devteam.core.module.misc.print;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import com.devteam.core.util.io.IOUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDFDocument {
  private byte[]      data;

  private PDDocument  document;
  private PDFRenderer pdfRenderer;
  
  public PDFDocument(File file) throws Exception {
    this(IOUtil.getFileContentAsBytes(file));
  }

  public PDFDocument(InputStream is) throws Exception {
    this(IOUtil.getStreamContentAsBytes(is));
  }
  
  public PDFDocument(byte[] bytes) throws Exception {
    data = bytes;
  }
  
  public byte[] getRawData() { return data; }
  
  public PDDocument getPDDocument() { 
    checkInitPDDocument();
    return document; 
  }
  
  public int getNumberOfPages() { 
    checkInitPDDocument();
    return document.getNumberOfPages(); 
  }
  
  public BufferedImage getPageImage(int page, float zoom) throws Exception {
    checkInitPDDocument();
    BufferedImage image = pdfRenderer.renderImageWithDPI(page, 72 * zoom, ImageType.RGB);
    return image;
  }

  public float getPageWidth(int page) {
    checkInitPDDocument();
    return document.getPage(page).getBBox().getWidth();
  }

  public float getPageHeight(int page) {
    checkInitPDDocument();
    return document.getPage(page).getBBox().getHeight();
  }
  
  public void close() throws Exception {
    if(document == null) {
      document.close();
    }
  }
  
  private void checkInitPDDocument() {
    if(document == null) {
      try {
        document =    PDDocument.load(data);
        pdfRenderer = new PDFRenderer(document);
      } catch (Exception e) {
        throw new RuntimeException("Cannot load the pdf. Check the pdf data content", e);
      }
    }
  }
}