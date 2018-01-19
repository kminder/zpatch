/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Main {

  // patch --create --metadata <file> --catalog <file> --patch <file>
  // patch --apply --patchf<zip> (prepare>commit>clean, prepare>revert, prepare>commit>revert) --dir <dir>
  // patch --prepare --patch <zip> --dir <dir>
  // patch --commit --patch <zip> --dir <dir>
  // patch --revert --patch <zip> --dir <dir>
  // patch --clean --patch <zip> --dir <dir>

  // patch-metadata.properties
  //   patchVersion=x.y.z
  // patch-catalog.?
  //   For each file
  //     source location
  //     target location
  //     target user:group:permissions
  // {
  //   "target" : { "source": "location", "permissions": "xxx", "owner": "...", "group: "" }

  // --prepare
  // 1) Extract content of patch to files to {target}~commit~{patchVersion}
  // 2) Backup target files to {target}~revert~{patchVersion}

  // --commit
  // 1) Copy {base}~commit~{patchVersion} to {base}

  // --revert
  // 1) Copy {target}~revert~{patchVersion} to {target}

  // --cleanup
  // 1) Delete {target}~commit~{patchVersion}
  // 1) Delete {target}~revert~{patchVersion}

  static Args args;

  public static void main( String[] mainArgs ) throws Exception {
    try {
      args = new Args( mainArgs );
      if( args.isCreate() ) {
        createPatch();
      } else if( args.isApply() ) {
        applyPatch();
      } else if( args.isPrepare() ) {
        preparePatch();
      } else if( args.isList() ) {
        execute( new CommandList() );
      } else if( args.isStatus() ) {
        execute( new CommandStatus() );
      } else if( args.isExtract() ) {
        execute( new CommandBase( Args.CMD_EXTRACT ) );
      } else if( args.isUpdate() ) {
        execute( new CommandBase( Args.CMD_UPDATE ) );
      } else if( args.isCommit() ) {
        execute( new CommandBase( Args.CMD_COMMIT ) );
      } else if( args.isRevert() ) {
        execute( new CommandBase( Args.CMD_REVERT ) );
      } else if( args.isCleanup() ) {
        execute( new CommandBase( Args.CMD_CLEANUP ) );
      } else {
        Args.showHelpAndExit();
      }
    } catch ( Exception e ) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public static void applyPatch() throws Exception {
    preparePatch();
    finishPatch();
  }

  public static void preparePatch() throws Exception {
    try {
      execute( new CommandBase( Args.CMD_EXTRACT ) );
      execute( new CommandBase( Args.CMD_UPDATE ) );
    } catch ( Exception e ) {
      cleanupQuietly();
      throw e;
    }
  }

  public static void finishPatch() throws Exception {
    try {
      execute( new CommandBase( Args.CMD_COMMIT ) );
      cleanupQuietly();
    } catch ( Exception e ) {
      execute( new CommandBase( Args.CMD_REVERT ) );
      cleanupQuietly();
      throw e;
    }
  }

  private static void cleanupQuietly() {
    try {
      execute( new CommandBase( Args.CMD_CLEANUP ) );
    } catch ( Exception e ) {
      // Ignore it.
    }
  }

  private static void createPatch() throws IOException {
    Metadata metadata = readMetadataFromFile( args.getMetadataFile() );
    try ( OutputStream fos = FileUtil.createFileForWrite( args.getPatchFile() ) ) {
      ZipOutputStream zos = new ZipOutputStream( fos );
      String comment = JsonUtil.writeToString( metadata.getPatch() );
      zos.setComment( comment );
      for( Map.Entry<String, Metadata.FileInfo> file : metadata.getFiles().entrySet() ) {
        storeFileToZip( zos, file.getKey(), file.getValue() );
      }
      zos.close();
    }
  }

  private static void storeFileToZip( ZipOutputStream zos, String name, Metadata.FileInfo info ) throws IOException {
    File source = new File( args.getDirectoryFile(), info.getSrc() );
    try ( InputStream fis = FileUtil.openFileForRead( source ) ) {
      ZipEntry entry = new ZipEntry( name );
      String comment = JsonUtil.writeToString( info );
      entry.setComment( comment );
      zos.putNextEntry( entry );
      IOUtils.copy( fis, zos );
      zos.closeEntry();
    }
  }

  private static void execute( Command command ) throws Exception {
    ZipFile zipFile = new ZipFile( args.getPatchFile() );
    Metadata.PatchInfo patchInfo = JsonUtil.readFromString( Metadata.PatchInfo.class, zipFile.getComment() );
    command.init( zipFile, patchInfo );
    Enumeration<? extends ZipEntry> entries = zipFile.entries();
    while ( entries.hasMoreElements() ) {
      ZipEntry zipEntry = entries.nextElement();
      if ( !zipEntry.isDirectory() ) {
        String comment = zipEntry.getComment();
        Metadata.FileInfo fileInfo = JsonUtil.readFromString( Metadata.FileInfo.class, comment );
        command.visit( zipFile, patchInfo, zipEntry, fileInfo );
      }
    }
    command.done( zipFile, patchInfo );
    zipFile.close();
  }

  private static Metadata readMetadataFromFile( File file ) throws IOException {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    Metadata metadata = mapper.readValue( file, Metadata.class );
    System.out.println( ReflectionToStringBuilder.toString( metadata ) );
    return metadata;
  }

}
