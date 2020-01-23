package com.company;

import java.io.IOException;
import java.util.Scanner;

public class SimpleDB {
  public static final int RECORD_SIZE = 77;
  enum Operation {
    CREATE_DATABASE {
      @Override
      public void apply() throws IOException {
        CreateDB.createDB();
      }
    },
    OPEN_DATABASE {
      @Override
      public void apply() throws IOException {
        if (OpenDB.getDataBaseOpenStatus()) {
          System.out.println(OpenDB.INSTANCE.filePrefix + " database instance is already open. Please close the current database instance\nbefore trying to open another database instance\n\n Returning to Main Menu \n");
          SimpleDB.simpleMenu();
        } else {
          OpenDB.openDB();
        }
      }
    },
    CLOSE_DATABASE {
      @Override
      public void apply() throws IOException {
        if (OpenDB.getDataBaseOpenStatus()) {
          System.out.println("No database currently open, returning to main menu:\n\n");
          SimpleDB.simpleMenu();
        } else {
          OpenDB.closeDB();
        }

      }
    },
    DISPLAY_RECORD {
      @Override
      public void apply() throws IOException {
        if (OpenDB.getDataBaseOpenStatus()) {
          ReadDB.displayRecord();
          SimpleDB.simpleMenu();
        } else {
          System.out.println("No database currently open, returning to main menu:\n\n");
          SimpleDB.simpleMenu();
        }
      }
    },
    UPDATE_RECORD {
      @Override
      public void apply() throws IOException {
        if (OpenDB.getDataBaseOpenStatus()) {
          WriteDB.updateRecord();
          SimpleDB.simpleMenu();
        } else {
          System.out.println("No database currently open, returning to main menu:\n\n");
          SimpleDB.simpleMenu();
        }
      }
    },
    CREATE_RECORD {
      @Override
      public void apply() {

      }
    },
    ADD_RECORD {
      @Override
      public void apply() {

      }
    },
    DELETE_RECORD {
      @Override
      public void apply() {

      }
    },
    QUIT {
      @Override
      public void apply() {

      }
    };
    public abstract void apply() throws IOException;
  }

  static void simpleMenu() throws IOException {
    System.out.println("Pick enter one of the following operations (case sensitive):");
    for (Operation o : Operation.values()) {
      System.out.printf("%s%n", o);
    }
    Scanner operationInput = new Scanner(System.in);
    try {
      Operation operationToApply = Operation.valueOf(operationInput.next());
      operationToApply.apply();
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid database operation\n\n");
      simpleMenu();
    }
  }

  public static void main(String[] args) throws IOException {
    //Call menu option
    simpleMenu();
  }
}
