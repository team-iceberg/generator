package iceberg.generator.exceptions;

/**
 * Exception thrown when there is an error in the memberships file
 */
public class MembershipFileException extends ServiceException {

  /**
   * Constructor
   * @param message error message
   */
  public MembershipFileException(String message) {
    super(message);
  }
}
