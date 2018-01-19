/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

interface Visitor {

  void visit( ZipFile zip, Metadata.PatchInfo patchInfo, ZipEntry entry, Metadata.FileInfo fileInfo ) throws Exception;

}


