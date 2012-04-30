
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

package fm.audiobox.sync.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import fm.audiobox.sync.interfaces.ThreadListener;

/**
 * This abstract class is used to prototype AudioBox asynchronous tasks.
 * 
 * @author Fabio Tunno
 * @version 1.0.0
 */
public abstract class AsyncTask extends Observable implements Runnable {

  protected Map<String, Object> properties = new HashMap<String,Object>();
  //    private AsyncTaskManager threadManager = null;
  private boolean stopped = false;

  private ThreadListener threadListener = new ThreadListener() {
    @Override
    public boolean onStart(AsyncTask result) {return true;}

    @Override
    public void onProgress(AsyncTask result, long total, long completed, long remaining, Object item) {}

    @Override
    public void onComplete(AsyncTask result , Object item) {}

    @Override
    public void onStop(AsyncTask task) {}
  };


  //    protected final void setManager( AsyncTaskManager tm ){
  //        this.threadManager = tm;
  //    }

  public void setThreadListener(ThreadListener tl){
    this.threadListener = tl;
  }

  public ThreadListener getThreadListener(){
    return this.threadListener;
  }

  public void setProperty(String key, Object value){
    properties.put( key, value );
  }
  public Object getProperty(String key){
    return properties.get( key );
  }

  protected abstract boolean start();
  protected abstract void doTask();
  protected abstract Object end();


  @Override
  public final void run() {
    this.stopped = false;
    if ( this.threadListener.onStart( this ) ){
      
      if ( this.start() ){
        this.doTask();
      }

      if ( this.stopped )
        this.threadListener.onStop(this);
      else {
        Object result = this.end();
        this.threadListener.onComplete(this, result);
      }
    } else
      this.threadListener.onComplete(this, null);
    
    setChanged();
    notifyObservers();

  }

  public final void stop(){
    this.stopped = true;
  }

  public final boolean isStopped(){
    return this.stopped;
  }

}
