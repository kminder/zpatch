/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.io.IOException;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JsonUtilTest {

  @Test
  public void testWriteToString() throws IOException {
    String str;
    Metadata.FileInfo fileInfo;

    fileInfo = new Metadata.FileInfo();
    str = JsonUtil.writeToString( fileInfo );
    assertThat( str, is("{\"typ\":\"rep\"}") );

    fileInfo = new Metadata.FileInfo();
    fileInfo.setSrc( "test-source" );
    str = JsonUtil.writeToString( fileInfo );
    assertThat( str, is("{\"src\":\"test-source\",\"typ\":\"rep\"}") );

  }

}
