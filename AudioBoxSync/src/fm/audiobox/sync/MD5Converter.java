package fm.audiobox.sync;

import java.io.File;

import fm.audiobox.api.interfaces.CollectionListener;
import fm.audiobox.util.ThreadItem;

public class MD5Converter extends ThreadItem{
	
	private Thread _thread = null;
	private File _file = null;
	private CollectionListener _listener = null;
	
	
	public void setCollectionListener(CollectionListener collListener){
		this._listener = collListener;
	}
	
	public String convert(File file){
		this._file = file;
		if ( this._listener == null ) return this.convert();
		if ( this._thread != null && this._thread.isAlive() ) this._thread.interrupt();
		this._thread = new Thread( this );
		this._thread.start();
		return null;
	}
	
	private String convert(){
		return "";
	}
	
	@Override
	public void _run() {
		String result = this.convert();
		this._listener.onItemReady( 0, result);
		this._listener.onCollectionReady( 200, result);
	}
	
	
	@Override
	protected void end() {}

	@Override
	protected void start() {}
	
	
}
