
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

package fm.audiobox.sync.task;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fm.audiobox.sync.util.AsyncTask;

/**
 * 
 * 
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Scan extends AsyncTask {


    private boolean _recursive = false;
    private boolean _hidden = false;
    private File _folder = null;
    private FileFilter _ff = null;
    private List<File> files = null;

    private static final List<String> ALLOWED_MEDIA = Arrays.asList(new String[]{ "mp3", "mpeg3"});

    public Scan( File folder , boolean recursive){
        this(folder,recursive,false);
    }

    public Scan( File folder, boolean recursive , boolean hidden ){
        this._folder = folder;
        this._recursive = recursive;
        this._hidden = hidden;

    }

    public final void setFilter( FileFilter fileFilter ){
        this._ff = fileFilter;
    }

    public FileFilter getFilter(){
        return this._ff;
    }


    @Override
    protected synchronized void doTask() {

        this._startScan(  this._folder );

    }

    public List<File> scan(){
        this.start();
        this.doTask();
        return this.end();
    }

    private void _startScan( File folder ){
        File[] files = folder.listFiles( new FileFilter() {

            @Override
            public boolean accept(File pathname) {

                if ( ! pathname.canRead() ) return false;
                
                if ( pathname.isHidden() && ! _hidden ) return false;

                if ( pathname.isDirectory() && ! _recursive ) return false;
                
                if ( _ff != null ){
                	return _ff.accept( pathname );
                } else {
                	for ( String ext : ALLOWED_MEDIA ) 
                		if ( pathname.getName().endsWith( "." + ext ) ) 
                			return true;
                }
//                /* TODO: check file type */
//                String[] nameParts = pathname.getName().split("\\.");
//                
//                if ( ! ALLOWED_MEDIA.contains( nameParts[ nameParts.length -1 ].toLowerCase() ) && ! pathname.isDirectory() ) return false;

                return false;

            }

        });
        
        for ( File file : files ){
            
            if ( this.isStopped() ) return;
            
            if ( file.isDirectory() ){
                this._startScan( file );
                continue;
            }
            this.files.add( file );
            this.getThreadListener().onProgress(this, 0, 0, 0, file);
            
        }
    }


    @Override
    protected synchronized List<File> end() {
        return this.files;
    }

    @Override
    protected synchronized boolean start() {
        this.files = new ArrayList<File>();
        return true;
    }

}
