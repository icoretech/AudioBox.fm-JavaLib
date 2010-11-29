
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

import java.util.ArrayList;
import java.util.List;


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

	/** Parent of this ModelsCollection instance */
	protected ModelItem pParent;
	
	
	private List<ModelItem> pCollection = new ArrayList<ModelItem>();
	
	/**
	 * Getter method for the XML tag name of collection's elements.
	 * 
	 * <p>
	 * 
	 * For instance:
	 * <pre>
	 *     Tracks.getTagName() => "track"
	 * </pre>
	 * 
	 * @return the XML tag name for this ModelsCollection.
	 */
	protected abstract String getTagName();
    
    /**
     * <p>Getter method for the collection of this ModelsCollection</p>
     *
     * @return the {@link List} of {@link ModelItem} that this ModelsCollection contains.
     */
    public List<ModelItem> getCollection(){
    	return this.pCollection;
    }
    
    
    
    /**
     * <p>Setter method for <code>parent</code></p>
     * 
     * This method is while instantiating Model
     * 
     * @param parent ModelItem associated with this {@link ModelsCollection} instance
     */
    protected void setParent(ModelItem parent){
    	this.pParent = parent;
    }
    
    
    /**
     * <p>Getter method for a single {@link ModelItem} contained in the collection.</p>
     *
     * @param index the index of the desired item.
     * @return a {@link ModelItem} object.
     */
    public final ModelItem getItem(int index){
    	return (ModelItem)this.getCollection().get(index);
    }

    /**
     * <p>Getter method for a single {@link ModelItem} contained in the collection.</p>
     *
     * @param token the token of the desired ModelItem.
     * @return a {@link ModelItem} object.
     */
    public final ModelItem getItem(String token){
    	for ( ModelItem modelItem : this.getCollection() )
    		if ( modelItem.getToken().equals(token) )
    			return modelItem;
    	return null;
    }
    
    
    /**
     * This method adds a {@link ModelItem} instance to collection
     * 
     * @param item the ModelItem to add to collection
     * @return index of ModelItem put into collection, returns -1 if something went wrong
     */
    public final int addToCollection(ModelItem item){
    	// TODO: fire event
    	if ( this.getCollection().add( item ) ){
    		item.setParent( this );
    		return this.getCollection().size() -1;
    	}
    	return -1;
    }
    
    /**
     * This method removes a {@link ModelItem} item from collection
     * 
     * @param index the index of ModelItem to remove from collection
     * @return the ModelItem item removed
     */
    public final ModelItem removeFromCollection(int index){
    	// TODO: fire event
    	ModelItem item = this.getCollection().remove(index);
    	item.setParent(null);
    	return item; 
    }
    
    /**
     * This method removes a {@link ModelItem} item from collection
     * 
     * @param item the ModelItem to remove from collection
     * @return true if ModelItem was removed, false if not
     */
    public final boolean removeFromCollection(ModelItem item){
    	// TODO: fire event
    	item.setParent(null);
    	return this.getCollection().remove(item);
    }
    
    /** 
     * This method removes a {@link ModelItem} item from collection
     * 
     * @param token the token associated with ModelItem to remove
     * @return ModelItem item removed from collection
     */
    public final ModelItem removeFromCollection(String token){
    	// TODO: fire event
    	for ( ModelItem item : this.getCollection() )
    		if ( item.getToken().equals(token) ){
    			if ( this.removeFromCollection(item) ){
    				item.setParent(null);
    				return item;
    			}
    			break;
    		}
    	return null;
    }
    
    
    /**
     * This method empties the collection.
     * It is used while refreshing {@link ModelItem}
     */
    public void empties(){
    	// TODO: fire event before empty collection
    	this.pCollection = new ArrayList<ModelItem>();
    }
    
    
}
