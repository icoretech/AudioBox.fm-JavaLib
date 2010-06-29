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

import org.apache.http.client.methods.HttpGet;

import fm.audiobox.core.api.Model;
import fm.audiobox.core.api.ModelItem;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.interfaces.ResponseHandler;
import fm.audiobox.core.models.AudioBoxClient.AudioBoxConnector;
import fm.audiobox.core.util.Base64;

/**
 * 
 * User model is a special {@link Model} just because almost every library browse action is performed through this 
 * object.
 * 
 * <p>
 *   
 * <pre>
 * {@code
 * <user>
 *   <bytes-served>123456</bytes-served>
 *   <email>user@example.com</email>
 *   <play-count type="integer">1042</play-count>
 *   <quota>984354165</quota>
 *   <state>active</state>
 *   <tracks-count type="integer">1490</tracks-count>
 *   <username>Username</username>
 *   <available-storage type="integer">1232321123</available-storage>
 *   <avatar-url>http://url.to.avatar/avatar.png</avatar-url>
 *   <profile>
 *      <autoplay type="boolean">false</autoplay>
 *      <birth-date type="date">1970-01-01</birth-date>
 *      <country>US</country>
 *      <gender>m</gender>
 *      <home-page>http://www.myblog.com</home-page>
 *      <real-name>Real User Name</real-name>
 *      <time-zone>New York</time-zone>
 *   </profile>
 * </user>
 * 
 * }
 * </pre>
 * 
 * @author Valerio Chiodino
 * @version 0.2-beta
 * 
 */

public class User extends ModelItem implements ResponseHandler {

    public static final String TAG_NAME = "user";
    public static final String PATH = TAG_NAME;

    public static final String ACTIVE_STATE = "active";

    protected long bytesServed;
    protected String email;
    protected int playCount;
    protected long quota;
    protected String state;
    protected int tracksCount;
    protected String username;
    protected String password;
    protected long availableStorage;
    protected String avatarUrl;
    protected Profile profile;

    // User's collection relations
    protected Playlists playlists;
    protected Genres genres;
    protected Artists artists;
    protected Albums albums;
    
    private String[] tracks;

    protected User() {
        this.endPoint = PATH;
        this.name = TAG_NAME;
        this.token = null;
    }


    public void setBytesServed(String bytes) {
        this.bytesServed = Long.parseLong( bytes );
    }

    /**
     * @return the bytesServed
     */
    public long getBytesServed() {
        return this.bytesServed;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }



    public void setPlayCount(String playCount) {
        this.playCount = Integer.parseInt( playCount );
    }

    /**
     * @return the playCount
     */
    public int getPlayCount() {
        return this.playCount;
    }


    public void setQuota(String quota) {
        this.quota = Long.parseLong( quota );
    }

    /**
     * @return the quota
     */
    public long getQuota() {
        return this.quota;
    }



    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the state
     */
    public String getState() {
        return this.state;
    }


    public void setTracksCount(String tracksCount) {
        this.tracksCount = Integer.parseInt( tracksCount );
    }

    /**
     * @return the tracksCount
     */
    public int getTracksCount() {
        return this.tracksCount;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public void setAvalableStorage(String availableStorage) {
        this.availableStorage = Long.parseLong( availableStorage );
    }

    /**
     * @return the availableStorage
     */
    public long getAvailableStorage() {
        return this.availableStorage;
    }



    public void setAvatarUrl(String url) {
        this.avatarUrl = url;
    }

    /**
     * @return the avatarUrl
     */
    public String getAvatarUrl() {
        return this.avatarUrl;
    }



    public void setProfile(Profile profile) {
        this.profile = profile;
    }


    /**
     * @return the profile
     */
    public Profile getProfile() {
        return this.profile;
    }

    @Override
    public String getName() {
        return this.username;
    }


    public String getAuth() {
        String auth = ( email == null ? username : email ) + ":" + password;
        return Base64.encodeBytes( auth.getBytes() );
    }

    /**
     * @return the playlists
     * @throws LoginException 
     * @throws ServiceException 
     * @throws ModelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public Playlists getPlaylists() throws ServiceException, LoginException, ModelException {
    	this.playlists = (Playlists)AudioBoxClient.getModelInstance(AudioBoxClient.PLAYLISTS_KEY, this.getConnector());
    	this.getConnector().execute(Playlists.END_POINT, null, null, this.playlists, null);
        return playlists;
    }

    /**
     * @return the genres
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws LoginException 
     * @throws ServiceException 
     * @throws ModelException 
     */
    public Genres getGenres() throws ServiceException, LoginException, ModelException {
   		this.genres = (Genres)AudioBoxClient.getModelInstance(AudioBoxClient.GENRES_KEY, this.getConnector());
   		this.getConnector().execute(Genres.END_POINT, null, null, this.genres, null);
        return this.genres;
    }


    /**
     * @return the artists
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws LoginException 
     * @throws ServiceException 
     * @throws ModelException 
     */
    public Artists getArtists() throws ServiceException, LoginException, ModelException {
   		this.artists = (Artists)AudioBoxClient.getModelInstance(AudioBoxClient.ARTISTS_KEY, this.getConnector());
   		this.getConnector().execute(Artists.END_POINT, null, null, this.artists, null);
        return this.artists;
    }

    /**
     * @return the albums
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws LoginException 
     * @throws ServiceException 
     * @throws ModelException 
     */
    public Albums getAlbums() throws ServiceException, LoginException, ModelException {
   		this.albums = (Albums)AudioBoxClient.getModelInstance(AudioBoxClient.ALBUMS_KEY, this.getConnector());
   		this.getConnector().execute(Albums.END_POINT, null, null, this.albums, null);
        return this.albums;
    }

    public String[] getUploadedTracks() throws ServiceException, LoginException {
    	String[] result = this.getConnector().execute(Tracks.END_POINT, null, null, this, HttpGet.METHOD_NAME, AudioBoxConnector.TEXT_FORMAT);
    	String response = result[1];
    	result = response.split(";");
    	this.tracks =  new String[ result.length ];
    	int pos=0;
    	for ( String hash : result )
    		this.tracks[ pos++ ] = hash.trim();
        return this.tracks;
    }

}
