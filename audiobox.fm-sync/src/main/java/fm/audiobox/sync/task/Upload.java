
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

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFile;
import fm.audiobox.sync.util.AsyncTask;

/**
 * 
 * 
 * @author Fabio Tunno
 * @version 0.0.1
 */
public class Upload extends AsyncTask {

    private MediaFile media;
    private File mFile;

    public Upload(MediaFile media, File file){
        this.media = media;
        this.mFile = file;
    }


    @Override
    protected synchronized void doTask() {

        try {
			this.media.upload(this.mFile);
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }

    public String upload(){
        this.start();
        this.doTask();
        return this.end();
    }


    @Override
    protected synchronized String end() {
        return this.media.getToken();
    }

    @Override
    protected synchronized void start() {

//        final Upload me = this;
//        final ThreadListener tl = this.getThreadListener();
//        final long total = this.mFile.length();
//
//        FileBody mFileBody = new FileBody( this.mFile, new MimetypesFileTypeMap().getContentType(this.mFile) ) {
//
//            @Override
//            public void writeTo(final OutputStream outstream) throws IOException {
//
//                if (outstream == null) {
//                    throw new IllegalArgumentException("Output stream may not be null");
//                }
//
//                long completed = 0;
//
//                InputStream in = new FileInputStream(mFile);
//                try {
//                    byte[] tmp = new byte[4096];
//                    int l;
//                    while ((l = in.read(tmp)) != -1) {
//                    	if ( me.isStopped() ){
//                    		mTrack.getConnectionMethod().abort();
//                    		break;
//                    	}
//                        outstream.write(tmp, 0, l);
//                        completed += l;
//                        tl.onProgress(me, total, completed, (total - completed), mFile);
//                    }
//                    outstream.flush();
//                } finally {
//                    in.close();
//                }
//
//            }
//
//        }; 
//
//        this.mTrack.setFileBody( mFileBody );

    }


}



