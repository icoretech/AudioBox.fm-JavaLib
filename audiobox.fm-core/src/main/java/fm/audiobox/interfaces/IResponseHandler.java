package fm.audiobox.interfaces;

import java.io.InputStream;

import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConfiguration.RequestFormat;

public interface IResponseHandler {

  /**
   * This method parses the {@link InputStream} associated with this response and populates given {@link IEntity}
   * 
   * @param inputStream the the {@link InputStream} associated this response
   * @param destEntity  the {@link IEntity} to populate
   * @throws ServiceException
   */
  public void parseAsXml(InputStream inputStream, IEntity destEntity) throws ServiceException;
  
  /**
   * This method parses the {@link InputStream} associated with this response and populates given {@link IEntity}
   * 
   * @param inputStream the the {@link InputStream} associated this response
   * @param destEntity  the {@link IEntity} to populate
   * @throws ServiceException
   */
  public void parseAsJson(InputStream inputStream, IEntity destEntity) throws ServiceException;
  
  /**
   * This method parses the {@link InputStream} associated with this response and populates given {@link IEntity}
   * 
   * @param inputStream the the {@link InputStream} associated this response
   * @param destEntity  the {@link IEntity} to populate
   * @return {@link String} that represents the response content
   * @throws ServiceException
   */
  public String parseAsText(InputStream inputStream, IEntity destEntity) throws ServiceException;
  
  /**
   * This method parses the {@link InputStream} associated with this response and populates given {@link IEntity}
   * 
   * @param inputStream the the {@link InputStream} associated this response
   * @param destEntity  the {@link IEntity} to populate
   * @throws ServiceException
   */
  public void parseAsBinary(InputStream inputStream, IEntity destEntity) throws ServiceException;
  
  
  /**
   * This method parses the {@link InputStream} associated with this response and populates given {@link IEntity}
   * 
   * @param inputStream the the {@link InputStream} associated this response
   * @param destEntity  the {@link IEntity} to populate
   * @param format  the response format identified with {@link RequestFormat}
   * @return {@link String} that represents the response content
   * @throws ServiceException
   */ 
  public String parse(InputStream inputStream, IEntity destEntity, RequestFormat format) throws ServiceException;
  
  
}
