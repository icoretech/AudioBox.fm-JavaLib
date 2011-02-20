package fm.audiobox.core;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;

import fm.audiobox.interfaces.IConnector.IConnectionMethod;

public class RequestMethod implements IConnectionMethod {

  @Override
  public void send() {
    
  }

  @Override
  public void send(List<NameValuePair> params) {
    
  }

  @Override
  public void send(HttpEntity entity) {
    
  }

  @Override
  public void abort() {
    
  }

}
