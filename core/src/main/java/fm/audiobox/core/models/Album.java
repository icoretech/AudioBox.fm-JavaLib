
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
 * Album XML looks like this:
 *  
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
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Album extends ModelItem {

    /** The XML tag name for the Album element */
	public static final String TAG_NAME = "album";
	
	// Name and token are inherited from Model
    protected String coverUrlAsThumb;
    protected String coverUrl;
    protected Artist artist;
	
	/**
	 * <p>Constructor for Album.</p>
	 */
	protected Album() {
		this.pEndPoint = Albums.END_POINT;
	}
	
	
	/**
	 * <p>Setter for the field <code>coverUrlAsThumb</code>.</p>
	 *
	 * @param coverUrlAsThumb a {@link java.lang.String} object.
	 */
	public void setCoverUrlAsThumb( String coverUrlAsThumb ) {
        this.coverUrlAsThumb = coverUrlAsThumb;
    }
	
	/**
	 * <p>Getter for the field <code>coverUrlAsThumb</code>.</p>
	 *
	 * @return the coverUrlAsThumb
	 */
	public String getCoverUrlAsThumb() {
		return coverUrlAsThumb;
	}
	
	
	/**
	 * <p>Setter for the field <code>coverUrl</code>.</p>
	 *
	 * @param coverUrl a {@link java.lang.String} object.
	 */
	public void setCoverUrl( String coverUrl ) {
	    this.coverUrl = coverUrl;
	}
	
	/**
	 * <p>Getter for the field <code>coverUrl</code>.</p>
	 *
	 * @return the coverUrl
	 */
	public String getCoverUrl() {
		return coverUrl;
	}

	
	/**
	 * <p>Setter for the field <code>artist</code>.</p>
	 *
	 * @param artist a {@link fm.audiobox.core.models.Artist} object.
	 */
	public void setArtist(Artist artist ) {
	    this.artist = artist;
	}
	
	/**
	 * <p>Getter for the field <code>artist</code>.</p>
	 *
	 * @return the artist
	 */
	public Artist getArtist() {
		return artist;
	}

}
