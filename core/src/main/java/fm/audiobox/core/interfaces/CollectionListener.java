
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

package fm.audiobox.core.interfaces;


/**
 * CollectionListener provides a usefull interface to easily implement
 * callbacks functions during the parse process of the AudioBox.fm APIs responses.
 * 
 * <p>
 *
 * Default implementation in {@link AudioBoxClient} does nothing.<br/>
 * Implementing your interface you will be able to take advantage of events 
 * fired from each parsed element or the parse completed event.
 * 
 * <p>
 * 
 * This will be usefull in case of asyncronous thread that can interact with
 * a user interface.
 * 
 * <p>
 * 
 * Events fired by this interface are:
 * 
 * <ul>
 *  <li>Item is ready for use</li>
 *  <li>Document has been fully parsed</li>
 * </ul>
 * 
 * @author Valerio Chiodino
 * @version 0.0.1
 * 
 */

public interface CollectionListener {
    
    /** Message sent on document fully parsed event. */
    public static final int DOCUMENT_PARSED = -200;
    public static final int OBJECT_BUILT = -201;
    
    /**
     * This method is called when an element of a list (such as {@link Track} is ready for use.
     * 
     * <p>
     * 
     * You can use parameters passed as argument to make decisions on what to do in your callback.
     *
     * @param index the position of the ready element
     * @param item the item itself 
     */
    
    public void onItemReady(int index, Object item);
    
    /**
     * This method is called when the response document has been fully parsed.<br/>
     * The response message of this callback will be a negative value to avoid collision with the item ready
     * callback.
     * 
     * <p>
     * 
     * Actually the only message sent by this callback is the negative value represented by 
     * {@link CollectionListener#DOCUMENT_PARSED}.
     *
     * @param message the {@link CollectionListener#DOCUMENT_PARSED DOCUMENT_PARSED} value.
     * @param result the object obtained from the operation  
     */
    
    public void onCollectionReady(int message, Object result);
    
}
