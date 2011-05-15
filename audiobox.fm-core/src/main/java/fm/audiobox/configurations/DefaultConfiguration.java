package fm.audiobox.configurations;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.models.Album;
import fm.audiobox.core.models.Artist;
import fm.audiobox.core.models.Genre;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Track;
import fm.audiobox.interfaces.ICacheManager;
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


  private ContentFormat requestFormat = ContentFormat.XML;
  private boolean useCache = false;
  private boolean shortResponse = false;
  private IFactory factory = new DefaultFactory();
  private ILoginExceptionHandler loginHandler;
  private IServiceExceptionHandler serviceHandler;
  private String appName = APPLICATION_NAME;
  private String version = VERSION;
  private Class<? extends IConnectionMethod> connMethodClass = DefaultRequestMethod.class;
  private ExecutorService executor;
  private ICacheManager cacheManager;
  
  private Map<String, Track> tracks = new HashMap<String, Track>();
  private Map<String, Playlist> playlists = new HashMap<String, Playlist>();
  private Map<String, Album> albums = new HashMap<String, Album>();
  private Map<String, Genre> genres = new HashMap<String, Genre>();
  private Map<String, Artist> artists = new HashMap<String, Artist>();


  public DefaultConfiguration(String appName, int major, int minor, int revision, ContentFormat requestFormat){
    this.setApplicationName(appName);
    this.setVersion(major, minor, revision);
    this.setRequestFormat(requestFormat);
    log.debug("Application name: " + appName);
    log.debug("Application version: " + this.getVersion() );
    log.debug("Request format: " + requestFormat.toString() );
    log.info("Configuration loaded");
    this.executor = Executors.newSingleThreadExecutor();
    this.setCacheManager( new DefaultCacheManager() );
  }


  public DefaultConfiguration(String appName, int major, int minor, int revision){
    this(appName, major, minor, revision, ContentFormat.XML);
  }

  public DefaultConfiguration(String appName){
    this(appName, MAJOR, MINOR, REVISION);
  }


  @Override
  public void setRequestFormat(ContentFormat requestFormat) {
    this.requestFormat = requestFormat;
  }

  @Override
  public ContentFormat getRequestFormat() {
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
  public boolean isCacheEnabled() {
    return this.useCache;
  }

  @Override
  public void setShortResponse(boolean shortResponse) {
    this.shortResponse = shortResponse;
  }

  @Override
  public boolean isShortResponseEnabled() {
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


  @Override
  public void setCacheManager(ICacheManager manager) {
    this.cacheManager = manager;
  }


  @Override
  public ICacheManager getCacheManager() {
    return this.cacheManager;
  }


  
  
  
  /* 
   * Following code manages internal cache of {@link IEntities}
   */
  
  // ---------------------------------------------------
  
  @Override
  public boolean hasPlaylist(String token) {
    return this.playlists.containsKey(token);
  }


  @Override
  public Playlist getPlaylist(String token) {
    return this.playlists.get(token);
  }


  @Override
  public boolean hasTrack(String token) {
    return this.tracks.containsKey(token);
  }


  @Override
  public Track getTrack(String token) {
    return this.tracks.get(token);
  }


  @Override
  public boolean hasAlbum(String token) {
    return this.albums.containsKey(token);
  }


  @Override
  public Album getAlbum(String token) {
    return this.albums.get(token);
  }


  @Override
  public boolean hasGenre(String token) {
    return this.genres.containsKey(token);
  }


  @Override
  public Genre getGenre(String token) {
    return this.genres.get(token);
  }


  @Override
  public boolean hasArtist(String token) {
    return this.artists.containsKey(token);
  }


  @Override
  public Artist getArtist(String token) {
    return this.artists.get(token);
  }


  @Override
  public void addPlaylist(Playlist pl) {
    if ( ! this.hasPlaylist(pl.getToken() ) ){
      this.playlists.put( pl.getToken(), pl);
    }
  }


  @Override
  public void addTrack(Track tr) {
    if ( ! this.hasTrack(tr.getToken() ) ){
      this.tracks.put( tr.getToken(), tr);
    }
  }


  @Override
  public void addAlbum(Album al) {
    if ( ! this.hasAlbum(al.getToken() ) ){
      this.albums.put( al.getToken(), al);
    }
  }


  @Override
  public void addGenre(Genre gr) {
    if ( ! this.hasGenre(gr.getToken() ) ){
      this.genres.put( gr.getToken(), gr);
    }
  }


  @Override
  public void addArtist(Artist ar) {
    if ( ! this.hasArtist(ar.getToken() ) ){
      this.artists.put( ar.getToken(), ar);
    }
  }

}
