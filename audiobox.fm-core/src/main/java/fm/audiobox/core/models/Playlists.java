
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

import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;


/**
 * <p>Playlists is a {@link AbstractCollectionEntity} specialization for {@link Playlist} collections.</p>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Playlists extends AbstractCollectionEntity<Playlist> implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private static Logger log = LoggerFactory.getLogger(Playlists.class);
  
  /** Tracks API end point */
  public static final String NAMESPACE = "playlists";
  public static final String TAGNAME = NAMESPACE;

  public static enum Type {
    LocalPlaylist,
    CloudPlaylist,
    DropboxPlaylist,
    SkydrivePlaylist,
    BoxPlaylist,
    GdrivePlaylist,
    YoutubePlaylist,
    SoundcloudPlaylist,
    CustomPlaylist,
    OfflinePlaylist
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
   * Returns the <b>first</b> {@link MediaFiles} that matches with the given {@link Playlists.Type}
   * 
   * @param type the {@link Playlists.Type}
   * @return the first {@link MediaFiles} that matches with the given {@link Playlists.Type}
   */
  public Playlist getPlaylistByType( String type ){
    for ( Iterator<Playlist> it = this.iterator(); it.hasNext();  ){
      Playlist pl = it.next();
      if (  type.equals( pl.getType() )  ) {
        return pl;
      }
    }
    return null;
  }
  
  public Playlist getPlaylistByType( Type type ){
    return this.getPlaylistByType( type.toString() );
  }
  
  
  /**
   * @return the {@code offline} Playlist
   */
  public Playlist getOfflinePlaylist() {
    return this.getPlaylistByType(Type.OfflinePlaylist);
  }
  
  /**
   * Returns a list of {@link MediaFiles} that matches the given {@link Playlists.Type}
   * 
   * @param type the {@link Playlists.Type}
   * @return a list of {@link MediaFiles} that matches with the given {@link Playlists.Type}
   */
  public List<Playlist> getPlaylistsByType( String type ){
    List<Playlist> pls = new ArrayList<Playlist>();
    for ( Iterator<Playlist> it = this.iterator(); it.hasNext();  ){
      Playlist pl = it.next();
      if ( type.equals( pl.getType() ) ) {
        pls.add(pl);
      }
    }
    return pls;
  }
  
  
  public List<Playlist> getPlaylistsByType( Type type ){
    return this.getPlaylistsByType( type.toString() );
  }
  
  @Override
  protected void copy(IEntity entity) {
    // default: do nothing
  }


  @Override
  public Method getSetterMethod(String tagName) {

    if ( tagName.equals( Playlist.TAGNAME ) ) {
      try {
        return this.getClass().getMethod("add", Playlist.class);
      } catch (SecurityException e) {
        log.error("Security error", e);
      } catch (NoSuchMethodException e) {
        log.error("No method found", e);
      }
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
  
  
  protected List<NameValuePair> toQueryParameters(boolean all) {
    return new ArrayList<NameValuePair>();
  }


}
