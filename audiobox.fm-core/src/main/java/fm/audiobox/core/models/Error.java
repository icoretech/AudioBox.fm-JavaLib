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
 * This class is a representation of the {@code Error} entity
 */
public class Error implements IEntity, Serializable {

  private static final long serialVersionUID = 1L;
  
  private static Logger log = LoggerFactory.getLogger(Error.class);

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
  public Error() {

  }

  public Error(IConfiguration config) {
    this.configuration = config;
  }

  public String getTagName() {
    return TAGNAME;
  }

  public String getNamespace() {
    return NAMESPACE;
  }

  /**
   * This class has no token
   */
  public String getToken() {
    return null;
  }

  /**
   * Sets the response status code
   * @param status the response status code
   */
  public void setStatus(int status) {
    this.status = status;
  }

  /**
   * @return the response status
   */
  public int getStatus() {
    return this.status;
  }

  /**
   * Sets the response body message
   * @param message the response body message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * @return the response body message
   */
  public String getMessage() {
    return message;
  }

  public Method getSetterMethod(String tagName) {
    try {
      if (tagName.equals(MESSAGE)) {
        return this.getClass().getMethod("setMessage", String.class);

      } else if (tagName.equals(STATUS)) {
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
    if (value == null) {
      if (this.properties.containsKey(key)) {
        this.properties.remove(key);
      }
    } else {
      this.properties.put(key, value);
    }
  }

  public Object getProperty(String key) {
    return this.properties.get(key);
  }

  public IConfiguration getConfiguration() {
    return this.configuration;
  }

  public String getApiPath() {
    return null;
  }

  /**
   * This class has not any parent entity
   */
  public void setParent(IEntity parent) {
  }

  public void startLoading() {
    // nothning to do
  }

  public void endLoading() {
    // nothing to do
  }

}
