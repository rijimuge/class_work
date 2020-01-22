package com.company;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

/**
 * Create a new database from a csv file with 6 fields
 * reads and converts the csv file (e.g. file.csv) into 3 new files
 * like so: file.config, file.data and file.overflow
 * assumes that the records in the csv file are sorted in order of the primary key
 * file.config contains number of records, describes names and sizes of fields. The first field is the primary key.
 * file.data contains the records. One per line, fixed field sizes, there are no blank records
 * file.overflow is initially empty
 */
public class CreateDB {

  /**
   * Prompts the user for a the name of a .csv file to be converted to a database
   * @return Returns the filename specified by the user
   */
  static String getFileName() {
    System.out.println("Please enter the name of a CSV file to convert to the new DataBase");
    Scanner scan = new Scanner(System.in);
    return scan.next();
  }

  /**
   * calls getFileName() and creates the data, config and overflow file from the specified file
   */
  static void createDB () throws IOException {
    String fileName = getFileName();
    StringBuilder prefixSB = new StringBuilder();
    for (int i = 0; i < fileName.length() - 4; i++) {
      prefixSB.append(fileName.charAt(i));
    }
    String fileNamePrefix = prefixSB.toString();
    try {
      RandomAccessFile dataIn = new RandomAccessFile(fileName, "r");
      String[] fieldNames = dataIn.readLine().split(",");
      int numberOfRecords = createDataFile(dataIn, fileNamePrefix);
      createConfigFile(fieldNames, numberOfRecords, fileNamePrefix);
    } catch (FileNotFoundException e) {
      System.out.println("Invalid file name, returning to main menu: \n");
      SimpleDB.simpleMenu();
    }
  }

  private static int createDataFile(RandomAccessFile dataIn, String filePrefix) throws IOException {
    String nextLine = dataIn.readLine();
    RandomAccessFile dataFile = new RandomAccessFile(filePrefix + ".data", "rw");
    int size = 0;
    while (nextLine != null) {
      size++;
      String[] record = nextLine.split(",");
      dataFile.writeBytes(String.format("%-" + 7 + "s", record[0]));
      dataFile.writeBytes(String.format("%-" + 45 + "s", record[1]));
      dataFile.writeBytes(String.format("%-" + 10 + "s", record[2]));
      dataFile.writeBytes(String.format("%-" + 2 + "s", record[3]));
      dataFile.writeBytes(String.format("%-" + 5 + "s", record[4]));
      dataFile.writeBytes(String.format("%-" + 7 + "s", record[5]));
      nextLine = dataIn.readLine();
    }
    return size;
  }

  private static void createConfigFile (String[] fieldNames, int size, String filePrefix) {
    for (String s : fieldNames) {
      System.out.println(s);
    }
    System.out.println(size);
    System.out.println(filePrefix);
  }
}
