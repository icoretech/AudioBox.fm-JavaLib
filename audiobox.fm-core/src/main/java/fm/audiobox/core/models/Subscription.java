
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
 * Subscription is linked to the {@link User} and specify user's subscription details.
 * 
 * <p>
 * 
 * Subscription XML looks like this: 
 * 
 * <pre>
 * {@code
 * <subscription>
 *   <created-at type="datetime">2010-02-12T15:43:21+01:00</created-at>
 *   <plan-name>ultra</plan-name>
 *   <plan>
 *      <feat-third-party type="boolean">true</feat-third-party>
 *      <feat-youtube-channel type="boolean">true</feat-youtube-channel>
 *      <feat-mobile type="boolean">true</feat-mobile>
 *      <feat-web-player type="boolean">true</feat-web-player>
 *      <feat-dropbox type="boolean">true</feat-dropbox>
 *      <feat-multiformat type="boolean">true</feat-multiformat>
 *      <feat-library-manager type="boolean">true</feat-library-manager>
 *      <feat-social type="boolean">true</feat-social>
 *      <feat-download type="boolean">true</feat-download>
 *      <feat-marketplace type="boolean">true</feat-marketplace>
 *   </plan>
 * </subscription>
 * }
 * </pre>
 * 
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Subscription extends ModelItem {
    
    /** The XML tag name for the Profile element */
    public static final String TAG_NAME = "subscription";
    
    protected Date createdAt;
    protected String planName;
    protected Plan plan;
    
    /**
     * <p>Constructor for Subscription.</p>
     */
    protected Subscription() { }
    
    
    /* ------------------- */
    /* Getters and setters */
    /* ------------------- */
    
    
    /**
     * <p>Setter for the subscription creation date: used by the parser.</p>
     *
     * @param date the creation date String in format yyyy-MM-dd.
     * 
     * @throws ParseException if the date format is not respected.
     */
    public void setCreatedAt(String date) throws ParseException {
        this.createdAt = new SimpleDateFormat( "yyyy-MM-dd" ).parse( date );
    }

    /**
     * <p>Getter for the subscription creation date.</p>
     *
     * @return the subscription creation {@link Date}
     */
    public Date getCreatedAt() {
        return this.createdAt;
    }
    
    
    
    /**
     * <p>Setter for the user subscription plan name: used by the parser.</p>
     *
     * @param planName the plan name {@link String}.
     */
    public void setPlanName(String planName) {
        this.planName = planName;
    }

    /**
     * <p>Getter for the subscription plan name.</p>
     *
     * @return the subscription plan name
     */
    public String getPlanName() {
        return this.planName;
    }
    
    
    
    /**
     * <p>Setter for the subscription plan: used by the parser.</p>
     *
     * @param plan the subscription {@link Plan} object.
     */
    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    /**
     * <p>Getter for the subscription {@link Plan}.</p>
     *
     * @return the subscription Plan details
     */
    public Plan getPlan() {
        return this.plan;
    }
}
