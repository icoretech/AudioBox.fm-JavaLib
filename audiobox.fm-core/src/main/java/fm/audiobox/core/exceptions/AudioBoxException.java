package fm.audiobox.core.exceptions;


public class AudioBoxException extends Throwable {

  private static final long serialVersionUID = 1L;
  
  protected int errorCode = -1;
  
  public AudioBoxException(){
    super();
  }
  
  
  public AudioBoxException(String message){
    super(message);
  }
  
  
  public AudioBoxException(Throwable cause){
    super(cause);
  }
  
  
  public AudioBoxException(String message, Throwable cause){
    super(message,cause);
  }
  
  
  public AudioBoxException(int code){
    errorCode = code;
  }
  
  public AudioBoxException(int code, String message){
    super(message);
    errorCode = code;
  }
  
  
  public int getErrorCode(){
    return errorCode;
  }
  
}
