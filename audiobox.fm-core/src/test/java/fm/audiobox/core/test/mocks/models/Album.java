package fm.audiobox.core.test.mocks.models;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;

public class Album extends fm.audiobox.core.models.Album {

  private static final long serialVersionUID = 1L;

  public Album(IConnector connector, IConfiguration config) {
    super(connector, config);
    // TODO Auto-generated constructor stub
  }

  private String test;

  /**
   * @param test the test to set
   */
  public void setTest(String test) {
    this.test = test;
  }

  /**
   * @return the test
   */
  public String getTest() {
    return test;
  }

}
