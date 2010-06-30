
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
 * @author Fabio Tunno
 * 
 * @version 0.0.1
 * 
 */

public class Genres extends ModelsCollection {

    public static final String END_POINT = "genres";
    
    protected List<Genre> collection = new ArrayList<Genre>();
    
    protected Genres(){
        this.endPoint = END_POINT;
    }
    
    public String getTagName() {
        return Genre.TAG_NAME;
    }
    
    public List<? extends Genre> getCollection() {
        return this.collection;
    }
    
    public void addGenre(Genre genre) {
        this.collection.add(genre);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void setCollection(List<?> collection) {
        this.collection = (List<Genre>) collection;
    }
    
    public Genre get(int index) {
        return collection.get(index);
    }
    
    public Genre get(String token) {
        for (Genre genre : collection) {
            if ( token.equals( genre.getToken() ) )
                return genre;
        }
        return null;
    }
}
