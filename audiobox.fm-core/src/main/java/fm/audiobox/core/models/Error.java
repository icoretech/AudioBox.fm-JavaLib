
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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IEntity;




/**
 * Error class is a specialization for error XML elements.
 * 
 * <p>
 * 
 * Error XML looks like this: 
 * 
 * <pre>
 * @code
 * <error>
 *   <status>500</name>
 *   <message>This is an error message</token>
 * </error>
 * @endcode
 * </pre>
 *
 * @author Valerio Chiodino
 * @author Fabio Tunno
 */
public class Error implements IEntity, Serializable {


  private static final long serialVersionUID = 1L;
  
  private static Logger log = LoggerFactory.getLogger(Error.class);

  /** The XML tag name for the Error element */
  public static final String NAMESPACE = null;
  public static final String TAGNAME = "error";

  public static final String MESSAGE = "message";
  public static final String STATUS = "status";
  
  protected Map<String, Object> properties;
  
  private IConfiguration configuration;
  private int status;
  private String message;

  @SuppressWarnings("unused")
  private IEntity parent;
  
  
  
  /**
   * The {@link Error} entity can be manually instantiated
   * for any general purpose
   */
  public Error(){

  }
  
  

  /**
   * <p>Constructor for Error.</p>
   */
  public Error(IConfiguration config){
    this.configuration = config;
  }


  @Override
  public String getTagName() {
    return TAGNAME;
  }


  @Override
  public String getNamespace(){
    return NAMESPACE;
  }
  
  
  @Override
  public String getToken() {
    return null;
  }
  


  /* ------------------- */
  /* Getters and setters */
  /* ------------------- */


  /**
   * <p>Setter for the field <code>status</code>.</p>
   *
   * @param status the error status to set
   */
  public void setStatus(int status) {
    this.status = status;
  }

  /**
   * <p>Getter for the field <code>status</code>.</p>
   *
   * @return the status
   */
  public int getStatus() {
    return this.status;
  }



  /**
   * <p>Setter for the field <code>message</code>.</p>
   *
   * @param message the error message to set.
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * <p>Getter for the field <code>message</code>.</p>
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }


  @Override
  public Method getSetterMethod(String tagName) {
    try {
      
      if ( tagName.equals( MESSAGE ) ){
        return this.getClass().getMethod("setMessage", String.class);
        
      } else if ( tagName.equals( STATUS ) ){
        return this.getClass().getMethod("setStatus", int.class);
        
      }
      
    } catch (SecurityException e) {
      log.error("Security error", e);
    } catch (NoSuchMethodException e) {
      log.error("No method found", e);
    }
    
    return null;
  }


  public void setProperty(String key, Object value) {
    if ( value == null ) {
      if ( this.properties.containsKey( key )  ) {
        this.properties.remove(key);
      }
    } else {
      this.properties.put(key, value);
    }
  }
  
  public Object getProperty(String key) {
    return this.properties.get(key);
  }


  @Override
  public IConfiguration getConfiguration() {
    return this.configuration;
  }


  @Override
  public String getApiPath() {
    return null;
  }


  @Override
  public void setParent(IEntity parent) {
    // TODO: check
    // it doesn't need to be set as child
  }



  @Override
  public void startLoading() {
    // nothning to do
  }



  @Override
  public void endLoading() {
    // nothing to do
  }

}

