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
 * <album>
 *   <name>Album name</name>
 *   <token>coq8FfgK</token>
 *   <cover-url-as-thumb>http://url.to/thumb.png</cover-url-as-thumb>
 *   <cover-url>http://url.to/original.png</cover-url>
 *   <artist>
 *     <name>Artist name</name>
 *     <token>fs8d8g9d</token>
 *   </artist>
 * </album>
 * }
 * </pre>
 * 
 * @author Valerio Chiodino
 * @version 0.2-beta
 */

public class Album extends ModelItem {

    // Name and token are inherited from Model
    protected String coverUrlAsThumb;
    protected String coverUrl;
    protected ModelItem artist;
	
	
	public static final String TAG_NAME = "album";
	
	public Album() {
		this.endPoint = Albums.END_POINT;
	}
	
	
	public void setCoverUrlAsThumb( String coverUrlAsThumb ) {
        this.coverUrlAsThumb = coverUrlAsThumb;
    }
	
	/**
	 * @return the coverUrlAsThumb
	 */
	public String getCoverUrlAsThumb() {
		return coverUrlAsThumb;
	}
	
	
	public void setCoverUrl( String coverUrl ) {
	    this.coverUrl = coverUrl;
	}
	
	/**
	 * @return the coverUrl
	 */
	public String getCoverUrl() {
		return coverUrl;
	}

	
	public void setArtist(ModelItem artist ) {
	    this.artist = artist;
	}
	
	/**
	 * @return the artist
	 */
	public ModelItem getArtist() {
		return artist;
	}
	

}
