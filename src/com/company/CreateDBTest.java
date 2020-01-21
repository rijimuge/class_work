package com.company;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@DisplayName("CreateDB Tests")
class CreateDBTest {
  @DisplayName("Test of getFileName method")
  @Test
  void testgetFileName() {
    String testFileName = "the_file";
    //Load testFileName into input buffer and check that the getFileName() correctly retrieves it
    System.setIn( new ByteArrayInputStream(testFileName.getBytes()));
    assertEquals(testFileName, CreateDB.getFileName());
  }


}