
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

import java.util.List;

import org.xml.sax.SAXException;

import fm.audiobox.core.interfaces.CollectionListener;


/**
 * ModelsCollection is an abstract extension of {@link Model} class that provide useful methods for those XML objects
 * that represents a collection of some sort.
 * 
 * <p>
 * 
 * It acts seemingly like a {@link List} though it does not implement it.
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 * @version 0.0.1
 */
public abstract class ModelsCollection extends Model {

	protected CollectionListener pCollectionListener = null;
	
	/**
	 * <p>Getter method for the tag name.</p>
	 *
	 * @return the XML tag name for this ModelsCollection.
	 */
	protected abstract String getTagName();
    
    /**
     * <p>Setter method for the collection list.</p>
     *
     * @param collection a {@link List} object that represents the collection.
     */
    public abstract void setCollection(List<?> collection);
    
    /**
     * <p>Getter method for the collection of this ModelsCollection</p>
     *
     * @return the list of {@link ModelItem} that this ModelsCollection contains.
     */
    public abstract List<?> getCollection();
    
    /**
     * <p>Getter method for a single {@link ModelItem} contained in the collection.</p>
     *
     * @param index the index of the desired item.
     * @return a {@link ModelItem} object.
     */
    public abstract ModelItem get(int index);

    /**
     * <p>Getter method for a single {@link ModelItem} contained in the collection.</p>
     *
     * @param token the token of the desired ModelItem.
     * @return a {@link ModelItem} object.
     */
    public abstract ModelItem get(String token);
    
    /**
     * <p>Setter method for a custom {@link CollectionListener} </p>
     *
     * @param cl a {@link CollectionListener} implementation.
     */
    public void setCollectionListener(CollectionListener cl) {
        this.pCollectionListener = cl;
    }
    
    /** {@inheritDoc} */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (localName.trim().length() == 0) {
            localName = qName;
        }
        if ( this.getTagName().equals(localName) ) {
            int index = this.getCollection().size() -1;
            this.pCollectionListener.onItemReady( index, this.getCollection().get(index));
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void endDocument() throws SAXException {
        this.pCollectionListener.onCollectionReady(CollectionListener.DOCUMENT_PARSED, this);
    }
    
}
