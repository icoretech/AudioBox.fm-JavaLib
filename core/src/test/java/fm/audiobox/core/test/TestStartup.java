package fm.audiobox.core.test;
import java.net.SocketException;

import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.AudioBoxClient;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.models.User;

public class TestStartup extends junit.framework.TestCase {

	@Test
	public void testMain(){
		AudioBoxClient abc = new AudioBoxClient();
		//AudioBoxClient.initClass( AudioBoxClient.USER_KEY, myUser.class);
		try {
			User user = abc.login("fat@fatshotty.net", "f4tb0x");
			user.getUploadedTracks();
			
			Albums albums= user.getAlbums();
			Tracks tracks = albums.get(0).getTracks();
			
			System.out.println( tracks.get(0).getStreamUrl() );
			
			
			
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (SocketException e) {
            e.printStackTrace();
        }
		
	}
	
}
