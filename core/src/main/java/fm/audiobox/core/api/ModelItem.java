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

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.AudioBoxClient;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;

public abstract class ModelItem extends Model {

    public Tracks tracks;
    
    public Track getTrack(String uuid) {
        return this.tracks.get(uuid);
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }
    
    public Tracks getTracks() throws ServiceException, LoginException{
        
		this.tracks = (Tracks)AudioBoxClient.getModelClass(AudioBoxClient.TRACKS_KEY, this.getConnector());
		this.getConnector().execute(this.getEndPoint(), this.getToken(), null, this.tracks, null);
        
        return this.tracks;
    }
    
}
