package com.company;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class WriteDB {
 public static void updateRecord() throws IOException {
   System.out.println("Please enter a name (primary key) to search for:");
   String id = ReadDB.getID();
   String record = ReadDB.binarySearch(OpenDB.getDataFile(), id);
   if (record.equals("NOT_FOUND")) {
     System.out.println("A record with the specified primary key was not found");
     return;
   }
   String[] fields = new String[6];
   fields[0] = record.substring(0, 7);
   fields[1] = record.substring(7, 52);
   fields[2] = record.substring(52, 62);
   fields[3] = record.substring(62, 64);
   fields[4] = record.substring(64, 69);
   fields[5] = record.substring(69);
   StringBuilder toDisplay = new StringBuilder();
   for (int i = 0; i < 6; i++) {
     toDisplay.append(ReadDB.fieldNames[i].substring(0, 10).trim());
     toDisplay.append(": ");
     toDisplay.append(fields[i].trim());
     toDisplay.append(" ");
   }
   System.out.println(toDisplay);
   System.out.println("Please enter the (case sensitive) name of a field to update\nName (primary key) cannot be updated");
   Scanner fieldToUpdate = new Scanner(System.in);
   String toUpdate = fieldToUpdate.nextLine();
   switch (toUpdate) {
     case "RANK":
       System.out.println("Please enter a new value for the field (will be truncated to fit the field size):");
       fields[1] = String.format("%-" + 45 + "s", fieldToUpdate.nextLine()).substring(0, 45);
       break;
     case "CITY":
       System.out.println("Please enter a new value for the field (will be truncated to fit the field size):");
       fields[2] = String.format("%-" + 10 + "s", fieldToUpdate.nextLine()).substring(0, 10);
       break;
     case "STATE":
       System.out.println("Please enter a new value for the field (will be truncated to fit the field size):");
       fields[3] = String.format("%-" + 2 + "s", fieldToUpdate.nextLine()).substring(0, 2);
       break;
     case "ZIP":
       System.out.println("Please enter a new value for the field (will be truncated to fit the field size):");
       fields[4] = String.format("%-" + 5 + "s", fieldToUpdate.nextLine()).substring(0, 5);
       break;
     case "EMPLOYEES":
       System.out.println("Please enter a new value for the field (will be truncated to fit the field size):");
       fields[5] = String.format("%-" + 7 + "s", fieldToUpdate.nextLine()).substring(0, 7);
       break;
     default:
       System.out.println("Invalid field name, returning to main menu.");
       return;
   }
   System.out.println("Updated record:");
   toDisplay = new StringBuilder();
   for (int i = 0; i < 6; i++) {
     toDisplay.append(ReadDB.fieldNames[i].substring(0, 10).trim());
     toDisplay.append(": ");
     toDisplay.append(fields[i].trim());
     toDisplay.append(" ");
   }
   System.out.println(toDisplay);
   StringBuilder updatedRecord = new StringBuilder();
   for (int i = 0; i < 6; i++) {
     updatedRecord.append(fields[i]);
   }
   System.out.println(updatedRecord);
   binarySearchToWrite(OpenDB.getDataFile(), id, updatedRecord.toString());
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

  public static void writeRecord(RandomAccessFile Din, int recordNum, String updatedRecord) throws IOException {
      Din.seek(0); // return to the top of the file
      Din.skipBytes(recordNum * ReadDB.RECORD_SIZE);
      Din.writeBytes(updatedRecord);
  }

  /*Binary Search record id */
  public static void binarySearchToWrite(RandomAccessFile Din, String id, String updatedRecord) throws IOException {
    int Low = 0;
    int High = ReadDB.NUM_RECORDS - 1;
    int Middle;
    String MiddleId;
    boolean Found = false;
    String record;

    while (!Found && (High >= Low)) {
      Middle = (Low + High) / 2;
      record = ReadDB.getRecord(Din, Middle);
      MiddleId = record.substring(7, 52).trim();
      int result = MiddleId.compareTo(id);
      if (result == 0) {  // ids match
        Found = true;
        writeRecord(Din, Middle, updatedRecord);


        if (updatedRecord.substring(0,3).equals("DEL")) {
          System.out.println("Record Deleted");
          ReadDB.NUM_RECORDS--;
        }
        return;
      }
      else if (result < 0) {
        Low = Middle + 1;
      }
      else {
        High = Middle - 1;
      }
    }
    byte[] checkOverflow = new byte[ReadDB.RECORD_SIZE];
    RandomAccessFile overflow = OpenDB.getOverflowFile();
    String overflowID;
    if (overflow.length() > 1) {
      for (long i = 0; i < OpenDB.getOverflowFile().length() / 76; i++) {
        overflow.seek(i * 76);
        overflow.read(checkOverflow);
        overflowID = (new String(checkOverflow).substring(7, 52).trim());
        if (overflowID.equals(id)) {
          Found = true;
          writeRecord(overflow, (int) i, updatedRecord);
          if (updatedRecord.substring(0,3).equals("DEL")) {
            System.out.println("overflow Record Deleted");
            OpenDB.INSTANCE.overflowCount--;
          }
        }
      }
      if (!Found) {
        System.out.println("A record with the specified primary key was not found");
      }
    }
  }

  public static void deleteRecord() throws IOException {
    System.out.println("Please enter a name (primary key) to delete:");
    String id = ReadDB.getID();
    if (id.equals("001")) {
      System.out.println("equal");
    }
    binarySearchToWrite(OpenDB.getDataFile(), id, String.format("%-" + 76 + "s", "DEL"));
  }

  public static void addRecord() throws IOException {
    System.out.println("Please enter a value for the following fields:");
    Scanner fieldInput = new Scanner(System.in);
    StringBuilder newRecord = new StringBuilder();
    String pieceOfRecord;
    int fieldSize;
    for (int i = 0; i < 6; i ++) {
      System.out.println(ReadDB.fieldNames[i].substring(0,10).trim()+ ":");
      pieceOfRecord = fieldInput.nextLine();
      fieldSize = Integer.parseInt(ReadDB.fieldNames[i].substring(10));
      newRecord.append(String.format("%-" + fieldSize + "s", pieceOfRecord).substring(0, fieldSize));
    }
    if (!ReadDB.binarySearch(OpenDB.getDataFile(), newRecord.substring(7,52).trim()).equals("NOT_FOUND")) {
      System.out.println("Record with this primary key already exists.\n");
      return;
    }
    System.out.println("Record added\n");
    OpenDB.getOverflowFile().seek(OpenDB.getOverflowFile().length());
    OpenDB.getOverflowFile().writeBytes(newRecord.toString());
    OpenDB.INSTANCE.overflowCount++;

    if (OpenDB.INSTANCE.overflowCount > 4) {
      WriteDB.mergeOverflow();
    }
  }

  private static void mergeOverflow() throws IOException {
    String nextOverflow = getSmallestFromOverflow();
    String nextFromData = getNextFromData(true);
    File newData = new File("merge.tmp");
    RandomAccessFile mergeFile = new RandomAccessFile("merge.tmp", "rw");
    int count = 0;
    int result;
    while (count < ReadDB.NUM_RECORDS + 5) {
      if (nextOverflow.equals("EMPTY")) {
        mergeFile.writeBytes(nextFromData);
        nextFromData = getNextFromData(false);
        count++;
        continue;
      }
      if (nextFromData.equals("EMPTY")) {
        mergeFile.writeBytes(nextOverflow);
        nextOverflow = getSmallestFromOverflow();
        count++;
        continue;
      }
      result = nextOverflow.substring(7, 52).trim().compareTo(nextFromData.substring(7, 52).trim());
      if (result < 0 ) {
        mergeFile.writeBytes(nextOverflow);
        nextOverflow = getSmallestFromOverflow();
      } else {
        mergeFile.writeBytes(nextFromData);
        nextFromData = getNextFromData(false);
      }
      count++;
    }
    ReadDB.NUM_RECORDS+=5;
    RandomAccessFile updateConfig = OpenDB.getConfigFile();
    updateConfig.seek(0);
    updateConfig.writeBytes(String.format("%-" + 9 + "d", ReadDB.NUM_RECORDS));
    updateConfig.writeBytes("0");
    RandomAccessFile overflowToClose = OpenDB.getOverflowFile();
    RandomAccessFile dataToClose = OpenDB.getDataFile();
    overflowToClose.close();
    dataToClose.close();
    File overflowToDelete = new File(OpenDB.INSTANCE.filePrefix + ".overflow");
    File dataToDelete = new File(OpenDB.INSTANCE.filePrefix + ".data");
    overflowToDelete.delete();
    dataToDelete.delete();
    mergeFile.close();
    newData.renameTo(new File(OpenDB.INSTANCE.filePrefix + ".data"));
    File newOverflow = new File(OpenDB.INSTANCE.filePrefix + ".overflow");
    newOverflow.createNewFile();
    RandomAccessFile dataFile = new RandomAccessFile(OpenDB. INSTANCE.filePrefix + ".data", "rw");
    RandomAccessFile overflowFile = new RandomAccessFile(OpenDB.INSTANCE.filePrefix + ".overflow", "rw");
    OpenDB.setInstanceFiles(updateConfig, dataFile, overflowFile);
  }

  private static String getNextFromData(boolean started) throws IOException {
    byte[] recordRead = new byte[ReadDB.RECORD_SIZE];
    if (OpenDB.getDataFile().getFilePointer() == OpenDB.getDataFile().length()) {
      return "EMPTY";
    }
    if (started) {
      OpenDB.getDataFile().seek(0);
      OpenDB.getDataFile().read(recordRead);
      return new String(recordRead);
    } else {
      OpenDB.getDataFile().read(recordRead);
      String toReturn = new String(recordRead);
      while (toReturn.substring(0,3).equals("DEL")) {
        if (OpenDB.getDataFile().getFilePointer() == OpenDB.getDataFile().length()) {
          return "EMPTY";
        }
        OpenDB.getDataFile().read(recordRead);
        toReturn = new String(recordRead);
      }
      return toReturn;
    }
  }

  private static String getSmallestFromOverflow() throws IOException {
    if (OpenDB.INSTANCE.overflowCount == 0) {
      return "EMPTY";
    }
    int smallestIndex = 0;
    byte[] checkNextOverflowRecord = new byte[76];
    String nextOverflowRecord;
    OpenDB.getOverflowFile().seek(0);
    OpenDB.getOverflowFile().read(checkNextOverflowRecord);
    nextOverflowRecord = new String(checkNextOverflowRecord);
    while (nextOverflowRecord.substring(0,3).equals("DEL")) {
      smallestIndex++;
      OpenDB.getOverflowFile().seek(smallestIndex * 76);
      OpenDB.getOverflowFile().read(checkNextOverflowRecord);
      nextOverflowRecord = new String(checkNextOverflowRecord);
    }
    OpenDB.getOverflowFile().seek(smallestIndex * 76);
    OpenDB.getOverflowFile().read(checkNextOverflowRecord);
    String smallestRecord = new String(checkNextOverflowRecord);
    for (int i = smallestIndex + 1; i < OpenDB.getOverflowFile().length() / 76; i++) {
      OpenDB.getOverflowFile().seek(i * 76);
      OpenDB.getOverflowFile().read(checkNextOverflowRecord);
      nextOverflowRecord = new String(checkNextOverflowRecord);
      if (nextOverflowRecord.substring(0,3).equals("DEL")) {
        continue;
      }
      if (nextOverflowRecord.substring(7, 52).compareTo(smallestRecord.substring(7, 52)) < 0) {
        smallestIndex = i;
        smallestRecord = nextOverflowRecord;
      }
    }
    writeRecord(OpenDB.getOverflowFile(), smallestIndex, String.format("%-" + 76 + "s", "DEL"));
    OpenDB.INSTANCE.overflowCount--;
    return smallestRecord;
  }
}
