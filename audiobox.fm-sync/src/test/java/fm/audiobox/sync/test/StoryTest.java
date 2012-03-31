//package fm.audiobox.sync.test;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//import org.apache.http.HttpStatus;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import fm.audiobox.core.exceptions.LoginException;
//import fm.audiobox.core.exceptions.ModelException;
//import fm.audiobox.core.exceptions.ServiceException;
//import fm.audiobox.core.models.AudioBoxClient;
//import fm.audiobox.core.models.Playlist;
//import fm.audiobox.core.models.Playlists;
//import fm.audiobox.core.models.Track;
//import fm.audiobox.core.models.UploadTrack;
//import fm.audiobox.core.models.User;
//import fm.audiobox.core.models.AudioBoxClient.AudioBoxConnector;
//import fm.audiobox.sync.task.Upload;
//import fm.audiobox.sync.test.mocks.fixtures.Fixtures;
//
//
//public class StoryTest extends junit.framework.TestCase {
//    
//    private static Logger logger = LoggerFactory.getLogger(DownloadTest.class);
//    File media = null;
//    
//    @Test
//    public void testMediaTrasfersAndRemoval() {
//        
//        try {
//            
//            AudioBoxClient abc = new AudioBoxClient();
//            AudioBoxClient.setModelClassFor( AudioBoxClient.NEW_TRACK_KEY , UploadTrack.class );
//            
//            // User logs in
//            User user = abc.login( Fixtures.get( Fixtures.LOGIN), Fixtures.get(Fixtures.RIGHT_PASS) );
//            assertNotNull( user );
//            
//            
//            // He browse his playlists
//            Playlists pls = user.getPlaylists();
//            assertNotNull(pls);
//            
//            // Chooses one
//            Playlist pl = pls.getPlaylistByName("development");
//            assertNotNull(pl);
//            
//            // Pick first tracks on this playlist
//            Track track = pl.getTracks().get(0);
//            assertNotNull(track);
//            assertNotNull( track.getOriginalFileName() );
//            
//            String filePath = Fixtures.get(Fixtures.SCAN_FOLDER) + System.getProperty("file.separator") + track.getOriginalFileName();
//            assertTrue( filePath.toLowerCase().endsWith( ".mp3" ));
//            
//            media = new File( filePath );
//            // delete file if already exists
//            if ( media.exists() ) media.delete();
//            // Create new media file
//            try { media.createNewFile(); } catch (IOException e) { fail(e.getMessage()); }
//            
//            
//            long total = track.getAudioFileSize();
//            
//            try {
//                // Starts download track
//                track.download( this.new TestFileOutputStream( media, total ) );
//            } catch (FileNotFoundException e) {
//                fail(e.getMessage());
//            }
//            
//            File f = new File ( filePath );
//            assertNotNull( f );
//            assertTrue( f.exists() );
//            assertTrue( filePath.equals( f.getAbsolutePath() ) );
//            assertTrue( f.length() == total );
//            
//            logger.info("New file: " + f.getPath());
//            
//            // Now remove this track from audiobox.fm
//            user.dropTrack(track);
//            user.emptyTrash();
//            
//            // Ops! it was an error
//            Track up =  user.newTrack();
//            assertNotNull( up );
//            assertTrue( up instanceof UploadTrack);
//            
//            Upload upload = new Upload( (UploadTrack) up, f);
//            assertNotNull( upload );
//            
//            // Re-upload it
//            String token = upload.upload();
//            assertNotNull( token );
//            
//            // And move to the previous playlist
//            Track newTrack = user.getTrackByToken( token );
//            while ( Integer.parseInt( newTrack.refresh()[ AudioBoxConnector.RESPONSE_CODE ], 10) == HttpStatus.SC_NO_CONTENT) {
//                Thread.sleep( 1000 );
//            }
//            
//            assertEquals(token, newTrack.getToken());
//            assertTrue( newTrack.addTo( pl ) );
//            
//            // Keep tree clean
//            if ( media.exists() ) media.delete();
//            
//        } catch (LoginException e) {
//            fail(e.getMessage());
//        } catch (ServiceException e) {
//            fail(e.getMessage());
//        } catch (ModelException e) {
//            fail(e.getMessage());
//        } catch (InterruptedException e) {
//            fail(e.getMessage());
//        }
//    }
//    
//    
//    
//    
//    
//    
//    private class TestFileOutputStream extends FileOutputStream {
//
//        private long total_bytes = -1;
//        private long current_bytes = 0;
//        private int percent = 0;
//        
//        public TestFileOutputStream(File file, long total) throws FileNotFoundException {
//            super(file);
//            this.total_bytes = total;
//        }
//        
//        public void write(byte[] bytes, int offset, int len) throws IOException{
//            super.write(bytes, offset, len);
//            
//            this.current_bytes += len;
//            
//            if (( (current_bytes * 100 )/ total_bytes ) != percent) {
//                percent = (int) ( (current_bytes * 100 )/ total_bytes );
//                logger.debug("Downloaded: " + percent + " %" );
//            }
//        }
//        
//    }
//}
