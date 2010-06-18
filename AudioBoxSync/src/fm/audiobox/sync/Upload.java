package fm.audiobox.sync;

import java.io.File;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.Header;

import fm.audiobox.api.AudioBoxClient;
import fm.audiobox.api.exceptions.LoginException;
import fm.audiobox.api.exceptions.ServiceException;
import fm.audiobox.api.interfaces.ThreadListener;
import fm.audiobox.api.models.Track;
import fm.audiobox.api.models.Tracks;
import fm.audiobox.util.ThreadItem;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.mime.content.FileBody;

public class Upload extends ThreadItem {
	
	private UploadTrack _track = null;
	private FileBody _fileBody = null;
	private File _file = null;
	
	public Upload(File file){
		this._file = file;
	}
	

	@Override
	protected synchronized void _run() {
		
		try {
			this._track.upload();
		} catch (ServiceException e) {
			e.printStackTrace( System.out );
		} catch (LoginException e) {
			e.printStackTrace( System.out );
		}
		
	}
	
	public String upload(){
		this.start();
		this._run();
		return this.end();
	}
	
	
	@Override
	protected synchronized String end() {
		return this._track.getUuid();
	}

	@Override
	protected synchronized void start() {

	    final Upload me = this;
	    final ThreadListener tl = this.getThreadListener();
	    final File file = this._file;
	    this._fileBody = new FileBody(this._file){
	      public void writeTo(final OutputStream out) throws IOException {
	
	        if (out == null) {
	            throw new IllegalArgumentException("Output stream may not be null");
	        }
	
	        long total = file.length(),
	          completed = 0;
	
	        InputStream in = new FileInputStream(file);
	        try {
	            byte[] tmp = new byte[4096];
	            int l;
	            while ((l = in.read(tmp)) != -1) {
	                out.write(tmp, 0, l);
	                completed += l;
	                tl.onProgress(me, total, completed, total-completed, file);
	            }
	            out.flush();
	        } finally {
	            in.close();
	        }
	      }
	
	    };

    
		this._track = this.new UploadTrack( this._fileBody );

	}
	
	private class UploadTrack extends Track{
		
		private static final int CHUNK = 4096;
		
		public UploadTrack(){
			super();
		}
		public UploadTrack( FileBody fb ){
			super(fb);
		}
		public void upload() throws ServiceException, LoginException{
			AudioBoxClient.execute(Tracks.END_POINT , null, null, this, HttpPost.METHOD_NAME);
		}
		public void parseResponse( InputStream input, Header contentType ) throws IOException {
			byte[] bytes = new byte[CHUNK];
			int read;
			StringBuffer result = new StringBuffer();
			while( ( read = input.read(bytes) ) != -1  ){
				result.append( new String( bytes, 0 ,read )  );
			}
			this.setUuid( result.toString().trim() );
		}
	}
	
	
}
