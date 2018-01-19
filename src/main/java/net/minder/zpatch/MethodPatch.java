/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MethodPatch extends MethodBase {

  @Override
  public String getName() {
    return Methods.PATCH;
  }

  public Visitor getUpdate() {
    return new Update();
  }

  private class Update implements Visitor {

    @Override
    public void visit( ZipFile zip, Metadata.PatchInfo patchInfo, ZipEntry entry, Metadata.FileInfo fileInfo ) throws Exception {
      File target = FileUtil.getFile( patchInfo, fileInfo, entry, States.NONE );
      File diff = FileUtil.getFile( patchInfo, fileInfo, entry, States.UPDATE );
      File commit = FileUtil.getFile( patchInfo, fileInfo, entry, States.COMMIT );
      patch( diff, target, commit );
      FileUtil.deleteFile( diff );
    }

    private void patch( File diff, File original, File patched ) throws Exception {
      List<String> command = Arrays.asList(
          "patch", original.getPath(),
          "-i", diff.getPath(),
          "-o", patched.getPath() );
      ProcessBuilder launcher = new ProcessBuilder( command );
      launcher.inheritIO();
      Process process = launcher.start();
      int status = process.waitFor();
      if ( status != 0 ) {
        throw new RuntimeException( "Patch failed:" + String.join( " ", command ) );
      }
    }
  }

}
