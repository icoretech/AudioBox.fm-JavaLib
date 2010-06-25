
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

import fm.audiobox.core.AudioBoxClient;
import fm.audiobox.core.api.Model;


/**
 * AudioBoxModelLoader interface allow a develper to extends default models
 * classes with his own.
 *
 * <p>
 *
 * A {@link Model} object can be created while parsing the AudioBox.fm API XML or
 * when a model itself contains submodels. Anytime a model is dynamically
 * created the only method this interface provides is called.
 *
 * <p>
 *
 * You can override the default AudioBoxModelLoader in {@link AudioBoxClient}
 * by providing your own implementation of this interface. Keep in mind though
 * that this will be almost never necessary if your models resides in the same
 * package.
 *
 * <p>
 *
 * By specifying your custom model package with {@link AudioBoxClient#setCustomModelsPackage(String)}
 * you don't need to override the default AudioBoxModelLoader implementation.
 *
 * <p>
 *
 * A good reason to implement your version of AudioBoxModelLoader is, for instance,
 * that your custom models don't reside in the same package.
 *
 * @author Valerio Chiodino
 * @version 0.0.1
 */

public interface AudioBoxModelLoader {
    
    
    /**
     * This method is used to gather the correct class of a specific model.
     * Default implementation of this method (found in {@link AudioBoxClient}) will first
     * look for the specifyed class in the custom package set by user. If the class is not found
     * then it will return the default model class.
     *
     * @param clazz the class used to probe the package where the desired class should be found
     * @param tagName used by the parser when a "typed" tag is found
     * @return a class that extends a Model
     */
    public Class<?> getModelClassName(Class<? extends Model> clazz, String tagName);
    
}
