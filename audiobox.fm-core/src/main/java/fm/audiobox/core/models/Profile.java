
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
 * Profile is linked to the {@link User} and specify more user's aspects and options.
 * 
 * <p>
 * 
 * Profile XML looks like this: 
 * 
 * <pre>
 * {@code
 * <profile>
 *   <autoplay>false</autoplay>
 *   <real_name>Real User Name</real_name>
 *   <maximum_portability>false</maximum_portability>
 * </profile>
 * }
 * </pre>
 * 
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.2
 */
public class Profile extends ModelItem {

    /** The XML tag name for the Profile element */
    public static final String TAG_NAME = "profile";
    
    private boolean autoplay;
    private String realName;
    private boolean maximumPortability;
    
    /**
     * <p>Constructor for Profile.</p>
     */
    protected Profile() { }


    /* ------------------- */
    /* Getters and setters */
    /* ------------------- */


    /**
     * This method is used by the parser. Please use {@link Profile#setMaximumPortability(boolean)} instead.
     * 
     * <p>Setter for the Maximum Portability option: used by the parser.</p>
     *
     * @param maximumPortability String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setMaximumPortability(String maximumPortability) {
        this.setAutoplay( Boolean.parseBoolean( maximumPortability ) );
    }
    
    /**
     * <p>Setter for the maximum portability option.</p>
     *
     * @param maximumPortability String representing the boolean value, true to enable false to disable.
     */
    public void setMaximumPortability(boolean maximumPortability) {
        this.maximumPortability = maximumPortability;
    }

    /**
     * Checks whether the user has enabled maximum portability or not.
     *
     * @return the maximum portability user option value
     */
    public boolean hasMaximumPortability() {
        return this.maximumPortability;
    }


    /**
     * <p>Setter for the user real name: used by the parser.</p>
     *
     * @param realName the real name {@link String}.
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * <p>Getter for the user real name.</p>
     *
     * @return the user real name
     */
    public String getRealName() {
        return this.realName;
    }
    
    
    /**
     * This method is used by the parser. Please use {@link Profile#setAutoplay(boolean)} instead.
     * 
     * <p>Setter for the autoplay option: used by the parser.</p>
     *
     * @param autoplay String representing the boolean value, true to enable false to disable.
     */
    @Deprecated
    public void setAutoplay(String autoplay) {
        this.setAutoplay( Boolean.parseBoolean( autoplay ) );
    }
    
    /**
     * <p>Setter for the autoplay option.</p>
     *
     * @param autoplay String representing the boolean value, true to enable false to disable.
     */
    public void setAutoplay(boolean autoplay) {
        this.autoplay = autoplay;
    }

    /**
     * Checks whether the user has enabled autoplay or not.
     *
     * @return the autoplay user option value
     */
    public boolean hasAutoplay() {
        return this.autoplay;
    }


    /* --------- */
    /* Overrides */
    /* --------- */

    
    /** {@inheritDoc} */
    @Override
    public String getName() {
        return this.getRealName();
    }

}
