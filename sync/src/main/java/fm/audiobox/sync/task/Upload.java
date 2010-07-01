
/***************************************************************************
 *   Copyright (C) 2010 iCoreTech research labs                            *
 *   Contributed code from:                                                *
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.MimetypesFileTypeMap;

import org.apache.http.entity.FileEntity;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.AudioBoxClient;
import fm.audiobox.core.models.UploadTrack;
import fm.audiobox.sync.interfaces.ThreadListener;
import fm.audiobox.sync.util.AsyncTask;

public class Upload extends AsyncTask {

    private AudioBoxClient mAbc;
    private UploadTrack mTrack;
    private FileEntity mFileEntity;
    private File mFile;

    public Upload(File file, AudioBoxClient abc){
        this.mFile = file;
        this.mAbc = abc;
    }


    @Override
    protected synchronized void doTask() {

        try {
            this.mTrack.upload();
        } catch (ServiceException e) {
            e.printStackTrace( System.out );
        } catch (LoginException e) {
            e.printStackTrace( System.out );
        }

    }

    public String upload(){
        this.start();
        this.doTask();
        return this.end();
    }


    @Override
    protected synchronized String end() {
        return this.mTrack.getUuid();
    }

    @Override
    protected synchronized void start() {

        final Upload me = this;
        final ThreadListener tl = this.getThreadListener();
        final File file = this.mFile;

        this.mFileEntity = new FileEntity( file, new MimetypesFileTypeMap().getContentType(file) ) {

            @Override
            public void writeTo(final OutputStream outstream) throws IOException {

                if (outstream == null) {
                    throw new IllegalArgumentException("Output stream may not be null");
                }

                long total = file.length(),
                completed = 0;

                InputStream in = new FileInputStream(file);
                try {
                    byte[] tmp = new byte[4096];
                    int l;
                    while ((l = in.read(tmp)) != -1) {
                        outstream.write(tmp, 0, l);
                        completed += l;
                        tl.onProgress(me, total, completed, total-completed, file);
                    }
                    outstream.flush();
                } finally {
                    in.close();
                }

            }

        };

        this.mTrack = new UploadTrack( this.mFileEntity, this.mAbc );

    }
    


}
