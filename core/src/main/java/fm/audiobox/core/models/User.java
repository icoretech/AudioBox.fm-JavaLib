
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
import fm.audiobox.core.api.ModelsCollection;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.AudioBoxClient.AudioBoxConnector;


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
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class User extends ModelItem {

    /** Constant <code>TAG_NAME="user"</code> */
    public static final String TAG_NAME = "user";
    /** Constant <code>PATH="TAG_NAME"</code> */
    public static final String PATH = TAG_NAME;

    /** Constant <code>ACTIVE_STATE="active"</code> */
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

    /**
     * <p>Constructor for User.</p>
     */
    protected User() {
        this.pEndPoint = PATH;
        this.pName = TAG_NAME;
        this.pToken = null;
    }


    /**
     * <p>Setter for the field <code>bytesServed</code>.</p>
     *
     * @param bytes a {@link java.lang.String} object.
     */
    public void setBytesServed(String bytes) {
        this.bytesServed = Long.parseLong( bytes );
    }

    /**
     * <p>Getter for the field <code>bytesServed</code>.</p>
     *
     * @return the bytesServed
     */
    public long getBytesServed() {
        return this.bytesServed;
    }


    /**
     * <p>Setter for the field <code>email</code>.</p>
     *
     * @param email a {@link java.lang.String} object.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * <p>Getter for the field <code>email</code>.</p>
     *
     * @return the email
     */
    public String getEmail() {
        return this.email;
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
     * <p>Getter for the field <code>playCount</code>.</p>
     *
     * @return the playCount
     */
    public int getPlayCount() {
        return this.playCount;
    }


    /**
     * <p>Setter for the field <code>quota</code>.</p>
     *
     * @param quota a {@link java.lang.String} object.
     */
    public void setQuota(String quota) {
        this.quota = Long.parseLong( quota );
    }

    /**
     * <p>Getter for the field <code>quota</code>.</p>
     *
     * @return the quota
     */
    public long getQuota() {
        return this.quota;
    }



    /**
     * <p>Setter for the field <code>state</code>.</p>
     *
     * @param state a {@link java.lang.String} object.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * <p>Getter for the field <code>state</code>.</p>
     *
     * @return the state
     */
    public String getState() {
        return this.state;
    }


    /**
     * <p>Setter for the field <code>tracksCount</code>.</p>
     *
     * @param tracksCount a {@link java.lang.String} object.
     */
    public void setTracksCount(String tracksCount) {
        this.tracksCount = Integer.parseInt( tracksCount );
    }

    /**
     * <p>Getter for the field <code>tracksCount</code>.</p>
     *
     * @return the tracksCount
     */
    public int getTracksCount() {
        return this.tracksCount;
    }


    /**
     * <p>Setter for the field <code>username</code>.</p>
     *
     * @param username a {@link java.lang.String} object.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * <p>Getter for the field <code>username</code>.</p>
     *
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }


    /**
     * <p>Setter for the field <code>password</code>.</p>
     *
     * @param password a {@link java.lang.String} object.
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * <p>setAvalableStorage</p>
     *
     * @param availableStorage a {@link java.lang.String} object.
     */
    public void setAvailableStorage(String availableStorage) {
        this.availableStorage = Long.parseLong( availableStorage );
    }

    /**
     * <p>Getter for the field <code>availableStorage</code>.</p>
     *
     * @return the availableStorage
     */
    public long getAvailableStorage() {
        return this.availableStorage;
    }



    /**
     * <p>Setter for the field <code>avatarUrl</code>.</p>
     *
     * @param url a {@link java.lang.String} object.
     */
    public void setAvatarUrl(String url) {
        this.avatarUrl = url;
    }

    /**
     * <p>Getter for the field <code>avatarUrl</code>.</p>
     *
     * @return the avatarUrl
     */
    public String getAvatarUrl() {
        return this.avatarUrl;
    }



    /**
     * <p>Setter for the field <code>profile</code>.</p>
     *
     * @param profile a {@link fm.audiobox.core.models.Profile} object.
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }


    /**
     * <p>Getter for the field <code>profile</code>.</p>
     *
     * @return the profile
     */
    public Profile getProfile() {
        return this.profile;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return this.username;
    }


    /**
     * Given a known track UUID this method returns a valid {@link Track} object.
     *
     * @param uuid the UUID of the track you are asking for.
     * @return the requested track if it exists.
     * @throws fm.audiobox.core.exceptions.LoginException if user has not been authenticated
     * @throws fm.audiobox.core.exceptions.ServiceException if the requested resource doesn't exists or any other fm.audiobox.core.exceptions.ServiceException occur.
     * @throws ModelException 
     */
    public Track getTrackByUuid(String uuid) throws ServiceException, LoginException, ModelException {
        Track tr = (Track) AudioBoxClient.getModelInstance(AudioBoxClient.TRACK_KEY, this.getConnector());
        this.getConnector().execute(Tracks.END_POINT, uuid, null, tr, HttpGet.METHOD_NAME);
        return tr;
    }
    
    
    /**
     * <p>Getter for the field <code>playlists</code>.</p>
     *
     * @return the playlists
     * @throws fm.audiobox.core.exceptions.ModelException if any.
     */
    public Playlists getPlaylists() throws ModelException {
        return this.getPlaylists(false);
    }


    /**
     * <p>Getter for the field <code>playlists</code>.</p>
     *
     * @param async a boolean.
     * @return a {@link fm.audiobox.core.models.Playlists} object.
     * @throws fm.audiobox.core.exceptions.ModelException if any.
     */
    public Playlists getPlaylists(boolean async) throws ModelException {
        this.playlists = (Playlists) AudioBoxClient.getModelInstance(AudioBoxClient.PLAYLISTS_KEY, this.getConnector());
        Thread t = populateCollection( Playlists.END_POINT, this.playlists );
        if (async)
            t.start();
        else
            t.run();

        return playlists;
    }

    /**
     * <p>Getter for the field <code>genres</code>.</p>
     *
     * @return the genres
     * @throws fm.audiobox.core.exceptions.ModelException if any.
     */
    public Genres getGenres() throws ModelException {
        return this.getGenres(false);
    }

    /**
     * <p>Getter for the field <code>genres</code>.</p>
     *
     * @param async a boolean.
     * @return a {@link fm.audiobox.core.models.Genres} object.
     * @throws fm.audiobox.core.exceptions.ModelException if any.
     */
    public Genres getGenres(boolean async) throws ModelException {
        this.genres = (Genres) AudioBoxClient.getModelInstance(AudioBoxClient.GENRES_KEY, this.getConnector());
        Thread t = populateCollection( Genres.END_POINT, this.genres );
        if (async)
            t.start();
        else
            t.run();

        return this.genres;
    }


    /**
     * <p>Getter for the field <code>artists</code>.</p>
     *
     * @return the artists
     * @throws fm.audiobox.core.exceptions.ModelException if any.
     */
    public Artists getArtists() throws ModelException {
        return this.getArtists(false);
    }

    /**
     * <p>Getter for the field <code>artists</code>.</p>
     *
     * @param async a boolean.
     * @return a {@link fm.audiobox.core.models.Artists} object.
     * @throws fm.audiobox.core.exceptions.ModelException if any.
     */
    public Artists getArtists(boolean async) throws ModelException {
        this.artists = (Artists) AudioBoxClient.getModelInstance(AudioBoxClient.ARTISTS_KEY, this.getConnector());
        Thread t = populateCollection( Artists.END_POINT, this.artists );
        if (async)
            t.start();
        else
            t.run();

        return this.artists;
    }

    /**
     * <p>Getter for the field <code>albums</code>.</p>
     *
     * @return the albums
     * @throws fm.audiobox.core.exceptions.ModelException if any.
     */
    public Albums getAlbums() throws ModelException {
        return this.getAlbums(false);
    }

    /**
     * <p>Getter for the field <code>albums</code>.</p>
     *
     * @param async a boolean.
     * @return a {@link fm.audiobox.core.models.Albums} object.
     * @throws fm.audiobox.core.exceptions.ModelException if any.
     */
    public Albums getAlbums(boolean async) throws ModelException {
        this.albums = (Albums) AudioBoxClient.getModelInstance(AudioBoxClient.ALBUMS_KEY, this.getConnector());
        Thread t = populateCollection( Albums.END_POINT, this.albums );
        if (async)
            t.start();
        else
            t.run();

        return this.albums;
    }

    /**
     * <p>getUploadedTracks</p>
     *
     * @return an array of {@link java.lang.String} objects.
     * @throws fm.audiobox.core.exceptions.ServiceException if any.
     * @throws fm.audiobox.core.exceptions.LoginException if any.
     */
    public String[] getUploadedTracks() throws ServiceException, LoginException {
        String[] result = this.getConnector().execute(Tracks.END_POINT, null, null, this, HttpGet.METHOD_NAME, AudioBoxConnector.TEXT_FORMAT);
        String response = result[ AudioBoxConnector.RESPONSE_BODY ];
        result = response.split( ";" , response.length() );
        this.tracks =  new String[ result.length ];
        int pos=0;
        for ( String hash : result )
            this.tracks[ pos++ ] = hash.trim();
        return this.tracks;
    }


    private Thread populateCollection(final String endpoint, final ModelsCollection collection) {

        final User user = this;

        return new Thread() {

            public void run() {
                try {
                    user.getConnector().execute(endpoint, null, null, collection, null);
                } catch (ServiceException e) {
                    e.printStackTrace();
                } catch (LoginException e) {
                    e.printStackTrace();
                }
            }

        };

    }

}
