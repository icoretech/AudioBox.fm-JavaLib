
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
    
    /** Constant <code>TAG_NAME="playlist"</code> */
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

    
    /**
     * <p>Setter for the field <code>playlistTracksCount</code>.</p>
     *
     * @param tracksCount a {@link java.lang.String} object.
     */
    public void setPlaylistTracksCount(String tracksCount) {
        this.playlistTracksCount = Integer.parseInt( tracksCount );
    }

    /**
     * <p>Getter for the field <code>playlistTracksCount</code>.</p>
     *
     * @return the playlistTracksCount
     */
    public int getPlaylistTracksCount() {
        return playlistTracksCount;
    }

    
    

    /**
     * <p>Setter for the field <code>playlistType</code>.</p>
     *
     * @param type a {@link java.lang.String} object.
     */
    public void setPlaylistType(String type) {
        this.playlistType = type;
    }

    /**
     * <p>Getter for the field <code>playlistType</code>.</p>
     *
     * @return the playlistType
     */
    public String getPlaylistType() {
        return playlistType;
    }

    
    
    

    /**
     * <p>Setter for the field <code>position</code>.</p>
     *
     * @param position a {@link java.lang.String} object.
     */
    public void setPosition(String position) {
        this.position = Integer.parseInt(position);
    }

    /**
     * <p>Getter for the field <code>position</code>.</p>
     *
     * @return the position
     */
    public int getPosition() {
        return position;
    }

}
