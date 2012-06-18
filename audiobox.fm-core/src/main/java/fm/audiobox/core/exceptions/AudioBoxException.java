package fm.audiobox.core.exceptions;

import java.io.IOException;


public class AudioBoxException extends IOException {

  private static final long serialVersionUID = 1L;
  
  /** Generic error code. Usually used to identify an undefined error */
  public static final int GENERIC_ERROR = -1;
  
  protected int errorCode = -1;
  protected Throwable cause;
  protected String message;
  protected boolean fireGlobally = true;

  
  public AudioBoxException(){
    super();
  }
  
  public AudioBoxException(String message){
    super(message);
    this.message = message;
  }
  
  public AudioBoxException(Throwable cause){
    super(cause);
    this.cause = cause;
  }
  
  
  public AudioBoxException(String message, Throwable cause){
    super(message,cause);
    this.message = message;
    this.cause = cause;
  }
  
  
  
  public int getErrorCode(){
    return errorCode;
  }
  
  public Throwable getCause() {
    return cause;
  }

  public String getMessage() {
    return message;
  }
  
  public void setFireGlobally(boolean value) {
    this.fireGlobally = value;
  }
  
  public boolean getFireGlobally() {
    return this.fireGlobally;
  }
  
  
}
