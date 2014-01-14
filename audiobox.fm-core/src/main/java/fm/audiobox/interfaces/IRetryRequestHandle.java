/**
 * 
 */
package fm.audiobox.interfaces;

import org.apache.http.client.HttpRequestRetryHandler;

import fm.audiobox.core.exceptions.AudioBoxException;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;


/**
 * @author fatshotty
 *
 */
public interface IRetryRequestHandle extends HttpRequestRetryHandler {

  
  /**
   * Use this method for handling the retry process.
   * <br />
   * 
   * @param exception the {@link AudioBoxException} thrown while executing request
   * @param request the {@link IConnectionMethod} that executes the request
   * @param executionCount the number of retry request
   * 
   * @return {@code true} if request should be re-sent. @{code false} if not
   */
  public boolean handle(final AudioBoxException exception, IConnectionMethod request, int executionCount);
}
