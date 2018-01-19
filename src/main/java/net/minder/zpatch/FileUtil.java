/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.Set;
import java.util.zip.ZipEntry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

abstract class FileUtil {

  static void deleteFile( File file ) throws IOException {
    Files.delete( file.toPath() );
  }

  static void deleteFileIfExists( File file ) throws IOException {
    Files.deleteIfExists( file.toPath() );
  }

  static void moveFile( File source, File target ) throws IOException {
    Files.move( source.toPath(), target.toPath() );//, StandardCopyOption.REPLACE_EXISTING );
  }

//  static void copyFile( File source, File target ) throws IOException {
//    Files.copy( source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES );
//  }

  static void writeFile( InputStream source, File target, Metadata.FileInfo fileInfo ) throws IOException {
    try ( OutputStream output = createFileForWrite( target, fileInfo ) ) {
      IOUtils.copy( source, output );
    }
  }

  static void createFile( File file, Metadata.FileInfo info ) throws IOException {
    FileUtils.forceMkdirParent( file );
    Path path = file.toPath();
    Files.createFile( path );
    setFileAttributes( path, info );
  }

  static void setFileAttributes( Path path, Metadata.FileInfo info ) throws IOException {
    if ( info != null ) {
      FileSystem fs = FileSystems.getDefault();
      UserPrincipalLookupService principals = fs.getUserPrincipalLookupService();
      PosixFileAttributeView view = Files.getFileAttributeView( path, PosixFileAttributeView.class );
      String permStr = info.getMod();
      if( permStr != null ) {
        Set<PosixFilePermission> permSet = PosixFilePermissions.fromString( permStr );
        view.setPermissions( permSet );
      }
      String groupName = info.getGrp();
      if( groupName != null ) {
        GroupPrincipal groupPrincipal = principals.lookupPrincipalByGroupName( "everyone" );
        view.setGroup( groupPrincipal );
      }
      String ownerName = info.getOwn();
      if( ownerName != null ) {
        UserPrincipal ownerPrincipal = principals.lookupPrincipalByName( "guest" );
        view.setOwner( ownerPrincipal );
      }
    }
  }

  static OutputStream createFileForWrite( File file ) throws IOException {
    return createFileForWrite( file, null );
  }

  static OutputStream createFileForWrite( File file, Metadata.FileInfo fileInfo ) throws IOException {
    createFile( file, fileInfo );
    FileOutputStream fos = new FileOutputStream( file );
    BufferedOutputStream bos = new BufferedOutputStream( fos );
    return bos;
  }

  static InputStream openFileForRead( File file ) throws FileNotFoundException {
    FileInputStream fis = new FileInputStream( file );
    BufferedInputStream bis = new BufferedInputStream( fis );
    return bis;
  }

  static File getFile( Metadata.PatchInfo patchInfo, Metadata.FileInfo fileInfo, ZipEntry entry, String state ) {
    if ( state == null ) {
      return new File( Main.args.getDirectoryFile(), entry.getName() );
    } else {
      return new File( Main.args.getDirectoryFile(), entry.getName() + "~" + state + "~" + patchInfo.getVersion() );
    }
  }

}
