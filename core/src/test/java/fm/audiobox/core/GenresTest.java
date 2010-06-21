/**
 * 
 */
package fm.audiobox.core;


import java.net.SocketException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.mocks.fixtures.UserFixture;
import fm.audiobox.core.models.Genre;
import fm.audiobox.core.models.Genres;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.models.User;

/**
 * @author keytwo
 *
 */
public class GenresTest extends junit.framework.TestCase {

    AudioBoxClient abc;
    User user;
    Genres genres;
    
    @Before
    public void setUp() throws Exception {
        abc = new AudioBoxClient();
        user = abc.login(UserFixture.LOGIN, UserFixture.RIGHT_PASS);
    }
    
    @Test
    public void testPreconditions() {
        assertNotNull( abc );
        assertNotNull( user );
    }

   
    @Test
    public void testGenresShouldBePopulated() {
        loginCatched();
        try {
            
            loadGenres();
            
            assertNotNull(genres);
            Genre al = (Genre) genres.get(0);
            assertNotNull(al);

        } catch (LoginException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGenreShouldBePopulatedAndContainsTracks() {
        loginCatched();
        try {
            
            loadGenres();
            
            assertNotNull(genres);
            Genre al = (Genre) genres.get(0);
            assertNotNull(al);
            
            Tracks trs = (Tracks) al.getTracks();
            assertNotNull(trs);
            
            trs.invoke();
            
            Track tr = (Track) trs.get(0);
            assertNotNull(tr);

        } catch (LoginException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    
    @After
    public void tearDown() throws Exception {
        
    }
    
    private void loadGenres() throws ServiceException, LoginException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        genres = (Genres) user.getGenres();
        genres.invoke();
    }
    
    private void loginCatched() {
        try {
            user = abc.login( UserFixture.LOGIN , UserFixture.RIGHT_PASS );
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        
    }
}
