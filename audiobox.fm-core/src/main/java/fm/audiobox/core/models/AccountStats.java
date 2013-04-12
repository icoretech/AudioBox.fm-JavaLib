package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

/**
 * This class contains all streaming data amount related to all AudioBox.fm drives.
 * <p>
 * It is meant for {@code read-only} use only
 * <p>
 * Data are related to {@link User}
 * </p>
 */
public final class AccountStats extends AbstractEntity implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  private static final Logger log = LoggerFactory.getLogger(AccountStats.class);
  
  public static final String NAMESPACE = "stats";
  public static final String TAGNAME = NAMESPACE;
  
  
  public static final String DATA_SERVED_THIS_MONTH = "data_served_this_month";
  public static final String DATA_SERVED_OVERALL = "data_served_overall";
  public static final String CLOUD_DATA_STORED_OVERALL = "cloud_data_stored_overall";
  public static final String CLOUD_DATA_STORED_THIS_MONTH = "cloud_data_stored_this_month";
  public static final String LOCAL_DATA_STORED_OVERALL = "local_data_stored_overall";
  public static final String LOCAL_DATA_STORED_THIS_MONTH = "local_data_stored_this_month";
  public static final String DROPBOX_DATA_STORED_OVERALL = "dropbox_data_stored_overall";
  public static final String DROPBOX_DATA_STORED_THIS_MONTH = "dropbox_data_stored_this_month";
  public static final String GDRIVE_DATA_STORED_THIS_MONTH = "gdrive_data_stored_this_month";
  public static final String GDRIVE_DATA_STORED_OVERALL = "gdrive_data_stored_overall";
  public static final String SKYDRIVE_DATA_STORED_THIS_MONTH = "skydrive_data_stored_this_month";
  public static final String SKYDRIVE_DATA_STORED_OVERALL = "skydrive_data_stored_overall";
  public static final String BOX_DATA_STORED_THIS_MONTH = "box_data_stored_this_month";
  public static final String BOX_DATA_STORED_OVERALL = "box_data_stored_overall";
  public static final String PARTNER_DATA_STORED_THIS_MONTH = "partner_data_stored_this_month";
  public static final String PARTNER_DATA_STORED_OVERALL = "partner_data_stored_overall";
  public static final String SOUNDCLOUD_DATA_STORED_THIS_MONTH = "soundcloud_data_stored_this_month";
  public static final String SOUNDCLOUD_DATA_STORED_OVERALL = "soundcloud_data_stored_overall";
  public static final String YOUTUBE_DATA_STORED_THIS_MONTH = "youtube_data_stored_this_month";
  public static final String YOUTUBE_DATA_STORED_OVERALL = "youtube_data_stored_overall";
  
  
  private long data_served_this_month;
  private long data_served_overall;
  private long cloud_data_stored_overall;
  private long cloud_data_stored_this_month;
  private long local_data_stored_overall;
  private long local_data_stored_this_month;
  private long dropbox_data_stored_overall;
  private long dropbox_data_stored_this_month;
  private long gdrive_data_stored_this_month;
  private long gdrive_data_stored_overall;
  private long skydrive_data_stored_this_month;
  private long skydrive_data_stored_overall;
  private long box_data_stored_this_month;
  private long box_data_stored_overall;
  private long partner_data_stored_this_month;
  private long partner_data_stored_overall;
  private long soundcloud_data_stored_this_month;
  private long soundcloud_data_stored_overall;
  private long youtube_data_stored_this_month;
  private long youtube_data_stored_overall;
  
  
  private static final Map<String, Method> setterMethods = new HashMap<String, Method>();
  static {
    try {
      setterMethods.put( DATA_SERVED_THIS_MONTH, AccountStats.class.getMethod( "setDataServedThisMonth", long.class ) );
      setterMethods.put( DATA_SERVED_OVERALL, AccountStats.class.getMethod( "setDataServedOverall", long.class ) );
      setterMethods.put( CLOUD_DATA_STORED_OVERALL, AccountStats.class.getMethod( "setCloudDataStoredOverall", long.class ) );
      setterMethods.put( CLOUD_DATA_STORED_THIS_MONTH, AccountStats.class.getMethod( "setCloudDataStoredThisMonth", long.class ) );
      setterMethods.put( LOCAL_DATA_STORED_OVERALL, AccountStats.class.getMethod( "setLocalDataStoredOverall", long.class ) );
      setterMethods.put( LOCAL_DATA_STORED_THIS_MONTH, AccountStats.class.getMethod( "setLocalDataStoredThisMonth", long.class ) );
      setterMethods.put( DROPBOX_DATA_STORED_OVERALL, AccountStats.class.getMethod( "setDropboxDataStoredOverall", long.class ) );
      setterMethods.put( DROPBOX_DATA_STORED_THIS_MONTH, AccountStats.class.getMethod( "setDropboxDataStoredThisMonth", long.class ) );
      setterMethods.put( GDRIVE_DATA_STORED_THIS_MONTH, AccountStats.class.getMethod( "setGdriveDataStoredThisMonth", long.class ) );
      setterMethods.put( GDRIVE_DATA_STORED_OVERALL, AccountStats.class.getMethod( "setGdriveDataStoredOverall", long.class ) );
      setterMethods.put( SKYDRIVE_DATA_STORED_THIS_MONTH, AccountStats.class.getMethod( "setSkydriveDataStoredThisMonth", long.class ) );
      setterMethods.put( SKYDRIVE_DATA_STORED_OVERALL, AccountStats.class.getMethod( "setSkydriveDataStoredOverall", long.class ) );
      setterMethods.put( BOX_DATA_STORED_THIS_MONTH, AccountStats.class.getMethod( "setBoxDataStoredThisMonth", long.class ) );
      setterMethods.put( BOX_DATA_STORED_OVERALL, AccountStats.class.getMethod( "setBoxDataStoredOverall", long.class ) );
      setterMethods.put( PARTNER_DATA_STORED_THIS_MONTH, AccountStats.class.getMethod( "setPartnerDataStoredThisMonth", long.class ) );
      setterMethods.put( PARTNER_DATA_STORED_OVERALL, AccountStats.class.getMethod( "setPartnerDataStoredOverall", long.class ) );
      setterMethods.put( SOUNDCLOUD_DATA_STORED_THIS_MONTH, AccountStats.class.getMethod( "setSoundcloudDataStoredThisMonth", long.class ) );
      setterMethods.put( SOUNDCLOUD_DATA_STORED_OVERALL, AccountStats.class.getMethod( "setSoundcloudDataStoredOverall", long.class ) );
      setterMethods.put( YOUTUBE_DATA_STORED_THIS_MONTH, AccountStats.class.getMethod( "setYoutubeDataStoredThisMonth", long.class ) );
      setterMethods.put( YOUTUBE_DATA_STORED_OVERALL, AccountStats.class.getMethod( "setYoutubeDataStoredOverall", long.class ) );
    } catch (SecurityException e) {
      log.error("Security error", e);
    } catch (NoSuchMethodException e) {
      log.error("No method found", e);
    }
  }
  
  

  public AccountStats(IConfiguration config) {
    super(config);
  }

  public String getNamespace() {
    return NAMESPACE;
  }

  public String getTagName() {
    return TAGNAME;
  }

  /**
   * @return the streaming data amount <b>in the last month</b> related to <b>all {@code AudioBox.fm} drives</b>
   */
  public long getDataServedThisMonth() {
    return data_served_this_month;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setDataServedThisMonth(long data_served_this_month) {
    this.data_served_this_month = data_served_this_month;
  }
  

  /**
   * @return the <b>total</b> streaming data amount related to <b>all {@code AudioBox.fm} drives</b>
   */
  public long getDataServedOverall() {
    return data_served_overall;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setDataServedOverall(long data_served_overall) {
    this.data_served_overall = data_served_overall;
  }

  /**
   * @return the <b>total</b> streaming data amount related to <b>{@code AudioBox.fm Cloud}</b> drive
   */
  public long getCloudDataStoredOverall() {
    return cloud_data_stored_overall;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setCloudDataStoredOverall(long cloud_data_stored_overall) {
    this.cloud_data_stored_overall = cloud_data_stored_overall;
  }

  /**
   * @return the streaming data amount <b>in the last month</b> related to <b>{@code AudioBox.fm Cloud}</b> drive
   */
  public long getCloudDataStoredThisMonth() {
    return cloud_data_stored_this_month;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setCloudDataStoredThisMonth(long cloud_data_stored_this_month) {
    this.cloud_data_stored_this_month = cloud_data_stored_this_month;
  }

  /**
   * @return the <b>total</b> streaming data amount related to <b>{@code AudioBox.fm Desktop}</b> drive
   */
  public long getLocalDataStoredOverall() {
    return local_data_stored_overall;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setLocalDataStoredOverall(long local_data_stored_overall) {
    this.local_data_stored_overall = local_data_stored_overall;
  }

  /**
   * @return the streaming data amount <b>in the last month</b> related to <b>{@code AudioBox.fm Desktop}</b> drive
   */
  public long getLocalDataStoredThisMonth() {
    return local_data_stored_this_month;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setLocalDataStoredThisMonth(long local_data_stored_this_month) {
    this.local_data_stored_this_month = local_data_stored_this_month;
  }

  /**
   * @return the <b>total</b> streaming data amount related to <b>{@code Dropbox}</b> drive
   */
  public long getDropboxDataStoredOverall() {
    return dropbox_data_stored_overall;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setDropboxDataStoredOverall(long dropbox_data_stored_overall) {
    this.dropbox_data_stored_overall = dropbox_data_stored_overall;
  }

  /**
   * @return the streaming data amount <b>in the last month</b> related to <b>{@code AudioBox.fm Dropbox}</b> drive
   */
  public long getDropboxDataStoredThisMonth() {
    return dropbox_data_stored_this_month;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setDropboxDataStoredThisMonth(long dropbox_data_stored_this_month) {
    this.dropbox_data_stored_this_month = dropbox_data_stored_this_month;
  }

  /**
   * @return the streaming data amount <b>in the last month</b> related to <b>{@code AudioBox.fm GDrive}</b> drive
   */
  public long getGdriveDataStoredThisMonth() {
    return gdrive_data_stored_this_month;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setGdriveDataStoredThisMonth(long gdrive_data_stored_this_month) {
    this.gdrive_data_stored_this_month = gdrive_data_stored_this_month;
  }

  /**
   * @return the <b>total</b> streaming data amount related to <b>{@code GDrive}</b> drive
   */
  public long getGdriveDataStoredOverall() {
    return gdrive_data_stored_overall;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setGdriveDataStoredOverall(long gdrive_data_stored_overall) {
    this.gdrive_data_stored_overall = gdrive_data_stored_overall;
  }

  
  /**
   * @return the streaming data amount <b>in the last month</b> related to <b>{@code AudioBox.fm Skydrive}</b> drive
   */
  public long getSkydriveDataStoredThisMonth() {
    return skydrive_data_stored_this_month;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setSkydriveDataStoredThisMonth(long skydrive_data_stored_this_month) {
    this.skydrive_data_stored_this_month = skydrive_data_stored_this_month;
  }

  /**
   * @return the <b>total</b> streaming data amount related to <b>{@code Skydrive}</b> drive
   */
  public long getSkydriveDataStoredOverall() {
    return skydrive_data_stored_overall;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setSkydriveDataStoredOverall(long skydrive_data_stored_overall) {
    this.skydrive_data_stored_overall = skydrive_data_stored_overall;
  }

  
  /**
   * @return the streaming data amount <b>in the last month</b> related to <b>{@code AudioBox.fm Box.net}</b> drive
   */
  public long getBoxDataStoredThisMonth() {
    return box_data_stored_this_month;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setBoxDataStoredThisMonth(long box_data_stored_this_month) {
    this.box_data_stored_this_month = box_data_stored_this_month;
  }

  /**
   * @return the <b>total</b> streaming data amount related to <b>{@code Box.net}</b> drive
   */
  public long getBoxDataStoredOverall() {
    return box_data_stored_overall;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setBoxDataStoredOverall(long box_data_stored_overall) {
    this.box_data_stored_overall = box_data_stored_overall;
  }

  /**
   * @return the streaming data amount <b>in the last month</b> related to <b>{@code Partners'}</b> drives
   */
  public long getPartnerDataStoredThisMonth() {
    return partner_data_stored_this_month;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setPartnerDataStoredThisMonth(long partner_data_stored_this_month) {
    this.partner_data_stored_this_month = partner_data_stored_this_month;
  }

  
  /**
   * @return the <b>total</b> streaming data amount related to <b>{@code Partners'}</b> drives
   */
  public long getPartnerDataStoredOverall() {
    return partner_data_stored_overall;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setPartnerDataStoredOverall(long partner_data_stored_overall) {
    this.partner_data_stored_overall = partner_data_stored_overall;
  }
  
  /**
   * @return the streaming data amount <b>in the last month</b> related to <b>{@code AudioBox.fm Soundcloud}</b> drive
   */
  public long getSoundcloudDataStoredThisMonth() {
    return soundcloud_data_stored_this_month;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setSoundcloudDataStoredThisMonth(long soundcloud_data_stored_this_month) {
    this.soundcloud_data_stored_this_month = soundcloud_data_stored_this_month;
  }
  
  
  /**
   * @return the <b>total</b> streaming data amount related to <b>{@code Soundcloud}</b> drive
   */
  public long getSoundcloudDataStoredOverall() {
    return soundcloud_data_stored_overall;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setSoundcloudDataStoredOverall(long soundcloud_data_stored_overall) {
    this.soundcloud_data_stored_overall = soundcloud_data_stored_overall;
  }
  
  
  /**
   * @return the streaming data amount <b>in the last month</b> related to <b>{@code AudioBox.fm Youtube}</b> drive
   */
  public long getYoutubeDataStoredThisMonth() {
    return youtube_data_stored_this_month;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setYoutubeDataStoredThisMonth(long youtube_data_stored_this_month) {
    this.youtube_data_stored_this_month = youtube_data_stored_this_month;
  }

  /**
   * @return the <b>total</b> streaming data amount related to <b>{@code Youtube}</b> drive
   */
  public long getYoutubeDataStoredOverall() {
    return youtube_data_stored_overall;
  }

  /**
   * This method is used by response parser
   */
  @Deprecated
  public void setYoutubeDataStoredOverall(long youtube_data_stored_overall) {
    this.youtube_data_stored_overall = youtube_data_stored_overall;
  }
  
  
  public Method getSetterMethod(String tagName) {
    if ( setterMethods.containsKey( tagName) ) {
      return setterMethods.get( tagName );
    }
    return null;
  }
  
  
  public String getApiPath() {
    return null;
  }

  protected void copy(IEntity entity) { }

  public IConnectionMethod load(boolean sync) throws ServiceException, LoginException {
    return this.load(false, null);
  }

  public IConnectionMethod load(boolean sync, IResponseHandler responseHandler) throws ServiceException, LoginException {
    throw new ServiceException("method not supported");
  }
  
  protected List<NameValuePair> toQueryParameters(boolean all) {
    String prefix = TAGNAME + "[";
    String suffix = "]";
    
    List<NameValuePair> params = new ArrayList<NameValuePair>();

    params.add(new BasicNameValuePair(prefix + DATA_SERVED_THIS_MONTH + suffix, String.valueOf( this.data_served_this_month ) )  );
    params.add(new BasicNameValuePair(prefix + DATA_SERVED_OVERALL + suffix, String.valueOf( this.data_served_overall ) )  );
    params.add(new BasicNameValuePair(prefix + CLOUD_DATA_STORED_OVERALL + suffix, String.valueOf( this.cloud_data_stored_overall ) )  );
    params.add(new BasicNameValuePair(prefix + CLOUD_DATA_STORED_THIS_MONTH + suffix, String.valueOf( this.cloud_data_stored_this_month ) )  );
    params.add(new BasicNameValuePair(prefix + LOCAL_DATA_STORED_OVERALL + suffix, String.valueOf( this.local_data_stored_overall ) )  );
    params.add(new BasicNameValuePair(prefix + LOCAL_DATA_STORED_THIS_MONTH + suffix, String.valueOf( this.local_data_stored_this_month ) )  );
    params.add(new BasicNameValuePair(prefix + DROPBOX_DATA_STORED_OVERALL + suffix, String.valueOf( this.dropbox_data_stored_overall ) )  );
    params.add(new BasicNameValuePair(prefix + DROPBOX_DATA_STORED_THIS_MONTH + suffix, String.valueOf( this.dropbox_data_stored_this_month ) )  );
    params.add(new BasicNameValuePair(prefix + GDRIVE_DATA_STORED_THIS_MONTH + suffix, String.valueOf( this.gdrive_data_stored_this_month ) )  );
    params.add(new BasicNameValuePair(prefix + GDRIVE_DATA_STORED_OVERALL + suffix, String.valueOf( this.gdrive_data_stored_overall ) )  );
    params.add(new BasicNameValuePair(prefix + SKYDRIVE_DATA_STORED_THIS_MONTH + suffix, String.valueOf( this.skydrive_data_stored_this_month ) )  );
    params.add(new BasicNameValuePair(prefix + SKYDRIVE_DATA_STORED_OVERALL + suffix, String.valueOf( this.skydrive_data_stored_overall ) )  );
    params.add(new BasicNameValuePair(prefix + BOX_DATA_STORED_THIS_MONTH + suffix, String.valueOf( this.box_data_stored_this_month ) )  );
    params.add(new BasicNameValuePair(prefix + BOX_DATA_STORED_OVERALL + suffix, String.valueOf( this.box_data_stored_overall ) )  );
    params.add(new BasicNameValuePair(prefix + PARTNER_DATA_STORED_THIS_MONTH + suffix, String.valueOf( this.partner_data_stored_this_month ) )  );
    params.add(new BasicNameValuePair(prefix + PARTNER_DATA_STORED_OVERALL + suffix, String.valueOf( this.partner_data_stored_overall ) )  );
    params.add(new BasicNameValuePair(prefix + SOUNDCLOUD_DATA_STORED_THIS_MONTH + suffix, String.valueOf( this.soundcloud_data_stored_this_month ) )  );
    params.add(new BasicNameValuePair(prefix + SOUNDCLOUD_DATA_STORED_OVERALL + suffix, String.valueOf( this.soundcloud_data_stored_overall ) )  );
    
    return params;
  }
  
}
