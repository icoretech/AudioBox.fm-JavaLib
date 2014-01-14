package fm.audiobox.core.exceptions;

/**
 * Created by keytwo on 15/01/14.
 */
public class ForbiddenException extends AudioBoxException {

  private static final long serialVersionUID = 1L;

  boolean paymentRequired = false;


  public ForbiddenException(int errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public ForbiddenException(int errorCode, String message, boolean isPaymentRequired) {
    this(errorCode, message);
    this.paymentRequired = isPaymentRequired;
  }

  @Override
  public void fireGlobally() {
    this.configuration.getDefaultForbiddenExceptionHandler().handle(this);
  }

  public boolean isPaymentRequired() {
    return this.paymentRequired;
  }
}
