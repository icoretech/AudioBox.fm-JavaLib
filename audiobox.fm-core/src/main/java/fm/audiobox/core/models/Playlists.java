
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
public class Playlists extends AbstractCollectionEntity<MediaFiles> implements Serializable {

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
   *   <li>{@link PlaylistTypes#AUDIO audio}</li>
   *   <li>{@link PlaylistTypes#NATIVE native}</li>
   *   <li>{@link PlaylistTypes#TRASH trash}</li>
   *   <li>{@link PlaylistTypes#VIDEO video}</li>
   *   <li>{@link PlaylistTypes#CUSTOM custom}</li>
   * </ul> 
   */
  public enum Types {
    /** The so called "Music" playlist, master audio media container */
    AUDIO,

    /** Recycle bin meta playlist */
    TRASH,

    /** Mainly the YouTube&trade; Channel playlist */
    VIDEO,

    /** User defined playlists */
    CUSTOM,

    /** Offline playlist */
    OFFLINE
  }

  /**
   * <p>Constructor for Playlists.</p>
   */
  public Playlists(IConnector connector, IConfiguration config){
    super(connector, config);
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
  public boolean add(MediaFiles entity) {
    String token = entity.getToken();
    if ( getConfiguration().hasMediaFiles( token ) ) {
      MediaFiles pl = this.getConfiguration().getMediaFiles( token );
      pl.copy( entity );
      entity = pl;
    } else {
      getConfiguration().addMediaFiles( entity );
    }
    return super.addEntity(entity);
  }

  
  /**
   * Returns the {@link MediaFiles} associated with the given <code>name</code>
   * @param name the MediaFiles name
   * @return the {@link MediaFiles} associated with the given <code>name</code>
   */
  public MediaFiles getMediaFilesByName(String name){
    for ( Iterator<MediaFiles> it = this.iterator(); it.hasNext();  ){
    	MediaFiles pl = it.next();
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
  public MediaFiles getMediaFilesByType( Types type ){
    for ( Iterator<MediaFiles> it = this.iterator(); it.hasNext();  ){
    	MediaFiles pl = it.next();
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
  public List<MediaFiles> getPlaylistsByType( Types type ){
    List<MediaFiles> pls = new ArrayList<MediaFiles>();
    for ( Iterator<MediaFiles> it = this.iterator(); it.hasNext();  ){
    	MediaFiles pl = it.next();
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

    if ( tagName.equals( MediaFiles.TAGNAME ) ) {
      return this.getClass().getMethod("add", MediaFiles.class);
    }

    return null;
  }


  @Override
  public String getSubTagName() {
	return MediaFiles.TAGNAME;
  }

  

}
