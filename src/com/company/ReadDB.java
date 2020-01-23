package com.company;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ReadDB {
  static int RECORD_SIZE = 77;
  static int NUM_RECORDS;
  static String[] fieldNames = new String[6];

  public static void initializeRead() throws IOException {
    String config = OpenDB.getConfigFile().readLine();
    NUM_RECORDS = Integer.parseInt(config.substring(0, 10).trim());

    fieldNames[0] = config.substring(10, 20).trim();
    fieldNames[1] = config.substring(22, 32).trim();
    fieldNames[2] = config.substring(34, 44).trim();
    fieldNames[3] = config.substring(46, 56).trim();
    fieldNames[4] = config.substring(58, 68).trim();
    fieldNames[5] = config.substring(70, 80).trim();
    for (String s : fieldNames) {
      System.out.println(s);
    }
  }
  public static void displayReccord() throws IOException {

    initializeRead();
  }
  /**
  public static void main(String[] args) throws IOException {
    RandomAccessFile Din = new RandomAccessFile(FILENAME, "r");
    String record;

    record = getRecord(Din, 3);
    System.out.println("getRecord(3): \n" + record + "\n\n");

    record = getRecord(Din, 2);
    System.out.println("getRecord(2): \n" + record + "\n\n");

    record = getRecord(Din, 99999);
    System.out.println("getRecord(99999): \n" + record + "\n\n");

    record = binarySearch(Din, "00175");
    System.out.println("binarySearch(00175): \n" + record + "\n");

    Din.close();
  }
   */

  /*Get record number n-th (from 1 to 4360) */
  //public static String getRecord(RandomAccessFile Din, int recordNum) throws IOException
  public static String getRecord(RandomAccessFile Din, int recordNum) throws IOException {
    String record = "NOT_FOUND";
    if ((recordNum >= 1) && (recordNum <= NUM_RECORDS)) {
      Din.seek(0); // return to the top of the file
      Din.skipBytes(recordNum * RECORD_SIZE);
      record = Din.readLine();
    }
    return record;
  }

  /*Binary Search record id */
  public static String binarySearch(RandomAccessFile Din, String id) throws IOException {
    int Low = 0;
    int High = NUM_RECORDS - 1;
    int Middle;
    String MiddleId;
    String record = "NOT_FOUND";
    boolean Found = false;

    while (!Found && (High >= Low)) {
      Middle = (Low + High) / 2;
      record = getRecord(Din, Middle + 1);
      MiddleId = record.substring(0, 5);

      int result = MiddleId.compareTo(id);
      if (result == 0)   // ids match
        Found = true;
      else if (result < 0)
        Low = Middle + 1;
      else
        High = Middle - 1;
    }
    if (Found)
      return record;
    else
      return "NOT_FOUND";
  }


}