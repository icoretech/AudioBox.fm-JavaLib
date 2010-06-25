
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
public class Genres extends ModelsCollection {

    /** Constant <code>END_POINT="genres"</code> */
    public static final String END_POINT = "genres";
    
    protected List<Genre> collection = new ArrayList<Genre>();
    
    /**
     * <p>Constructor for Genres.</p>
     */
    public Genres(){
        this.endPoint = END_POINT;
    }
    
    /**
     * <p>getTagName</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getTagName() {
        return Genre.TAG_NAME;
    }
    
    /**
     * <p>Getter for the field <code>collection</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<? extends Genre> getCollection() {
        return this.collection;
    }
    
    /**
     * <p>addGenre</p>
     *
     * @param genre a {@link fm.audiobox.core.models.Genre} object.
     */
    public void addGenre(Genre genre) {
        this.collection.add(genre);
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public void setCollection(List<?> collection) {
        this.collection = (List<Genre>) collection;
    }
    
    /** {@inheritDoc} */
    public Genre get(int index) {
        return collection.get(index);
    }
    
    /**
     * <p>get</p>
     *
     * @param token a {@link java.lang.String} object.
     * @return a {@link fm.audiobox.core.models.Genre} object.
     */
    public Genre get(String token) {
        for (Genre genre : collection) {
            if ( token.equals( genre.getToken() ) )
                return genre;
        }
        return null;
    }
}
