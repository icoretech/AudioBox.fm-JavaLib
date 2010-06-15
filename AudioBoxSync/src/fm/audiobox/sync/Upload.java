package fm.audiobox.sync;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import org.apache.http.client.methods.HttpPost;

import fm.audiobox.api.AudioBoxClient;
import fm.audiobox.api.models.Track;
import fm.audiobox.util.ThreadItem;

public class Upload extends ThreadItem {
	
	
	private File _file = null;
	private Track track = null;
	private int CHUNK = 8192*3;
	
	public Upload(File file){
		this._file = file;
		this.track = new Track( file );
	}
	

	@Override
	protected void _run() {
		
		try{
			this.track.hash();
			this.track.upload();
			
			
		}catch(Throwable t){
			// TODO: exceptions management
			System.out.println( "##### ERRORE: " + t);
		}
		
		
		
	}
	
	
	private String generateBoundary(){
		StringBuffer sb = new StringBuffer();
		
		// TODO: to fix
		sb.append( "Wqg5U6mQBaOWMxY18kmYhoWcPJ19YwMy" /*System.currentTimeMillis() */);
		
//		while(true){
//			int u = (int) Math.random();
//			
//			Character.
//			
//			
//			break;
//		}
		
		return sb.toString();
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void start() {
		// TODO Auto-generated method stub
		
	}
	
	
	public synchronized String upload( String url, String filename, String boundary, String bodylength ){
		try{
			
			
		} catch(Throwable t){
			
		}
		
		return "";
	}
	
	

}
