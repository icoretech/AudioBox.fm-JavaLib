package fm.audiobox.interfaces;

import fm.audiobox.core.exceptions.ServiceException;


public interface IServiceExceptionHandler {

  /**
   * Method invoked while throwing a {@link ServiceException}
   * @param serivceException the {@link ServiceException} that throws the error
   */
  public void handle(ServiceException serivceException);
  
  
}
