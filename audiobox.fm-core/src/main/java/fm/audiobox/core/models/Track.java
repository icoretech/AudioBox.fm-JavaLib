
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.mime.content.FileBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.AudioBox.Connector;
import fm.audiobox.core.api.ModelItem;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;


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
 * {@code
 * <track>
 *   <token>asjad89087r3</token>
 *   <title>Track title</title>
 *   <duration>3:51</duration>
 *   <duration_in_seconds>231</duration_in_seconds>
 *   <stream_url>
 *      http://url/to/steram/url
 *   </stream_url>
 *   <year>2010</year>
 *   <loved>true</loved>
 *   <play_count>31</play_count>
 *   <audio_file_size>4933632</audio_file_size>
 *   <track_number>2</track_number>
 *   <disc_number>1</disc_number>
 *   <original_file_name>origina_file_name.mp3</original_file_name>
 *   <album>
 *     <token>2sd3Fgd7</token>
 *     <name>Album name</name>
 *   </album>
 *   <artist>
 *     <token>Sast5pt</token>
 *     <name>Artist Name</name>
 *   </artist>
 * </track>
 * }
 * </pre>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Track extends ModelItem {

    private static Logger logger = LoggerFactory.getLogger(Track.class);
    
    // Constants
    /** The XML tag name for the Track element */
    public static final String TAG_NAME = "track";

    /** The HTTP Parameter to use when uploading a track */
    public static final String HTTP_PARAM = "media";

    protected static final String STREAM_ACTION = "stream";
    protected static final String DOWNLOAD_ACTION = "download";

    private static final String SCROBBLE_ACTION = "scrobble";
    private static final String TOGGLE_LOVE_ACTION = "toggle_loved";

    // XML model fields
    private String duration;
    private long durationInSeconds;
    private boolean loved;
    private int playCount;
    private String title;
    private String trackNumber;
    private int discNumber;
    private int year;
    private String fileHash;
    private String streamUrl;
    private long audioFileSize;
    private String originalFileName;
    private Artist artist;
    private Album album;
    
    protected FileOutputStream fileOutputStream;


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
    protected State trackState = Track.State.IDLE;

    // Used for upload purpose
    protected FileBody fileBody;


    /**
     * <p>Constructor for Track.</p>
     */
    protected Track() {
        this.pEndPoint = Tracks.END_POINT;
    }

    /**
     * <p>Constructor for Track.</p>
     *
     * @param fileBody a {@link FileBody} object.
     */
    public Track(FileBody fileBody) {
        this();
        this.fileBody = fileBody;
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
    public void setTrackNumber(String number) {
        this.trackNumber = number;
    }

    /**
     * <p>Getter for the track number.</p>
     *
     * @return the number of the track
     */
    public String getTrackNumber() {
        return this.trackNumber;
    }


    /**
     * <p>Setter for the track's disc-number.</p>
     * 
     * @param discNumber {@link String} that will be converted into integer
     */
    public void setDiscNumber(String discNumber) {
    	this.setDiscNumber( Integer.parseInt( discNumber) );
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
     * Same as using {@link #getStreamUrl(boolean) getStreamUrl(false)}
     * 
     * @return track's streamUrl 
     * @throws LoginException
     * @throws ServiceException
     */
    public String getStreamUrl() {
        try {
            return this.getStreamUrl(false);
        } catch (LoginException e) { 
            logger.error("Login exception: " + e.getMessage());
        } catch (ServiceException e) { 
            logger.error("Service exception: " + e.getMessage());
        }
        return null;
    }
    
    
    /**
     * <p>
     * Getter method for the track streaming URL.<br/>
     * Use this method if you need the real, volatile remote stream URL.<br/>
     * If you need the API value use {@link #getStreamUrl() getStreamUrl()} instead.
     * </p>
     *
     * @param remote if true this methods executes a request and return the real stream url
     * @return the track stream URL to be consumed by a media player (for example).
     * 
     * @throws LoginException if any authentication problem during the request occurs.
     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
     */
    public String getStreamUrl(boolean remote) throws LoginException, ServiceException {
        if ( remote ){
        	HttpRequestBase method = this.getConnector().createConnectionMethod(HttpGet.METHOD_NAME, this, STREAM_ACTION, null);
        	method.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
        	String[] result = this.getConnector().request(method, this);
            return result[ Connector.RESPONSE_BODY ];
        } else return this.streamUrl;
    }


    

    /**
     * This method is used by the parser. Please use {@link Track#setLoved(boolean)} instead.
     * 
     * <p>
     * 
     * Setter for the field <code>loved</code>: used by the parser.
     * 
     * @param loved a String representing the "loved state" of the track.
     */
    @Deprecated
    public void setLoved(String loved) {
        this.setLoved( Boolean.parseBoolean( loved ) );
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
     * This method is used by the parser. Please use {@link Track#setPlayCount(int)} instead.
     * 
     * <p>
     * 
     * Setter for the numbers of plays for this song: used by the parser.
     * 
     * @param playCount String of the number of this track's plays
     */
    @Deprecated
    public void setPlayCount(String playCount) {
        this.setPlayCount( Integer.parseInt( playCount ) );
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
    @Deprecated
    public void setYear(String year) {
        this.setYear( Integer.parseInt( year ) );
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
     * <p>Setter for the track's file MD5 hash: used by the parser.</p>
     *
     * @param fileHash the MD5 file hash to set
     */
    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    /**
     * <p>Getter for the track's file hash.</p>
     *
     * @return the track's file MD5 hash
     */
    public String getFileHash() {
        return fileHash;
    }



    /**
     * <p>Setter for the track's file size in bytes: used by the parser.</p>
     *
     * @param fileSize the String of the size in bytes.
     */
    public void setAudioFileSize(String fileSize) {
        this.audioFileSize = Long.parseLong( fileSize );
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
     * This method is used by the parser. Please use {@link Track#setDurationInSeconds(long)} instead.
     * 
     * <p>
     * 
     * Setter for the track's duration in seconds: used by the parser.
     *
     * @param durationInSeconds the String representing the number of seconds of the track's duration
     */
    @Deprecated
    public void setDurationInSeconds(String durationInSeconds) {
        this.setDurationInSeconds( Long.parseLong( durationInSeconds ) );
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



    /**
     * <p>Use this method to get the file content for upload process.</p>
     *
     * @return the FileBody to upload
     */
    public FileBody getFileBody(){
        return this.fileBody;
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
     * This method also increment the track's playcount if the scrobble is correctly received by AudioBox.fm.
     *
     * @throws LoginException if any authentication problem during the request occurs.
     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
     */
    public void scrobble() throws ServiceException, LoginException {
        String[] response = this.getConnector().post( this, this, SCROBBLE_ACTION, null);
        if ( Integer.parseInt( response[ Connector.RESPONSE_CODE ] ) == HttpStatus.SC_OK)
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
        String[] result = this.getConnector().put( this, this, TOGGLE_LOVE_ACTION, null);
        if ( HttpStatus.SC_OK == Integer.parseInt( result[ Connector.RESPONSE_CODE ] )  )
        	this.setLoved( ! this.isLoved() );
        return this.isLoved();
    }



    /**
     * <p>Download track and put binary data into given {@link FileOutputStream}.
     * 
     * @param fos the {@link FileOutputStream} to write binary data to
     * 
     * @throws ServiceException	if any connection problem to AudioBox.fm occurs.
     * @throws LoginException if any authentication problem during the request occurs.
     */
    public void download(final FileOutputStream fos) throws ServiceException, LoginException {
        this.fileOutputStream = fos;
        this.getConnector().get( this, this, DOWNLOAD_ACTION);
    }


    /**
     * <p>Upload the track to AudioBox.fm</p>
     * 
     * <b>Method is empty, must be overwritten</b>
     * 
     * @throws IOException if any file exception occurs.
     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
     * @throws LoginException if any authentication problem during the request occurs.
     */
    public void upload() throws IOException ,ServiceException, LoginException { }


    
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


    /* --------- */
    /* Overrides */
    /* --------- */

    /**
     * This method is used to parse the binary content of HTTP response.
     * 
     * <p>
     * 
     * Returns an empty string by default.
     *
     * @param response the {@link HttpResponse} object.
     * 
     * @return an empty {@link String}
     * 
     * @throws IOException if the parse process fails for some reasons.
     */
    @Override
    public String parseBinaryResponse( HttpResponse response ) throws IOException {

        if ( this.fileOutputStream != null ) {
            
            InputStream input = response.getEntity().getContent();
            
            try {

                int read;
                byte[] bytes = new byte[CHUNK];

                while( (read = input.read(bytes)) != -1   )
                    this.fileOutputStream.write(bytes, 0, read);


                this.fileOutputStream.flush();

            } finally {
                this.fileOutputStream.close();
            }
            
            this.fileOutputStream = null;
        }

        return super.parseBinaryResponse(response);
    }


    /** {@inheritDoc} */
    @Override
    public String getName() {
        return this.title + " - " + this.artist.getName() + " (" + this.duration + ")";
    }

    /**
     * Returns the current track instance
     */
    @Override
    public Track getTrack(String token) {
        if ( this.getToken().equals( token ) )
            return this;
        else return null;
    }


    /** 
     * Returns <code>null</code>: prevent undesired behaviours.
     * 
     * @return null
     */
    @Override
    public Tracks getTracks() { return (Tracks) this.pParent; }

    
    /* ------------------------ */
    /* Track management methods */
    /* ------------------------ */
    
    
    /**
     * Use this method to add this {@link Track track} to a playlist
     * 
     * @param playlist the {@link Playlist} to add this track to
     * @return true if the action succeed false if something goes wrong.
     * 
     * @throws LoginException if any authentication problem during the request occurs.
     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
     */
    public boolean addTo(Playlist playlist) throws LoginException, ServiceException {
        return playlist.addTrack(this);
    }
    
    
    /**
     * Use this method to remove this {@link Track track} from a playlist
     * 
     * @param playlist the {@link Playlist} to remove this track from
     * @return true if the action succeed false if something goes wrong.
     * 
     * @throws LoginException if any authentication problem during the request occurs.
     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
     */
    public boolean removeFrom(Playlist playlist) throws LoginException, ServiceException {
        return playlist.removeTrack(this);
    }
    
}


