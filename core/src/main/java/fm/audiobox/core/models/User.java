
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;

import fm.audiobox.core.AudioBoxClient;
import fm.audiobox.core.api.Model;
import fm.audiobox.core.api.ModelItem;
import fm.audiobox.core.api.ModelsCollection;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.interfaces.ResponseHandler;
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
 * @version 0.0.1
 */

public class User extends ModelItem implements ResponseHandler {

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
    protected ModelItem profile;

    // User's collection relations
    protected ModelsCollection playlists;
    protected ModelsCollection genres;
    protected ModelsCollection artists;
    protected ModelsCollection albums;
    
    private String[] tracks;

    /**
     * <p>Constructor for User.</p>
     */
    public User() {
        this.endPoint = PATH;
        this.name = TAG_NAME;
        this.token = null;
        try {
            this.init();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void init() throws InstantiationException, IllegalAccessException {
        this.playlists = (ModelsCollection) abml.getModelClassName(this.getClass(), Playlists.END_POINT ).newInstance();
        this.genres    = (ModelsCollection) abml.getModelClassName(this.getClass(), Genres.END_POINT ).newInstance();
        this.artists   = (ModelsCollection) abml.getModelClassName(this.getClass(), Artists.END_POINT ).newInstance();
        this.albums    = (ModelsCollection) abml.getModelClassName(this.getClass(), Albums.END_POINT ).newInstance();
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
    public void setAvalableStorage(String availableStorage) {
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
     * @param profile a {@link fm.audiobox.core.api.ModelItem} object.
     */
    public void setProfile(ModelItem profile) {
        this.profile = profile;
    }


    /**
     * <p>Getter for the field <code>profile</code>.</p>
     *
     * @return the profile
     */
    public ModelItem getProfile() {
        return this.profile;
    }

    
    /** {@inheritDoc} */
    @Override
    public String getName() {
        return this.username;
    }


    /**
     * <p>getAuth</p>
     *
     * @return the base64 encoded username:password string pair for Basic Authentications
     */
    public String getAuth() {
        String auth = ( email == null ? username : email ) + ":" + password;
        return Base64.encodeBytes( auth.getBytes() );
    }

    
    /**
     * <p>Getter for the field <code>playlists</code>.</p>
     *
     * @return the playlists
     * @throws fm.audiobox.core.exceptions.LoginException if any.
     * @throws fm.audiobox.core.exceptions.ServiceException if any.
     */
    public ModelsCollection getPlaylists() throws ServiceException, LoginException {
        return playlists;
    }
    

    /**
     * <p>Getter for the field <code>genres</code>.</p>
     *
     * @return the genres
     */
    public ModelsCollection getGenres() {
        return genres;
    }


    /**
     * <p>Getter for the field <code>artists</code>.</p>
     *
     * @return the artists
     */
    public ModelsCollection getArtists() {
        return artists;
    }

    
    /**
     * <p>Getter for the field <code>albums</code>.</p>
     *
     * @return the albums
     */
    public ModelsCollection getAlbums() {
        return albums;
    }

    
    /**
     * <p>getUploadedTracks</p>
     *
     * @return an array of MD5 hashes of uploaded tracks.
     * @throws fm.audiobox.core.exceptions.ServiceException if any.
     * @throws fm.audiobox.core.exceptions.LoginException if any.
     */
    public String[] getUploadedTracks() throws ServiceException, LoginException {
        AudioBoxClient.execute(Tracks.END_POINT, null, null, this, HttpGet.METHOD_NAME, "txt");
        return this.tracks;
    }


    /** {@inheritDoc} */
    public void parseResponse(InputStream is, Header contentType) throws IOException {

        if (contentType.getValue().contains("xml")) {
            super.parseResponse(is, contentType);
            return;
        }

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } finally {
                is.close();
            }


            /*
             * TODO: check this code
             * remove whitespaces
             */
            String[] result = sb.toString().split(";");
            this.tracks =  new String[ result.length ];
            int pos=0;
            for ( String hash : result )
              this.tracks[ pos++ ] = hash.trim();
            
        } else {       
            this.tracks = new String[]{};
        }

    }

}
