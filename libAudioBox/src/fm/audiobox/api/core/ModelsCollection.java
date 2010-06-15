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

package fm.audiobox.api.core;

import java.util.List;

import org.xml.sax.SAXException;

import fm.audiobox.api.AudioBoxClient;

public abstract class ModelsCollection extends Model {

    protected abstract String getTagName();
    
    public abstract void setCollection(List<?> collection);
    
    public abstract List<?> getCollection();
    
    public abstract ModelItem get(int index);

    public abstract ModelItem get(String token);
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (localName.trim().length() == 0) {
            localName = qName;
        }
        if ( this.getTagName().equals(localName) ) {
            int index = this.getCollection().size() -1;
            AudioBoxClient.getCollectionListener().onItemReady( index, this.getCollection().get(index));
        }
    }

}
