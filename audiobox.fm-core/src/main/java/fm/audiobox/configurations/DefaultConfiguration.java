package fm.audiobox.configurations;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fm.audiobox.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.interfaces.IConnector.IConnectionMethod;


/**
 * This is the standard configuration used for general purpose
 */
public class DefaultConfiguration implements IConfiguration {

  private static Logger log = LoggerFactory.getLogger(DefaultConfiguration.class);

  private static final String APP_NAME_PLACEHOLDER = "${APP_NAME}";
  private static final String VERSION_PLACEHOLDER = "${VERSION}";

  /** Prefix for properties keys */
  private static final String PROP_PREFIX = "libaudioboxfm-core.";

  /** Keyword used to check if libs have GA */
  private static final String SNAPSHOT = "-SNAPSHOT";

  public static final String APPLICATION_NAME = "Java libs";
  public static final int MAJOR = 2;
  public static final int MINOR = 1;
  public static final int REVISION = 1;
  public static final String VERSION = MAJOR + "." + MINOR + "." + REVISION;
  public static final String PROTOCOL = "http";
  public static final String HOST = "audiobox.fm";
  public static final int PORT = 80;
  public static final String PATH = "api";

  /** Properties descriptor reader */
  private static Properties sProperties = new Properties();


  private ContentFormat requestFormat = ContentFormat.XML;
  private boolean useCache = false;
  private IFactory factory = new DefaultFactory();
  private ILoginExceptionHandler loginHandler;
  private IServiceExceptionHandler serviceHandler;
  private IForbiddenExceptionHandler forbiddenHandler;
  private String appName = APPLICATION_NAME;
  private String version = VERSION;
  private Class<? extends IConnectionMethod> connMethodClass = DefaultRequestMethod.class;
  private Class<? extends IResponseHandler> responseParserClass = DefaultResponseDeserializer.class;
  private Class<? extends IRequestHandler> requestParserClass = DefaultRequestSerializer.class;
  protected ExecutorService executor;
  private ICacheManager cacheManager;
  private IAuthenticationHandle authenticationHandle = new DefaultAuthenticationHandle();
  private IRetryRequestHandle retryHandle = new DefaultRetryRequestHandle(0, false);
  
  private String mUserAgent;
  private IConfiguration.Environments environment = IConfiguration.Environments.live;
  
  private static Map<IConfiguration.Environments, Map<IConfiguration.Connectors, Map<String, String>>> envProperties = new HashMap<IConfiguration.Environments, Map<IConfiguration.Connectors, Map<String, String>>>();
  
