/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import org.apache.commons.collections.map.MultiKeyMap;

abstract class Methods {

  public static final String REPLACE = "rep";
  public static final String PATCH = "pat";
  public static final String DELETE = "del";

  private static MultiKeyMap visitors = null;

  private static synchronized MultiKeyMap getVisitors() {
    if ( visitors == null ) {
      visitors = loadVisitors();
    }
    return visitors;
  }

  static Visitor getVisitor( String command, String type ) {
    Visitor visitor = (Visitor)(getVisitors().get( command, type ));
    if ( visitor == null ) {
      throw new IllegalArgumentException( "Invalid visitor type: " + type );
    }
    return visitor;
  }

  private static MultiKeyMap loadVisitors() {
    MultiKeyMap map = new MultiKeyMap();
    registerMethodVisitors( map, new MethodReplace() );
    registerMethodVisitors( map, new MethodPatch() );
    registerMethodVisitors( map, new MethodDelete() );
    return map;
  }

  private static void registerMethodVisitors( MultiKeyMap map, Method method ) {
    map.put( "extract", method.getName(), method.getExtract() );
    map.put( "update", method.getName(), method.getUpdate() );
    map.put( Args.CMD_COMMIT, method.getName(), method.getCommit() );
    map.put( Args.CMD_REVERT, method.getName(), method.getRevert() );
    map.put( Args.CMD_CLEANUP, method.getName(), method.getCleanup() );
  }

}
