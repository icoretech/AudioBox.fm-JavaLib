
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
 * Cover Url class is a {@link ModelItem} specialization for Cover Url XML elements, child of album.
 * 
 * <p>
 * 
 * Cover Url XML looks like this: 
 * 
 * <pre>
 * {@code
 * <cover-urls>
 *   <big>http://url.to/big.png</big>
 *   <tiny>http://url.to/tiny.png</tiny>
 *   <thumb>http://url.to/thumb.png</thumb>
 *   <tiny-normal>http://url.to/tiny-normal.png</tiny-normal>
 * </cover-urls>
 * }
 * </pre>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.2
 */
public class CoverUrls extends ModelItem {
    
    /** The XML tag name for the Album element */
    public static final String TAG_NAME = "cover-urls";
    
    protected String big;
    protected String tiny;
    protected String thumb;
    protected String tinyNormal;
    
    /**
     * <p>Constructor for CoverUrl.</p>
     */
    protected CoverUrls() { }
    
    /* ------------------- */
    /* Getters and setters */
    /* ------------------- */
    
    /**
     * This method is used by the parser. Do not use it directly.
     * 
     * <p>Setter for the field <code>big</code>.</p>
     *
     * @param big a {@link java.lang.String} object.
     */
    public void setBig( String big ) {
        this.big = big;
    }
    
    /**
     * <p>Getter for the field <code>big</code>.</p>
     *
     * @return the big
     */
    public String getBig() {
        return this.big;
    }
    
    
    /**
     * This method is used by the parser. Do not use it directly.
     * 
     * <p>Setter for the field <code>tiny</code>.</p>
     *
     * @param tiny a {@link java.lang.String} object.
     */
    public void setTiny( String tiny ) {
        this.tiny = tiny;
    }
    
    /**
     * <p>Getter for the field <code>tiny</code>.</p>
     *
     * @return the tiny
     */
    public String getTiny() {
        return this.tiny;
    }
    
   
    /**
     * This method is used by the parser. Do not use it directly.
     * 
     * <p>Setter for the field <code>thumb</code>.</p>
     *
     * @param thumb a {@link java.lang.String} object.
     */
    public void setThumb( String thumb ) {
        this.thumb = thumb;
    }
    
    /**
     * <p>Getter for the field <code>thumb</code>.</p>
     *
     * @return the thumb
     */
    public String getThumb() {
        return this.thumb;
    }
    
    
    /**
     * This method is used by the parser. Do not use it directly.
     * 
     * <p>Setter for the field <code>tinyNormal</code>.</p>
     *
     * @param tinyNormal a {@link java.lang.String} object.
     */
    public void setTinyNormal( String tinyNormal ) {
        this.tinyNormal = tinyNormal;
    }
    
    /**
     * <p>Getter for the field <code>tinyNormal</code>.</p>
     *
     * @return the tinyNormal
     */
    public String getTinyNormal() {
        return this.tinyNormal;
    }
    
}
