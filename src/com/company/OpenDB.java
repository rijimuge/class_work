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
  int overflowCount;


  private OpenDB() {
  }

  public static RandomAccessFile getDataFile() {
    return INSTANCE.dataFile;
  }

  public static RandomAccessFile getConfigFile() {
    return INSTANCE.configFile;
  }

  public static RandomAccessFile getOverflowFile() {
    return INSTANCE.overflowFile;
  }

  public static boolean getDataBaseOpenStatus() {
    return databaseAlreadyOpen;
  }

  public static void setInstanceFiles(RandomAccessFile config, RandomAccessFile data, RandomAccessFile overflow) {
    INSTANCE.dataFile = data;
    INSTANCE.configFile = config;
    INSTANCE.overflowFile = overflow;
  }

  public static void closeDB() throws IOException {
    INSTANCE.configFile.seek(0);
    INSTANCE.configFile.writeBytes(String.format("%-" + 9 + "d", ReadDB.NUM_RECORDS));
    INSTANCE.configFile.writeBytes(String.valueOf(INSTANCE.overflowCount));
    System.out.println(INSTANCE.filePrefix + " database closed.\n");
    databaseAlreadyOpen = false;
    INSTANCE.dataFile.close();
    INSTANCE.configFile.close();
    INSTANCE.overflowFile.close();
    INSTANCE.dataFile = null;
    INSTANCE.configFile = null;
    INSTANCE.overflowFile = null;
    INSTANCE.filePrefix = null;
  }

  public static void openDB() throws IOException {
    System.out.println("Please enter the prefix for an existing database triplet:");
    INSTANCE.filePrefix = getFilePrefix();
    try {
      RandomAccessFile configFile = new RandomAccessFile(INSTANCE.filePrefix + ".config", "r");
      RandomAccessFile dataFile = new RandomAccessFile(INSTANCE.filePrefix + ".data", "r");
      RandomAccessFile overflowFile = new RandomAccessFile(INSTANCE.filePrefix + ".overflow", "r");
      configFile.close();
      dataFile.close();
      overflowFile.close();
      configFile = new RandomAccessFile(INSTANCE.filePrefix + ".config", "rw");
      dataFile = new RandomAccessFile(INSTANCE.filePrefix + ".data", "rw");
      overflowFile = new RandomAccessFile(INSTANCE.filePrefix + ".overflow", "rw");
      databaseAlreadyOpen = true;
      setInstanceFiles(configFile, dataFile, overflowFile);
      ReadDB.initializeRead();
      System.out.println(INSTANCE.overflowCount);
      System.out.println(INSTANCE.filePrefix + " database is now open.\n");
    } catch (FileNotFoundException e) {
      System.out.println("One or more of the triplet of database files for the specified prefix is missing\nfrom the working directory is missing from the working directory\nreturning to Main Menu \n\n");
    }
  }

  private static String getFilePrefix() {
    Scanner getInput = new Scanner(System.in);
    return getInput.next();
  }
}
