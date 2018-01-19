/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

abstract class YamlUtil {

  static <T> T readFromString( Class<T> type, String str ) throws IOException {
    ObjectMapper mapper = new ObjectMapper( new YAMLFactory() );
    T value = mapper.readValue( str, type );
    return value;
  }

  static String writeToString( Object value ) throws IOException {
    StringWriter writer = new StringWriter();
    writeToStream( value, writer );
    return writer.toString();
  }

  static void writeToStream( Object value, Writer writer ) throws IOException {
    YAMLFactory factory = new YAMLFactory();
    factory.disable( YAMLGenerator.Feature.WRITE_DOC_START_MARKER );
    factory.enable( YAMLGenerator.Feature.MINIMIZE_QUOTES );
    factory.disable( YAMLGenerator.Feature.SPLIT_LINES );
    ObjectMapper mapper = new ObjectMapper( factory );
    mapper.writeValue( writer, value );
  }

}
