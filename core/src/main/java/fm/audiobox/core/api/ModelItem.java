
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
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.AudioBoxClient;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;


/**
 * ModelItem class is an abstract extension of {@link Model}.
 * 
 * <p>
 * 
 * It provides common methods for those XML that represent some single element.
 * 
 * <p>
 * 
 * Each ModelItem inherit a {@link Tracks} object that represents the collection 
 * of tracks that this particular model offers.
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public abstract class ModelItem extends Model {

    /** The collection of tracks for this object */
    private Tracks mTracks;

    /**
     * <p>Getter method for a particular {@link Track} of the ModelItem {@link Tracks} collection.</p>
     *
     * @param uuid the uuid of the desired track.
     * 
     * @return a {@link Track} object.
     */
    public Track getTrack(String uuid) {
        return this.mTracks.get(uuid);
    }

    /**
     * <p>Setter for the {@link Tracks} collection.</p>
     *
     * @param tracks a {@link Tracks} ModelsCollection object.
     */
    public void setTracks(Tracks tracks) {
        this.mTracks = tracks;
    }

    /**
     * Getter for the {@link Tracks} collection.
     * 
     * @return this item {@link Tracks} collection.
     * 
     * @throws ServiceException if any connection problem to AudioBox.fm occurs.
     * @throws LoginException if any authentication problem occurs.
     * @throws ModelException if any custom model is specified and an exception occurs while trying to use it.
     */
    public Tracks getTracks() throws ServiceException, LoginException, ModelException {

        this.setTracks( (Tracks) AudioBoxClient.getModelInstance(AudioBoxClient.TRACKS_KEY, this.getConnector()) );
        this.getConnector().execute(this.getEndPoint(), this.getToken(), null, this.mTracks, null);

        return this.mTracks;
    }

}
