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

public class Albums extends ModelsCollection {

    public static final String END_POINT = "albums";
    
    protected List<Album> collection = new ArrayList<Album>();
    
    public Albums(){
        this.endPoint = END_POINT;
    }
    
    public String getTagName() {
        return Album.TAG_NAME;
    }
    
    public List<? extends Album> getCollection() {
        return this.collection;
    }
    
    public void addAlbum(Album album) {
        this.collection.add( album );
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setCollection(List<?> collection) {
        this.collection = (List<Album>) collection;
    }
    
    public Album get(int index) {
        return collection.get(index);
    }
    
    public Album get(String token) {
        for (Album album : collection) {
            if ( token.equals( album.getToken() ) )
                return album;
        }
        return null;
    }
}
