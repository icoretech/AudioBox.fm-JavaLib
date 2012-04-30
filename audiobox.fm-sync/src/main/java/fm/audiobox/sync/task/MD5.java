
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.twmacinta.util.MD5InputStream;

import fm.audiobox.sync.util.AsyncTask;

/**
 * 
 * 
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class MD5 extends AsyncTask {

    private File _file = null;
    private String result = "";


    public MD5(File file){
        this._file = file;
    }


    @Override
    protected synchronized void doTask() {

    byte[] buf = new byte[65536];
        int num_read = -1;
    
    com.twmacinta.util.MD5.initNativeLibrary(true);
        MD5InputStream in = null;
    try {
      in = new MD5InputStream(new BufferedInputStream(new FileInputStream(this._file)));
      
      long file_length = this._file.length(),
        completed = 0;
      
      while ((num_read = in.read(buf)) != -1){
        completed+=num_read;
        this.getThreadListener().onProgress( this , file_length , completed, file_length - completed, this._file );
      }
      
      this.result = com.twmacinta.util.MD5.asHex( in.hash() );
      
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    

    }


    public synchronized String digest(){
        this.start();
        this.doTask();
        return this.end();
    }


    @Override
    protected synchronized String end() {
        return this.result.toLowerCase();
    }

    @Override
    protected synchronized boolean start() {
      return true;
    }


}
