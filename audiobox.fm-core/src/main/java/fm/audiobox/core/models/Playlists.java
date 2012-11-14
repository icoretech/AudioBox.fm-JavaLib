
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;


/**
 * <p>Playlists is a {@link ModelsCollection} specialization for {@link MediaFiles} collections.</p>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Playlists extends AbstractCollectionEntity<Playlist> implements Serializable {

  private static final long serialVersionUID = 1L;
  
  /** Tracks API end point */
  public static final String NAMESPACE = "playlists";
  public static final String TAGNAME = NAMESPACE;

  public static final String EMPTY_TRASH_ACTION = "empty_trash";
  public static final String ADD_TRACKS_ACTION = "add_tracks";
  public static final String REMOVE_TRACKS_ACTION = "remove_tracks";

  /** 
   * Playlists are grouped by types that are:
   * <ul> 
   *   <li>{@link PlaylistTypes#AudioPlaylist audio}</li>
   *   <li>{@link PlaylistTypes#SmartPlaylist smart}</li>
   *   <li>{@link PlaylistTypes#VideoPlaylist video}</li>
   *   <li>{@link PlaylistTypes#CustomPlaylist custom}</li>
   * </ul> 
   */
  public enum Types {
    /** The playlist associated with user's local storage */
    LocalPlaylist,

    /** The playlist associated with user's AudioBox storage */
    CloudPlaylist,
    
    /** The playlist associated with user's dropbox storage */
    DropboxPlaylist,

    /** The playlist associated with user's gdrive storage */
    GdrivePlaylist,
    
    /** The playlist associated with user's skydrive storage */
    SkydrivePlaylist,

    /** The playlist associated with user's youtube storage */
    YoutubePlaylist,
    
    /** The playlist associated with user's soundcloud storage */
    SoundcloudPlaylist,
    
    /** The playlist associated with user's box.net storage */
    BoxPlaylist,

    /** User defined playlists */
    CustomPlaylist,

    /** User defined playlists */
    SmartPlaylist,
    
    /** The playlist associated with user's box.net storage */
    BoxPlaylist

  }

  /**
   * <p>Constructor for Playlists.</p>
   */
  public Playlists(IConfiguration config){
    super(config);
  }


  @Override
  public String getTagName(){
    return NAMESPACE;
  }

  @Override
  public String getNamespace(){
    return TAGNAME;
  }



  @Override
  public boolean add(Playlist entity) {
    return super.addEntity(entity);
  }

  
  /**
   * Returns the {@link MediaFiles} associated with the given <code>name</code>
   * @param name the MediaFiles name
   * @return the {@link MediaFiles} associated with the given <code>name</code>
   */
  public Playlist getPlaylistByName(String name){
    for ( Iterator<Playlist> it = this.iterator(); it.hasNext();  ){
      Playlist pl = it.next();
      if (  pl.getName().equalsIgnoreCase( name )  ) {
        return pl;
      }
    }
    return null;
  }
  
  /**
   * Returns the <b>first</b> {@link MediaFiles} that matches with the given {@link PlaylistTypes}
   * 
   * @param type the {@link PlaylistTypes}
   * @return the first {@link MediaFiles} that matches with the given {@link PlaylistTypes}
   */
  public Playlist getPlaylistByType( Types type ){
    for ( Iterator<Playlist> it = this.iterator(); it.hasNext();  ){
      Playlist pl = it.next();
      if (  pl.getType() == type  ) {
        return pl;
      }
    }
    return null;
  }
  
  /**
   * Returns a list of {@link MediaFiles} that matches the given {@link PlaylistTypes}
   * 
   * @param type the {@link PlaylistTypes}
   * @return a list of {@link MediaFiles} that matches with the given {@link PlaylistTypes}
   */
  public List<Playlist> getPlaylistsByType( Types type ){
    List<Playlist> pls = new ArrayList<Playlist>();
    for ( Iterator<Playlist> it = this.iterator(); it.hasNext();  ){
      Playlist pl = it.next();
      if (  pl.getType() == type  ) {
        pls.add(pl);
      }
    }
    return pls;
  }
  
  @Override
  protected void copy(IEntity entity) {
    // default: do nothing
  }


  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {

    if ( tagName.equals( Playlist.TAGNAME ) ) {
      return this.getClass().getMethod("add", Playlist.class);
    }

    return null;
  }


  @Override
  public String getSubTagName() {
    return Playlist.TAGNAME;
  }


  @Override
  public String getApiPath() {
    return IConnector.URI_SEPARATOR + NAMESPACE;
  }


  @Override
  public void setParent(IEntity parent) {}

}
