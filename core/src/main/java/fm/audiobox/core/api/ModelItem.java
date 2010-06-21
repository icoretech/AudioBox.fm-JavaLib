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

import fm.audiobox.core.models.Tracks;

public abstract class ModelItem extends Model {

    public ModelsCollection tracks;
    
    public ModelItem getTrack(String uuid) {
        return (ModelItem) tracks.get(uuid);
    }

    public void setTracks(ModelsCollection tracks) {
        this.tracks = tracks;
    }
    
    public Model getTracks(){
        
        if (tracks == null) {
            try {
                tracks = (ModelsCollection) abml.getModelClassName(this.getClass(), Tracks.END_POINT ).newInstance();
                
                tracks.setToken( this.getToken() );
                tracks.setEndPoint( this.endPoint );
                
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return tracks;
    }
    
}
