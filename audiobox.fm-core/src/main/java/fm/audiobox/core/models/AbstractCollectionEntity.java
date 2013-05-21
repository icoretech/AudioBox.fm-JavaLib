package fm.audiobox.core.models;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.observables.Event;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

/**
 * This class represents a Collection of {@link IEntity}
 * <p>
 * It extends {@link Observable} to allow you to add event observer.
 * <br/>
 * Actually it is used for collections such as:
 * <ul>
 *   <li>{@link Albums}</li>
 *   <li>{@link Playlists}</li>
 *   <li>{@link MediaFiles}</li>
 * </ul>
 * </p>
 * @param <E> 
 */
@SuppressWarnings("unchecked")
public abstract class AbstractCollectionEntity<E> extends AbstractEntity implements List<E> {

  /**
   * {@link IEntity} collection. Used to store all {@link IEntity} children
   */
  private List<IEntity> collection = new LinkedList<IEntity>();


  public AbstractCollectionEntity(IConfiguration config) {
    super(config);
  }


  /**
   * Empties collection. You may have to call the {@link AbstractCollectionEntity#load} method again
   * <p>
   * Note: it fires the {@link Event.States#COLLECTION_CLEARED}
   * </p>
   */
  public void clear() {
    this.collection.clear();
    this.setChanged();
    this.loaded = false;
    Event event = new Event(this, Event.States.COLLECTION_CLEARED);
    this.notifyObservers(event);
  }

  public boolean contains(Object entity) {
    return this.collection.contains( entity );
  }



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


  public int indexOf(Object entity) {
    return this.collection.indexOf(entity);
  }

  public boolean isEmpty() {
    return this.size() == 0;
  }

  public Iterator<E> iterator() {
    return (Iterator<E>)this.collection.iterator();
  }


  /**
   * This method may not works fine. Use {@link AbstractCollectionEntity#indexOf(Object)} instead
   * 
   * @param entity the {@link IEntity} to search
   * @return int the last index of given {@link IEntity}
   */
  @Deprecated
  public int lastIndexOf(Object entity) {
    return this.collection.lastIndexOf(entity);
  }

  public ListIterator<E> listIterator() {
    return (ListIterator<E>)this.collection.listIterator();
  }

  public ListIterator<E> listIterator(int index) {
    return (ListIterator<E>)this.collection.listIterator(index);
  }

  
  public E remove(int index) {
    IEntity entity = this.collection.get(index);
    this.remove(entity);
    return (E)entity;
  }

  
  /**
   * Removes an {@link IEntity} associated to this collection
   * 
   * @param token the {@link IEntity} token to be removed
   * @return true if everything went right
   */
  public boolean remove(String token){
    IEntity entity = (IEntity)this.get(token);
    if ( entity != null )
      return this.remove(entity);
    return false;
  }

  /**
   * This method must be override by each {@link AbstractCollectionEntity} class implementation
   * in order to {@code add} given {@link IEntity} to the collection.
   * <br/>
   * This method should invoke the {@link AbstractCollectionEntity#addEntity(IEntity)}
   * 
   * @param entity the {@link IEntity} to add
   * @return {@code true} if everything went ok. {@code false} if not
   */
  public abstract boolean add(E entity);
  
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
   * Adds an {@link IEntity} to the collection
   * 
   * @param entity the {@link IEntity} to add
   * @return {@code true} if everything went ok. {@code false} if not
   */
  protected boolean addEntity(IEntity entity) {
    boolean result = this.collection.add( entity );
    if ( result ){
      entity.setParent( this );
      this.setChanged();
      Event event = new Event(entity, Event.States.ENTITY_ADDED);
      event.detail = this.collection.size();
      this.notifyObservers(event);
    }
    return result;
  }
  

  
  public IConnectionMethod load(boolean async) throws ServiceException, LoginException {
    return this.load(async, null);
  }
  
  
  public IConnectionMethod load(boolean async, IResponseHandler responseHandler) throws ServiceException, LoginException {
    this.clear();
    IConnectionMethod request = getConnector(IConfiguration.Connectors.RAILS).get(this, null, null);
    request.send(async, null, responseHandler);
    return request;
  }
  
  
  public int size() {
    return this.collection.size();
  }

  public List<E> subList(int fromIndex, int toIndex) {
    return (List<E>)this.collection.subList(fromIndex, toIndex);
  }

  public Object[] toArray() {
    return this.collection.toArray();
  }

  public <T> T[] toArray(T[] a) {
    return this.collection.toArray(a);
  }


  /**
   * This method is not implemented. Use {@link AbstractCollectionEntity#add(IEntity)} instead.
   */
  @Deprecated
  public void add(int index, E entity) {
  }


  /**
   * This method is not implemented. Use {@link AbstractCollectionEntity#add(IEntity)} instead.
   */
  @Deprecated
  public boolean addAll(Collection<? extends E> c) {
    return false;
  }

  /**
   * This method is not implemented. Use {@link AbstractCollectionEntity#add(IEntity)} instead.
   */
  @Deprecated
  public boolean addAll(int index, Collection<? extends E> c) {
    return false;
  }


  /**
   * This method is not implemented. Use {@link AbstractCollectionEntity#contains(Object)} instead.
   */
  @Deprecated
  public boolean containsAll(Collection<?> c) {
    return false;
  }

  /**
   * This method is not implemented. Use {@link AbstractCollectionEntity#remove(Object)} instead.
   */
  @Deprecated
  public boolean removeAll(Collection<?> c) {
    return false;
  }

  /**
   * This method is not implemented.
   */
  @Deprecated
  public boolean retainAll(Collection<?> c) {
    return false;
  }

  /**
   * This method is not implemented. Use {@link AbstractCollectionEntity#add(Object)} instead.
   */
  @Deprecated
  public E set(int index, E element) {
    return null;
  }

  /**
   * Returns the {@link IEntity#getTagName()} of the entity which can be contained in this collection.
   * <p>
   *  <b>This method is usually used by the response parser</b>
   * </p>
   * @return the {@link IEntity#getTagName()}
   */
  public abstract String getSubTagName();
  
  public String serialize(IConfiguration.ContentFormat format) {
    return this.getNamespace();
  }
  
  public String toString(){
    StringBuffer buf = new StringBuffer();
    buf.append( this.getTagName() );
    buf.append( "{" );
    buf.append( this.getSubTagName() + ": " + this.collection.size() );
    buf.append( "}" );
    return buf.toString();
  }
  
}
