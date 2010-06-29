/**
 * 
 */
package fm.audiobox.core.test;


import java.net.SocketException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.StaticAudioBox;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Artist;
import fm.audiobox.core.models.Artists;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.UserFixture;

/**
 * @author keytwo
 *
 */
public class ArtistsTest extends junit.framework.TestCase {

	StaticAudioBox abc;
    User user;
    Artists artists;
    
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
    public void testArtistsShouldBePopulated() {
        loginCatched();
        try {
            
            loadArtists();
            
            assertNotNull(artists);
            Artist al = (Artist) artists.get(0);
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
        } catch (ModelException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testArtistshouldBePopulatedAndContainsTracks() {
        loginCatched();
        try {
            
            loadArtists();
            
            assertNotNull(artists);
            Artist al = (Artist) artists.get(0);
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
    
    private void loadArtists() throws ServiceException, LoginException, InstantiationException, IllegalAccessException, ClassNotFoundException, ModelException {
        artists = (Artists) user.getArtists();
        //artists.invoke();
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
