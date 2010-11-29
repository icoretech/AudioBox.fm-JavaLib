
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

import java.util.ArrayList;
import java.util.List;

import fm.audiobox.core.api.ModelItem;
import fm.audiobox.core.api.ModelsCollection;


/**
 * <p>Playlists is a {@link ModelsCollection} specialization for {@link Playlist} collections.</p>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Playlists extends ModelsCollection {

    /** Tracks API end point */
    public static final String END_POINT = "playlists";
    
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
        
        /** Maximum portability audio media container, for media that are not Mpeg Layer 3 encoded */
        NATIVE,
        
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
    protected Playlists(){
        this.pEndPoint = END_POINT;
    }
    
    /**
     * Getter method for the XML tag name of collection's elements.
     * 
     * @return the XML tag name {@link String} for Playlists collection elements.
     */
    public String getTagName() {
        return Playlist.TAG_NAME;
    }

    /**
     * Adds a Playlist to the collection: this is mainly used by the parser.
     *
     * @param playlist a {@link Playlist} to add to the collection.
     */
    public void addPlaylist(Playlist playlist) {
        super.addToCollection(playlist);
    }
    
    
    /**
     * <p>Getter method for a single {@link Playlist} contained in the collection.</p>
     *
     * @param index the index of the desired Playlist.
     * 
     * @return a {@link Playlist} object.
     */
    public Playlist get(int index) {
        return (Playlist) super.getItem(index);
    }
    
    /**
     * <p>Getter method for a single {@link Playlist} contained in the collection.</p>
     *
     * @param token the uuid of the desired Playlist.
     * 
     * @return a {@link Playlist} object.
     */
    public Playlist get(String token) {
    	return (Playlist) super.getItem(token);
    }
    
    /**
     * <p>Getter method for a single {@link Playlist} contained in the collection.</p>
     *
     * @param name the name (case insensitive) of the desired Playlist.
     * 
     * @return a {@link Playlist} object if the playlist is in pool <code>null</code> otherwise.
     */
    public Playlist getPlaylistByName(String name) {
        for (ModelItem playlist : super.getCollection()) {
            if ( name.equalsIgnoreCase( playlist.getName() ) )
                return (Playlist)playlist;
        }
        return null;
    }
    
    /**
     * <p>Getter method for a {@link List} of {@link Playlist} of a defined <code>type</code>.</p>
     *
     * @param type the {@link PlaylistTypes} of the desired Playlists group.
     * 
     * @return a {@link Playlist} {@link List} if any. An empty list otherwise.
     */
    public List<Playlist> getPlaylistsByType(PlaylistTypes type) {
        List<Playlist> result = new ArrayList<Playlist>();
        for (ModelItem playlist : super.getCollection() ) {
            if ( type.name().equalsIgnoreCase( ((Playlist)playlist).getPlaylistType() ) )
                result.add( (Playlist) playlist);
        }
        return result;
    }
    
    
}
