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


package fm.audiobox.core.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.observables.Event;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

@SuppressWarnings("unchecked")
public abstract class AbstractCollectionEntity<E> extends AbstractEntity implements List<E> {

  /**
   * {@link IEntity} collection. Used to store all {@IEntity} children
   */
  private List<IEntity> collection = new ArrayList<IEntity>();


  public AbstractCollectionEntity(IConfiguration config) {
    super(config);
  }


  /**
   * Empties collection. You may have to call the {@link AbstractCollectionEntity#load} method again
   */
  public void clear() {
    this.collection.clear();
    this.setChanged();
    Event event = new Event(this, Event.States.COLLECTION_CLEARED);
    this.notifyObservers(event);
  }

  @Override
  public boolean contains(Object entity) {
    return this.collection.contains( entity );
  }



  @Override
  public E get(int index) {
    return (E)this.collection.get(index);
  }

  /**
   * Returns the {@link IEntity} by a given token
   * 
   * @param token {@link IEntity} token
   * @return the {@link IEntity} associated with this token
   */
  public E get(String token) {
    for (Iterator<IEntity> it = this.collection.iterator(); it.hasNext(); ){
      IEntity entity = it.next();
      if ( entity.getToken().equals( token ) )
        return (E)entity;
    }
    return null;
  }


  @Override
  public int indexOf(Object entity) {
    return this.collection.indexOf(entity);
  }

  @Override
  public boolean isEmpty() {
    return this.size() == 0;
  }

  @Override
  public Iterator<E> iterator() {
    return (Iterator<E>)this.collection.iterator();
  }


  /**
   * This method may not works fine. Use {@link AbstractCollectionEntity#indexOf(Object)} instead
   * 
   * @param entity the {@link IEntity} to search
   * @return int the last index of given {@link IEntity}
   */
  @Override
  public int lastIndexOf(Object entity) {
    return this.collection.lastIndexOf(entity);
  }

  @Override
  public ListIterator<E> listIterator() {
    return (ListIterator<E>)this.collection.listIterator();
  }

  
  @Override
  public ListIterator<E> listIterator(int index) {
    return (ListIterator<E>)this.collection.listIterator(index);
  }

  
  

  @Override
  public E remove(int index) {
    IEntity entity = this.collection.get(index);
    this.remove(entity);
    return (E)entity;
  }

  
  /**
   * Removes an {@link IEntity} associated with given token
   * 
   * @param token the {@link IEntity} token
   * @return true if everything went right
   */
  public boolean remove(String token){
    IEntity entity = (IEntity)this.get(token);
    if ( entity != null )
      return this.remove(entity);
    return false;
  }

  
  @Override
  public abstract boolean add(E entity);
  
  @Override
  public boolean remove(Object entity){
    boolean result = this.collection.remove( entity );
    if ( result ){
      this.setChanged();
      Event event = new Event(entity, Event.States.ENTITY_REMOVED);
      this.notifyObservers(event);
    }
    return result;
  }
  

  /**
   * Adds an {@link IEntity} to collection
   * 
   * @param entity the {@link IEntity} to add
   * @return true if everything's ok
   */
  protected boolean addEntity(IEntity entity) {
    boolean result = this.collection.add( entity );
    entity.setParent( this );
    if ( result ){
      this.setChanged();
      Event event = new Event(entity, Event.States.ENTITY_ADDED);
      event.detail = this.collection.size();
      this.notifyObservers(event);
    }
    return result;
  }
  

  
  /**
   * Executes request populating this class
   * 
   * @return a String array containing the response code and the response body ( used in {@link User#getUploadedTracks()}
   * 
   * @throws ServiceException
   * @throws LoginException
   */
  public IConnectionMethod load(boolean async) throws ServiceException, LoginException {
    return this.load(async, null);
  }
  
  
  /**
   * Executes request populating this class.
   * <b>Note: this method invokes {@link AbstractCollectionEntity#clear()} before executing any request</b>
   * 
   * @param responseHandler the {@link IResponseHandler} used as response content parser
   * @return a String array containing the response code and the response body ( used in {@link User#getUploadedTracks()}
   * @throws ServiceException
   * @throws LoginException
   */
  public IConnectionMethod load(boolean async, IResponseHandler responseHandler) throws ServiceException, LoginException {
    this.clear();
    IConnectionMethod request = getConnector(IConfiguration.Connectors.RAILS).get(this, null, null);
    request.send(async, null, responseHandler);
    return request;
  }
  
  
  @Override
  public int size() {
    return this.collection.size();
  }

  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    return (List<E>)this.collection.subList(fromIndex, toIndex);
  }

  @Override
  public Object[] toArray() {
    return this.collection.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return this.collection.toArray(a);
  }


  /**
   * This method is not implemented. Use {@link AbstractCollectionEntity#add(IEntity)} instead.
   * 
   * @param index
   * @param entity
   */
  @Override
  @Deprecated
  public void add(int index, E entity) {
  }


  /**
   * This method is not implemented. Use {@link AbstractCollectionEntity#add(IEntity)} instead.
   * 
   * @param index
   * @param entity
   */
  @Override
  @Deprecated
  public boolean addAll(Collection<? extends E> c) {
    return false;
  }

  /**
   * This method is not implemented. Use {@link AbstractCollectionEntity#add(IEntity)} instead.
   * 
   * @param index
   * @param entity
   */
  @Override
  @Deprecated
  public boolean addAll(int index, Collection<? extends E> c) {
    return false;
  }


  /**
   * This method is not implemented. Use {@link AbstractCollectionEntity#contains(Object)} instead.
   * 
   * @param Collection
   */
  @Override
  @Deprecated
  public boolean containsAll(Collection<?> c) {
    return false;
  }

  /**
   * This method is not implemented. Use {@link AbstractCollectionEntity#remove(Object)} instead.
   * 
   * @param Collection
   */
  @Override
  @Deprecated
  public boolean removeAll(Collection<?> c) {
    return false;
  }

  /**
   * This method is not implemented.
   * 
   * @param Collection
   */
  @Override
  @Deprecated
  public boolean retainAll(Collection<?> c) {
    return false;
  }

  /**
   * This method is not implemented. Use {@link AbstractCollectionEntity#add(Object)} instead.
   * 
   * @param Collection
   */
  @Override
  @Deprecated
  public E set(int index, E element) {
    return null;
  }

  /**
   * Returns the tag name associated with the Entity content in this list
   * <b>This method is used by parser</b>
   * @return
   */
  public abstract String getSubTagName();
}
