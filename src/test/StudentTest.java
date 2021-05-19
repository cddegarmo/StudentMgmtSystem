package test;

import appclasses.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class StudentTest {

   @Test
   public void testGetters() {
      Student student = new Student(12345, "Jane", "Doe", 4);
      assertAll(
           () -> assertEquals(12345, student.getId()),
           () -> assertEquals("Jane", student.getFirstName()),
           () -> assertEquals("Doe", student.getLastName()),
           () -> assertEquals(4, student.getYear()),
           () -> assertEquals("Senior", student.getYearName(student.getYear())));
   }

   @Test
   public void testBalances() {
      Student student = new Student(12345, "John", "Doe", 3);
      student.setTuitionBalance(10000);
      assertEquals(10000, student.getTuitionBalance());
      student.payTuition(3465);
      assertEquals(6535, student.getTuitionBalance());
      student.payTuition(6545);
      assertEquals(-10, student.getTuitionBalance());
   }

   @Test
   public void testOverrides() {
      Student first = new Student(12345, "Jane", "Doe", 4);
      Student second = new Student(12345, "Jane", "Doe", 4);
      assertAll(
           () -> assertTrue(first.equals(second)),
           () -> assertEquals(first.hashCode(), second.hashCode()),
           () -> assertEquals("\nDoe, Jane : Senior", first.toString()));
   }
}