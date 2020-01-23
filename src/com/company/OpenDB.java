package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;


public class OpenDB {
  public static final OpenDB INSTANCE = new OpenDB();
  private static boolean databaseAlreadyOpen = false;
  RandomAccessFile configFile;
  RandomAccessFile dataFile;
  RandomAccessFile overflowFile;
  String filePrefix;

  private OpenDB() {
  }

  private static void setInstanceFiles(RandomAccessFile config, RandomAccessFile data, RandomAccessFile overflow) {
    INSTANCE.dataFile = data;
    INSTANCE.configFile = config;
    INSTANCE.overflowFile = overflow;
  }

  public static RandomAccessFile[] getInstanceFiles() {
    return new RandomAccessFile[]{INSTANCE.dataFile, INSTANCE.configFile, INSTANCE.overflowFile};
  }

  public static void closeDB() throws IOException {
    if (databaseAlreadyOpen) {
      System.out.println(INSTANCE.filePrefix + " database closed.\n");
      databaseAlreadyOpen = false;
      INSTANCE.dataFile.close();
      INSTANCE.configFile.close();
      INSTANCE.overflowFile.close();
      INSTANCE.dataFile = null;
      INSTANCE.configFile = null;
      INSTANCE.overflowFile = null;
      INSTANCE.filePrefix = null;
      SimpleDB.simpleMenu();
    } else {
      System.out.println("No database currently open, returning to main menu:\n\n");
      SimpleDB.simpleMenu();
    }
  }

  public static void openDB() throws IOException {
    if (!databaseAlreadyOpen) {
      INSTANCE.filePrefix = getFilePrefix();
      System.out.println("Please enter the prefix for an existing database triplet:");
      try {
        RandomAccessFile configFile = new RandomAccessFile(INSTANCE.filePrefix + ".config", "rw");
        RandomAccessFile dataFile = new RandomAccessFile(INSTANCE.filePrefix + ".data", "rw");
        RandomAccessFile overflowFile = new RandomAccessFile(INSTANCE.filePrefix + ".overflow", "rw");
        databaseAlreadyOpen = true;
        setInstanceFiles(configFile, dataFile, overflowFile);
        System.out.println(INSTANCE.filePrefix + " database is now open.\n");
        SimpleDB.simpleMenu();
      } catch (FileNotFoundException e) {
        System.out.println("One or more of the triplet of database files for the specified prefix is missing\nfrom the working directory is missing from the working directory\nreturning to Main Menu \n\n");
      }
    } else {
      System.out.println(INSTANCE.filePrefix + " database instance is already open. Please close the current database instance\nbefore trying to open another database instance\n\n Returning to Main Menu \n");
      SimpleDB.simpleMenu();
    }
  }

  private static String getFilePrefix() {
    Scanner getInput = new Scanner(System.in);
    return getInput.next();
  }
}
