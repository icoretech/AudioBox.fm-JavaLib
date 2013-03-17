
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

import fm.audiobox.core.observables.Event;
import fm.audiobox.sync.util.JobTask;

public class MD5 extends JobTask {

  private File _file = null;
  private String result = "";


  public MD5(String name, File file){
    super(name);
    this._file = file;
  }


  public synchronized Object doTask() {

    byte[] buf = new byte[1024 * 1024];
    int num_read = -1;
    
    com.twmacinta.util.MD5.initNativeLibrary(true);
    MD5InputStream in = null;
    
    try {
      in = new MD5InputStream(new BufferedInputStream(new FileInputStream(this._file)));
      
      long file_length = this._file.length(),
      completed = 0;
      
      while (  ( (num_read = in.read(buf)) != -1 ) && !this.isCancelled() ){
        completed += num_read;
        this.fireEvent(Event.States.ENTITY_REFRESHED, ((file_length - completed) * 100) / file_length );
      }
      
      if ( this.isCancelled() ){
        return "";
      }
      
      this.result = com.twmacinta.util.MD5.asHex( in.hash() );
      
    } catch (IOException e) {
      this.fireEvent(Event.States.ERROR, e.getMessage() );
    } finally {
      try {
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    return this.result;
  }


  public synchronized String digest(){
    this.execute();
    return this.result;
  }


  public synchronized void end(Object result) {
    
  }

  public boolean start() {
    return true;
  }

}
