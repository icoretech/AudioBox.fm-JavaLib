package fm.audiobox.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.models.User;
import fm.audiobox.interfaces.IAuthenticationHandle;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;

public class DefaultAuthenticationHandle implements IAuthenticationHandle {
  
  private static final Logger log = LoggerFactory.getLogger( DefaultAuthenticationHandle.class );

  public void handle(IConnectionMethod request) {
    User user = request.getUser();
    if ( user != null && user.getAuthToken() != null ){
      request.addHeader( IConnector.X_AUTH_TOKEN_HEADER, user.getAuthToken() );
      if ( log.isDebugEnabled() ) {
        log.debug( "-> " + IConnector.X_AUTH_TOKEN_HEADER + ": ******" + user.getAuthToken().substring( user.getAuthToken().length() - 5 ) );
      }
    }
  }
  
}
