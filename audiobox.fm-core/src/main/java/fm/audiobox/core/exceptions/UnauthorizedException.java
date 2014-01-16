package fm.audiobox.core.exceptions;

import org.apache.http.HttpStatus;

import fm.audiobox.interfaces.IConnector.IConnectionMethod;


public class UnauthorizedException extends LoginException {

  private static final long serialVersionUID = 1L;
  
  private volatile transient IConnectionMethod request;

  public UnauthorizedException(String message) {
    super(HttpStatus.SC_UNAUTHORIZED, message);
  }
  
  public UnauthorizedException(String message, IConnectionMethod request) {
    super(HttpStatus.SC_UNAUTHORIZED, message);
    this.request = request;
  }
  
  public IConnectionMethod getRequest() {
    return this.request;
  }
  
}
