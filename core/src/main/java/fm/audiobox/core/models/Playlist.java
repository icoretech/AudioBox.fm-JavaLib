
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

import fm.audiobox.core.api.ModelItem;


/**
 * Playlist class is a {@link ModelItem} specialization for playlists XML elements.
 * 
 * <p>
 * 
 * Profile XML looks like this: 
 * 
 * <pre>
 * {@code
 * <playlist>
 *   <name>Music</name>
 *   <playlist-tracks-count type="integer">1591</playlist-tracks-count>
 *   <playlist-type>audio</playlist-type>
 *   <position type="integer">1</position>
 *   <token>ass8sad909sh</token>
 * </playlist>
 * }
 * </pre>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Playlist extends ModelItem {
    
    /** The XML tag name for the Playlist element */
    public static final String TAG_NAME = "playlist";
    
    protected int playlistTracksCount;
    protected String playlistType;
    protected int position;

    
    /**
     * <p>Constructor for Playlist.</p>
     */
    protected Playlist() {
        this.pEndPoint = Playlists.END_POINT;
    }

    
    /* ------------------- */
    /* Getters and setters */
    /* ------------------- */
    
    
    /**
     * <p>Setter for the playlist tracks count: used by the parser.</p>
     *
     * @param tracksCount the tracks count {@link String} to set.
     */
    public void setPlaylistTracksCount(String tracksCount) {
        this.playlistTracksCount = Integer.parseInt( tracksCount );
    }

    /**
     * <p>Getter for the playlist tracks count.</p>
     *
     * @return the playlist tracks count
     */
    public int getPlaylistTracksCount() {
        return playlistTracksCount;
    }

    
    
    /**
     * <p>Setter for the playlist type: used by the parser.</p>
     *
     * @param type the playlist type {@link String} to set.
     */
    public void setPlaylistType(String type) {
        this.playlistType = type;
    }

    /**
     * <p>Getter for the playlist type.</p>
     *
     * @return the playlist type
     */
    public String getPlaylistType() {
        return playlistType;
    }
    
    

    /**
     * <p>Setter for the playlist position: used by the parser.</p>
     *
     * @param position the playlist position {@link java.lang.String} to set.
     */
    public void setPosition(String position) {
        this.position = Integer.parseInt(position);
    }

    /**
     * <p>Getter for the playlist position.</p>
     *
     * @return the playlist position
     */
    public int getPosition() {
        return position;
    }

}