  static {
    
    // Production
    {
      Map<String, String> railsValues = new HashMap<String, String>();
      railsValues.put("protocol", "http");
      railsValues.put("host", "audiobox.fm");
      railsValues.put("port", "80");
      railsValues.put("apiPath", "/api/v1");
      Map<String, String> nodeValues = new HashMap<String, String>();
      nodeValues.put("protocol", "http");
      nodeValues.put("host", "audiobox.fm");
      nodeValues.put("port", "80");
      nodeValues.put("apiPath", "/api/v1");
      Map<String, String> daemonValues = new HashMap<String, String>();
      daemonValues.put("protocol", "http");
      daemonValues.put("host", "audiobox.fm");
      daemonValues.put("port", "8082");
      daemonValues.put("apiPath", "/api/v1");
      
      Map<IConfiguration.Connectors, Map<String, String>> prodConnectors = new HashMap<IConfiguration.Connectors, Map<String, String>>();
      prodConnectors.put(IConfiguration.Connectors.RAILS, railsValues);
      prodConnectors.put(IConfiguration.Connectors.NODE, nodeValues);
      prodConnectors.put(IConfiguration.Connectors.DAEMON, daemonValues);
      
      envProperties.put(IConfiguration.Environments.live, prodConnectors);
      
    }
    
    {
      Map<String, String> railsValues = new HashMap<String, String>();
      railsValues.put("protocol", "http");
      railsValues.put("host", "staging.audiobox.fm");
      railsValues.put("port", "80");
      railsValues.put("apiPath", "/api/v1");
      Map<String, String> nodeValues = new HashMap<String, String>();
      nodeValues.put("protocol", "http");
      nodeValues.put("host", "staging.audiobox.fm");
      nodeValues.put("port", "80");
      nodeValues.put("apiPath", "/api/v1");
      Map<String, String> daemonValues = new HashMap<String, String>();
      daemonValues.put("protocol", "http");
      daemonValues.put("host", "staging.audiobox.fm");
      daemonValues.put("port", "8082");
      daemonValues.put("apiPath", "/api/v1");
      
      Map<IConfiguration.Connectors, Map<String, String>> testConnectors = new HashMap<IConfiguration.Connectors, Map<String, String>>();
      testConnectors.put(IConfiguration.Connectors.RAILS, railsValues);
      testConnectors.put(IConfiguration.Connectors.NODE, nodeValues);
      testConnectors.put(IConfiguration.Connectors.DAEMON, daemonValues);
      
      envProperties.put(IConfiguration.Environments.test, testConnectors);
    }
    
    {
      Map<String, String> railsValues = new HashMap<String, String>();
      railsValues.put("protocol", "http");
      railsValues.put("host", "dev.audiobox.fm");
      railsValues.put("port", "5000");
      railsValues.put("apiPath", "/api/v1");
      Map<String, String> nodeValues = new HashMap<String, String>();
      nodeValues.put("protocol", "http");
      nodeValues.put("host", "dev.audiobox.fm");
      nodeValues.put("port", "3000");
      nodeValues.put("apiPath", "/api/v1");
      Map<String, String> daemonValues = new HashMap<String, String>();
      daemonValues.put("protocol", "http");
      daemonValues.put("host", "dev.audiobox.fm");
      daemonValues.put("port", "3000");
      daemonValues.put("apiPath", "/api/v1");
      
      Map<IConfiguration.Connectors, Map<String, String>> devConnectors = new HashMap<IConfiguration.Connectors, Map<String, String>>();
      devConnectors.put(IConfiguration.Connectors.RAILS, railsValues);
      devConnectors.put(IConfiguration.Connectors.NODE, nodeValues);
      devConnectors.put(IConfiguration.Connectors.DAEMON, daemonValues);
      envProperties.put(IConfiguration.Environments.development, devConnectors);
    }
    
  }


  public DefaultConfiguration(String appName, ContentFormat requestFormat){
    this(appName, requestFormat, Environments.live);
  }
  
  public DefaultConfiguration(String appName, ContentFormat requestFormat, Environments env){
    this.setApplicationName(appName);
    
    this.version = MAJOR + "." + MINOR + "." + REVISION;
    
    this.setRequestFormat(requestFormat);
    
    this.setCacheManager(new DefaultCacheManager());

    String version = "unattended";
    String ga_flag = "S";
    try {
      version = DefaultConfiguration.getProperty(PROP_PREFIX + "version");
      ga_flag = version.contains(SNAPSHOT) ? ga_flag : "GA";
      version = version.replace(SNAPSHOT, "");
    } catch (FileNotFoundException e) {
      log.error("Environment properties file not found: " + e.getMessage());
    } catch (IOException e) {
      log.error("Unable to access the environment properties file: " + e.getMessage());
    }

    mUserAgent = "AudioBox.fm/" + version + " (Java; " + ga_flag + "; " +
        System.getProperty("os.name") + " " +
        System.getProperty("os.arch") + "; " + 
        System.getProperty("user.language") + "; " +
        System.getProperty("java.runtime.version") +  ") " +
        System.getProperty("java.vm.name") + "/" + 
        System.getProperty("java.vm.version") + 
        " " + APP_NAME_PLACEHOLDER + "/" + VERSION_PLACEHOLDER;
   
    this.setEnvironment( this.environment );
    
    log.info("Configuration loaded");
  }


  public DefaultConfiguration(String appName){
    this(appName, ContentFormat.JSON);
  }

  @Override
  public void setRequestFormat(ContentFormat requestFormat) {
    this.requestFormat = requestFormat;
    log.info("Request format: " + requestFormat.toString() );
  }

  @Override
  public ContentFormat getRequestFormat() {
    return this.requestFormat;
  }

  
  public Class<? extends IResponseHandler> getResponseDeserializer() {
    return responseParserClass;
  }
  
  public void setResponseDeserializer(Class<? extends IResponseHandler> responseParser){
    this.responseParserClass = responseParser;
  }
  
  public Class<? extends IRequestHandler> getRequestSerializer() {
    return requestParserClass;
  }
  
  public void setRequestSerializer(Class<? extends IRequestHandler> responseParser){
    this.requestParserClass = responseParser;
  }
  
  
  
  public IAuthenticationHandle getAuthenticationHandle(){
    return this.authenticationHandle;
  }
  
