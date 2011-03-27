package fm.audiobox.core.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

@SuppressWarnings("unchecked")
public abstract class AbstractCollectionEntity<E> extends AbstractEntity implements List<E> {

  /**
   * {@link IEntity} collection. Used to store all {@IEntity} children
   */
  private List<IEntity> collection = new ArrayList<IEntity>();


  public AbstractCollectionEntity(IConnector connector, IConfiguration config) {
    super(connector, config);
  }


  /**
   * Empties collection. You may have to call the {@link AbstractCollectionEntity#load} method again
   */
  public void clear() {
    this.collection.clear();
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
    return null;// TODO: (E)this.collection.get( this.indexes.get( token ) );
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
    return (E)this.collection.remove(index);
  }

  
  /**
   * <b>Not implemented.</b>
   * Removes an {@link IEntity} associated with given token
   * 
   * @param token the {@link IEntity} token
   * @return true if everything went right
   */
  @Deprecated
  public boolean remove(String token){
    return false;
  }

  
  @Override
  public abstract boolean add(E entity);
  
  @Override
  public boolean remove(Object entity){
    return this.collection.remove( entity );
  }
  

  /**
   * Adds an {@link IEntity} to collection
   * 
   * @param entity the {@link IEntity} to add
   * @return true if everything's ok
   */
  protected boolean addEntity(IEntity entity) {
    return this.collection.add( entity );
  }
  

  
  /**
   * Executes request populating this class
   * 
   * @throws ServiceException
   * @throws LoginException
   */
  public void load() throws ServiceException, LoginException {
    this.load(null);
  }
  
  /**
   * Executes request populating this class.
   * <b>Note: this method invokes {@link AbstractCollectionEntity#clear()} before executing any request</b>
   * 
   * @param responseHandler the {@link IResponseHandler} used as response content parser
   * @throws ServiceException
   * @throws LoginException
   */
  public void load(IResponseHandler responseHandler) throws ServiceException, LoginException {
    this.clear();
    getConnector().get(this, null, null).send(null, responseHandler);
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


}
