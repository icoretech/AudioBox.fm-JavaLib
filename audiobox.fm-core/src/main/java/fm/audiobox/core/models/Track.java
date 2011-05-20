
/***************************************************************************
 *   Copyright (C) 2010 iCoreTech research labs                            *
 *   Contributed code from:                                                *
 *   - Valerio Chiodino - keytwo at keytwo dot net                         *
 *   - Fabio Tunno      - fat at fatshotty dot net                         *
 *                                                                         *
 *   This program is free software: you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation, either version 3 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program. If not, see http://www.gnu.org/licenses/     *
 *                                                                         *
 ***************************************************************************/

package fm.audiobox.core.models;

import java.io.Serializable;
import java.lang.reflect.Method;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.observables.Event;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;


/**
 * Track is the main subject of the scope of these libraries.
 * 
 * <p>
 * 
 * Once browsed till arriving to a Track, many operations can be done with it.
 * 
 * <p>
 * 
 * The XML response looks like this:
 *
 * <pre>
 * @code
 * <track> 
 *   <token>{token}</token> 
 *   <title>Title</title> 
 *   <duration>3:41</duration> 
 *   <duration_in_seconds>221</duration_in_seconds> 
 *   <stream_url>http://audiobox.fm/api/tracks/{token}/stream</stream_url> 
 *   <year>2007</year> 
 *   <loved>false</loved> 
 *   <play_count>25</play_count> 
 *   <audio_file_size>5297412</audio_file_size> 
 *   <track_number>1</track_number> 
 *   <disc_number>1</disc_number> 
 *   <file_name>file.mp3</file_name> 
 *   <album>...</album> 
 *   <artist>...</artist> 
 *   <genre>...</genre> 
 * </track>
 * @endcode
 * </pre>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Track extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The XML tag name for the Track element */
  public static final String NAMESPACE = Tracks.TAGNAME;
  public static final String TAGNAME = "track";
  
  public static final String SCROBBLE_ACTION = "scrobble";
  public static final String LOVE_ACTION = "love";
  public static final String UNLOVE_ACTION = "unlove";
  

  // XML model fields
  private String duration;
  private long durationInSeconds;
  private boolean loved;
  private int playCount;
  private String title;
  private int trackNumber;
  private int discNumber;
  private int year;
  private String fileName;
  private String streamUrl;
  private long audioFileSize;
  private String originalFileName;
  private Artist artist;
  private Album album;
  private Genre genre;


  /**
   * During the lifecycle a Track can be placed in differents states.
   * 
   * <p>
   * 
   * Note though that this is an util element.<br/> 
   * <strong>Playing of the song is out of these libraries scope.</strong>
   * 
   * <ul>
   *  <li>IDLE state: track is ready for any action. It is equivalent to "Stopped".</li>
   *  <li>PLAYING state: the track is being played.</li>
   *  <li>ERROR state: any error occurred while trying to playing the track.</li>
   *  <li>BUFFERING state: track is currently downloading.</li>
   *  <li>PAUSED state: track has been paused.</li>
   * </ul>
   */
  public enum State { 
    /** Idle state: track is ready for any action. It is equivalent to "Stopped". */
    IDLE, 

    /** Playing state: the track is being played. */
    PLAYING, 

    /** Error state: any error occurred while trying to playing the track. */
    ERROR, 

    /** Buffering state: track is currently downloading. */
    BUFFERING, 

    /** Paused state: track has been paused. */
    PAUSED
  }

  private State trackState = Track.State.IDLE;


  /**
   * <p>Constructor for Track.</p>
   */
  public Track(IConnector connector, IConfiguration config){
    super(connector, config);
  }


  @Override
  public String getTagName() {
    return TAGNAME;
  }

  
  @Override
  public String getNamespace(){
    return NAMESPACE;
  }


  /* ------------------- */
  /* Getters and setters */
  /* ------------------- */


  /**
   * <p>Setter for the track duration: used by the parser.</p>
   *
   * @param duration the String of the duration in MM:SS format.
   */
  public void setDuration(String duration) {
    this.duration = duration;
  }


  /**
   * <p>Getter for the track duration.</p>
   *
   * @return the duration of the track in MM:SS format
   */
  public String getDuration() {
    return duration;
  }



  /**
   * <p>Setter for the track title: used by the parser.</p>
   *
   * @param title the track title.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * <p>Getter for the track title.</p>
   *
   * @return the title of the track
   */
  public String getTitle() {
    return title;
  }



  /**
   * <p>Setter for the track number: used by the parser.</p>
   *
   * @param number the track.
   */
  public void setTrackNumber(int number) {
    this.trackNumber = number;
  }

  /**
   * <p>Getter for the track number.</p>
   *
   * @return the number of the track
   */
  public int getTrackNumber() {
    return this.trackNumber;
  }


  /**
   * <p>Getter for the track's disc-number.</p>
   * 
   * @param discNumber the track's disc-number as integer
   */
  public void setDiscNumber(int discNumber){
    this.discNumber = discNumber;
  }

  /**
   * <p>Getter for the track's disc-number
   * @return	the track's disc-number
   */
  public int getDiscNumber(){
    return this.discNumber;
  }



  /**
   * <p>Setter for the URL of the streaming: used by the parser.</p>
   *
   * @param url the String of the url
   */
  public void setStreamUrl(String url) {
    this.streamUrl = url;
  }



  /**
   * <p>
   * Getter method for the track streaming URL.<br/>
   * This method returns the stream url populated while parsing response content
   * </p>
   *
   * @return the track stream URL
   * 
   * @throws LoginException if any authentication problem during the request occurs.
   * @throws ServiceException if any connection problem to AudioBox.fm occurs.
   */
  public String getStreamUrl() {
    return this.streamUrl;
  }


  /**
   * <p>Setter for the field <code>loved</code>.</p>
   *
   * @param loved true if the track is loved, false otherwise.
   */
  public void setLoved(boolean loved) {
    this.loved = loved;
  }

  /**
   * <p>Check whether the track is loved or not</p>
   *
   * @return the loved state of the track
   */
  public boolean isLoved() {
    return loved;
  }



  /**
   * <p>Setter for the numbers of plays for this song.</p>
   *
   * @param playCount int of the number of plays
   */
  public void setPlayCount(int playCount) {
    this.playCount = playCount;
  }

  /**
   * <p>Getter for the track total plays.</p>
   *
   * @return the track plays count
   */
  public int getPlayCount() {
    return playCount;
  }


  /**
   * This method is used by the parser. Please use {@link Track#setYear(int)} instead.
   * 
   * <p>
   * 
   * Setter for the year of the track: used by the parser.
   *
   * @param year the String representing the year of the track.
   */
  public void setYear(int year) {
    this.year = year;
  }

  /**
   * <p>Getter for the track's year.</p>
   *
   * @return the year of the track
   */
  public int getYear() {
    return year;
  }



  /**
   * <p>Setter for the track's file name: used by the parser.</p>
   *
   * @param fileName the file name to set
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * <p>Getter for the track's file name.</p>
   *
   * @return the track's file name
   */
  public String getFileName() {
    return fileName;
  }



  /**
   * <p>Setter for the track's file size in bytes: used by the parser.</p>
   *
   * @param fileSize the size in bytes.
   */
  public void setAudioFileSize(long fileSize) {
    this.audioFileSize = fileSize;
  }

  /**
   * <p>Getter for the audio file size</p>
   *
   * @return the track's file size in bytes
   */
  public long getAudioFileSize() {
    return audioFileSize;
  }



  /**
   * <p>Setter for the track's orginal file name: used by the parser.</p>
   * 
   * @param fileName the track's file name.
   */
  public void setOriginalFileName(String fileName) {
    this.originalFileName = fileName;
  }

  /**
   * <p>Getter for the original track's file name</p>
   * 
   * @return the original file name String
   */
  public String getOriginalFileName() {
    return this.originalFileName;
  }



  /**
   * Setter for the track's duration in seconds.
   *
   * @param durationInSeconds the String representing the number of seconds of the track's duration
   */
  public void setDurationInSeconds(long durationInSeconds) {
    this.durationInSeconds = durationInSeconds;
  }

  /**
   * <p>Getter for the track's duration in seconds.</p>
   *
   * @return the number of seconds of the track's duration
   */
  public long getDurationInSeconds() {
    return durationInSeconds;
  }



  /**
   * <p>Setter for the track's artist: used by the parser.</p>
   *
   * @param artist the track's artist.
   */
  public void setArtist(Artist artist) {
    this.artist = artist;
  }

  /**
   * <p>Getter for the track's artist.</p>
   *
   * @return the artist of this track
   */
  public Artist getArtist() {
    return artist;
  }



  /**
   * <p>Setter for track's album: used by the parser.</p>
   *
   * @param album the track's album to set
   */
  public void setAlbum(Album album) {
    this.album = album;
  }

  /**
   * <p>Getter for the album of the track.</p>
   *
   * @return the track's album
   */
  public Album getAlbum() {
    return album;
  }
  
  
  public void setGenre(Genre genre){
    this.genre = genre;
  }
  
  public Genre getGenre(){
    return this.genre;
  }


  
  /**
   * Executes request populating this class
   * 
   * @throws ServiceException
   * @throws LoginException
   */
  public void load() throws ServiceException, LoginException {
    this.load(null);
  }
  
  /**
   * Executes request populating this class and passing the {@link IResponseHandler} as response parser
   * 
   * @param responseHandler the {@link IResponseHandler} used as response content parser
   * @throws ServiceException
   * @throws LoginException
   */
  public void load(IResponseHandler responseHandler) throws ServiceException, LoginException {
    getConnector().get(this, null, null).send(false, null, responseHandler);
  }
  
  

  /* -------------- */
  /* Remote Actions */
  /* -------------- */


  /**
   * Use this method at the end of a "just listened" song to scrobble it to Last.fm and to keep track of the
   * plays count to AudioBox.fm
   *
   * <p>
   * 
   * Remember that scrobbles have to be done at least one for listening and at 96% of the listening progres or
   * at the end.
   * 
   * <p>
   * 
   * This method also increments the track's playcount if the scrobble is correctly received by AudioBox.fm.
   *
   * @throws LoginException if any authentication problem during the request occurs.
   * @throws ServiceException if any connection problem to AudioBox.fm occurs.
   */
  public void scrobble() throws ServiceException, LoginException {
    getConnector().post(this, SCROBBLE_ACTION).send(false);
    this.setPlayCount( this.getPlayCount() + 1 );
  }

  /**
   * Use this method to mark the track as loved/unlove.
   * 
   * This method also sets to <code>true/false</code> the loved state.
   *
   * @return boolean the "love" state of this track instance
   * 
   * @throws LoginException if any authentication problem during the request occurs.
   * @throws ServiceException if any connection problem to AudioBox.fm occurs.
   */
  public boolean toggleLove() throws ServiceException, LoginException  {
    if ( this.isLoved() ){
      return this.unlove();
    } else {
      return this.love();
    }
  }
  
  
  /**
   * This method sets track as loved
   * @return boolean the track's loved state
   * @throws LoginException 
   * @throws ServiceException 
   */
  public boolean love() throws ServiceException, LoginException{
    this.getConnector().put(this, LOVE_ACTION ).send( false );
    return this.isLoved();
  }
  
  
  /**
   * This method sets track as unloved
   * @return boolean the track's loved state
   * @throws LoginException 
   * @throws ServiceException 
   */
  public boolean unlove() throws ServiceException, LoginException{
    this.getConnector().put(this, UNLOVE_ACTION ).send(false);
    return this.isLoved();
  }
  
  



