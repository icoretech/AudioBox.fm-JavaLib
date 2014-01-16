package fm.audiobox.core.exceptions;

import org.apache.http.HttpStatus;

/**
 * Created by keytwo on 15/01/14.
 */
public class ForbiddenException extends LoginException {

  private static final long serialVersionUID = 1L;

  public ForbiddenException(String message) {
    super(HttpStatus.SC_FORBIDDEN, message);
  }

}
