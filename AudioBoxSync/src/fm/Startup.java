package fm;

import java.io.File;

import fm.audiobox.api.AudioBoxClient;
import fm.audiobox.api.exceptions.LoginException;
import fm.audiobox.api.exceptions.ServiceException;
import fm.audiobox.api.models.User;
import fm.audiobox.sync.Upload;
import fm.audiobox.util.ThreadManager;

public class Startup {

	
	public static void main(String[] args){
		
		//Upload upload = new Upload( new File("D:\\Downloads\\asd\\lkXIBgjyzd9fFBxN2R9fQA.mp3") );
		
		ThreadManager tm = new ThreadManager();
		
		AudioBoxClient abc = new AudioBoxClient();
		
		try {
			User user = abc.login( "fat@fatshotty.net", "f4tb0x");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		tm.newThread( new Upload( new File("D:\\Downloads\\asd\\lkXIBgjyzd9fFBxN2R9fQA.mp3") ) );
		
	}
	
}
