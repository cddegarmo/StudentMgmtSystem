package appclasses;

public class StudentManagerException extends Exception {
   public StudentManagerException() {
      super();
   }

   public StudentManagerException(String message) {
      super(message);
   }

   public StudentManagerException(String message, Throwable cause) {
      super(message, cause);
   }
}
