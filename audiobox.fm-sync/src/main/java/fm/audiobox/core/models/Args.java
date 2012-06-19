package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IEntity;

public class Args extends AbstractEntity implements Serializable{

  private static final long serialVersionUID = 1L;
  
  public static final String TAGNAME = "args";
  
  public static final String NAMESPACE = Args.TAGNAME;
  
  private String filename;
  private String etag;
  private long rangeMin;
  private long rangeMax;
  private String serverIp;
  private String serverPort;
  
  public Args(IConfiguration config) {
    super(config);
  }

  @Override
  public String getNamespace() {
    return NAMESPACE;
  }

  @Override
  public String getTagName() {
    return TAGNAME;
  }

  
  public String getFileName() {
    return filename;
  }

  public void setFileName(String filename) {
    this.filename = filename;
  }
  
  public String getServerIp() {
    return this.serverIp;
  }

  public void setServerIp(String ip) {
    this.serverIp = ip;
  }
  
  public String getServerPort() {
    return this.serverPort;
  }

  public void setServerPort(String port) {
    this.serverPort = port;
  }

  public long getRangeMin() {
    return rangeMin;
  }

  public void setRangeMin(long rangeMin) {
    this.rangeMin = rangeMin;
  }

  public long getRangeMax() {
    return rangeMax;
  }

  public void setRangeMax(long rangeMax) {
    this.rangeMax = rangeMax;
  }

  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.equals("filename") ){
      return this.getClass().getMethod("setFileName", String.class);
    } else if ( tagName.equals("rangeMin") ){
      return this.getClass().getMethod("setRangeMin", long.class);
    } else if ( tagName.equals("rangeMax") ){
      return this.getClass().getMethod("setRangeMax", long.class);
    } else if ( tagName.equals("etag") ){
      return this.getClass().getMethod("setEtag", String.class);
    } else if ( tagName.equals("server_ip") ){
      return this.getClass().getMethod("setServerIp", String.class);
    } else if ( tagName.equals("server_port") ){
      return this.getClass().getMethod("setServerPort", String.class);
    }
  
    return null;
    
  }

  @Override
  public String getApiPath() {
    return null;
  }

  @Override
  public void setParent(IEntity parent) {
  }

  @Override
  protected void copy(IEntity entity) {
  }

  
  public String getEtag() {
    return etag;
  }

  public void setEtag(String etag) {
    this.etag = etag;
  }

}
