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
import org.apache.http.entity.FileEntity;

import fm.audiobox.core.api.ModelItem;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;

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
 * @version 0.2-beta
 */


public class Track extends ModelItem {
	
	// Constants
	public static final String TAG_NAME = "track";
	
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
	protected FileEntity entity;
	
	
	protected Track() {
	    this.endPoint = Tracks.END_POINT;
	}
	
	public Track(FileEntity file) {
		super();
		this.entity = file;
	}
	
	@Override
	public String getToken() {
	    return getUuid();
	}
	
	public void setUuid(String uuid) {
	    this.uuid = uuid;
	}
	
	/**
	 * @return the unique id of the track
	 */
	public String getUuid() {

		if (uuid == null) {
			String	regex = "^" + AudioBoxClient.API_PATH.replace(".", "\\.") + PATH + "/([^\\s]+)/stream$";
			java.util.regex.Matcher m = java.util.regex.Pattern.compile(regex).matcher(streamUrl);
			m.find();
			uuid = m.group(1);
		}
		
		return uuid;		
	}
	
	
	public void setDuration(String duration) {
	    this.duration = duration;
	}
	
	/**
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}
	
	
	public void setTitle(String title) {
	    this.title = title;
	}
	
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	
	
	
	public void setStreamUrl(String url) {
	    this.streamUrl = url;
	}
	
	
	/**
	 * @return the streamUrl
	 */
	public String getStreamUrl() throws LoginException , SocketException {
		String[] result = this.getConnector().execute( this.endPoint, this.getUuid(), STREAM_ACTION, this, null);
		return result[1];
	}
	
	public void setLoved(String loved) {
        this.loved = Boolean.parseBoolean( loved );
    }
	
	public void setLoved(boolean loved) {
	    this.loved = loved;
	}
	
	/**
	 * @return the loved
	 */
	public boolean isLoved() {
		return loved;
	}
	
	
	public void setPlayCount(String playCount) {
	    this.playCount = Integer.parseInt( playCount );
	}
	

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }
	
	/**
	 * @return the playCount
	 */
	public int getPlayCount() {
		return playCount;
	}

	
	
	public void setYear(String year) {
	    this.year = Integer.parseInt( year );
	}
	
	/**
	 * @return the loved
	 */
	public int getYear() {
		return year;
	}
	
	
	
	
	/**
     * @param fileHash the fileHash to set
     */
    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    /**
     * @return the fileHash
     */
    public String getFileHash() {
        return fileHash;
    }

    public void setAudioFileSize(String fileSize) {
	    this.audioFileSize = Long.parseLong( fileSize );
	}
	
	/**
	 * @return the audioFileSize
	 */
	public long getAudioFileSize() {
		return audioFileSize;
	}
	
	
	
	public void setArtist(Artist artist) {
	    this.artist = artist;
	}
	
	
	/**
	 * @return the artist
	 */
	public Artist getArtist() {
		return artist;
	}
	
	
	
	public void setAlbum(Album album) {
	    this.album = album;
	}
	
	/**
	 * @return the album
	 */
	public Album getAlbum() {
		return album;
	}

	
	
	public void setDurationInSeconds(String durationInSeconds) {
	    this.durationInSeconds = Long.parseLong( durationInSeconds );
	}
	
	/**
	 * @return the durationInSeconds
	 */
	public long getDurationInSeconds() {
		return durationInSeconds;
	}
	
	/**
	 * @return the file to upload
	 */
	public FileEntity getFileEntity(){
		return this.entity;
	}
	
	
	/* ------- */
	/* Actions */
	/* ------- */
	
	public void scrobble() throws ServiceException, LoginException {
		this.getConnector().execute( this.endPoint, this.getUuid(), SCROBBLE_ACTION, null, HttpPost.METHOD_NAME);
	}
	
	public boolean love() throws ServiceException, LoginException  {
		String[] result = this.getConnector().execute( this.endPoint, this.getUuid(), LOVE_ACTION, null, HttpPut.METHOD_NAME);
	    return HttpStatus.SC_OK == Integer.parseInt( result[0] );
	}
	
	public boolean unlove() throws ServiceException, LoginException {
		String[] result = this.getConnector().execute( this.endPoint, this.getUuid(), UNLOVE_ACTION, null, HttpPut.METHOD_NAME);
	    return HttpStatus.SC_OK == Integer.parseInt( result[0] );
	}
	
	public boolean delete() throws ServiceException, LoginException {
		String[] result = this.getConnector().execute( this.endPoint, this.getUuid(), null, null, HttpDelete.METHOD_NAME);
	    return HttpStatus.SC_OK == Integer.parseInt( result[0] );
	}
	
	
	@Override
    public String getName() {
        return this.title + " - " + this.artist.getName() + " (" + this.duration + ")";
    }
	
	
	/* ----- */
	/* State */
	/* ----- */
	
	/**
	 * @param trackState the trackState to set
	 */
	public void setState(State trackState) {
		this.trackState = trackState;
	}

	/**
	 * @return the  track status
	 */
	public State getState() {
		return trackState;
	}

	/**
	 * @return true if the track contains errors
	 */
	public boolean hasErrors() {
		return getState() == Track.State.ERROR;
	}

	/**
	 * @return true if the track in in playing
	 */
	public boolean isPlaying() {
		return getState() == Track.State.PLAYING;
	}
	
	/**
	 * @return true if the track in in playing
	 */
	public boolean isPaused() {
		return getState() == Track.State.PAUSED;
	}

	/**
	 * @return true if the track is currently buffering from network
	 */
	public boolean isBuffering() {
		return getState() == Track.State.BUFFERING;
	}
	
	
	/* Overrides */
	@Override
	public Track getTrack(String uuid) {
	    if ( this.getUuid().equals( uuid ) )
	        return this;
	    else return null;
	}

	@Override
    public void setTracks(Tracks tracks) { }
    
    @Override
    public Tracks getTracks() { return null; }

}


