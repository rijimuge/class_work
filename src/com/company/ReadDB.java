package com.company;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class ReadDB {
  static int RECORD_SIZE = 76;
  static int NUM_RECORDS;
  static String[] fieldNames = new String[6];

  public static void initializeRead() throws IOException {
    String config = OpenDB.getConfigFile().readLine();
    NUM_RECORDS = Integer.parseInt(config.substring(0, 10).trim());

    fieldNames[0] = config.substring(10, 22).trim();
    fieldNames[1] = config.substring(22, 34).trim();
    fieldNames[2] = config.substring(34, 46).trim();
    fieldNames[3] = config.substring(46, 58).trim();
    fieldNames[4] = config.substring(58, 70).trim();
    fieldNames[5] = config.substring(70, 82).trim();
  }
  public static void displayRecord() throws IOException {
    initializeRead();
    String id = getID();
    String record = binarySearch(OpenDB.getDataFile(), id);
    String[] fields = new String[6];
    fields[0] = record.substring(0,7);
    fields[1] = record.substring(7,52);
    fields[2] = record.substring(52,62);
    fields[3] = record.substring(62,64);
    fields[4] = record.substring(64,69);
    fields[5] = record.substring(69);
    StringBuilder toDisplay = new StringBuilder();
    for (int i = 0; i < 6; i ++) {
      toDisplay.append(fieldNames[i].substring(0,10).trim());
      toDisplay.append(": ");
      toDisplay.append(fields[i].trim());
      toDisplay.append(" ");
    }
    System.out.println(toDisplay);

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
    byte[] recordInBytes = new byte[RECORD_SIZE];
    if ((recordNum >= 1) && (recordNum <= NUM_RECORDS)) {
      Din.seek(0); // return to the top of the file
      Din.skipBytes(recordNum * RECORD_SIZE);
      Din.read(recordInBytes);
    }
    return new String(recordInBytes);
  }

  /*Binary Search record id */
  public static String binarySearch(RandomAccessFile Din, String id) throws IOException {
    int Low = 0;
    int High = NUM_RECORDS - 1;
    int Middle;
    Integer MiddleId;
    Integer intid = Integer.parseInt(id);
    String record = "NOT_FOUND";
    boolean Found = false;

    while (!Found && (High >= Low)) {
      Middle = (Low + High) / 2;
      record = getRecord(Din, Middle + 1);
      MiddleId = Integer.parseInt(record.substring(0, 7).trim());
      int result = MiddleId.compareTo(intid);
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

  public static String getID() {
    Scanner in = new Scanner(System.in);
    return in.next();
  }


}