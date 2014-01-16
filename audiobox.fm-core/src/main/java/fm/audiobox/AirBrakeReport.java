package fm.audiobox;

import java.util.HashMap;
import java.util.Map;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IReport;


public class AirBrakeReport implements IReport {

  private static Map<String, String> urls = new HashMap<String, String>();
  
  public static enum ENDPOINT {
    desktop,
    any
  }
  
  static {
    urls.put( ENDPOINT.desktop.toString(), "" );
  }
  
  public AirBrakeReport(IConfiguration configuration, ENDPOINT endpoint) {
  }
  
  public void report(String message, String extra) {
  }

  public void report(Throwable exception, String extra) {
  }

}
