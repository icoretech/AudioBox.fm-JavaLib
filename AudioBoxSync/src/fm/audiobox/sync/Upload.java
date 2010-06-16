package fm.audiobox.sync;

import java.io.File;

import org.apache.http.client.methods.HttpPost;

import fm.audiobox.api.AudioBoxClient;
import fm.audiobox.api.exceptions.LoginException;
import fm.audiobox.api.exceptions.ServiceException;
import fm.audiobox.api.models.Track;
import fm.audiobox.util.ThreadItem;

public class Upload extends ThreadItem {
	
	private Track _track = null;
	private File _file = null;
	
	public Upload(File file){
		this._file = file;
	}
	

	@Override
	protected synchronized void _run() {
		
		try {
			AudioBoxClient.execute("", null, null, this._track, HttpPost.METHOD_NAME);
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
		return this._track.getFileHash();
	}

	@Override
	protected synchronized void start() {
		this._track = new Track( this._file );
	}
	
	
}
