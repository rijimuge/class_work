package com.company;

import java.util.Scanner;

public class simbleDB {
  enum Operation {
    CREATE_DATABASE {
      @Override
      public void apply() {
        System.out.println("success");
      }
    },
    OPEN_DATABASE {
      @Override
      public void apply() {

      }
    },
    CLOSE_DATABASE {
      @Override
      public void apply() {

      }
    },
    DISPLAY_RECORD {
      @Override
      public void apply() {

      }
    },
    UPDATE_RECORD {
      @Override
      public void apply() {

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
    public abstract void apply();
  }

  static void simpleMenu() {
    System.out.println("Pick enter one of the following operations (case sensitive):");
    for (Operation o : Operation.values()) {
      System.out.printf("%s%n", o);
    }
    Scanner scan = new Scanner(System.in);
    try {
      Operation toApply = Operation.valueOf(scan.next());
      toApply.apply();
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid database operation\n\n");
      simpleMenu();
    }
  }

  public static void main(String[] args) {
    //Call menu option
    simpleMenu();
  }
}
