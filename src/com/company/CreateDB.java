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
   * calls getFileName, createDataFile, createConfigFile, and createOverflowFile
   * to create the data, config and overflow file from the specified .csv file
   * @throws IOException to handle RandomAccessFile
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
      createOverflowFile(fileNamePrefix);
      System.out.println(fileNamePrefix + " database has been created.");
    } catch (FileNotFoundException e) {
      System.out.println("Invalid file name, returning to main menu: \n");
    }
  }

  /**
   * Writes the .csv record to a RandomAccessFile with name prefix + .data (suffix)
   * record fields are of fixed length
   * @param dataIn the .csv file being converted
   * @param filePrefix name prefix for file created
   * @return returns the number of data records created
   * @throws IOException to handle RandomAccessFile exceptions
   */
  private static int createDataFile(RandomAccessFile dataIn, String filePrefix) throws IOException {
    String nextLine = dataIn.readLine();
    RandomAccessFile dataOut = new RandomAccessFile(filePrefix + ".data", "rw");
    int size = 0;
    while (nextLine != null) {
      size++;
      String[] record = nextLine.split(",");
      dataOut.writeBytes(String.format("%-" + 7 + "s", record[0]).substring(0,7));
      dataOut.writeBytes(String.format("%-" + 45 + "s", record[1]).substring(0,45));
      dataOut.writeBytes(String.format("%-" + 10 + "s", record[2]).substring(0,10));
      dataOut.writeBytes(String.format("%-" + 2 + "s", record[3]).substring(0,2));
      dataOut.writeBytes(String.format("%-" + 5 + "s", record[4]).substring(0,5));
      dataOut.writeBytes(String.format("%-" + 7 + "s", record[5]).substring(0,7));
      nextLine = dataIn.readLine();
    }
    dataIn.close();
    dataOut.close();
    return size;
  }

  /**
   * config file contains number of records, describes names and sizes of fields. The first field is the primary key.
   * 10 bytes - number of records, followed by 6 * 12 Bytes in the following form: 10 bytes for field name, 2 bytes for
   * field size(in bytes)
   * @param fieldNames This is an array of Strings, that represent the names of the fields in the database
   *                   they are in the order they appear in, in the original .csv file and the database
   * @param numberOfRecords the number of records in the datafile, transcribed in the first 10 bytes of the file
   * @param fileNamePrefix the filename prefix of the database files, will be used to derive config filename
   */
  private static void createConfigFile (String[] fieldNames, int numberOfRecords, String fileNamePrefix) throws IOException {
    RandomAccessFile dataOut = new RandomAccessFile(fileNamePrefix + ".config", "rw");
    dataOut.writeBytes(String.format("%-" + 10 + "d", numberOfRecords));
    dataOut.writeBytes(String.format("%-" + 10 + "s", fieldNames[0]));
    dataOut.writeBytes(String.format("%-" + 2 + "d", 7));
    dataOut.writeBytes(String.format("%-" + 10 + "s", fieldNames[1]));
    dataOut.writeBytes(String.format("%-" + 2 + "d", 45));
    dataOut.writeBytes(String.format("%-" + 10 + "s", fieldNames[2]));
    dataOut.writeBytes(String.format("%-" + 2 + "d", 10));
    dataOut.writeBytes(String.format("%-" + 10 + "s", fieldNames[3]));
    dataOut.writeBytes(String.format("%-" + 2 + "d", 2));
    dataOut.writeBytes(String.format("%-" + 10 + "s", fieldNames[4]));
    dataOut.writeBytes(String.format("%-" + 2 + "d", 5));
    dataOut.writeBytes(String.format("%-" + 10 + "s", fieldNames[5]));
    dataOut.writeBytes(String.format("%-" + 2 + "d", 7));
    dataOut.close();
    for (String s : fieldNames) {
      System.out.println(s);
    }
  }

  /**
   * Creates a placeholder for insertions into database record
   * @param fileNamePrefix this is the filename prefix that will be used to generate the overflow filename
   * @throws IOException handles RandomAccessFile exceptions
   */
  private static void createOverflowFile(String fileNamePrefix) throws IOException {
    RandomAccessFile dataOut = new RandomAccessFile(fileNamePrefix + ".overflow", "rw");
    dataOut.close();
  }

  /**
   * Prompts the user for a the name of a .csv file to be converted to a database
   * @return Returns the filename specified by the user
   */
  static String getFileName() {
    System.out.println("Please enter the name of a CSV file to convert to the new DataBase");
    Scanner scan = new Scanner(System.in);
    return scan.next();
  }
}
