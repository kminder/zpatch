/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class CommandBase implements Command {

  private String name;

  public CommandBase( String name ) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void init( ZipFile zipFile, Metadata.PatchInfo patchInfo ) throws Exception {
  }

  public void visit( ZipFile zip, Metadata.PatchInfo patchInfo, ZipEntry entry, Metadata.FileInfo fileInfo ) throws Exception {
    Visitor visitor = Methods.getVisitor( getName(), fileInfo.getTyp() );
    visitor.visit( zip, patchInfo, entry, fileInfo );
  }

  @Override
  public void done( ZipFile zipFile, Metadata.PatchInfo patchInfo ) throws Exception {
  }

}
