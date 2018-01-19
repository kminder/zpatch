/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

abstract class MethodBase implements Method {

  public Visitor getExtract() {
    return new Extract();
  }

  public Visitor getUpdate() {
    return new Update();
  }

  public Visitor getCommit() {
    return new Commit();
  }

  public Visitor getRevert() {
    return new Revert();
  }

  public Visitor getCleanup() {
    return new Cleanup();
  }

  private static class Extract implements Visitor {
    @Override
    public void visit( ZipFile zip, Metadata.PatchInfo patchInfo, ZipEntry entry, Metadata.FileInfo fileInfo ) throws Exception {
      File extract = FileUtil.getFile( patchInfo, fileInfo, entry, States.EXTRACT );
      try ( InputStream zis = zip.getInputStream( entry ) ) {
        FileUtil.writeFile( zis, extract, fileInfo );
      }
      File update = FileUtil.getFile( patchInfo, fileInfo, entry, States.UPDATE );
      FileUtil.moveFile( extract, update );
    }
  }

  private static class Update implements Visitor {
    @Override
    public void visit( ZipFile zip, Metadata.PatchInfo patchInfo, ZipEntry entry, Metadata.FileInfo fileInfo ) throws Exception {
      File update = FileUtil.getFile( patchInfo, fileInfo, entry, States.UPDATE );
      File commit = FileUtil.getFile( patchInfo, fileInfo, entry, States.COMMIT );
      FileUtil.moveFile( update, commit  );
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

  private static class Revert implements Visitor {
    @Override
    public void visit( ZipFile zip, Metadata.PatchInfo patchInfo, ZipEntry entry, Metadata.FileInfo fileInfo ) throws Exception {
      File target = FileUtil.getFile( patchInfo, fileInfo, entry, States.NONE );
      File delete = FileUtil.getFile( patchInfo, fileInfo, entry, States.DELETE );
      if ( delete.exists() ) {
        FileUtil.deleteFile( target );
        FileUtil.deleteFile( delete );
      } else {
        File stash = FileUtil.getFile( patchInfo, fileInfo, entry, States.STASH );
        if( target.exists() ) {
          FileUtil.moveFile( target, stash );
        }
        File backup = FileUtil.getFile( patchInfo, fileInfo, entry, States.BACKUP );
        FileUtil.moveFile( backup, target );
        if ( stash.exists() ) {
          FileUtil.deleteFile( stash );
        }
      }
    }
  }

  private static class Cleanup implements Visitor {
    @Override
    public void visit( ZipFile zip, Metadata.PatchInfo patchInfo, ZipEntry entry, Metadata.FileInfo fileInfo ) throws Exception {
      FileUtil.deleteFileIfExists( FileUtil.getFile( patchInfo, fileInfo, entry, States.EXTRACT ) );
      FileUtil.deleteFileIfExists( FileUtil.getFile( patchInfo, fileInfo, entry, States.UPDATE ) );
      FileUtil.deleteFileIfExists( FileUtil.getFile( patchInfo, fileInfo, entry, States.COMMIT ) );
      FileUtil.deleteFileIfExists( FileUtil.getFile( patchInfo, fileInfo, entry, States.BACKUP ) );
      FileUtil.deleteFileIfExists( FileUtil.getFile( patchInfo, fileInfo, entry, States.DELETE ) );
      FileUtil.deleteFileIfExists( FileUtil.getFile( patchInfo, fileInfo, entry, States.STASH ) );
    }
  }



}
