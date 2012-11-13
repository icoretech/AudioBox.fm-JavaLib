package fm.audiobox.interfaces;

import fm.audiobox.interfaces.IConnector.IConnectionMethod;

public interface IAuthenticationHandle {
  
  public void handle(IConnectionMethod request);

}
