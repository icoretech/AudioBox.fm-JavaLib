package fm.audiobox.core;

import java.util.List;

import org.apache.http.NameValuePair;

import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;

public class Connector implements IConnector {

  @Override
  public IConnectionMethod get(IEntity destEntity, String action, List<NameValuePair> params) {
    return null;
  }

  @Override
  public IConnectionMethod get(IEntity destEntity, String action) {
    return null;
  }

  @Override
  public IConnectionMethod put(IEntity destEntity, String action) {
    return null;
  }

  @Override
  public IConnectionMethod post(IEntity destEntity, String action) {
    return null;
  }

  @Override
  public IConnectionMethod delete(IEntity destEntity, String action) {
    return null;
  }

}
