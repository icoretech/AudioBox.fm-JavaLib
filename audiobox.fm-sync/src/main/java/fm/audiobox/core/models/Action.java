package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IEntity;

public class Action extends AbstractEntity implements Serializable{

  private static final long serialVersionUID = 1L;
  private static Logger log = LoggerFactory.getLogger( Action.class );

  public static final String TAGNAME = "action";
  public static final String NAMESPACE = Action.TAGNAME;

  private String name;
  private int id;
  private Args args;

  //  private ThreadListener threadListener = new ThreadListener() {
  //    @Override
  //    public boolean onStart(Runnable result) {return true;}
  //
  //    @Override
  //    public void onProgress(Runnable result, long total, long completed, long remaining, Object item) {}
  //
  //    @Override
  //    public void onComplete(Runnable result , Object item) {}
  //
  //    @Override
  //    public void onStop(Runnable task) {}
  //  };

  public Action(IConfiguration config) {
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Args getArgs() {
    return args;
  }

  public void setArgs(Args args) {
    this.args = args;
  }

  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {

    if ( tagName.equals("name") ){
      return this.getClass().getMethod("setName", String.class);
    } else if ( tagName.equals("id") ){
      return this.getClass().getMethod("setId", int.class);
    } else if ( tagName.equals("args") ){
      return this.getClass().getMethod("setArgs", Args.class);
    } else if ( tagName.equals("stream") ){
      return this.getClass().getMethod("stream",Args.class);
    }

    return null;
  }

  public void stream(Args args){
    long i_str = System.currentTimeMillis();
    log.debug("Start strem file " + args.getFileName() );
    MediaFile md = new MediaFile(getConfiguration());

    log.debug("End strem file " + args.getFileName() + " in " + ( System.currentTimeMillis() - i_str) + "ms");
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
