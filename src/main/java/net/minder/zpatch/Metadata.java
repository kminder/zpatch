/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Metadata {

  private PatchInfo patch;
  private Map<String,FileInfo> files;

  public PatchInfo getPatch() {
    return patch;
  }

  public void setPatch( PatchInfo patch ) {
    this.patch = patch;
  }

  public void setFiles( Map<String,FileInfo> files ) {
    this.files = files;
  }

  public Map<String,FileInfo> getFiles() {
    return files;
  }

  @JsonInclude( JsonInclude.Include.NON_NULL)
  public static class PatchInfo {

    private String product = null;
    private String version = null;
    private String format = "1.0.0";

    public String getProduct() {
      return product;
    }

    public void setProduct( String product ) {
      this.product = product;
    }

    public String getVersion() {
      return version;
    }

    public void setVersion( String version ) {
      this.version = version;
    }

    public String getZpatch() {
      return format;
    }

    public void setZpatch( String format ) {
      this.format = format;
    }

  }

  @JsonInclude( JsonInclude.Include.NON_NULL)
  public static class FileInfo {

    private String type = null;
    private String mode = null;
    private String owner = null;
    private String group = null;
    private String src = null;

    public String getTyp() {
      String t = type;
      if ( t == null ) {
        t = Methods.REPLACE;
      }
      return t;
    }

    public void setTyp( String type ) {
      this.type = type;
    }

    public String getMod() {
      return mode;
    }

    public void setMod( String mode ) {
      this.mode = mode;
    }

    public String getOwn() {
      return owner;
    }

    public void setOwn( String owner ) {
      this.owner = owner;
    }

    public String getGrp() {
      return group;
    }

    public void setGrp( String group ) {
      this.group = group;
    }

    public String getSrc() {
      return src;
    }

    public void setSrc( String src ) {
      this.src = src;
    }

  }

}
