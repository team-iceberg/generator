package iceberg.generator.exceptions;

/**
 * Exception thrown when an erro occur in a service
 */
public class ServiceException extends RuntimeException {

  /**
   * Constructor
   * @param message error message
   * @param cause stub exception
   */
  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructor
   * @param message error message
   */
  public ServiceException(String message) {
    super(message);
  }
}
