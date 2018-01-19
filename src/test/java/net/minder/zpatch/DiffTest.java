/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

public class DiffTest {

  @Ignore
  @Test
  public void testDiffPatchExec() throws IOException, InterruptedException {
    File originalFile = new File( "src/test/resources/test-diff-a.txt" );
    File updatedFile = new File( "src/test/resources/test-diff-b.txt" );
    File diffFile = new File( "target/test-diff.diff" );
    File patchedFile = new File( "target/test-diff-b.txt" );
    ProcessBuilder launcher = new ProcessBuilder( "diff", originalFile.getPath(), updatedFile.getPath() );
    launcher.redirectOutput( diffFile );
    launcher.redirectError( ProcessBuilder.Redirect.INHERIT );
    Process process = launcher.start();
    int status = process.waitFor();
    System.out.println( status );

    launcher = new ProcessBuilder( "patch", originalFile.getPath(), "-i", diffFile.getPath(), "-o", patchedFile.getPath() );
    launcher.redirectOutput( ProcessBuilder.Redirect.INHERIT );
    launcher.redirectError( ProcessBuilder.Redirect.INHERIT );
    process = launcher.start();
    status = process.waitFor();
    System.out.println( status );

  }


}
