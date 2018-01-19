/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalLookupService;

import org.junit.Ignore;
import org.junit.Test;

public class NioTest {

  @Ignore
  @Test
  public void testCreate() throws Exception {
    File file = new File( "target", "test-file-create.tmp" );
    Path path = file.toPath();
    Files.deleteIfExists( path );
    FileSystem fs = FileSystems.getDefault();
    UserPrincipalLookupService principals = fs.getUserPrincipalLookupService();
    UserPrincipal owner = principals.lookupPrincipalByName( "guest" );
    GroupPrincipal group = principals.lookupPrincipalByGroupName( "everyone" );
    Files.createFile( path, PosixFilePermissions.asFileAttribute( PosixFilePermissions.fromString("rw-r--r--") ) );
    PosixFileAttributeView view = Files.getFileAttributeView( path, PosixFileAttributeView.class );
    view.setGroup( group );
    view.setOwner( owner );
  }

}
