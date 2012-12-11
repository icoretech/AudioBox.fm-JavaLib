package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

public class Action extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  private static Logger log = LoggerFactory.getLogger( Action.class );

  public static final String TAGNAME = "action";
  public static final String NAMESPACE = Action.TAGNAME;
  
  
  public static final String NAME = "name";
  public static final String REQUESTID = "requestId";
  public static final String ARGS = "args";


  public static enum Actions {
    stream,
    disconnect,
    check,
    error
  }

  private String name;
  private String id;
  private Args args;
  
  
  private static final Map<String, Method> setterMethods = new HashMap<String, Method>();
  static {
    try {
      setterMethods.put( NAME, Action.class.getMethod("setName", String.class) );
      setterMethods.put( REQUESTID, Action.class.getMethod("setId", String.class) );
      setterMethods.put( ARGS, Action.class.getMethod("setArgs", Args.class) );
    } catch (SecurityException e) {
      log.error("Security error", e);
    } catch (NoSuchMethodException e) {
      log.error("No method found", e);
    }
  }


  public Action(IConfiguration config) {
    super(config);
    log.info("new Action instantiated");
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Args getArgs() {
    return args;
  }

  public void setArgs(Args args) {
    this.args = args;
  }

  
  
  public Method getSetterMethod(String tagName) {
    if ( setterMethods.containsKey( tagName) ) {
      return setterMethods.get( tagName );
    }
    return null;
  }


  public boolean is(Actions type) {
    try {
      return Actions.valueOf( this.getName() ).compareTo( type ) == 0;
    } catch( Exception e) {
      log.warn("Exception occurred while checking the action: " + this.getName() );
    }
    return false;
  }

  @Override
  public String getApiPath() {
    return null;
  }

  @Override
  public void setParent(IEntity parent) {
    // not needed

  }

  @Override
  protected void copy(IEntity entity) {
    // temporary not supported
  }

  @Override
  public IConnectionMethod load(boolean sync) throws ServiceException, LoginException {
    return this.load(false, null);
  }

  @Override
  public IConnectionMethod load(boolean sync, IResponseHandler responseHandler) throws ServiceException, LoginException {
    throw new ServiceException("method not supported");
  }
}
