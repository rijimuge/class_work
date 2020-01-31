package com.company;

import java.io.IOException;
import java.util.Scanner;

public class SimpleDB {
  public static final int RECORD_SIZE = 76;
  private static boolean toQuit = false;
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
        } else {
          System.out.println("No database currently open, returning to main menu:\n\n");
        }
      }
    },
    UPDATE_RECORD {
      @Override
      public void apply() throws IOException {
        if (OpenDB.getDataBaseOpenStatus()) {
          WriteDB.updateRecord();
        } else {
          System.out.println("No database currently open, returning to main menu:\n\n");
        }
      }
    },
    CREATE_REPORT {
      @Override
      public void apply() throws IOException {
        if (OpenDB.getDataBaseOpenStatus()) {
          ReadDB.createReport();
        } else {
          System.out.println("No database currently open, returning to main menu:\n\n");
        }
      }
    },
    ADD_RECORD {
      @Override
      public void apply() throws IOException {
        if (OpenDB.getDataBaseOpenStatus()) {
          WriteDB.addRecord();
        } else {
          System.out.println("No database currently open, returning to main menu:\n\n");
        }
      }
    },
    DELETE_RECORD {
      @Override
      public void apply() throws IOException {
        if (OpenDB.getDataBaseOpenStatus()) {
          WriteDB.deleteRecord();
        } else {
          System.out.println("No database currently open, returning to main menu:\n\n");
        }
      }
    },
    QUIT {
      @Override
      public void apply() {
        if (OpenDB.getDataBaseOpenStatus()) {
          System.out.println("A database is currently open, please close the database before quiting.\n Returning to main menu.\n");
        } else {
        System.out.println("Quitting database management system.");
        toQuit = true;
      }
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
    }
  }

  public static void main(String[] args) throws IOException {
    //Call menu option
    while (!toQuit) {
      simpleMenu();
    }
    if (OpenDB.getDataBaseOpenStatus()) {
      OpenDB.closeDB();
    }
  }
}
