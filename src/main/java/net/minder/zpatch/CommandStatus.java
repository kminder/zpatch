/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CommandStatus extends CommandBase {

  public CommandStatus() {
    super( Args.CMD_STATUS );
  }

  @Override
  public void visit( ZipFile zip, Metadata.PatchInfo patchInfo, ZipEntry entry, Metadata.FileInfo fileInfo ) throws Exception {
    System.out.println( entry.getName() + ": " + JsonUtil.writeToString( fileInfo ) );
  }

}

