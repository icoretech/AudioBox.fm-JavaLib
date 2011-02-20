package fm.audiobox.interfaces;

import fm.audiobox.core.exceptions.LoginException;


public interface ILoginExceptionHandler {

  /**
   * Method invoked while throwing a {@link LoginException}
   * @param loginException
   */
  public void handle(LoginException loginException);
  
  
}
