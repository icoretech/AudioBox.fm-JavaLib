package fm;

import java.io.File;

import fm.audiobox.sync.Upload;
import fm.audiobox.util.ThreadManager;

public class Startup {

	
	public static void main(String[] args){
		
		//Upload upload = new Upload( new File("D:\\Downloads\\asd\\lkXIBgjyzd9fFBxN2R9fQA.mp3") );
		
		ThreadManager tm = new ThreadManager();
		
		tm.newThread( new Upload( new File("D:\\Downloads\\asd\\lkXIBgjyzd9fFBxN2R9fQA.mp3") ) );
		/*tm.newThread( new Upload( new File("D:\\Downloads\\asd\\lkXIBgjyzd9fFBxN2R9fQA.mp3") ) );
		tm.newThread( new Upload( new File("D:\\Downloads\\asd\\lkXIBgjyzd9fFBxN2R9fQA.mp3") ) );*/
    
		
	}
	
}
