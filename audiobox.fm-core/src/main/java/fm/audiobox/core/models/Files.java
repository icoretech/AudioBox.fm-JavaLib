
/***************************************************************************
 *   Copyright (C) 2010 iCoreTech research labs                            *
 *   Contributed code from:                                                *
 *   - Lucio Regina 													   *
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
 * <p>Files is a {@link File} collection.</p>
 *
 * @author Lucio Regina
 */
public class Files extends AbstractCollectionEntity<File> implements Serializable{

	private static final long serialVersionUID = 1L;

	public static final String NAMESPACE = "files";
	public static final String TAGNAME = NAMESPACE;

	private IEntity parent;
	/**
	 * <p>Constructor for Files.</p>
	 */
	public Files(IConnector connector, IConfiguration config) {
		super(connector, config);
	}

	public enum Types{

		AudioFile ,
		VideoFile
	}

	@Override
	public String getTagName() {
		return TAGNAME;
	}

	/**
	 * Returns the parent token if parent is set.
	 * Retuns null if not.
	 */
	public String getToken(){
		if ( parent != null ){
			return parent.getToken();
		}
		return super.getToken();
	}

	/**
	 * Returns the parent namespace if parent is set.
	 * Retuns the {@link Files#NAMESPACE} if not.
	 */
	public String getNamespace(){
		if ( parent != null ){
			return parent.getNamespace();
		}
		return NAMESPACE;
	}

	/**
	 * Sets the parent {@link IEntity}
	 * <p>
	 * <code>Files</code> can be a {@link MediaFiles} or {@link Genre} or {@link Artist} or {@link Album} child.
	 * So we have to manage each case setting this attribute
	 * </p>
	 * @param parent the {@link IEntity} parent object
	 */
	protected void setParent(IEntity parent){
		this.parent = parent;
	}

	@Override
	public Method getSetterMethod(String tagName) throws SecurityException,
	NoSuchMethodException {
		if ( tagName.equals( File.TAGNAME ) ){
			return this.getClass().getMethod("add", File.class);
		}
		return null;
	}

	@Override
	public boolean add(File entity) {
		String token = entity.getToken();
		if ( getConfiguration().hasFile( token ) ) {
			File file = this.getConfiguration().getFile( token );
			file.copy( entity );
			entity = file;
		} else {
			getConfiguration().addFile( entity );
		}
		return super.addEntity(entity);
	}

	@Override
	public String getSubTagName() {
		return File.TAGNAME;
	}

	@Override
	protected void copy(IEntity entity) {

	}
}
