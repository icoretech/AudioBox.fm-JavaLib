package fm.audiobox.core.models;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;

public class CoverFlows extends MediaFiles {

  private static final long serialVersionUID = 1L;
  
  public CoverFlows(IConfiguration config) {
    super(config);
  }

  public String getApiPath() {
    return this.getParent().getApiPath() + IConnector.URI_SEPARATOR + "coverflow";
  }
  
  
}
