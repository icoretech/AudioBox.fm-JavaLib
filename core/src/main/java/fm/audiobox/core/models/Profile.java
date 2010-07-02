
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 *   <autoplay type="boolean">false</autoplay>
 *   <birth-date type="date">1980-01-01</birth-date>
 *   <country>US</country>
 *   <gender>m</gender>
 *   <home-page>http://www.myBlog.com</home-page>
 *   <real-name>Real Name</real-name>
 *   <time-zone>New York</time-zone>
 * </profile>
 * }
 * </pre>
 * 
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Profile extends ModelItem {

    /** The XML tag name for the Profile element */
    public static final String TAG_NAME = "profile";
    
    protected boolean autoplay;
    protected Date birthDate;
    protected String country;
    protected String gender;
    protected String homePage;
    protected String realName;
    protected String timeZone;

    /**
     * <p>Constructor for Profile.</p>
     */
    protected Profile() { }


    /* ------------------- */
    /* Getters and setters */
    /* ------------------- */


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


    
    /**
     * <p>Setter for the user birthDate: used by the parser.</p>
     *
     * @param date the birthdate String in format yyyy-MM-dd.
     * 
     * @throws ParseException if the date format is not respected.
     */
    public void setBirthDate(String date) throws ParseException {
        this.birthDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( date );
    }

    /**
     * <p>Getter for the user's birthdate.</p>
     *
     * @return the user's birthdate {@link Date}
     */
    public Date getBirthDate() {
        return this.birthDate;
    }


    
    /**
     * <p>Setter for the user country: used by the parser.</p>
     *
     * @param country the country {@link String}.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * <p>Getter for the user country.</p>
     *
     * @return the user country
     */
    public String getCountry() {
        return this.country;
    }

    

    /**
     * <p>Setter for the user gender: used by the parser.</p>
     *
     * @param gender the gender {@link String}.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * <p>Getter for the user gender.</p>
     *
     * @return the user gender
     */
    public String getGender() {
        return this.gender;
    }

    

    /**
     * <p>Setter for the user home page: used by the parser.</p>
     *
     * @param homePage the user home page url {@link String}.
     */
    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    /**
     * <p>Getter for the user home page.</p>
     *
     * @return the user home page
     */
    public String getHomePage() {
        return this.homePage;
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
     * <p>Setter for the user time zone: used by the parser.</p>
     *
     * @param timeZone the time zone {@link String}.
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * <p>Getter for the user time zone.</p>
     *
     * @return the user time zone
     */
    public String getTimeZone() {
        return this.timeZone;
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
