package fm.audiobox.sync;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import fm.audiobox.util.ThreadItem;

public class Scan extends ThreadItem {

	
	private boolean _recursive = false;
  private boolean _hidden = false;
	private File _folder = null;
	private FileFilter _ff = null;
	private List<File> files = null;

  public Scan( File folder , boolean recursive){
    this(folder,recursive,false);
  }

	public Scan( File folder, boolean recursive , boolean hidden ){
		this._folder = folder;
		this._recursive = recursive;
    this._hidden = hidden;
		
		this.setFilter( new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				
				if ( ! pathname.canRead() ) return false;
        if ( pathname.isHidden() && ! _hidden ) return false;
				
				if ( pathname.isDirectory() && ! _recursive ) return false;
				
				/* TODO: check file type */
				if ( ! pathname.getName().toLowerCase().endsWith("mp3") && ! pathname.isDirectory() ) return false;
				
				return true;
				
			}
			
		});
	}
	
	public final void setFilter( FileFilter fileFilter ){
		this._ff = fileFilter;
	}
	
	public FileFilter getFilter(){
		return this._ff;
	}
	
	
	@Override
	protected synchronized void _run() {
		
		this._startScan(  this._folder );
		
	}
	
	public List<File> scan(){
		this.start();
		this._run();
		return this.end();
	}
	
	private void _startScan( File folder ){
		File[] files = folder.listFiles( this._ff );
		for ( File file : files ){
			if ( file.isDirectory() ){
				this._startScan( file );
				continue;
			}
			this.getThreadListener().onProgress(this, 0, 0, 0, file);
			this.files.add( file );
		}
	}


	@Override
	protected synchronized List<File> end() {
		return this.files;
	}

	@Override
	protected synchronized void start() {
		this.files = new ArrayList<File>();
	}
	
}
