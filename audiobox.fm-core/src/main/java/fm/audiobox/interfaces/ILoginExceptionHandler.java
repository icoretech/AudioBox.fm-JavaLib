package fm.audiobox.interfaces;

import fm.audiobox.core.exceptions.LoginException;


public interface ILoginExceptionHandler {

  /**
   * Method invoked while throwing a {@link LoginException}
   * @param loginException the {@link LoginException} that throws the error
   */
  public void handle(LoginException loginException);
  
  
}
