package fm.audiobox.interfaces;

import fm.audiobox.interfaces.IConnector.IConnectionMethod;


/**
 * This is the interface for managing the authentication process
 */
public interface IAuthenticationHandle {
  
  /**
   * Use this method for handling the authentication process.
   * <br />
   * You can set {@code headers} or add {@code querystring} to the url
   * 
   * @param request the {@link IConnectionMethod} used for this request
   */
  public void handle(IConnectionMethod request);

}
