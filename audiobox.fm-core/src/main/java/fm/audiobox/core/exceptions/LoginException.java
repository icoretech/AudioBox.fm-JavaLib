package fm.audiobox.core.exceptions;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HttpContext;

import fm.audiobox.core.models.User;


/**
 * This exception is thrown when a connection to AudioBox.fm service fails due to
 * an invalid user authentication or {@link User} has not a valid subscription state.
 */
public class LoginException extends AudioBoxException {
  
  private static final long serialVersionUID = 1L;

  private volatile transient HttpContext ctx;
  
  public LoginException(int errorCode, String message, HttpContext ctx){
    this(errorCode, message);
    this.ctx = ctx;
  }

  public LoginException(int errorCode, String message){
    super(errorCode, message);
    this.errorCode = errorCode;
  }

  public void fireGlobally() {
    this.configuration.getDefaultLoginExceptionHandler().handle( this );
  }

  public HttpContext getContext() {
    return this.ctx;
  }

  
  public boolean isPaymentError() {
    return this.getErrorCode() == HttpStatus.SC_PAYMENT_REQUIRED;
  }
  
  public boolean isUserForbidden() {
    return this.getErrorCode() == HttpStatus.SC_FORBIDDEN;
  }
  
  public boolean isCredentialError() {
    return this.getErrorCode() == HttpStatus.SC_UNAUTHORIZED;
  }
  
}
