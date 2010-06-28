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

public class Tracks extends ModelsCollection {

    public static final String END_POINT = "tracks";
    
    protected List<Track> collection = new ArrayList<Track>();
    
    protected Tracks(){
        this.endPoint = END_POINT;
    }
    
    public String getTagName() {
        return Track.TAG_NAME;
    }
    
    public List<? extends Track> getCollection() {
        return this.collection;
    }
    
    public void addTrack(Track track) {
        this.collection.add(track);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void setCollection(List<?> collection) {
        this.collection = (List<Track>) collection;
    }
    
    public Track get(int index) {
        return collection.get(index);
    }
    
    public Track get(String token) {
        for (Track track : collection) {
            if ( token.equals( track.getToken() ) )
                return track;
        }
        return null;
    }
}
