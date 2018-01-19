/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

interface Method {

  String getName();

  Visitor getExtract();

  Visitor getUpdate();

  Visitor getCommit();

  Visitor getRevert();

  Visitor getCleanup();

}
