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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.models.Playlists.Types;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;

/**
 * MediaFilelist class is a {@link ModelItem} specialization for playlists elements.
 * 
 * <p>
 * 
 * Profile Json looks like this: 
 * 
 *    { token: 'BZKsRc453WJz',
 *      name: 'Music',
 *      position: 1,
 *      media_files_count: 297,
 *      type: 'AudioPlaylist',
 *      updated_at: '2012-02-28T22:58:27Z',
 *      last_accessed: true }  
 *
 * @author Lucio Regina
 * @version 0.0.1
 */
public class MediaFiles extends AbstractEntity implements Serializable {


	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(MediaFiles.class);

	/** The XML tag name for the MediaFiles element */
	public static final String NAMESPACE = MediaFiles.TAGNAME;
	public static final String TAGNAME = "media_files";

	private String name;
	private int position = 0;
	private Types type;
	private long media_files_count;
	private Files files;
	private String updated_at;
	private boolean last_accessed;


	/**
	 * <p>Constructor for MediaFiles.</p>
	 */	
	public MediaFiles(IConnector connector, IConfiguration config) {
		super(connector, config);
	}

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}
	@Override
	public String getTagName() {
		return TAGNAME;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Types getType() {
		return type;
	}

	public void setType(String type) {
		if ( type.equals( Types.AUDIO.toString().toLowerCase() ) ){
			this.type = Types.AUDIO;

		} else if ( type.equals( Types.TRASH.toString().toLowerCase()  ) ){
			this.type = Types.TRASH;

		} else if ( type.equals( Types.VIDEO.toString().toLowerCase()  ) ){
			this.type = Types.VIDEO;

		} else if ( type.equals( Types.CUSTOM.toString().toLowerCase()  ) ){
			this.type = Types.CUSTOM;

		} else if ( type.equals( Types.OFFLINE.toString().toLowerCase()  ) ){
			this.type = Types.OFFLINE;

		}
	}

	public long getMedia_files_count() {
		return media_files_count;
	}

	public void setMedia_files_count(long media_files_count) {
		this.media_files_count = media_files_count;
	}

	public Files getFiles() {
		if( this.files == null){
			this.files = (Files)getConfiguration().getFactory().getEntity( Files.TAGNAME , getConfiguration() );
			this.files.setParent(this);
		}
		return files;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public boolean isLast_accessed() {
		return last_accessed;
	}

	public void setLast_accessed(boolean last_accessed) {
		this.last_accessed = last_accessed;
	}

	@Override
	public Method getSetterMethod(String tagName) throws SecurityException, NoSuchMethodException {
		if ( tagName.equals("token") ) {
			return this.getClass().getMethod("setToken", String.class);

		} else if ( tagName.equals("name") ) {
			return this.getClass().getMethod("setName", String.class);

		} else if ( tagName.equals("position") ) {
			return this.getClass().getMethod("setPosition", int.class);

		} else if ( tagName.equals("type")) {
			return this.getClass().getMethod("setType", String.class);

		} else if ( tagName.equals("media_files_count") ) {
			return this.getClass().getMethod("setMedia_files_count", long.class);

		} else if ( tagName.equals("updated_at") ) {
			return this.getClass().getMethod("setUpdated_at", String.class);

		} else if ( tagName.equals("last_accessed") ) {
			return this.getClass().getMethod("setLast_accessed", boolean.class);
		}
		return null;
	}
	
	@Override
	protected void copy(IEntity entity) {
		// TODO Auto-generated method stub

	}


}
