package com.company;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class ReadDB {
  static final int RECORD_SIZE = 76;
  static int NUM_RECORDS;
  static String[] fieldNames = new String[6];

  public static void initializeRead() throws IOException {
    String config = OpenDB.getConfigFile().readLine();
    NUM_RECORDS = Integer.parseInt(config.substring(0, 9).trim());
    OpenDB.INSTANCE.overflowCount = Integer.parseInt(config.substring(9,10));

    fieldNames[0] = config.substring(10, 22).trim();
    fieldNames[1] = config.substring(22, 34).trim();
    fieldNames[2] = config.substring(34, 46).trim();
    fieldNames[3] = config.substring(46, 58).trim();
    fieldNames[4] = config.substring(58, 70).trim();
    fieldNames[5] = config.substring(70, 82).trim();
  }
  public static void displayRecord() throws IOException {
    System.out.println("Please enter a name (primary key) to search for:");
    String id = getID();
    String record = binarySearch(OpenDB.getDataFile(), id);
    if (record.equals("NOT_FOUND")) {
      System.out.println("A record with the specified primary key was not found");
      return;
    }
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

  public static void createReport() throws IOException {
    int count = 0;
    int offset = 0;
    RandomAccessFile report = new RandomAccessFile(OpenDB.INSTANCE.filePrefix + ".report", "rw");
    while (count < 10) {
      String record = getRecord(OpenDB.getDataFile(), offset);
      count++;
      offset++;
      String[] fields = new String[6];
      fields[0] = record.substring(0,7);
      fields[1] = record.substring(7,52);
      fields[2] = record.substring(52,62);
      fields[3] = record.substring(62,64);
      fields[4] = record.substring(64,69);
      fields[5] = record.substring(69);
      StringBuilder toDisplay = new StringBuilder();
      for (int i = 0; i < 6; i ++) {
        toDisplay.append(fieldNames[i].substring(0, 10).trim());
        toDisplay.append(": ");
        toDisplay.append(fields[i].trim());
        toDisplay.append(" ");
      }
      toDisplay.append("\n");
      report.writeBytes(toDisplay.toString());
    }
    report.close();
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
    Din.seek(0); // return to the top of the file
    Din.skipBytes(recordNum * RECORD_SIZE);
    Din.read(recordInBytes);
    String toReturn = new String(recordInBytes);
    while (toReturn.substring(0,3).equals("DEL") && Din.getFilePointer() < Din.length()) {
      Din.read(recordInBytes);
      toReturn = new String(recordInBytes);
    }
    return toReturn;
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
      record = getRecord(Din, Middle);
      MiddleId = record.substring(7, 52).trim();
      int result = MiddleId.compareTo(id);
      if (result == 0)   // ids match
        Found = true;
      else if (result < 0)
        Low = Middle + 1;
      else
        High = Middle - 1;
    }
    if (Found) {
      return record;
    }
    else {
      byte[] checkOverflow = new byte[ReadDB.RECORD_SIZE];
      RandomAccessFile overflow = OpenDB.getOverflowFile();
      String overflowID;
      if (overflow.length() > 1) {
        for (long i = 0; i < OpenDB.getOverflowFile().length() / 76; i++) {
          System.out.println("checking overflow");
          overflow.seek(i * 76);
          System.out.println("Seeking");
          overflow.read(checkOverflow);
          System.out.println("Reading");
          System.out.println(checkOverflow.toString());
          overflowID = new String(checkOverflow).substring(7, 52).trim();
          System.out.println(overflowID);
          System.out.println(id);
          if (overflowID == id) {
            System.out.println("They match");
            return new String(checkOverflow);
          }
        }
      }
      return "NOT_FOUND";
    }
  }

  public static String getID() {
    Scanner in = new Scanner(System.in);
    return in.next();
  }


}