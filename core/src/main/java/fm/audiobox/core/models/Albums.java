
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

import fm.audiobox.core.api.ModelsCollection;


/**
 *
 * @author Valerio Chiodino
 * @version 0.0.1
 */

public class Albums extends ModelsCollection {

    /** Constant <code>END_POINT="albums"</code> */
    public static final String END_POINT = "albums";
    
    protected List<Album> collection = new ArrayList<Album>();
    
    /**
     * <p>Constructor for Albums.</p>
     */
    public Albums(){
        this.endPoint = END_POINT;
    }
    
    /**
     * <p>getTagName</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getTagName() {
        return Album.TAG_NAME;
    }
    
    /**
     * <p>Getter for the field <code>collection</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<? extends Album> getCollection() {
        return this.collection;
    }
    
    /**
     * <p>addAlbum</p>
     *
     * @param album a {@link fm.audiobox.core.models.Album} object.
     */
    public void addAlbum(Album album) {
        this.collection.add( album );
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public void setCollection(List<?> collection) {
        this.collection = (List<Album>) collection;
    }
    
    /** {@inheritDoc} */
    public Album get(int index) {
        return collection.get(index);
    }
    
    /**
     * <p>get</p>
     *
     * @param token a {@link java.lang.String} object.
     * @return a {@link fm.audiobox.core.models.Album} object.
     */
    public Album get(String token) {
        for (Album album : collection) {
            if ( token.equals( album.getToken() ) )
                return album;
        }
        return null;
    }
}
