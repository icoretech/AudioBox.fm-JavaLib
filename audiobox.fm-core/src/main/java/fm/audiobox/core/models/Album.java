
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
 * Album class is a {@link ModelItem} specialization for albums XML elements.
 * 
 * <p>
 * 
 * Album XML looks like this: 
 * 
 * <pre>
 * {@code
 * 
 * <album>
 *   <name>Album name</name>
 *   <token>coq8FfgK</token>
 *   <cover-urls>
 *     <big>http://url.to/big.png</big>
 *     <tiny>http://url.to/tiny.png</tiny>
 *     <thumb>http://url.to/thumb.png</thumb>
 *     <tiny-normal>http://url.to/tiny-normal.png</tiny-normal>
 *   </cover-urls>
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
 * @version 0.0.2
 */
public class Album extends ModelItem {

    /** The XML tag name for the Album element */
	public static final String TAG_NAME = "album";
	
	// Name and token are inherited from Model
	protected CoverUrls coverUrls;
    protected Artist artist;
	
	/**
	 * <p>Constructor for Album.</p>
	 */
	protected Album() {
		this.pEndPoint = Albums.END_POINT;
	}
	
	
	/* ------------------- */
    /* Getters and setters */
    /* ------------------- */
	
	
	/**
     * <p>Setter for the field <code>coverUrls</code>.</p>
     *
     * @param coverUrls a {@link fm.audiobox.core.models.CoverUrls} object.
     */
    public void setCoverUrls(CoverUrls coverUrls ) {
        this.coverUrls = coverUrls;
    }
    
    /**
     * <p>Getter for the field <code>coverUrls</code>.</p>
     *
     * @return the coverUrls
     */
    public CoverUrls getCoverUrls() {
        return this.coverUrls;
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
