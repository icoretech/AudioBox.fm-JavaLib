
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

import fm.audiobox.core.api.ModelItem;


/**
 * Plan is linked to the {@link User} and specify user's plan details.
 * 
 * <p>
 * 
 * Plan XML looks like this: 
 * 
 * <pre>
 * {@code
 * <plan>
 *   <feat_api>true</feat_api>
 *   <feat_cloud_web_player>true</feat_cloud_web_player>
 *   <feat_dropbox>true</feat_dropbox>
 *   <feat_lastfm>true</feat_lastfm>
 *   <feat_manager>true</feat_manager>
 *   <feat_multiformat>true</feat_multiformat>
 *   <feat_social_networks>true</feat_social_networks>
 *   <feat_youtube_channel>true</feat_youtube_channel>
 *   <name>ultra</name>
 * </plan>
 * }
 * </pre>
 * 
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Plan extends ModelItem {

    /** The XML tag name for the Profile element */
    public static final String TAG_NAME = "plan";
    
    private boolean featApi;
    private boolean featCloudWebPlayer;
    private boolean featDropbox;
    private boolean featLastfm;
    private boolean featManager;
    private boolean featMultiformat;
    private boolean featSocialNetworks;
    private boolean featYoutubeChannel;
    
    /**
     * <p>Constructor for Plan.</p>
     */
    protected Plan() { }
    
    
    /* ------------------- */
    /* Getters and setters */
    /* ------------------- */
    
    
    /**
     * This method is used by the parser.
     * 
     * <p>Setter for the "API" option: used by the parser.</p>
     *
     * @param featApi String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setFeatApi(String featApi) {
        this.featApi = Boolean.parseBoolean( featApi );
    }
    
    /**
     * Checks whether the user has "API" option enabled or not.
     *
     * @return true if "API" option is available.
     */
    public boolean hasApi() {
        return this.featApi;
    }
    
    
    
    /**
     * This method is used by the parser.
     * 
     * <p>Setter for the "Youtube Channel" option: used by the parser.</p>
     *
     * @param featYoutubeChannel String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setFeatYoutubeChannel(String featYoutubeChannel) {
        this.featYoutubeChannel = Boolean.parseBoolean( featYoutubeChannel );
    }
    
    /**
     * Checks whether the user has "Youtube Channel" option enabled or not.
     *
     * @return true if "Youtube Channel" option is available.
     */
    public boolean hasYoutubeChannel() {
        return this.featYoutubeChannel;
    }
    
    
    
    /**
     * This method is used by the parser.
     * 
     * <p>Setter for the "Cloud Web Player" option: used by the parser.</p>
     *
     * @param featCloudWebPlayer String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setFeatCloudWebPlayer(String featCloudWebPlayer) {
        this.featCloudWebPlayer = Boolean.parseBoolean( featCloudWebPlayer );
    }
    
    /**
     * Checks whether the user has "Cloud Web Player" option enabled or not.
     *
     * @return true if "Cloud Web Player" option is available.
     */
    public boolean hasCloudWebPlayer() {
        return this.featCloudWebPlayer;
    }
    
    
    
    
    /**
     * This method is used by the parser.
     * 
     * <p>Setter for the "Dropbox" option: used by the parser.</p>
     *
     * @param featDropbox String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setFeatDropbox(String featDropbox) {
        this.featDropbox = Boolean.parseBoolean( featDropbox );
    }
    
    /**
     * Checks whether the user has "Dropbox" option enabled or not.
     *
     * @return true if "Dropbox" option is available.
     */
    public boolean hasDropbox() {
        return this.featDropbox;
    }
    
    
    
    
    /**
     * This method is used by the parser.
     * 
     * <p>Setter for the "Multiformat" option: used by the parser.</p>
     *
     * @param featMultiformat String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setFeatMultiformat(String featMultiformat) {
        this.featMultiformat = Boolean.parseBoolean( featMultiformat );
    }
    
    /**
     * Checks whether the user has "Multiformat" option enabled or not.
     *
     * @return true if "Multiformat" option is available.
     */
    public boolean hasMultiformat() {
        return this.featMultiformat;
    }
    
    
    
    
    /**
     * This method is used by the parser.
     * 
     * <p>Setter for the "Manager" option: used by the parser.</p>
     *
     * @param featManager String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setFeatManager(String featManager) {
        this.featManager = Boolean.parseBoolean( featManager );
    }
    
    /**
     * Checks whether the user has "Manager" option enabled or not.
     *
     * @return true if "Manager" option is available.
     */
    public boolean hasManager() {
        return this.featManager;
    }
    
    
    
    
    /**
     * This method is used by the parser.
     * 
     * <p>Setter for the "Social Networks" option: used by the parser.</p>
     *
     * @param featSocialNetworks String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setFeatSocialNetworks(String featSocialNetworks) {
        this.featSocialNetworks = Boolean.parseBoolean( featSocialNetworks );
    }
    
    /**
     * Checks whether the user has "Social Networks" option enabled or not.
     *
     * @return true if "Social Networks" option is available.
     */
    public boolean hasSocialNetworks() {
        return this.featSocialNetworks;
    }
    
    
    
    
    
    /**
     * This method is used by the parser.
     * 
     * <p>Setter for the "Last.fm" option: used by the parser.</p>
     *
     * @param featLastfm String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setFeatLastfm(String featLastfm) {
        this.featLastfm = Boolean.parseBoolean( featLastfm );
    }
    
    /**
     * Checks whether the user has "Last.fm" option enabled or not.
     *
     * @return true if "Last.fm" option is available.
     */
    public boolean hasLastfm() {
        return this.featLastfm;
    }
}
