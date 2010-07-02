
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
 * <p>Tracks class.</p>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Tracks extends ModelsCollection {

    /** Constant <code>END_POINT="tracks"</code> */
    public static final String END_POINT = "tracks";
    
    protected List<Track> collection = new ArrayList<Track>();
    
    /**
     * <p>Constructor for Tracks.</p>
     */
    protected Tracks(){
        this.pEndPoint = END_POINT;
    }
    
    /**
     * <p>getTagName</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getTagName() {
        return Track.TAG_NAME;
    }
    
    /**
     * <p>Getter for the field <code>collection</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<? extends Track> getCollection() {
        return this.collection;
    }
    
    /**
     * <p>addTrack</p>
     *
     * @param track a {@link fm.audiobox.core.models.Track} object.
     */
    public void addTrack(Track track) {
        this.collection.add(track);
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public void setCollection(List<?> collection) {
        this.collection = (List<Track>) collection;
    }
    
    /** {@inheritDoc} */
    public Track get(int index) {
        return collection.get(index);
    }
    
    /**
     * <p>get</p>
     *
     * @param uuid the UUID of the track you are looking for.
     * 
     * @return a {@link fm.audiobox.core.models.Track} object.
     */
    public Track get(String uuid) {
        for (Track track : collection) {
            if ( uuid.equals( track.getUuid() ) )
                return track;
        }
        return null;
    }
}
