package fm.audiobox.interfaces;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;


public interface IServiceExceptionHandler {

  /**
   * Method invoked while throwing a {@link LoginException}
   * @param loginException
   */
  public void handle(ServiceException serivceException);
  
  
}
