
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
 * <pre>
 * {@code
 * <profile>
 *   <autoplay type="boolean">false</autoplay>
 *   <birth-date type="date">1982-04-20</birth-date>
 *   <country>IT</country>
 *   <gender>m</gender>
 *   <home-page>http://www.keytwo.net</home-page>
 *   <real-name>Valerio Chiodino</real-name>
 *   <time-zone>Rome</time-zone>
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


    /* =================== */
    /* Getters and Setters */
    /* =================== */


    /**
     * <p>Setter for the field <code>autoplay</code>.</p>
     *
     * @param autoplay a {@link java.lang.String} object.
     */
    public void setAutoplay(String autoplay) {
        this.autoplay = Boolean.parseBoolean( autoplay );
    }

    /**
     * <p>hasAutoplay</p>
     *
     * @return the autoplay
     */
    public boolean hasAutoplay() {
        return this.autoplay;
    }


    /**
     * <p>Setter for the field <code>birthDate</code>.</p>
     *
     * @param date a {@link java.lang.String} object.
     * @throws java.text.ParseException if any.
     */
    public void setBirthDate(String date) throws ParseException {
        this.birthDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( date );
    }

    /**
     * <p>Getter for the field <code>birthDate</code>.</p>
     *
     * @return the birthDate
     */
    public Date getBirthDate() {
        return this.birthDate;
    }


    /**
     * <p>Setter for the field <code>country</code>.</p>
     *
     * @param country a {@link java.lang.String} object.
     */
    public void setCountry(String country) {
        this.country = country;
    }


    /**
     * <p>Getter for the field <code>country</code>.</p>
     *
     * @return the country
     */
    public String getCountry() {
        return this.country;
    }


    /**
     * <p>Setter for the field <code>gender</code>.</p>
     *
     * @param gender a {@link java.lang.String} object.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * <p>Getter for the field <code>gender</code>.</p>
     *
     * @return the gender
     */
    public String getGender() {
        return this.gender;
    }


    /**
     * <p>Setter for the field <code>homePage</code>.</p>
     *
     * @param homePage a {@link java.lang.String} object.
     */
    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }


    /**
     * <p>Getter for the field <code>homePage</code>.</p>
     *
     * @return the homePage
     */
    public String getHomePage() {
        return this.homePage;
    }


    /**
     * <p>Setter for the field <code>realName</code>.</p>
     *
     * @param realName a {@link java.lang.String} object.
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * <p>Getter for the field <code>realName</code>.</p>
     *
     * @return the realName
     */
    public String getRealName() {
        return this.realName;
    }


    /**
     * <p>Setter for the field <code>timeZone</code>.</p>
     *
     * @param timeZone a {@link java.lang.String} object.
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * <p>Getter for the field <code>timeZone</code>.</p>
     *
     * @return the timeZone
     */
    public String getTimeZone() {
        return this.timeZone;
    }


    /* ============= */
    /* Utils methods */
    /* ============= */

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return this.realName;
    }

}
