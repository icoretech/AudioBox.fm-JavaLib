package fm.audiobox.core.exceptions;

import fm.audiobox.core.models.User;
import org.apache.http.protocol.HttpContext;


/**
 * This exception is thrown when a connection to AudioBox.fm service fails due to
 * an invalid user authentication or {@link User} has not a valid subscription state.
 */
public class LoginException extends AudioBoxException {
  
  private static final long serialVersionUID = 1L;

  private HttpContext ctx;
  
  public LoginException(int errorCode, String message, HttpContext ctx){
    this(errorCode, message);
    this.ctx = ctx;
  }

  private LoginException(int errorCode, String message){
    super(message);
    this.errorCode = errorCode;
  }

  public void fireGlobally() {
    this.configuration.getDefaultLoginExceptionHandler().handle( this );
  }

  public HttpContext getContext() {
    return this.ctx;
  }

  
}
