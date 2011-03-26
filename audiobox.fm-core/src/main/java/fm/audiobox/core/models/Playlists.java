
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

import fm.audiobox.core.api.ModelsCollection;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;


/**
 * <p>Playlists is a {@link ModelsCollection} specialization for {@link Playlist} collections.</p>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Playlists extends AbstractCollectionEntity<Playlist> implements Serializable {

  private static final long serialVersionUID = 1L;

    /** Tracks API end point */
    public static final String NAMESPACE = "playlists";
    
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
    public enum PlaylistTypes {
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

    
    public static String getTagName(){
      return NAMESPACE;
    }
    
    @Override
    public String getNamespace(){
      return getTagName();
    }
    
    
    
    @Override
    public boolean add(Playlist entity) {
      return super.addEntity(entity);
    }


    @Override
    public boolean remove(Object entity) {
      return super.removeEntity( (IEntity)entity);
    }
    
    
    
    @Override
    public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
      
      if ( tagName.equals("playlist") ) {
        return this.getClass().getMethod("add", Playlist.class);
      }
      
      return null;
    }


}
