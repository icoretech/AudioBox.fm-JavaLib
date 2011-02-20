
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.AudioBox.Connector;
import fm.audiobox.core.api.ModelItem;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;


/**
 * Playlist class is a {@link ModelItem} specialization for playlists XML elements.
 * 
 * <p>
 * 
 * Profile XML looks like this: 
 * 
 * <pre>
 * {@code
 * <playlist>
 *   <name>Music</name>
 *   <playlist_tracks_count type="integer">1591</playlist_tracks_count>
 *   <playlist_type>audio</playlist_type>
 *   <position type="integer">1</position>
 *   <token>ass8sad909sh</token>
 * </playlist>
 * }
 * </pre>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Playlist extends ModelItem {
    
	private static final Logger log = LoggerFactory.getLogger(Playlist.class);
	
    /** The XML tag name for the Playlist element */
    public static final String TAG_NAME = "playlist";
    
    /* Parameters */
    /** Used as HTTP parameters to specify the tracks list */
    private static final String HTTP_PARAM = "track_tokens[]";
    
    /** Used to encode the entity string */
    private static final String CHAR_ENCODING = "UTF-8";
    
    protected int playlistTracksCount;
    protected String playlistType;
    protected int position;
    
    /**
     * <p>Constructor for Playlist.</p>
     */
    protected Playlist() {
        this.pEndPoint = Playlists.END_POINT;
    }

    
    /* ------------------- */
    /* Getters and setters */
    /* ------------------- */
    
    
    /**
     * <p>Setter for the playlist tracks count: used by the parser.</p>
     *
     * @param tracksCount the tracks count {@link String} to set.
     */
    public void setPlaylistTracksCount(String tracksCount) {
        this.playlistTracksCount = Integer.parseInt( tracksCount );
    }

    /**
     * <p>Getter for the playlist tracks count.</p>
     *
     * @return the playlist tracks count
     */
    public int getPlaylistTracksCount() {
        return playlistTracksCount;
    }

    
    
    /**
     * <p>Setter for the playlist type: used by the parser.</p>
     *
     * @param type the playlist type {@link String} to set.
     */
    public void setPlaylistType(String type) {
        this.playlistType = type;
    }

    /**
     * <p>Getter for the playlist type.</p>
     *
     * @return the playlist type
     */
    public String getPlaylistType() {
        return playlistType;
    }
    
    

    /**
     * <p>Setter for the playlist position: used by the parser.</p>
     *
     * @param position the playlist position {@link java.lang.String} to set.
     */
    public void setPosition(String position) {
        this.position = Integer.parseInt(position);
    }

    /**
     * <p>Getter for the playlist position.</p>
     *
     * @return the playlist position
     */
    public int getPosition() {
        return position;
    }

    
    /* ---------------------------- */
    /* Playlists management methods */
    /* ---------------------------- */
    

    /**
     * Use this method to add a single {@link Track track} to this playlist
     * 
     * @param track the {@link Track} to add to playlist
     * @return true if the action succeed false if something goes wrong.
     * 
     * @throws LoginException if any authentication problem during the request occurs.
     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
     */
    public boolean addTrack(Track track) throws LoginException, ServiceException {
    	List<Track> tracks = new ArrayList<Track>();
    	tracks.add(track);
        return this.addTracks(tracks);
    }

    
    /**
     * Use this method to add multiple {@link Track tracks} to this playlist
     * 
     * @param tracks the {@link List} of {@link Track Tracks} to add to playlist
     * @return true if the action succeed false if something goes wrong.
     * 
     * @throws LoginException if any authentication problem during the request occurs.
     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
     */
    public boolean addTracks(List<Track> tracks) throws LoginException, ServiceException {
    	Tracks addedTracks = performAction(tracks, Playlists.ADD_TRACKS_ACTION);
    	if ( addedTracks == null ) return false;
    	if ( this.mTracks != null ) {
    		// Tracks already loaded, manual management
	    	for ( ModelItem addedTrack : addedTracks.getCollection() ){
	    		for ( Track track : tracks ){
	    			if ( track.getToken().equals( addedTrack.getToken() )){
	    				// TODO: fire collection listener events
	    				this.mTracks.addTrack(track);
	    				this.playlistTracksCount++;
	    			}
	    		}
	    	}
    	}
        return true;
    }
    
    
    /**
     * Use this method to remove a single {@link Track} from this playlist
     * 
     * @param track the {@link Track} to remove from playlist
     * @return true if the action succeed false if something goes wrong.
     * 
     * @throws LoginException if any authentication problem during the request occurs.
     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
     */
    public boolean removeTrack(Track track) throws LoginException, ServiceException {
    	List<Track> tracks = new ArrayList<Track>();
    	tracks.add(track);
        return this.removeTracks(tracks);
    }

    
    /**
     * Use this method to remove multiple {@link Track tracks} from this playlist
     * 
     * @param tracks the {@link List} of {@link Track Tracks} to remove from playlist
     * @return true if the action succeed false if something goes wrong.
     * 
     * @throws LoginException if any authentication problem during the request occurs.
     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
     */
    public boolean removeTracks(List<Track> tracks) throws LoginException, ServiceException {
    	Tracks removedTracks = performAction(tracks, Playlists.REMOVE_TRACKS_ACTION);
    	if ( removedTracks == null ) return false;
    	if ( this.mTracks != null ) {
    		// Tracks already loaded, manual management
	    	for ( ModelItem removedTrack : removedTracks.getCollection() ){
	    		for ( Track track : tracks ){
	    			if ( track.getToken().equals(removedTrack.getToken() )){
	    				// TODO: fire collection listener events
	    				if (  this.mTracks.removeFromCollection( track.getToken()  ) != null  )
	    					this.playlistTracksCount--;	// remove correctly
	    			}
	    		}
	    	}
    	}
        return true;
    }
    
    
    
    
    /* --------------- */
    /* Private methods */
    /* --------------- */
    
    
    /**
     * Use this method to add or remove multiple {@link Track tracks} from this playlist
     * 
     * @param tracks the {@link List} of {@link Track Tracks} to remove/add
     * @param action the action to perform. Should be one of {@link Playlists#ADD_TRACKS_ACTION} or {@link Playlists#REMOVE_TRACKS_ACTION} 
     * 
     * @return Tracks instance that contains all added/removed tracks tokens
     * 
     * @throws LoginException if any authentication problem during the request occurs.
     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
     */
    private Tracks performAction(List<Track> tracks, String action) throws LoginException, ServiceException {
        
    	try {
    		
        	List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (Track track : tracks)
                params.add(new BasicNameValuePair(HTTP_PARAM, track.getToken()));
        	
            HttpEntity entity = new UrlEncodedFormEntity(params, CHAR_ENCODING);
            
            Tracks _tracks = new Tracks();
            
            String[] result = this.getConnector().put( this, _tracks, action, entity);
            
            
            if ( HttpStatus.SC_OK == Integer.parseInt( result[ Connector.RESPONSE_CODE ] )  ) {
            	return _tracks;
            }
            
        } catch (UnsupportedEncodingException e) {
        	log.error("An error occurred while encoding", e);
        }
        
        return null;
    }
}
