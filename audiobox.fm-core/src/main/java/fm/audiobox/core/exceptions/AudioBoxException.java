package fm.audiobox.core.exceptions;

import java.io.IOException;


public class AudioBoxException extends IOException {

  private static final long serialVersionUID = 1L;
  
  protected int errorCode = -1;
  protected Throwable cause;
  protected String message;

  
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
  
}