//  /**
//   * <p>Download track and put binary data into given {@link FileOutputStream}.
//   * 
//   * @param fos the {@link FileOutputStream} to write binary data to
//   * 
//   * @throws ServiceException	if any connection problem to AudioBox.fm occurs.
//   * @throws LoginException if any authentication problem during the request occurs.
//   */
//  public void download(final FileOutputStream fos) throws ServiceException, LoginException {
//    this.fileOutputStream = fos;
//    this.getConnector().get( this, this, DOWNLOAD_ACTION);
//  }



  /* ----- */
  /* State */
  /* ----- */

  /**
   * Changes the states of the track.
   * 
   * <p>
   *
   * Useful for media player and sync tools.
   * 
   * @param trackState the {@link State} to put the track in
   */
  public void setState(State trackState) {
    this.trackState = trackState;
  }

  /**
   * <p>Use this method to get the {@link State} of the track.</p>
   *
   * @return the track {@link State}
   */
  public State getState() {
    return trackState;
  }

  /**
   * <p>Util method to know if the track contains errors.</p>
   *
   * @return true if the track contains errors
   */
  public boolean hasErrors() {
    return getState() == Track.State.ERROR;
  }

  /**
   * <p>Util method to know if the track is currently playing.</p>
   *
   * @return true if the track in in playing
   */
  public boolean isPlaying() {
    return getState() == Track.State.PLAYING;
  }

  /**
   * <p>Util method to know if the track is currently paused.</p>
   *
   * @return true if the track in in playing
   */
  public boolean isPaused() {
    return getState() == Track.State.PAUSED;
  }

  /**
   * <p>Util method to know if the track is currently buffering.</p>
   *
   * @return true if the track is currently buffering from network
   */
  public boolean isBuffering() {
    return getState() == Track.State.BUFFERING;
  }



  public String getName() {
    return this.title + " - " + this.artist.getName() + " (" + this.duration + ")";
  }


  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.equals("token") || tagName.equals("tk") ){
      return this.getClass().getMethod("setToken", String.class);
      
    } else if ( tagName.equals("title") || tagName.equals("t") ){
      return this.getClass().getMethod("setTitle", String.class);
      
    } else if ( tagName.equals("duration") || tagName.equals("d") ){
      return this.getClass().getMethod("setDuration", String.class);
      
    } else if ( tagName.equals("duration_in_seconds") || tagName.equals("ds") ){
      return this.getClass().getMethod("setDurationInSeconds", long.class);
    
    } else if ( tagName.equals("stream_url") || tagName.equals("su") ){
      return this.getClass().getMethod("setStreamUrl", String.class);
      
    } else if ( tagName.equals("year") || tagName.equals("y") ){
      return this.getClass().getMethod("setYear", int.class);
      
    } else if ( tagName.equals("loved") || tagName.equals("l") ){
      return this.getClass().getMethod("setLoved", boolean.class);
      
    } else if ( tagName.equals("play_count") || tagName.equals("pc") ){
      return this.getClass().getMethod("setPlayCount", int.class);
      
    } else if ( tagName.equals("audio_file_size") || tagName.equals("fs") ){
      return this.getClass().getMethod("setAudioFileSize", long.class);
      
    } else if ( tagName.equals("track_number") || tagName.equals("tn") ){
      return this.getClass().getMethod("setTrackNumber", int.class);
      
    } else if ( tagName.equals("disc_number") || tagName.equals("dn") ){
      return this.getClass().getMethod("setDiscNumber", int.class);
      
    } else if ( tagName.equals("file_name") || tagName.equals("fn") ){
      return this.getClass().getMethod("setFileName", String.class);
      
    } else if ( tagName.matches( Album.TAGNAME ) ){
      return this.getClass().getMethod("setAlbum", Album.class);
      
    } else if ( tagName.matches( Artist.TAGNAME ) ){
      return this.getClass().getMethod("setArtist", Artist.class);
      
    } else if ( tagName.matches( Genre.TAGNAME ) ){
      return this.getClass().getMethod("setGenre", Genre.class);
      
    }
      
    return null;
  }


  @Override
  protected void copy(IEntity entity) {
    Track track = (Track) entity;
    
    this.duration =  track.getDuration();
    this.durationInSeconds = track.getDurationInSeconds();
    this.loved = this.isLoved();
    this.playCount = this.getPlayCount();
    this.title = this.getTitle();
    this.trackNumber = this.getTrackNumber();
    this.discNumber = this.getDiscNumber();
    this.year = this.getYear();
    this.fileName = this.getFileName();
    this.audioFileSize = this.getAudioFileSize();
    this.originalFileName = this.getOriginalFileName();
    
    if ( track.getAlbum() != null ){
      if ( this.album == null ){
        this.album = track.getAlbum();
      } else {
        this.album.copy( track.getAlbum() );
      }
    }
    if ( track.getArtist() != null ){
      if ( this.artist == null ){
        this.artist = track.getArtist();
      } else {
        this.artist.copy( track.getArtist() );
      }
    }
    if ( track.getGenre() != null ){
      if ( this.genre == null ){
        this.genre = track.getGenre();
      } else {
        this.genre.copy( track.getGenre() );
      }
    }
    
    
    this.setChanged();
    Event event = new Event( this, Event.States.ENTITY_REFRESHED );
    this.notifyObservers(event);
    
  }


}


