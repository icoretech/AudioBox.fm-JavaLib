package fm.audiobox.configurations;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.AudioBoxException;

public class Response implements Serializable {

  private static Logger log = LoggerFactory.getLogger(Response.class);
  private static final long serialVersionUID = 1L;

  private int status;
  private String body;
  private InputStream in;
  
  /**
   * Used to store the {@link AudioBoxException} as generic exception.
   * This attribute is used while executing an asynchronous request
   */
  private AudioBoxException exception;
  
  
  /**
   * Default {@link Response} constructor.
   * 
   * @param status the {@link HttpStatus} code to set
   * @param body the response content body to set
   */
  public Response(int status, String body) {
    this.status = status;
    this.body = body;
  }
  
  /**
   * Response constructor given an {@link InputStream}
   * 
   * @param status the {@link HttpStatus} code to set
   * @param in the {@link InputStream} to read the body from
   */
  public Response(int status, InputStream in) {
    this.status = status;
    this.in = in;
  }
  
  /**
   * @return the response status
   */
  public int getStatus() {
    return status;
  }
  
  /**
   * @return the body of the response
   */
  public String getBody() {
    if (this.in != null && body == null) {
      try {
        streamToString();
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    }
    
    return body;
  }
  
  
  public InputStream getStream() {
    return this.in;
  }
  
  /**
   * Use this method to read the response from {@link InputStream}
   * @return the response body
   */
  public String streamToString() throws IOException{
    return this.body = Response.streamToString(this.in);
  }
  
  
  /**
   * @see {@link Response#exception}
   */
  public AudioBoxException getException() {
    return exception;
  }

  /**
   * @see {@link Response#exception}
   */
  public void setException(AudioBoxException exception) {
    this.exception = exception;
  }
  
  
  /**
   * Use this method to read the response from {@link InputStream}
   * @param in the {@link InputStream} to read response from
   * @return the response body
   */
  public static synchronized String streamToString(InputStream in) throws IOException {
    int read;
    byte[] bytes = new byte[ 1024 ];
    StringBuffer sb = new StringBuffer();
    while(  ( read = in.read( bytes) ) != -1 )
        sb.append( new String( bytes, 0, read ));
    
    return sb.toString();
  }
}
