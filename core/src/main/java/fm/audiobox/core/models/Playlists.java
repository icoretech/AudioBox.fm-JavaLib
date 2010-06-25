
/**
 *************************************************************************
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
 **************************************************************************
 *
 * @author keytwo
 * @version $Id: $
 */

package fm.audiobox.core.models;

import java.util.ArrayList;
import java.util.List;

import fm.audiobox.core.api.ModelsCollection;
public class Playlists extends ModelsCollection {

    /** Constant <code>END_POINT="playlists"</code> */
    public static final String END_POINT = "playlists";
    
    protected List<Playlist> collection = new ArrayList<Playlist>();
    
    /**
     * <p>Constructor for Playlists.</p>
     */
    public Playlists(){
        this.endPoint = END_POINT;
    }
    
    /**
     * <p>getTagName</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getTagName() {
        return Playlist.TAG_NAME;
    }

    /**
     * <p>Getter for the field <code>collection</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<? extends Playlist> getCollection() {
        return this.collection;
    }
    
    /**
     * <p>addPlaylist</p>
     *
     * @param playlist a {@link fm.audiobox.core.models.Playlist} object.
     */
    public void addPlaylist(Playlist playlist) {
        this.collection.add(playlist);
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public void setCollection(List<?> collection) {
        this.collection = (List<Playlist>) collection;
    }
    
    /** {@inheritDoc} */
    public Playlist get(int index) {
        return collection.get(index);
    }
    
    /**
     * <p>get</p>
     *
     * @param token a {@link java.lang.String} object.
     * @return a {@link fm.audiobox.core.models.Playlist} object.
     */
    public Playlist get(String token) {
        for (Playlist playlist : collection) {
            if ( token.equals( playlist.getToken() ) )
                return playlist;
        }
        return null;
    }
}
