
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

package fm.audiobox.core.api;

import org.apache.http.HttpEntity;



/**
 * EnclosingEntityModelItem class is an abstract extension of {@link ModelItem}.
 * 
 * <p>
 * 
 * It provides common methods for those XML that represent some single element that, to make particular requests
 * to AudioBox.fm services need an entity.
 * 
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public abstract class EnclosingEntityModelItem extends ModelItem {
    
    /** The {@link HttpEntity} to pass to the request for a particular action */
    protected HttpEntity pEntity;
    
    /**
     * <p>Getter method for the {@link HttpEntity} of this model for the specified action</p>
     * @param action the action associated to the desired entity
     *  
     * @return the http entity used to perform remote http actions.
     */
    public abstract HttpEntity getEntity(String action);

}