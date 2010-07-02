
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

import java.net.SocketException;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.content.FileBody;

import fm.audiobox.core.api.ModelItem;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.AudioBoxClient.AudioBoxConnector;


/**
 * The XML response looks like this:
 *
 * <pre>
 * {@code
 * <track>
 *   <duration>4:15</duration>
 *   <duration-in-seconds type="integer">296</duration-in-seconds>
 *   <loved type="boolean">true</loved>
 *   <play-count type="integer">3</play-count>
 *   <title>Track title</title>
 *   <year type="integer">2001</year>
 *   <stream-url>http://url.to/<uuid>/stream</stream-url>
 *   <audio-file-size>12313124432</audio-file-size>
 *   <artist>
 *     <name>Artist name</name>
 *     <token>iq6ieJ9z</token>
 *   </artist>
 *   <album>
 *     <name>Album name</name>
 *     <token>DUoSAAoN</token>
 *     <cover-url-as-thumb>http://url.to/thumb.png</cover-url-as-thumb>
 *     <cover-url>http://url.to/original.jpg?1269960151</cover-url>
 *   </album>
 * </track>
 * }
 * </pre>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Track extends ModelItem {

    // Constants
    /** Constant <code>TAG_NAME="track"</code> */
    public static final String TAG_NAME = "track";
    public static final String HTTP_PARAM = "media";

    private static final String PATH = "tracks";
    private static final String STREAM_ACTION = "stream";
    private static final String SCROBBLE_ACTION = "scrobble";
    private static final String LOVE_ACTION = "love";
    private static final String UNLOVE_ACTION = "unlove";

    // Customized fields
    private String uuid;

    // XML model fields
    protected String duration;
    protected long durationInSeconds;
    protected boolean loved;
    protected int playCount;
    protected String title;
    protected int year;
    protected String fileHash;

    protected String streamUrl;
    protected long audioFileSize;
    protected Artist artist;
    protected Album album;


    // Utility fields
    public enum State { IDLE, PLAYING, ERROR, BUFFERING, PAUSED }
    protected State trackState = Track.State.IDLE;
    protected FileBody fileBody;


    /**
     * <p>Constructor for Track.</p>
     */
    protected Track() {
        this.endPoint = Tracks.END_POINT;
    }

    /**
     * <p>Constructor for Track.</p>
     *
     * @param fileBody a {@link FileBody} object.
     */
    public Track(FileBody fileBody) {
        super();
        this.fileBody = fileBody;
    }

    /** {@inheritDoc} */
    @Override
    public String getToken() {
        return getUuid();
    }

    /**
     * <p>Setter for the field <code>uuid</code>.</p>
     *
     * @param uuid a {@link java.lang.String} object.
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * <p>Getter for the field <code>uuid</code>.</p>
     *
     * @return the unique id of the track
     */
    public String getUuid() {

        if (this.uuid == null) {
            String	regex = "^" + AudioBoxClient.AudioBoxConnector.API_PATH.replace(".", "\\.") + PATH + "/([^\\s]+)/stream$";
            java.util.regex.Matcher m = java.util.regex.Pattern.compile(regex).matcher(streamUrl);
            m.find();
            this.setUuid( m.group(1) );
        }

        return this.uuid;		
    }


    /**
     * <p>Setter for the field <code>duration</code>.</p>
     *
     * @param duration a {@link java.lang.String} object.
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * <p>Getter for the field <code>duration</code>.</p>
     *
     * @return the duration
     */
    public String getDuration() {
        return duration;
    }


    /**
     * <p>Setter for the field <code>title</code>.</p>
     *
     * @param title a {@link java.lang.String} object.
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * <p>Getter for the field <code>title</code>.</p>
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>Setter for the field <code>streamUrl</code>.</p>
     *
     * @param url a {@link java.lang.String} object.
     */
    public void setStreamUrl(String url) {
        this.streamUrl = url;
    }


    /**
     * <p>Getter for the field <code>streamUrl</code>.</p>
     *
     * @return the streamUrl
     * @throws fm.audiobox.core.exceptions.LoginException if any.
     * @throws java.net.SocketException if any.
     */
    public String getStreamUrl() throws LoginException , SocketException {
        String[] result = this.getConnector().execute( this.endPoint, this.getUuid(), STREAM_ACTION, this, null);
        return result[ AudioBoxConnector.RESPONSE_BODY ];
    }

    /**
     * <p>Setter for the field <code>loved</code>.</p>
     *
     * @param loved a {@link java.lang.String} object.
     */
    public void setLoved(String loved) {
        this.loved = Boolean.parseBoolean( loved );
    }

    /**
     * <p>Setter for the field <code>loved</code>.</p>
     *
     * @param loved a boolean.
     */
    public void setLoved(boolean loved) {
        this.loved = loved;
    }

    /**
     * <p>isLoved</p>
     *
     * @return the loved
     */
    public boolean isLoved() {
        return loved;
    }


    /**
     * <p>Setter for the field <code>playCount</code>.</p>
     *
     * @param playCount a {@link java.lang.String} object.
     */
    public void setPlayCount(String playCount) {
        this.playCount = Integer.parseInt( playCount );
    }


    /**
     * <p>Setter for the field <code>playCount</code>.</p>
     *
     * @param playCount a int.
     */
    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    /**
     * <p>Getter for the field <code>playCount</code>.</p>
     *
     * @return the playCount
     */
    public int getPlayCount() {
        return playCount;
    }



    /**
     * <p>Setter for the field <code>year</code>.</p>
     *
     * @param year a {@link java.lang.String} object.
     */
    public void setYear(String year) {
        this.year = Integer.parseInt( year );
    }

    /**
     * <p>Getter for the field <code>year</code>.</p>
     *
     * @return the loved
     */
    public int getYear() {
        return year;
    }




    /**
     * <p>Setter for the field <code>fileHash</code>.</p>
     *
     * @param fileHash the fileHash to set
     */
    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    /**
     * <p>Getter for the field <code>fileHash</code>.</p>
     *
     * @return the fileHash
     */
    public String getFileHash() {
        return fileHash;
    }

    /**
     * <p>Setter for the field <code>audioFileSize</code>.</p>
     *
     * @param fileSize a {@link java.lang.String} object.
     */
    public void setAudioFileSize(String fileSize) {
        this.audioFileSize = Long.parseLong( fileSize );
    }

    /**
     * <p>Getter for the field <code>audioFileSize</code>.</p>
     *
     * @return the audioFileSize
     */
    public long getAudioFileSize() {
        return audioFileSize;
    }



    /**
     * <p>Setter for the field <code>artist</code>.</p>
     *
     * @param artist a {@link fm.audiobox.core.models.Artist} object.
     */
    public void setArtist(Artist artist) {
        this.artist = artist;
    }


    /**
     * <p>Getter for the field <code>artist</code>.</p>
     *
     * @return the artist
     */
    public Artist getArtist() {
        return artist;
    }



    /**
     * <p>Setter for the field <code>album</code>.</p>
     *
     * @param album a {@link fm.audiobox.core.models.Album} object.
     */
    public void setAlbum(Album album) {
        this.album = album;
    }

    /**
     * <p>Getter for the field <code>album</code>.</p>
     *
     * @return the album
     */
    public Album getAlbum() {
        return album;
    }



    /**
     * <p>Setter for the field <code>durationInSeconds</code>.</p>
     *
     * @param durationInSeconds a {@link java.lang.String} object.
     */
    public void setDurationInSeconds(String durationInSeconds) {
        this.durationInSeconds = Long.parseLong( durationInSeconds );
    }

    /**
     * <p>Getter for the field <code>durationInSeconds</code>.</p>
     *
     * @return the durationInSeconds
     */
    public long getDurationInSeconds() {
        return durationInSeconds;
    }

    /**
     * <p>getFileBody</p>
     *
     * @return the FileBody to upload
     */
    public FileBody getFileBody(){
        return this.fileBody;
    }


    /* ------- */
    /* Actions */
    /* ------- */

    /**
     * <p>scrobble</p>
     *
     * @throws fm.audiobox.core.exceptions.ServiceException if any.
     * @throws fm.audiobox.core.exceptions.LoginException if any.
     */
    public void scrobble() throws ServiceException, LoginException {
        this.getConnector().execute( this.endPoint, this.getUuid(), SCROBBLE_ACTION, this, HttpPost.METHOD_NAME);
    }

    /**
     * <p>love</p>
     *
     * @return a boolean.
     * @throws fm.audiobox.core.exceptions.ServiceException if any.
     * @throws fm.audiobox.core.exceptions.LoginException if any.
     */
    public boolean love() throws ServiceException, LoginException  {
        String[] result = this.getConnector().execute( this.endPoint, this.getUuid(), LOVE_ACTION, this, HttpPut.METHOD_NAME);
        boolean markLoved = HttpStatus.SC_OK == Integer.parseInt( result[ AudioBoxConnector.RESPONSE_CODE ] );
        this.setLoved( markLoved );
        return markLoved;
    }

    /**
     * <p>unlove</p>
     *
     * @return a boolean.
     * @throws fm.audiobox.core.exceptions.ServiceException if any.
     * @throws fm.audiobox.core.exceptions.LoginException if any.
     */
    public boolean unlove() throws ServiceException, LoginException {
        String[] result = this.getConnector().execute( this.endPoint, this.getUuid(), UNLOVE_ACTION, this, HttpPut.METHOD_NAME);
        boolean markLoved = HttpStatus.SC_OK == Integer.parseInt( result[ AudioBoxConnector.RESPONSE_CODE ] );
        this.setLoved( !markLoved );
        return markLoved;
    }

    /**
     * <p>delete</p>
     *
     * @return a boolean.
     * @throws fm.audiobox.core.exceptions.ServiceException if any.
     * @throws fm.audiobox.core.exceptions.LoginException if any.
     */
    public boolean delete() throws ServiceException, LoginException {
        String[] result = this.getConnector().execute( this.endPoint, this.getUuid(), null, this, HttpDelete.METHOD_NAME);
        return HttpStatus.SC_OK == Integer.parseInt( result[ AudioBoxConnector.RESPONSE_CODE ] );
    }


    /** {@inheritDoc} */
    @Override
    public String getName() {
        return this.title + " - " + this.artist.getName() + " (" + this.duration + ")";
    }


    /* ----- */
    /* State */
    /* ----- */

    /**
     * <p>setState</p>
     *
     * @param trackState the trackState to set
     */
    public void setState(State trackState) {
        this.trackState = trackState;
    }

    /**
     * <p>getState</p>
     *
     * @return the  track status
     */
    public State getState() {
        return trackState;
    }

    /**
     * <p>hasErrors</p>
     *
     * @return true if the track contains errors
     */
    public boolean hasErrors() {
        return getState() == Track.State.ERROR;
    }

    /**
     * <p>isPlaying</p>
     *
     * @return true if the track in in playing
     */
    public boolean isPlaying() {
        return getState() == Track.State.PLAYING;
    }

    /**
     * <p>isPaused</p>
     *
     * @return true if the track in in playing
     */
    public boolean isPaused() {
        return getState() == Track.State.PAUSED;
    }

    /**
     * <p>isBuffering</p>
     *
     * @return true if the track is currently buffering from network
     */
    public boolean isBuffering() {
        return getState() == Track.State.BUFFERING;
    }


    /* Overrides */
    /** {@inheritDoc} */
    @Override
    public Track getTrack(String uuid) {
        if ( this.getUuid().equals( uuid ) )
            return this;
        else return null;
    }

    /** {@inheritDoc} */
    @Override
    public void setTracks(Tracks tracks) { }

    /** {@inheritDoc} */
    @Override
    public Tracks getTracks() { return null; }

}


