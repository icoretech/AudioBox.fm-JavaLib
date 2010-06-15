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

package fm.audiobox.api.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fm.audiobox.api.core.ModelItem;

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
 * @version 0.2-beta
 *
 */

public class Profile extends ModelItem {

    public static final String TAG_NAME = "profile";
    
    protected boolean autoplay;
    protected Date birthDate;
    protected String country;
    protected String gender;
    protected String homePage;
    protected String realName;
    protected String timeZone;

    // Override default constructor
    public Profile() { }


    /* =================== */
    /* Getters and Setters */
    /* =================== */


    public void setAutoplay(String autoplay) {
        this.autoplay = Boolean.parseBoolean( autoplay );
    }

    /**
     * @return the autoplay
     */
    public boolean hasAutoplay() {
        return this.autoplay;
    }


    public void setBirthDate(String date) throws ParseException {
        this.birthDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( date );
    }

    /**
     * @return the birthDate
     */
    public Date getBirthDate() {
        return this.birthDate;
    }


    public void setCountry(String country) {
        this.country = country;
    }


    /**
     * @return the country
     */
    public String getCountry() {
        return this.country;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return this.gender;
    }


    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }


    /**
     * @return the homePage
     */
    public String getHomePage() {
        return this.homePage;
    }


    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * @return the realName
     */
    public String getRealName() {
        return this.realName;
    }


    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * @return the timeZone
     */
    public String getTimeZone() {
        return this.timeZone;
    }


    /* ============= */
    /* Utils methods */
    /* ============= */

    @Override
    public String getName() {
        return this.realName;
    }

}