  public void setAuthenticationHandle(IAuthenticationHandle handle){
    this.authenticationHandle = handle;
  }
  
  
  public IRetryRequestHandle getRetryRequestHandle() {
    return this.retryHandle;
  }
  
  public void setRetryRequestHandle(IRetryRequestHandle handle){
    this.retryHandle = handle;
  }
  
  
  @Override
  public IFactory getFactory() {
    return this.factory;
  }

  public void setApplicationName(String appName) {
    this.appName = appName;
    log.info("Application name: " + appName);
  }

  public String getApplicationName() {
    return this.appName;
  }
  

  public void setVersion(int major, int minor, int revision) {
    this.version = major + "." + minor + "." + revision;
    log.info("Application version: " + this.version );
  }


  public String getVersion() {
    return this.version;
  }

  public String getUserAgent(){
    mUserAgent = mUserAgent
        .replace(APP_NAME_PLACEHOLDER, getApplicationName() )
        .replace(VERSION_PLACEHOLDER, getVersion() );
    return mUserAgent;
  }


  @Override
  public String getProtocol(IConfiguration.Connectors server) {
    return envProperties.get( this.environment ).get(server).get("protocol"); 
  }

  @Override
  public String getHost(IConfiguration.Connectors server) {
    return envProperties.get( this.environment ).get(server).get("host");
  }

  
  @Override
  public int getPort(IConfiguration.Connectors server){
    String port = envProperties.get( this.environment ).get(server).get("port");
    return port != null ? Integer.parseInt( port ) : PORT;
  }

  @Override
  public String getPath(IConfiguration.Connectors server) {
    return envProperties.get( this.environment ).get(server).get("apiPath");
  }

  
  
  @Override
  public void setUseCache(boolean useCache) {
    this.useCache = useCache;
  }

  @Override
  public boolean isCacheEnabled() {
    return this.useCache;
  }


  @Override
  public void setDefaultLoginExceptionHandler(ILoginExceptionHandler handler) {
    this.loginHandler = handler;
  }

  @Override
  public ILoginExceptionHandler getDefaultLoginExceptionHandler() {
    return this.loginHandler;
  }


  @Override
  public void setDefaultServiceExceptionHandler(IServiceExceptionHandler handler) {
    this.serviceHandler = handler;
  }


  @Override
  public IServiceExceptionHandler getDefaultServiceExceptionHandler() {
    return this.serviceHandler;
  }

  @Override
  public void setDefaultForbiddenExceptionHandler(IForbiddenExceptionHandler handler) {
    this.forbiddenHandler = handler;
  }

  @Override
  public IForbiddenExceptionHandler getDefaultForbiddenExceptionHandler() {
    return this.forbiddenHandler;
  }

  @Override
  public void setHttpMethodType(Class<? extends IConnectionMethod> method) {
    this.connMethodClass = method; 
  }


  @Override
  public Class<? extends IConnectionMethod> getHttpMethodType() {
    return this.connMethodClass;
  }


  @Override
  public ExecutorService getExecutor() {
    if ( this.executor == null ) {
      this.executor = Executors.newSingleThreadExecutor();
    }
    return this.executor;
  }


  @Override
  public void setCacheManager(ICacheManager manager) {
    this.cacheManager = manager;
    if ( this.cacheManager != null ) {
      this.cacheManager.setConfiguration( this );
      this.setUseCache( true );
    } else {
      this.setUseCache( false );
    }
  }


  @Override
  public ICacheManager getCacheManager() {
    return this.cacheManager;
  }



  public IConfiguration.Environments getEnvironment(){
    return this.environment;
  }
  
  public void setEnvironment(IConfiguration.Environments env) {
    this.environment = env;
    log.info("Environment set to: " + env);
  }


  /* 
   * Following code manages internal cache of {@link IEntities}
   */

  // ---------------------------------------------------

  /* --------------- */
  /* Private methods */
  /* --------------- */


  /**
   * This method returns the AudioBox.fm properties file reader.
   * 
   * @param key the property you are looking for
   * 
   * @return the value of the property
   * @throws IOException if property files is not accessible or does not exists
   */
  private static String getProperty(String key) throws IOException {
    if (sProperties == null || sProperties.isEmpty()) {
      sProperties.load(DefaultConfiguration.class.getResourceAsStream("/fm/audiobox/core/config/env.properties"));
    }

    return sProperties.getProperty(key);
  }
  
}
