package fm.audiobox.configurations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IFactory;
import fm.audiobox.interfaces.ILoginExceptionHandler;
import fm.audiobox.interfaces.IServiceExceptionHandler;

public class DefaultConfiguration implements IConfiguration {

  private static final Logger log = LoggerFactory.getLogger(DefaultConfiguration.class);

  private static final String APP_NAME_PLACEHOLDER = "${APP_NAME}";
  private static final String VERSION_PLACEHOLDER = "${VERSION}";

  public static final String APPLICATION_NAME = "Java libs";
  public static final int MAJOR = 1;
  public static final int MINOR = 0;
  public static final int REVISION = 0;
  public static final String VERSION = MAJOR + "." + MINOR + "." + REVISION;
  public static final String PROTOCOL = "http";
  public static final String HOST = "audiobox.fm";
  public static final int PORT = 80;
  public static final String PATH = "api";

  private static final String USER_AGENT = 
      "AudioBox.fm; " +
      System.getProperty("os.name") + " " +
      System.getProperty("os.arch") + "; " + 
      System.getProperty("user.language") + "; " +
      System.getProperty("java.runtime.version") +  ") " +
      System.getProperty("java.vm.name") + "/" + 
      System.getProperty("java.vm.version") + " " + 
      APP_NAME_PLACEHOLDER + "/" + VERSION_PLACEHOLDER;


  private RequestFormat requestFormat = RequestFormat.XML;
  private boolean useCache = false;
  private boolean shortResponse = false;
  private IFactory factory = new DefaultFactory();
  private ILoginExceptionHandler loginHandler;
  private IServiceExceptionHandler serviceHandler;
  private String appName = APPLICATION_NAME;
  private String version = VERSION;
  private Class<? extends IConnectionMethod> connMethodClass = DefaultRequestMethod.class;
  private ExecutorService executor;



  public DefaultConfiguration(String appName, int major, int minor, int revision, RequestFormat requestFormat){
    this.setApplicationName(appName);
    this.setVersion(major, minor, revision);
    this.setRequestFormat(requestFormat);
    log.debug("Application name: " + appName);
    log.debug("Application version: " + this.getVersion() );
    log.debug("Request format: " + requestFormat.toString() );
    log.info("Configuration loaded");
    this.executor = Executors.newSingleThreadExecutor();
  }


  public DefaultConfiguration(String appName, int major, int minor, int revision){
    this(appName, major, minor, revision, RequestFormat.XML);
  }

  public DefaultConfiguration(String appName){
    this(appName, MAJOR, MINOR, REVISION);
  }


  @Override
  public void setRequestFormat(RequestFormat requestFormat) {
    this.requestFormat = requestFormat;
  }

  @Override
  public RequestFormat getRequestFormat() {
    return this.requestFormat;
  }

  @Override
  public IFactory getFactory() {
    return this.factory;
  }

  @Override
  public void setApplicationName(String appName) {
    this.appName = appName;
  }

  @Override
  public String getApplicationName() {
    return this.appName;
  }


  @Override
  public void setVersion(int major, int minor, int revision) {
    this.version = major + "." + minor + "." + revision;
  }


  @Override
  public String getVersion() {
    return this.version;
  }

  @Override
  public String getUserAgent(){
    return USER_AGENT
      .replace(APP_NAME_PLACEHOLDER, getApplicationName() )
      .replace(VERSION_PLACEHOLDER, getVersion() );
  }


  @Override
  public String getProtocol() {
    return PROTOCOL;
  }

  @Override
  public String getHost() {
    return HOST;
  }

  public int getPort(){
    return PORT;
  }


  @Override
  public String getPath() {
    return PATH;
  }

  @Override
  public void setUseCache(boolean useCache) {
    this.useCache = useCache;
  }

  @Override
  public boolean isUsingCache() {
    return this.useCache;
  }

  @Override
  public void setShortResponse(boolean shortResponse) {
    this.shortResponse = shortResponse;
  }

  @Override
  public boolean isUsingShortResponse() {
    return this.shortResponse;
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
  public void setHttpMethodType(Class<? extends IConnectionMethod> method) {
    this.connMethodClass = method; 
  }


  @Override
  public Class<? extends IConnectionMethod> getHttpMethodType() {
    return this.connMethodClass;
  }


  @Override
  public ExecutorService getExecutor() {
    return executor;
  }

}
