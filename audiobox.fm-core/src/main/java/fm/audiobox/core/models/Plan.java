
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
 * Plan is linked to the {@link Subscription} and specify user's plan details.
 * 
 * <p>
 * 
 * Plan XML looks like this: 
 * 
 * <pre>
 * {@code
 * <plan>
 *    <feat-third-party type="boolean">true</feat-third-party>
 *    <feat-youtube-channel type="boolean">true</feat-youtube-channel>
 *    <feat-mobile type="boolean">true</feat-mobile>
 *    <feat-web-player type="boolean">true</feat-web-player>
 *    <feat-dropbox type="boolean">true</feat-dropbox>
 *    <feat-multiformat type="boolean">true</feat-multiformat>
 *    <feat-library-manager type="boolean">true</feat-library-manager>
 *    <feat-social type="boolean">true</feat-social>
 *    <feat-download type="boolean">true</feat-download>
 *    <feat-marketplace type="boolean">true</feat-marketplace>
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
    
    protected boolean featThirdParty;
    protected boolean featYoutubeChannel;
    protected boolean featMobile;
    protected boolean featWebPlayer;
    protected boolean featDropbox;
    protected boolean featMultiformat;
    protected boolean featLibraryManager;
    protected boolean featSocial;
    protected boolean featDownload;
    protected boolean featMarketplace;
    
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
     * <p>Setter for the "Third Party" option: used by the parser.</p>
     *
     * @param featThirdParty String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setFeatThirdParty(String featThirdParty) {
        this.featThirdParty = Boolean.parseBoolean( featThirdParty );
    }
    
    /**
     * Checks whether the user has "Third Party" option enabled or not.
     *
     * @return true if "Third Party" option is available.
     */
    public boolean hasThirdParty() {
        return this.featThirdParty;
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
     * <p>Setter for the "Mobile" option: used by the parser.</p>
     *
     * @param featMobile String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setFeatMobile(String featMobile) {
        this.featMobile = Boolean.parseBoolean( featMobile );
    }
    
    /**
     * Checks whether the user has "Mobile" option enabled or not.
     *
     * @return true if "Mobile" option is available.
     */
    public boolean hasMobile() {
        return this.featMobile;
    }
    
    
    
    
    /**
     * This method is used by the parser.
     * 
     * <p>Setter for the "Web Player" option: used by the parser.</p>
     *
     * @param featWebPlayer String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setFeatWebPlayer(String featWebPlayer) {
        this.featWebPlayer = Boolean.parseBoolean( featWebPlayer );
    }
    
    /**
     * Checks whether the user has "Web Player" option enabled or not.
     *
     * @return true if "Web Player" option is available.
     */
    public boolean hasWebPlayer() {
        return this.featWebPlayer;
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
     * <p>Setter for the "Library Manager" option: used by the parser.</p>
     *
     * @param featLibraryManager String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setFeatLibraryManager(String featLibraryManager) {
        this.featLibraryManager = Boolean.parseBoolean( featLibraryManager );
    }
    
    /**
     * Checks whether the user has "Library Manager" option enabled or not.
     *
     * @return true if "Library Manager" option is available.
     */
    public boolean hasLibraryManager() {
        return this.featLibraryManager;
    }
    
    
    
    
    /**
     * This method is used by the parser.
     * 
     * <p>Setter for the "Social" option: used by the parser.</p>
     *
     * @param featSocial String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setFeatSocial(String featSocial) {
        this.featSocial = Boolean.parseBoolean( featSocial );
    }
    
    /**
     * Checks whether the user has "Social" option enabled or not.
     *
     * @return true if "Social" option is available.
     */
    public boolean hasSocial() {
        return this.featSocial;
    }
    
    
    
    
    /**
     * This method is used by the parser.
     * 
     * <p>Setter for the "Download" option: used by the parser.</p>
     *
     * @param featDownload String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setFeatDownload(String featDownload) {
        this.featDownload = Boolean.parseBoolean( featDownload );
    }
    
    /**
     * Checks whether the user has "Download" option enabled or not.
     *
     * @return true if "Download" option is available.
     */
    public boolean hasDownload() {
        return this.featDownload;
    }
    
    
    
    
    /**
     * This method is used by the parser.
     * 
     * <p>Setter for the "Marketplace" option: used by the parser.</p>
     *
     * @param featMarketplace String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setFeatMarketplace(String featMarketplace) {
        this.featMarketplace = Boolean.parseBoolean( featMarketplace );
    }
    
    /**
     * Checks whether the user has "Marketplace" option enabled or not.
     *
     * @return true if "Marketplace" option is available.
     */
    public boolean hasMarketplace() {
        return this.featMarketplace;
    }
}