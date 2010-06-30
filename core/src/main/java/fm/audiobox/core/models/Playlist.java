
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
 *   <id type="integer">13</id>
 *   <name>Music</name>
 *   <playlist-tracks-count type="integer">1490</playlist-tracks-count>
 *   <playlist-type>audio</playlist-type>
 *   <position type="integer">1</position>
 * </playlist>
 * }
 * </pre>
 * 
 * 
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * 
 * @version 0.0.1
 * 
 */

public class Playlist extends ModelItem {
    
    public static final String TAG_NAME = "playlist";
    
    protected int id;
    protected int playlistTracksCount;
    protected String playlistType;
    protected int position;
    
    

    protected Playlist() {
        this.endPoint = Playlists.END_POINT;
    }

    
    
    
    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }


    
    
    public void setPlaylistTracksCount(String tracksCount) {
        this.playlistTracksCount = Integer.parseInt( tracksCount );
    }

    /**
     * @return the playlistTracksCount
     */
    public int getPlaylistTracksCount() {
        return playlistTracksCount;
    }

    
    

    public void setPlaylistType(String type) {
        this.playlistType = type;
    }

    /**
     * @return the playlistType
     */
    public String getPlaylistType() {
        return playlistType;
    }

    
    
    

    public void setPosition(String position) {
        this.position = Integer.parseInt(position);
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

}
