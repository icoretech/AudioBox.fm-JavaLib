/**
 * 
 */
package fm.audiobox.core.test;


import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.StaticAudioBox;
import fm.audiobox.core.api.Model;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Genre;
import fm.audiobox.core.models.Genres;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.UserFixture;

/**
 * @author keytwo
 *
 */
public class GenresTest extends junit.framework.TestCase {

	StaticAudioBox abc;
    User user;
    Genres genres;
    
    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws Exception {
    	abc = new StaticAudioBox();
    	abc.setForceTrust(true);
        
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
            
            Genre genre = null;
            
            for (Model g : genres.getCollection()) {
                Genre gnr = (Genre) g;
                assertNotNull(gnr);
                genre = gnr;
            }

            Genre g = (Genre) genres.get(genre.getToken());
            assertNotNull( g );
            assertSame( g, genre);
            
            List<Genre> list = new ArrayList<Genre>();
            genres.setCollection( list );
            
            assertNotNull( genres.getCollection() );
            assertSame( list, genres.getCollection() );
            
            genre = (Genre) genres.get(genre.getToken());
            assertNull( genre );
            

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
        } catch (ModelException e) {
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
            
            //trs.invoke();
            
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
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }
    
    
    @After
    public void tearDown() throws Exception {
        
    }
    
    private void loadGenres() throws ServiceException, LoginException, InstantiationException, IllegalAccessException, ClassNotFoundException, ModelException {
        genres = (Genres) user.getGenres(false);
        //genres.invoke();
    }
    
    private void loginCatched() {
        try {
            user = abc.login( UserFixture.LOGIN , UserFixture.RIGHT_PASS );
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (ModelException e) {
            e.printStackTrace();
        }
        
    }
}
