/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class JsonUtil {

  static <T> T readFromString( Class<T> type, String str ) throws IOException {
    JsonFactory factory = new JsonFactory();
    ObjectMapper mapper = new ObjectMapper( factory );
    T value = mapper.readValue( str, type );
    return value;
  }

  static String writeToString( Object value ) throws IOException {
    StringWriter writer = new StringWriter();
    writeToStream( value, writer );
    return writer.toString();
  }

  static void writeToStream( Object value, Writer writer ) throws IOException {
    JsonFactory factory = new JsonFactory();
    ObjectMapper mapper = new ObjectMapper( factory );
    mapper.writeValue( writer, value );
  }

}
