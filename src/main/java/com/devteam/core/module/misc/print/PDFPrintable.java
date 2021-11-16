package com.devteam.core.module.misc.print;

import java.io.ByteArrayOutputStream;

import com.devteam.core.util.error.ErrorType;
import com.devteam.core.util.error.RuntimeError;
import com.devteam.core.util.io.IOUtil;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.pdf.BaseFont;


public class PDFPrintable {
  private String fontDir ;
  
  public PDFPrintable(String fontDir) {
    this.fontDir = fontDir;
  }
  
  public byte[] createPDF(String xhtml) {
    try {
      ITextRenderer renderer = new ITextRenderer();
      String[] fontFiles = { "Times-New-Roman.ttf", "Arial.ttf", "Verdana.ttf" };

      for (int i = 0; i < fontFiles.length; i++) {
        String fontFile = fontDir + "/" + fontFiles[i];
        if (!IOUtil.hasResource(fontFile)) {
          fontFile = fontDir  + "/" + fontFiles[i];
        }
        renderer.getFontResolver().addFont(fontFile, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
      }
      renderer.setDocumentFromString(xhtml);
      renderer.layout();
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      renderer.createPDF(os, true);
      os.close();
      return  os.toByteArray();
    } catch(Exception ex) {
      System.out.println("#XHTML");
      System.out.println(xhtml);
      System.out.println("#XHTML");
      throw new RuntimeError(ErrorType.Unknown, ex);
    }
  }


  public PrintJob createPrintJob(String xhtml) {
    byte[] data = createPDF(xhtml);
    return new PrintJob("print", null, "hello-queue", "pdf/application", data, -1);
  }
}
