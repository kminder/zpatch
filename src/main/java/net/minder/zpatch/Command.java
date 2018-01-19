/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public interface Command {

  String getName();

  void init( ZipFile zip, Metadata.PatchInfo patchInfo ) throws Exception;

  void visit( ZipFile zip, Metadata.PatchInfo patchInfo, ZipEntry entry, Metadata.FileInfo fileInfo ) throws Exception;

  void done( ZipFile zip, Metadata.PatchInfo patchInfo ) throws Exception;

}
