package fm.audiobox.sync.test.mocks.fixtures;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Fixtures {

    private static Log log = LogFactory.getLog(Fixtures.class);
    
    public static final String LOGIN = "login";
    public static final String RIGHT_PASS = "right_pass";
    public static final String USERNAME = "username";
    public static final String WRONG_PASS = "wrong_pass";
    public static final String REAL_NAME = "real_name";
    public static final String CUSTOM_PLAYLIST_NAME = "custom_playlist_name";
    public static final String SMART_PLAYLIST_NAME = "smart_playlist_name";
    public static final String FILE_TO_UPLOAD = "file_to_upload";
    public static final String ARTIST_NAME = "artist_name";
    public static final String TRACK_TITLE = "track_title";
    public static final String GENRE_NAME = "genre_name";
    public static final String ALBUM_NAME = "album_name";
    public static final String TOKEN_LYRICS = "token_lyrics";
    
    private static Properties ps = new Properties();
    
    static {
    	try {
            
            ps.load( Fixtures.class.getResourceAsStream("fixtures.properties") );
            
        } catch (FileNotFoundException e) {
            log.error("Environment properties file not found: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Unable to access the environment properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return ps.getProperty(key);
    }

}
