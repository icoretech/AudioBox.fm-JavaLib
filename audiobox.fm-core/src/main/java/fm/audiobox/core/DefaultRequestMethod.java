package fm.audiobox.core;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;

import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;

public class DefaultRequestMethod implements IConnectionMethod {

  
  public DefaultRequestMethod(IConnector connector, HttpRequestBase method){
    
  }
  
  
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
