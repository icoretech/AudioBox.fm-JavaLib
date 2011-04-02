package fm.audiobox.core.test.mocks.models;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;

public class Track extends fm.audiobox.core.models.Track {
  public Track(IConnector connector, IConfiguration config) {
    super(connector, config);
    this.test = "TEST";
  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
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
