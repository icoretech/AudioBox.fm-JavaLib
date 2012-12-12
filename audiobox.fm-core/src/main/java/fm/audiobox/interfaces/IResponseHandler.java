package fm.audiobox.interfaces;

import java.io.InputStream;

import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration.ContentFormat;


/**
 * This is a simple interface used as Response interceptor.
 * <br/>
 * It deserializes the response body and populates given {@link IEntity}
 */
public interface IResponseHandler {

  /**
   * This method parses the {@link InputStream} associated with this response and populates given {@link IEntity}
   * 
   * @param inputStream the the {@link InputStream} associated this response
   * @param destEntity  the {@link IEntity} to populate
   * @throws ServiceException
   */
  public void deserializeXml(InputStream inputStream, IEntity destEntity) throws ServiceException;
  
  /**
   * This method parses the {@link InputStream} associated with this response and populates given {@link IEntity}
   * 
   * @param inputStream the the {@link InputStream} associated this response
   * @param destEntity  the {@link IEntity} to populate
   * @throws ServiceException
   */
  public void deserializeJson(InputStream inputStream, IEntity destEntity) throws ServiceException;
  
  /**
   * This method parses the {@link InputStream} associated with this response and populates given {@link IEntity}
   * 
   * @param inputStream the the {@link InputStream} associated this response
   * @param destEntity  the {@link IEntity} to populate
   * @return {@link String} that represents the response content
   * @throws ServiceException
   */
  public String deserializeText(InputStream inputStream, IEntity destEntity) throws ServiceException;
  
  /**
   * This method parses the {@link InputStream} associated with this response and populates given {@link IEntity}
   * 
   * @param inputStream the the {@link InputStream} associated this response
   * @param destEntity  the {@link IEntity} to populate
   * @throws ServiceException
   */
  public void deserializeBinary(InputStream inputStream, IEntity destEntity) throws ServiceException;
  
  
  /**
   * This method parses the {@link InputStream} associated with this response and populates given {@link IEntity}
   * 
   * @param inputStream the the {@link InputStream} associated this response
   * @param destEntity  the {@link IEntity} to populate
   * @param format  the response format identified with {@link RequestFormat}
   * @return {@link String} that represents the response content
   * @throws ServiceException
   */ 
  public String deserialize(InputStream inputStream, IEntity destEntity, ContentFormat format) throws ServiceException;
  
  
}
