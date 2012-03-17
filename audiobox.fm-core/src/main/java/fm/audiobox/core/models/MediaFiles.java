/***************************************************************************
 *   Copyright (C) 2010 iCoreTech research labs                            *
 *   Contributed code from:                                                *
 *   - Lucio Regina 							                           *
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;

/**
 * <p>MediaFiles is a {@link ModelsCollection} specialization for {@link MediaFile} collections.</p>
 * 
 *
 * @author Lucio Regina
 * @version 0.0.1
 */
public class MediaFiles extends AbstractCollectionEntity<MediaFile> implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The XML tag name for the MediaFiles element */
  public static final String NAMESPACE = MediaFiles.TAGNAME;
  public static final String TAGNAME = "media_files";

  private IEntity parent;
  /**
   *  MediaFiles are grouped by types that are:
   * <ul> 
   *   <li>{@link MediaFilesTypes#AudioFile AudioFile}</li>
   *   <li>{@link MediaFilesTypes#VideoFile VideoFile}</li>
   * </ul>
   */
  public enum Types {

    AudioFile,

    VideoFile

  }

  public MediaFiles(IConnector connector, IConfiguration config) {
    super(connector, config);
  }

  @Override
  public String getNamespace() {
    if ( parent != null ){
      return parent.getNamespace();
    }
    return NAMESPACE;
  }

  @Override
  public String getTagName() {
    if ( parent != null ){
      return parent.getTagName();
    }
    return TAGNAME;
  }

  @Override
  public Method getSetterMethod(String tagName) throws SecurityException,  NoSuchMethodException {

    if ( tagName.equals( MediaFile.TAGNAME ) ) {
      return this.getClass().getMethod("add", MediaFile.class);
    }

    return null;
  }

  @Override
  public boolean add(MediaFile entity) {

    String token = entity.getToken();
    if ( getConfiguration().hasMediaFile( token ) ) {
      MediaFile pl = this.getConfiguration().getMediaFile( token );
      pl.copy( entity );
      entity = pl;
    } else {
      getConfiguration().addMediaFile( entity );
    }
    return super.addEntity(entity);
  }

  /**
   * Returns the {@link MediaFile} associated with the given <code>filename</code>
   * @param filename the MediaFile filename
   * @return the {@link MediaFile} associated with the given <code>filename</code>
   */
  public MediaFile getMediaFileByName(String filename){
    for ( Iterator<MediaFile> it = this.iterator(); it.hasNext();  ){
      MediaFile mdf = it.next();
      if (  mdf.getFilename().equalsIgnoreCase( filename )  ) {
        return mdf;
      }
    }
    return null;
  }

  /**
   * Returns the <b>first</b> {@link MediaFile} that matches with the given {@link MediaFilesTypes}
   * 
   * @param type the {@link MediaFilesTypes}
   * @return the first {@link MediaFile} that matches with the given {@link MediaFilesTypes}
   */
  public MediaFile getMediaFileByType( Types type ){
    for ( Iterator<MediaFile> it = this.iterator(); it.hasNext();  ){
      MediaFile mdf = it.next();
      if (  mdf.getType() == type  ) {
        return mdf;
      }
    }
    return null;
  }

  /**
   * Returns a list of {@link MediaFile} that matches the given {@link MediaFilesTypes}
   * 
   * @param type the {@link MediaFilesTypes}
   * @return a list of {@link MediaFile} that matches with the given {@link MediaFilesTypes}
   */
  public List<MediaFile> getMediaFilesByType( Types type ){
    List<MediaFile> pls = new ArrayList<MediaFile>();
    for ( Iterator<MediaFile> it = this.iterator(); it.hasNext();  ){
      MediaFile mdf = it.next();
      if (  mdf.getType() == type  ) {
        pls.add(mdf);
      }
    }
    return pls;
  }

  /**
   * Sets the parent {@link IEntity}
   * <p>
   * <code>MediaFiles</code> can be a {@link Playlist}.
   * So we have to manage each case setting this attribute
   * </p>
   * @param parent the {@link IEntity} parent object
   */
  protected void setParent(IEntity parent){
    this.parent = parent;
  }

  @Override
  public String getSubTagName() {
    return MediaFile.TAGNAME;
  }


  @Override
  protected void copy(IEntity entity) {
  }



}
