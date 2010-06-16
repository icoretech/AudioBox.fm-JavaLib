package fm.audiobox.sync;

import java.io.File;
import java.io.FileFilter;

import fm.audiobox.api.interfaces.CollectionListener;
import fm.audiobox.util.ThreadItem;

public class Scan extends ThreadItem {

	
	private Thread _thread = null;
	private boolean _recursive = false;
	private File _folder = null;
	private FileFilter _ff = null;
	private CollectionListener _listener = null;

	public Scan(){
		
	}
	
	public void setListener(CollectionListener collListener){
		this._listener = collListener;
	}
	
	public void setFilter( FileFilter fileFilter){
		this._ff = fileFilter;
	}
	
	
	public File[] scan( File folder, boolean recursive ){
		this._recursive = recursive;
		this._folder = folder;
		if ( !this._folder.exists() ) return null;
		if ( this._listener == null ) return this._start( this._folder );
		if ( this._thread != null && this._thread.isAlive() ) this._thread.interrupt();
		this._thread = new Thread( this );
		this._thread.start();
		return null;
	}
	
	
	@Override
	public void _run() {
		
		this._listener.onCollectionReady( 200 , this._start(  this._folder ) );
		
	}
	
	public File[] _start( File folder ){
		File[] files = folder.listFiles( this._ff );
		for ( int i = 0, l = files.length ; i < l ; i++ ){
			File file = files[i];
			if ( file.isDirectory() ){
				_start( file );
				continue;
			}
			this._listener.onItemReady( i , file );
		}
		return files;
	}


	@Override
	protected void end() {
		
	}

	@Override
	protected void start() {
		
		this.setFilter( new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				
				if ( ! pathname.canRead() ) return false;
				
				if ( pathname.isDirectory() && ! _recursive ) return false;
				
				/* TODO: check file type */
				
				return true;
				
			}
			
		});
	}
	
}
