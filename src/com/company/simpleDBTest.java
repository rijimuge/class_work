package com.company;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Test of Main System output")
class simpleDBTest {
  private final boolean bool = true;
  @Test
  void trueTest() {
    assertTrue(bool);
  }
}