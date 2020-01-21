package com.company;
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
    RandomAccessFile dataIn = new RandomAccessFile(fileName, "r");

  }

  private static void createDataFile (String fieldNames, int size) {

  }
}
