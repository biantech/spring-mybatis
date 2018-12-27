package com.biantech.ssmd.utils;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author Jonathan Bian
 */

public class JsonUtil {

  private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

  static {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    SimpleModule module = new SimpleModule();
    module.addSerializer(Timestamp.class, new JsonSerializer<Timestamp>(){
      public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      gen.writeString(String.format("%s.%09d",DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(value.getTime()), value.getNanos()));
    } }
    );

    module.addSerializer(Date.class, new JsonSerializer<Date>(){
      public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException{
        gen.writeString(String.format("%s",DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(value)));
      } }
    );
    objectMapper.registerModule(module);
  }

  public static byte[] toJsonBytes(Object obj) {
    try {
      byte[] result = objectMapper.writeValueAsBytes(obj);
      return result;
    } catch (JsonProcessingException e) {
      throw new RuntimeException("create bytes json error", e);
    }
  }

  public static String toJsonString(Object object) {
    // StringWriter stringEmp = new StringWriter();
    try {
      return objectMapper.writeValueAsString(object);
      // objectMapper.writeValue(stringEmp, obj);
    } catch (IOException e) {
      throw new RuntimeException("create json error", e);
    }
    // return stringEmp.toString();
  }

  public static String toPrettyJsonString(Object object) {
    try {
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (IOException e) {
      throw new RuntimeException("create json error", e);
    }
  }

  public static <K, V> Map<K, V> toMap(Object obj) {
    try {
      // TypeReference<HashMap<K,V>> typeRef = new TypeReference<HashMap<K, V>>() {};
      return objectMapper.readValue(toJsonString(obj), new TypeReference<HashMap<K, V>>() {});
    } catch (IOException e) {
      throw new RuntimeException("create json error", e);
    }
  }

  public static <K, V> Map<K, V> parseByteJsonMap(byte[] packData, Class<K> clazzKey, Class<V> classValue) {
    try {
      JavaType javaType = objectMapper.getTypeFactory().constructMapType(HashMap.class, clazzKey, classValue);
      return objectMapper.readValue(packData, javaType);
    } catch (IOException e) {
      throw new RuntimeException("create json error", e);
    }
  }

  /*
   * public static <T> List<T> parserJsonList(InputStream instream, Class<T> clsT) { try {
   * 
   * JsonParser parser = objectMapper.getFactory().createParser(instream); //JsonParser parser =
   * objectMapper.getJsonFactory().createJsonParser(instream);
   * 
   * JsonNode nodes = parser.readValueAsTree();
   * 
   * List<T> list = new LinkedList<T>(); for (JsonNode node : nodes) { //objectMapper.readv //objectMapper.re
   * //list.add(objectMapper.readValue(node, clsT)); } return list; } catch (JsonParseException e) { throw new
   * CommonException("parse json error", e); } catch (IOException e) { throw new CommonException("parse json error", e);
   * } finally { try { instream.close(); } catch (Exception ignore) {
   * 
   * } } }
   */

  public static <T> List<T> parserJsonToList(byte[] sourceBytes, Class<T> clsT) {
    try {
      JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clsT);
      ArrayList<T> listValue = objectMapper.readValue(sourceBytes, javaType);
      return listValue;
    } catch (JsonParseException e) {
      throw new RuntimeException("parse json error str:", e);
    } catch (IOException e) {
      throw new RuntimeException("parse json error str:", e);
    }
  }

  public static <T> List<T> parserJsonList(String str, Class<T> clsT) {
    try {
      JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clsT);
      ArrayList<T> listValue = objectMapper.readValue(str, javaType);
      return listValue;
    } catch (JsonParseException e) {
      throw new RuntimeException("parse json error str:" + str, e);
    } catch (IOException e) {
      throw new RuntimeException("parse json error str:" + str, e);
    }
  }

  public static <T> LinkedHashMap<String, T> parserJsonMap(String str, Class<T> clsT) {
    LinkedHashMap<String, T> map = new LinkedHashMap<String, T>();
    try {
      // JsonParser parser = objectMapper.getFactory().createJsonParser(str);
      JsonParser parser = objectMapper.getFactory().createParser(str);
      // objectMapper.get
      JsonToken current;
      current = parser.nextToken();
      if (current != JsonToken.START_OBJECT) {
        throw new RuntimeException("parse json error: root should be object, quiting.");
      }
      while (parser.nextToken() != JsonToken.END_OBJECT) {
        String fieldName = parser.getCurrentName();
        current = parser.nextToken();
        T obj = parser.readValueAs(clsT);
        map.put(fieldName, obj);
      }
      return map;
    } catch (JsonParseException e) {
      throw new RuntimeException("parse json error str:" + str, e);
    } catch (IOException e) {
      throw new RuntimeException("parse json error str:" + str, e);
    }
  }

  /*
   * public static <T extends Enum<T>> EnumSet<T> parserJsonEnum(String str, Class<T> clsT) { try { JsonParser parser =
   * objectMapper.getJsonFactory().createJsonParser(str);
   * 
   * JsonNode nodes = parser.readValueAsTree();
   * 
   * EnumSet<T> enumSet = EnumSet.noneOf(clsT); for (JsonNode node : nodes) { enumSet.add(objectMapper.readValue(node,
   * clsT)); } return enumSet; } catch (JsonParseException e) { throw new CommonException("parse json error str:" + str,
   * e); } catch (IOException e) { throw new CommonException("parse json error str:" + str, e); } }
   */

  public static <T> T parserJson(InputStream instream, Class<T> cls) {
    try {

      // JsonParser parser = objectMapper.getJsonFactory().createJsonParser(instream);
      JsonParser parser = objectMapper.getFactory().createParser(instream);
      T t = objectMapper.readValue(parser, cls);
      return t;
    } catch (JsonParseException e) {
      throw new RuntimeException("parse json error", e);
    } catch (IOException e) {
      throw new RuntimeException("parse json error", e);
    } finally {
      try {
        instream.close();
      } catch (Exception ignore) {

      }
    }
  }

  public static <T> T parserJson(byte[] str, Class<T> cls) {
    try {
      return objectMapper.readValue(str, cls);
    } catch (JsonParseException e) {
      throw new RuntimeException("parse json error, str:", e);
    } catch (IOException e) {
      throw new RuntimeException("parse json error, str:", e);
    }
  }

  public static <T> T parserJson(String str, Class<T> cls) {
    try {
      return objectMapper.readValue(str, cls);
    } catch (JsonParseException e) {
      throw new RuntimeException("parse json error, str:" + str, e);
    } catch (IOException e) {
      throw new RuntimeException("parse json error, str:" + str, e);
    }
  }

  /*
   * public static String getJsonFromObject(Object object) { try { return objectMapper.writeValueAsString(object); }
   * catch (JsonGenerationException e) { throw new CommonException("get json error", e); } catch (JsonMappingException
   * e) { throw new CommonException("get json error", e); } catch (IOException e) { throw new
   * CommonException("get json error", e); } }
   */
}
