package fm.audiobox.sync.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.AudioBoxClient;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.User;
import fm.audiobox.sync.test.mocks.fixtures.Fixtures;

/**
 * Unit test for simple App.
 */
public class DownloadTest extends junit.framework.TestCase {
    
    private static Logger logger = LoggerFactory.getLogger(DownloadTest.class);
    
    @Test
    public void testApp() {
        
        try {
            AudioBoxClient abc = new AudioBoxClient();
            User user = abc.login( Fixtures.get( Fixtures.LOGIN), Fixtures.get(Fixtures.RIGHT_PASS) );
            assertNotNull( user );
            
            Playlists pls = user.getPlaylists();
            assertNotNull(pls);
            
            Playlist pl = pls.getPlaylistByName("development");
            assertNotNull(pl);
            
            Track track = pl.getTracks().get(0);
            assertNotNull(track);
            assertNotNull( track.getOriginalFileName() );
            
            String filePath = Fixtures.get(Fixtures.SCAN_FOLDER) + System.getProperty("file.separator") + track.getOriginalFileName();
            assertTrue( filePath.toLowerCase().endsWith( ".mp3" ));
            
            File media = new File( filePath );
            // delete file if already exists
            if ( media.exists() ) media.delete();
            // Create new media file
            try { media.createNewFile(); } catch (IOException e) { fail(e.getMessage()); }
            
            
            long total = track.getAudioFileSize();
            
            try {
            	// Start download track
                track.download( this.new testFileOutputStream( media, total ) );
			} catch (FileNotFoundException e) {
				fail(e.getMessage());
			}
            
			File f = new File ( filePath );
            assertNotNull( f );
            assertTrue( f.exists() );
            assertTrue( filePath.equals( f.getAbsolutePath() ) );
			assertTrue( f.length() == total );
			
			logger.info("New file: " + f.getPath());
			
            
        } catch (LoginException e) {
            fail(e.getMessage());
        } catch (ServiceException e) {
            fail(e.getMessage());
        } catch (ModelException e) {
            fail(e.getMessage());
        }
    }
    
    private class testFileOutputStream extends FileOutputStream {

    	private long total_bytes = -1;
    	private long current_bytes = 0;
    	private int percent = 0;
    	
		public testFileOutputStream(File file, long total) throws FileNotFoundException {
			super(file);
			this.total_bytes = total;
		}
		
		public void write(byte[] bytes, int offset, int len) throws IOException{
			super.write(bytes, offset, len);
			
			this.current_bytes += len;
			
			if (( (current_bytes * 100 )/ total_bytes ) != percent) {
			    percent = (int) ( (current_bytes * 100 )/ total_bytes );
				logger.debug("Downloaded: " + percent + " %" );
			}
		}
    	
    }
    
}
