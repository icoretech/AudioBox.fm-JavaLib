/**
 * 
 */
package fm.audiobox.core.exceptions;

import org.apache.http.HttpStatus;


/**
 * @author fatshotty
 *
 */
public class PaymentException extends LoginException {

  private static final long serialVersionUID = 1L;

  public PaymentException(String message) {
    super(HttpStatus.SC_PAYMENT_REQUIRED, message);
  }

}
