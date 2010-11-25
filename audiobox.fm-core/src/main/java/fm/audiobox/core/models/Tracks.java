
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
 * <p>Tracks is a {@link ModelsCollection} specialization for {@link Track} collections.</p>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Tracks extends ModelsCollection {

    /** Tracks API end point */
    public static final String END_POINT = "tracks";
    
    protected List<Track> collection = new ArrayList<Track>();
    
    /**
     * <p>Constructor for Tracks.</p>
     */
    protected Tracks(){
        this.pEndPoint = END_POINT;
    }
    
    /**
     * Getter method for the XML tag name of collection's elements.
     * 
     * @return the XML tag name {@link String} for Tracks collection elements.
     */
    public String getTagName() {
        return Track.TAG_NAME;
    }
    
    /**
     * <p>Getter method for the Tracks collection.</p>
     *
     * @return the {@link List} of {@link Track} of this collection.
     */
    public List<? extends Track> getCollection() {
        return this.collection;
    }
    
    /**
     * Adds a Track to the collection: this is mainly used by the parser.
     *
     * @param track a {@link Track} to add to the collection.
     */
    public void addTrack(Track track) {
        this.collection.add(track);
    }
    
    /**
     * <p>Setter method for the collection list of {@link Track}.</p>
     *
     * @param collection a {@link List} of {@link Track} that represents the collection.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setCollection(List<?> collection) {
        this.collection = (List<Track>) collection;
    }
    
    /**
     * <p>Getter method for a single {@link Track} contained in the collection.</p>
     *
     * @param index the index of the desired Track.
     * 
     * @return a {@link Track} object.
     */
    public Track get(int index) {
        return collection.get(index);
    }
    
    /**
     * <p>Getter method for a single {@link Track} contained in the collection.</p>
     *
     * @param token the token of the desired Track.
     * 
     * @return a {@link Track} object.
     */
    public Track get(String token) {
        for (Track track : collection) {
            if ( token.equals( track.getToken() ) )
                return track;
        }
        return null;
    }
}
