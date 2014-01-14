package fm.audiobox.configurations;

import java.io.IOException;

import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;

import fm.audiobox.core.exceptions.AudioBoxException;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IRetryRequestHandle;


/**
 * This is the default class used for retrying request that throws {@link IOException}
 */
public class DefaultRetryRequestHandle extends DefaultHttpRequestRetryHandler implements IRetryRequestHandle {


  public DefaultRetryRequestHandle(int count, boolean retry) {
    super(count, retry);
  }

  public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
    boolean result = false;

    if ( exception instanceof AudioBoxException ) {
      IConnectionMethod req = (IConnectionMethod) context.getAttribute( IConnectionMethod.class.getName() );
      result = this.handle((AudioBoxException) exception, req, executionCount);
    }

    if (!result) {
      result = super.retryRequest(exception, executionCount, context);
    }

    return result;
  }

  public boolean handle(AudioBoxException exception, IConnectionMethod request, int executionCount) {
    return false;
  }


}
