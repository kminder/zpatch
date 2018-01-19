/* Copyright (c) 2018. Oracle and/or its affiliates. All rights reserved. */
package net.minder.zpatch;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Args {

  private static Options OPTIONS = options();

  private static CommandLine args;

  public static final String CMD_HELP = "help";
  public static final String CMD_CREATE = "create";
  public static final String CMD_APPLY = "apply";
  public static final String CMD_LIST = "list";
  public static final String CMD_STATUS = "status";
  public static final String CMD_EXTRACT = "extract";
  public static final String CMD_UPDATE = "update";
  public static final String CMD_PREPARE = "prepare";
  public static final String CMD_FINISH = "finish";
  public static final String CMD_COMMIT = "commit";
  public static final String CMD_REVERT = "revert";
  public static final String CMD_CLEANUP = "cleanup";
  private static final String OPT_PATCH = "patch";
  private static final String OPT_METADATA = "metadata";
  private static final String OPT_DIR = "dir";
  private static final String OPT_FORCE = "force";

  private static Options options() {
    Options options = new Options();
    options.addOption( CMD_HELP, false, "Display this help text." );
    options.addOption( CMD_CREATE, false, "Create a patch from metadata." );
    options.addOption( CMD_LIST, false, "List the content of a patch." );
    options.addOption( CMD_STATUS, false, "Display the status of the current patching flow." );
    options.addOption( CMD_APPLY, false, "Apply the patch.  Revert on failure and cleanup." );
    options.addOption( CMD_PREPARE, false, "Prepare patch for commit or finish. Cleanup on failure." );
    options.addOption( CMD_FINISH, false, "Finish a prepared or extract and updated patch. Revert on failure and cleanup." );
    options.addOption( CMD_EXTRACT, false, "Extract patch content for updating." );
    options.addOption( CMD_UPDATE, false, "Update extracted patch content for committing." );
    options.addOption( CMD_COMMIT, false, "Commit updated patch content." );
    options.addOption( CMD_REVERT, false, "Revert committed patch changes." );
    options.addOption( CMD_CLEANUP, false, "Cleanup intermediate files." );
    options.addOption( OPT_PATCH, true, "Patch file to use or create. Defaults to patch.zip. Required." );
    options.addOption( OPT_METADATA, true, "Location of metadata file for patch creation. Required for create." );
    options.addOption( OPT_DIR, true, "Root directory for patch creation or appliation. Defaults to current directory." );
    options.addOption( OPT_FORCE, true, "Force the overwrite of left over phase files. Defaults to false." );
    return options;
  }

  static void showHelpAndExit() {
    HelpFormatter formater = new HelpFormatter();
    formater.printHelp( "java -jar zpatch.jar", OPTIONS, true );
    System.exit(1);
  }

  private static boolean validate( CommandLine args ) {
    boolean valid = true;
    return valid;
  }

  private static CommandLine parse( String[] args ) {
    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = null;
    try {
      cmd = parser.parse( OPTIONS, args);
      if ( !validate( cmd ) ) {
        showHelpAndExit();
      }
    } catch ( ParseException e ) {
      showHelpAndExit();
    }
    return cmd;
  }

  Args( String[] argv ) {
    args = parse( argv );
  }

  boolean isShowHelp() {
    return args.hasOption( CMD_HELP );
  }

  boolean isCreate() {
    return args.hasOption( CMD_CREATE );
  }

  boolean isStatus() {
    return args.hasOption( CMD_STATUS );
  }

  boolean isList() {
    return args.hasOption( CMD_LIST );
  }

  boolean isApply() {
    return args.hasOption( CMD_APPLY );
  }

  boolean isPrepare() {
    return args.hasOption( CMD_PREPARE );
  }

  boolean isFinish() {
    return args.hasOption( CMD_FINISH );
  }

  boolean isExtract() {
    return args.hasOption( CMD_EXTRACT );
  }

  boolean isUpdate() {
    return args.hasOption( CMD_UPDATE );
  }

  boolean isCleanup() {
    return args.hasOption( CMD_CLEANUP );
  }

  boolean isCommit() {
    return args.hasOption( CMD_COMMIT );
  }

  boolean isRevert() {
    return args.hasOption( CMD_REVERT );
  }

  boolean isForce() {
    return args.hasOption( OPT_FORCE );
  }

  String getDirectory() {
    return args.getOptionValue( OPT_DIR, System.getProperty( "user.dir" ) );
  }

  File getDirectoryFile() {
    return new File( getDirectory() );
  }

  String getMetadata() {
    return args.getOptionValue( OPT_METADATA );
  }

  File getMetadataFile() {
    return new File( getMetadata() );
  }

  String getPatch() {
    return args.getOptionValue( OPT_PATCH, "patch.zip" );
  }

  File getPatchFile() {
    return new File( getPatch() );
  }

}