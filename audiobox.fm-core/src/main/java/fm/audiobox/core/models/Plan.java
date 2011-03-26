
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

import java.io.Serializable;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;


/**
 * Plan is linked to the {@link User} and specify user's plan details.
 * 
 * <p>
 * 
 * Plan XML looks like this: 
 * 
 * <pre>
 * {@code
   <plan>
     <feat_api>true</feat_api> 
     <feat_cloud_web_player>true</feat_cloud_web_player> 
     <feat_dropbox>true</feat_dropbox> 
     <feat_gigjunkie>true</feat_gigjunkie> 
     <feat_lastfm>true</feat_lastfm> 
     <feat_manager>true</feat_manager> 
     <feat_multiformat>true</feat_multiformat> 
     <feat_social_networks>true</feat_social_networks> 
     <feat_youtube_channel>true</feat_youtube_channel> 
     <name>plan</name>
   </plan>
 * }
 * </pre>
 * 
 * @author Valerio Chiodino
 * @author Fabio Tunno
 */
public final class Plan extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LoggerFactory.getLogger( Plan.class );

  public static final String NAMESPACE = null;
  public static final String TAGNAME = "plan";

  private String name;
  private boolean featApi;
  private boolean featCloudWebPlayer;
  private boolean featDropbox;
  private boolean featGigjunkie;
  private boolean featLastfm;
  private boolean featManager;
  private boolean featMultiformat;
  private boolean featSocialNetworks;
  private boolean featYoutubeChannel;
  

  /**
   * <p>Constructor for Plan.</p>
   */
  public Plan(IConnector connector, IConfiguration config) {
    super(connector, config);
    log.info("New Plan instanciated");
  }

  @Override
  public String getTagName(){
    return TAGNAME;
  }
  
  @Override
  public String getNamespace(){
    return NAMESPACE;
  }
  
  
  
  /**
   * This method is used by the parser.
   * 
   * <p>Sets the plan name: used by the parser.</p>
   *
   * @param name String the plan name
   */
  @Deprecated
  public void setName(String name) {
    this.name = name;
  }
  
  
  /**
   * @return the plan name
   */
  public String getName() {
    return this.name;
  }
  

  /**
   * This method is used by the parser.
   * 
   * <p>Setter for the "API" option: used by the parser.</p>
   *
   * @param featApi boolean representing the boolean value, true to enable false to disable.
   */
  @Deprecated
  public void setFeatApi(boolean featApi) {
    this.featApi = featApi;
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
   * @param featYoutubeChannel boolean representing the boolean value, true to enable false to disable.
   */
  @Deprecated
  public void setFeatYoutubeChannel(boolean featYoutubeChannel) {
    this.featYoutubeChannel = featYoutubeChannel;
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
   * @param featCloudWebPlayer boolean representing the boolean value, true to enable false to disable.
   */
  @Deprecated
  public void setFeatCloudWebPlayer(boolean featCloudWebPlayer) {
    this.featCloudWebPlayer = featCloudWebPlayer;
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
   * <p>Setter for the "Gigjunkie" option: used by the parser.</p>
   *
   * @param featGigjunkie boolean representing the boolean value, true to enable false to disable.
   */
  @Deprecated
  public void setFeatGigjunkie(boolean featGigjunkie) {
    this.featGigjunkie = featGigjunkie;
  }

  /**
   * Checks whether the user has "Gigjunkie" option enabled or not.
   *
   * @return true if "Gigjunkie" option is available.
   */
  public boolean hasGigjunkie() {
    return this.featGigjunkie;
  }


  
  /**
   * This method is used by the parser.
   * 
   * <p>Setter for the "Dropbox" option: used by the parser.</p>
   *
   * @param featDropbox boolean representing the boolean value, true to enable false to disable.
   */
  @Deprecated
  public void setFeatDropbox(boolean featDropbox) {
    this.featDropbox = featDropbox;
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
   * @param featMultiformat boolean representing the boolean value, true to enable false to disable.
   */
  @Deprecated
  public void setFeatMultiformat(boolean featMultiformat) {
    this.featMultiformat = featMultiformat;
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
  public void setFeatManager(boolean featManager) {
    this.featManager = featManager;
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
   * @param featSocialNetworks boolean representing the boolean value, true to enable false to disable.
   */
  @Deprecated
  public void setFeatSocialNetworks(boolean featSocialNetworks) {
    this.featSocialNetworks = featSocialNetworks;
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
   * @param featLastfm boolean representing the boolean value, true to enable false to disable.
   */
  @Deprecated
  public void setFeatLastfm(boolean featLastfm) {
    this.featLastfm = featLastfm;
  }

  /**
   * Checks whether the user has "Last.fm" option enabled or not.
   *
   * @return true if "Last.fm" option is available.
   */
  public boolean hasLastfm() {
    return this.featLastfm;
  }



  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {

    if ( tagName.equals("feat_api") ){
      return this.getClass().getMethod("setFeatApi", boolean.class );
      
    } else if ( tagName.equals("feat_cloud_web_player") ){
      return this.getClass().getMethod("setFeatCloudWebPlayer", boolean.class );
      
    } else if ( tagName.equals("feat_dropbox") ){
      return this.getClass().getMethod("setFeatDropbox", boolean.class );
      
    } else if ( tagName.equals("feat_gigjunkie") ){
      return this.getClass().getMethod("setFeatGigjunkie", boolean.class );
      
    } else if ( tagName.equals("feat_lastfm") ){
      return this.getClass().getMethod("setFeatLastfm", boolean.class );
      
    } else if ( tagName.equals("feat_manager") ){
      return this.getClass().getMethod("setFeatManager", boolean.class );
      
    } else if ( tagName.equals("feat_multiformat") ){
      return this.getClass().getMethod("setFeatMultiformat", boolean.class );
    
    } else if ( tagName.equals("feat_social_networks") ){
      return this.getClass().getMethod("setFeatSocialNetworks", boolean.class );
      
    } else if ( tagName.equals("feat_youtube_channel") ){
      return this.getClass().getMethod("setFeatYoutubeChannel", boolean.class );
      
    } else if ( tagName.equals("name") ){
      return this.getClass().getMethod("setName", String.class );
      
    }
    
    return null;
  }
}
