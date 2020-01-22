package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class OpenDB {
  public static void openDB() throws IOException {
    if (!SimpleDB.databaseAlreadyOpen) {
      String filePrefix = getFilePrefix();
      System.out.println("Please enter the prefix for an existing database triplet:");
      try {
        RandomAccessFile configFile = new RandomAccessFile(filePrefix + ".config", "rw");
        RandomAccessFile dataFile = new RandomAccessFile(filePrefix + ".data", "rw");
        RandomAccessFile overflowFile = new RandomAccessFile(filePrefix + ".overflow", "rw");
        SimpleDB.databaseAlreadyOpen = true;

      } catch (FileNotFoundException e) {
        System.out.println("One or more of the triplet of database files for the specified prefix is missing\nfrom the working directory is missing from the working directory\nreturning to Main Menu \n\n");
      }
    } else {
      System.out.println("A database instance is already open. Please close the current database instance\nbefore trying to open another database instance\n\n Returning to Main Menu \n");
      SimpleDB.simpleMenu();
    }
  }

  private static String getFilePrefix() {
    Scanner getInput = new Scanner(System.in);
    return getInput.next();
  }
}
