/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CommandList extends CommandBase {

  private Map<String,Metadata.FileInfo> files;

  public CommandList() {
    super( Args.CMD_LIST );
  }

  @Override
  public void init( ZipFile zip, Metadata.PatchInfo patchInfo ) throws Exception {
    files = new HashMap<String,Metadata.FileInfo>();
  }

  @Override
  public void visit( ZipFile zipFile, Metadata.PatchInfo patchInfo, ZipEntry entry, Metadata.FileInfo fileInfo ) throws Exception {
    files.put( entry.getName(), fileInfo );
  }

  @Override
  public void done( ZipFile zip, Metadata.PatchInfo patchInfo ) throws Exception {
    Metadata metadata = new Metadata();
    metadata.setPatch( patchInfo );
    metadata.setFiles( files );
    PrintWriter writer = new PrintWriter( System.out );
    YamlUtil.writeToStream( metadata, writer );
    writer.println();
    writer.flush();
  }

}

