
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

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;



/**
 * Profile is linked to the {@link User} and specify more user's aspects and options.
 * 
 * <p>
 * 
 * Profile XML looks like this: 
 * 
 * <pre>
 * @code
 * <profile> 
 *   <autoplay>false</autoplay> 
 *   <real_name>Name Surname</real_name> 
 * </profile>
 * @endcode
 * </pre>
 * 
 * @author Valerio Chiodino
 * @author Fabio Tunno
 */
public final class Profile extends AbstractEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final String NAMESPACE = null;
  public static final String TAGNAME = "profile";
  
  private boolean autoplay;
  private String realName;

  /**
   * <p>Constructor for Profile.</p>
   */
  public Profile(IConnector connector, IConfiguration config) {
    super(connector, config);
  }

  
  @Override
  public String getTagName(){
    return TAGNAME;
  }
  
  @Override
  public String getNamespace(){
    return NAMESPACE;
  }


  /**
   * <p>Setter for the user real name: used by the parser.</p>
   *
   * @param realName the real name {@link String}.
   */
  public void setRealName(String realName) {
    this.realName = realName;
  }

  /**
   * <p>Getter for the user real name.</p>
   *
   * @return the user real name
   */
  public String getRealName() {
    return this.realName;
  }


  /**
   * <p>Setter for the autoplay option.</p>
   *
   * @param autoplay String representing the boolean value, true to enable false to disable.
   */
  public void setAutoplay(boolean autoplay) {
    this.autoplay = autoplay;
  }

  /**
   * Checks whether the user has enabled autoplay or not.
   *
   * @return the autoplay user option value
   */
  public boolean hasAutoplay() {
    return this.autoplay;
  }


  @Override
  protected void copy(IEntity entity) {
    // default: do nothing
  }

  @Override
  public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
    
    if ( tagName.equals("autoplay") ){
      return this.getClass().getMethod("setAutoplay", boolean.class);
    } else if ( tagName.equals("real_name") ){
      return this.getClass().getMethod("setRealName", String.class);
    }
    
    return null;
  }


}
