
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

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;

import fm.audiobox.core.api.ModelItem;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.AudioBoxClient.AudioBoxConnector;


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
    
    /** The XML tag name for the Playlist element */
    public static final String TAG_NAME = "playlist";
    
    /* Actions */
    /** Used to empty the recycle bin */
    protected static final String EMPTY_TRASH_ACTION = "empty_trash";
    /** Used to add a list of tracks to a playlist */
    protected static final String ADD_ACTION = "add_tracks";
    /** Used to remove a list of tracks from a playlist */
    protected static final String REMOVE_ACTION = "remove_tracks";

    /* Parameters */
    /** Used as HTTP parameters to specify the tracks list */
    private static final String HTTP_PARAM = "track_tokens[]";
    
    /** Used to encode the entity string */
    private static final String CHAR_ENCODING = "UTF-8";
    
    protected int playlistTracksCount;
    protected String playlistType;
    protected int position;

    private UrlEncodedFormEntity entity;
    
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

    
    /**
     * <p>Getter for the http entity</p>
     * 
     * @return the entity to make management requests to AudioBox.fm
     */
    public UrlEncodedFormEntity getEntity() {
        return entity;
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
        return this.addTracks(listify(track));
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
        return performAction(tracks, ADD_ACTION);
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
        return this.removeTracks(listify(track));
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
        return performAction(tracks, REMOVE_ACTION);
    }
    
    
    
    
    /* --------------- */
    /* Private methods */
    /* --------------- */
    
    
    /**
     * Used to prepare parameters to send playlists management requests.
     * 
     * @param tracks a {@link List} of {@link Track tracks} to add to the request
     * 
     * @throws UnsupportedEncodingException if an error while evaluating {@link Track#getToken()} occurs.
     */
    private void buildEntity(List<Track> tracks) throws UnsupportedEncodingException {
        
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Track track : tracks)
            params.add(new BasicNameValuePair(HTTP_PARAM, track.getToken()));

        this.entity = new UrlEncodedFormEntity(params, CHAR_ENCODING);
        
    }
    
    
    /**
     * Used to transform a single track in a list of tracks containing a single element.
     * 
     * @param track the track to transform into a list of tracks
     * @return a list of tracks containing a single element
     */
    private List<Track> listify(Track track) {
        List<Track> tracks = new ArrayList<Track>();
        tracks.add(track);
        return tracks;
    }

    
    /**
     * Use this method to add or remove multiple {@link Track tracks} from this playlist
     * 
     * @param tracks the {@link List} of {@link Track Tracks} to remove from playlist
     * @param action the action to perform, can be one of {@link Playlist#ADD_ACTION} or {@link Playlist#REMOVE_ACTION}
     * 
     * @return true if the action succeed false if something goes wrong.
     * 
     * @throws LoginException if any authentication problem during the request occurs.
     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
     */
    private boolean performAction(List<Track> tracks, String action) throws LoginException, ServiceException {
        try {
            this.buildEntity(tracks);
            String[] result = this.getConnector().execute( this.pEndPoint, this.getToken(), action, this, HttpPut.METHOD_NAME);
            boolean ok = HttpStatus.SC_OK == Integer.parseInt( result[ AudioBoxConnector.RESPONSE_CODE ] );
            
            if (ok) {
                try {
                    this.buildCollection( false );
                    // Update tracks count
                    this.playlistTracksCount = this.getTracks().getCollection().size();
                } catch (ModelException e) {
                    e.printStackTrace();
                }
            }
            
            return ok;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
