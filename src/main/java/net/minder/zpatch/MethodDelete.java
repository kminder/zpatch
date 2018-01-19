/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MethodDelete extends MethodBase {

  public String getName() {
    return Methods.DELETE;
  }

  @Override
  public Visitor getExtract() {
    return new Extract();
  }

  @Override
  public Visitor getCommit() {
    return new Commit();
  }

  private static class Extract implements Visitor {
    @Override
    public void visit( ZipFile zip, Metadata.PatchInfo patchInfo, ZipEntry entry, Metadata.FileInfo fileInfo ) throws Exception {
      File extract = FileUtil.getFile( patchInfo, fileInfo, entry, States.UPDATE );
      FileUtil.createFile( extract, fileInfo );
    }
  }

  private static class Commit implements Visitor {
    @Override
    public void visit( ZipFile zip, Metadata.PatchInfo patchInfo, ZipEntry entry, Metadata.FileInfo fileInfo ) throws Exception {
      File target = FileUtil.getFile( patchInfo, fileInfo, entry, States.NONE );
      if ( target.exists() ) {
        File backup = FileUtil.getFile( patchInfo, fileInfo, entry, States.BACKUP );
        FileUtil.moveFile( target, backup );
      } else {
        File delete = FileUtil.getFile( patchInfo, fileInfo, entry, States.DELETE );
        FileUtil.createFile( delete, fileInfo );
      }
      File commit = FileUtil.getFile( patchInfo, fileInfo, entry, States.COMMIT );
      FileUtil.moveFile( commit, target );
    }
  }

}
