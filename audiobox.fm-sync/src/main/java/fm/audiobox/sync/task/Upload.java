
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
import java.io.IOException;
import java.io.OutputStream;

import fm.audiobox.core.exceptions.ForbiddenException;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFile;
import fm.audiobox.core.parsers.UploadHandler;
import fm.audiobox.sync.util.JobTask;

/**
 * 
 * 
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Upload extends JobTask {

  private MediaFile media;
  private File mFile;

  public Upload(String name, MediaFile media, File file){
    super(name);
    this.media = media;
    this.mFile = file;
  }


  public synchronized Object doTask() {

    try {
      this.media.upload(false, new UploadHandler(this.mFile) {
        public synchronized boolean write(OutputStream out, byte[] buffer, int length) throws IOException {
          out.write(buffer, 0, length);
          return !Upload.this.isCancelled();
        }
      });
    } catch (LoginException e) {
      this.log.error("Login error", e);
    } catch (ServiceException e) {
      this.log.error("Error occurred while uploading", e);
    } catch (ForbiddenException e) {
      this.log.error("Error occurred while uploading", e);
    }

    return null;

  }

  public void upload(){
    this.execute();
  }

  public boolean start() {
    return true;
  }


  public void end(Object result) {
    
  }


}