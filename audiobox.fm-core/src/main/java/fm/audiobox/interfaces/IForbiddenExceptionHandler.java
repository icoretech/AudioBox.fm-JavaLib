package fm.audiobox.interfaces;

import fm.audiobox.core.exceptions.ForbiddenException;

/**
 * Created by keytwo on 15/01/14.
 */
public interface IForbiddenExceptionHandler {

  /**
   * Method invoked while throwing a {@link ForbiddenException}
   * @param forbiddenException the {@link ForbiddenException} that throws the error
   */
  public void handle(ForbiddenException forbiddenException);

}
