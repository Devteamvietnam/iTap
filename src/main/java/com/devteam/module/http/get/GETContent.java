package com.devteam.module.http.get;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Getter @Setter
public class GETContent implements Serializable {
  private static final long serialVersionUID = 1L;

  private String fileName;
  private String mimeType;
  private long   size;
  private byte[] data;

  public GETContent(String fileName, byte[] data) {
    this.fileName = fileName;
    this.data     = data;
    this.size     = data.length;
  }

  public Resource createResource(HttpServletRequest request) {
    Resource resource = new GETContentResource(fileName, data);
    if(mimeType == null) {
      mimeType = request.getServletContext().getMimeType(fileName);
    }
    System.out.println("Mime Type = " + mimeType);
    return resource;
  }

  static public class GETContentResource extends ByteArrayResource {
    @Getter
    private String fileName;

    public GETContentResource(String fileName, byte[] byteArray) {
      super(byteArray);
      this.fileName = fileName;
    }
  }
}
