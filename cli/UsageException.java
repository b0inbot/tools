package boinsoft.tools.cli;

/**
 * Exception to be thrown in custom code that tells the CLI library to print a message, then usage
 * details, and finally exit with a 1.
 */
public class UsageException extends Exception {
  public UsageException(String msg) {
    super(msg);
  }
}
