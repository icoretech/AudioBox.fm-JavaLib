
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

package fm.audiobox.sync.interfaces;

import fm.audiobox.sync.util.AsyncTask;

/**
 * 
 * 
 * @author Fabio Tunno
 * @version 0.0.1
 */
public interface ThreadListener {

    
    /**
     * 
     * 
     * @param result
     */
    public boolean onStart(AsyncTask result);
    
    
    /**
     * 
     * 
     * @param result
     * @param total
     * @param completed
     * @param remaining
     * @param item
     */
    public void onProgress(AsyncTask result, long total, long completed, long remaining, Object item);
    
    
    /**
     * 
     * 
     * @param result
     * @param item
     */
    public void onComplete(AsyncTask result, Object item);
    
    
    /**
     * 
     * 
     * @param task
     */
    public void onStop(AsyncTask task);

}
