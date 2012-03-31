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
  private int rangeMin;
  private int rangeMax;
  
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

  public int getRangeMin() {
    return rangeMin;
  }

  public void setRangeMin(int rangeMin) {
    this.rangeMin = rangeMin;
  }

  public int getRangeMax() {
    return rangeMax;
  }

  public void setRangeMax(int rangeMax) {
    this.rangeMax = rangeMax;
  }

  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.equals("filename") ){
      return this.getClass().getMethod("setFileName", String.class);
    } else if ( tagName.equals("rangemin") ){
      return this.getClass().getMethod("setRangeMin", int.class);
    } else if ( tagName.equals("rangemax") ){
      return this.getClass().getMethod("setRangeMax", int.class);
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

}
