package com.devteam.util.dataformat;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class DataSerializer {
  final static public Charset        UTF8              = StandardCharsets.UTF_8;
  final static public DateFormat     COMPACT_DATE_TIME = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss 'GMT'Z");

  final static public DataSerializer JSON = new DataSerializer(new MappingJsonFactory()) ;
  final static public DataSerializer YAML = new DataSerializer(new YAMLFactory()) ;

  private final ObjectMapper mapper ;

  public DataSerializer(JsonFactory factory) {
    mapper = new ObjectMapper(factory); // can reuse, share globally
    configure(mapper) ;
  }

  public DataSerializer(JsonFactory factory, Module ... module) {
    this(factory) ;
    for(Module selModule : module) {
      mapper.registerModule(selModule);
    }
  }

  public void register(Module module) { mapper.registerModule(module); }

  public <T> byte[] toBytes(T idoc)  {
    try {
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      OutputStreamWriter writer = new OutputStreamWriter(outStream);
      mapper.writeValue(writer, idoc);
      writer.close() ;
      return outStream.toByteArray() ;
    } catch(IOException e) {
      throw new RuntimeException(e) ;
    }
  }

  public <T> T fromBytes(byte[] data, Class<T> type)  {
    try {
      ByteArrayInputStream inStream = new ByteArrayInputStream(data);
      Reader reader  = new InputStreamReader(inStream);
      return mapper.readValue(reader , type);
    } catch (IOException e) {
      throw new RuntimeException(e) ;
    }
  }

  public <T> T fromBytes(byte[] data, TypeReference<T> typeRef) {
    try {
      ByteArrayInputStream inStream = new ByteArrayInputStream(data);
      Reader reader  = new InputStreamReader(inStream);
      return mapper.readValue(reader , typeRef);
    } catch (IOException e) {
      throw new RuntimeException(e) ;
    }
  }

  public <T> String toString(T idoc) {
    if(idoc == null) return "" ;
    try  {
      ObjectWriter owriter  = mapper.writerWithDefaultPrettyPrinter() ;
      return owriter.writeValueAsString(idoc) ;
    } catch(IOException ex) {
      throw new RuntimeException(ex) ;
    }
  }

  public <T> T fromString(String data, Class<T> type) {
    try {
      StringReader reader = new StringReader(data) ;
      return mapper.readValue(reader , type);
    } catch (IOException e) {
      throw new RuntimeException(e) ;
    }
  }

  public <T> T fromInputStream(InputStream is, String encoding, Class<T> type) {
    try {
      Reader reader = new InputStreamReader(is, encoding) ;
      T value = mapper.readValue(reader , type);
      is.close();
      return value;
    } catch (IOException e) {
      throw new RuntimeException(e) ;
    }
  }

  public <T> T fromString(String data, TypeReference<T> typeRef) {
    try {
      StringReader reader = new StringReader(data) ;
      return mapper.readValue(reader , typeRef);
    } catch (IOException e) {
      throw new RuntimeException(e) ;
    }
  }

  public JsonNode fromString(String data) throws IOException {
    StringReader reader = new StringReader(data) ;
    return mapper.readTree(reader);
  }

  public <T> T clone(T obj) {
    return clone((Class<T>)obj.getClass(), obj);
  }

  public <T> T clone(Class<T> type, T obj) {
    try  {
      Writer writer = new StringWriter() ;
      ObjectWriter owriter  = mapper.writerWithDefaultPrettyPrinter() ;
      owriter.writeValue(writer, obj);
      String json =  writer.toString() ;
      return fromString(json, type);
    } catch(IOException ex) {
      throw new RuntimeException(ex) ;
    }
  }

  public <T> List<T> cloneList(List<T> list)  {
    ArrayList<T> holder = new ArrayList<>();
    for(T sel : list) {
      holder.add(clone(sel));
    }
    return holder;
  }

  static public <T> T jsonClone(T obj) { return JSON.clone(obj); }

  static public <T> T jsonClone(Class<T> type, T obj) { return JSON.clone(type, obj); }

  static public void configure(ObjectMapper mapper) {
    mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false) ;
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    mapper.setDateFormat(COMPACT_DATE_TIME) ;
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }
}