/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

public abstract class States {

  public static final String NONE = null;
  public static final String EXTRACT = "extract";
  public static final String UPDATE = "update";
  public static final String COMMIT = "commit";
  public static final String BACKUP = "backup";
  public static final String DELETE = "delete";
  public static final String STASH = "stash";

}
