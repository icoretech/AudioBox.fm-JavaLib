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

package fm.audiobox.api.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;

import fm.audiobox.api.AudioBoxClient;
import fm.audiobox.api.core.ModelItem;
import fm.audiobox.api.core.ModelsCollection;
import fm.audiobox.api.exceptions.LoginException;
import fm.audiobox.api.exceptions.ServiceException;
import fm.audiobox.api.interfaces.ResponseHandler;
import fm.audiobox.api.util.Base64;

/**
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
    protected ModelItem profile;

    // User's collection relations
    protected ModelsCollection playlists;
    protected ModelsCollection genres;
    protected ModelsCollection artists;
    protected ModelsCollection albums;
    
    private String[] tracks;

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



    public void setProfile(ModelItem profile) {
        this.profile = profile;
    }


    /**
     * @return the profile
     */
    public ModelItem getProfile() {
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
     */
    public ModelsCollection getPlaylists() throws ServiceException, LoginException {
        return playlists;
    }

    /**
     * @return the genres
     */
    public ModelsCollection getGenres() {
        return genres;
    }


    /**
     * @return the artists
     */
    public ModelsCollection getArtists() {
        return artists;
    }

    /**
     * @return the albums
     */
    public ModelsCollection getAlbums() {
        return albums;
    }

    public String[] getUploadedTracks() throws ServiceException, LoginException {
        AudioBoxClient.execute(Tracks.END_POINT, null, null, this, HttpGet.METHOD_NAME, "txt");
        return this.tracks;
    }


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

            this.tracks =  sb.toString().split(";");
        } else {       
            this.tracks = new String[]{};
        }

    }

}
