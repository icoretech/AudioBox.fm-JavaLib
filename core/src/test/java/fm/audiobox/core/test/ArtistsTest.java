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
            
            Artist artist = null;
            
            for (Model ar : artists.getCollection()) {
                Artist art = (Artist) ar;
                assertNotNull(art);
                artist = art;
            }

            assertNotNull(artist);
            
            Artist ar = (Artist) artists.get(artist.getToken());
            assertNotNull( ar );
            assertSame( ar, artist );
            
            List<Artist> list = new ArrayList<Artist>();
            artists.setCollection( list );
            
            assertNotNull( artists.getCollection() );
            assertSame( list, artists.getCollection() );
            
            ar = (Artist) artists.get(ar.getToken());
            assertNull( ar );

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
            Artist ar = (Artist) artists.get(0);
            assertNotNull(ar);
            
            Tracks trs = (Tracks) ar.getTracks();
            assertNotNull(trs);
            
            Track tr = (Track) trs.get(0);
            assertNotNull(tr);
            
            Track tr2 = ar.getTrack(tr.getUuid());
            
            assertNotNull( tr2 );
            assertSame(tr, tr2);

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
        artists = (Artists) user.getArtists(false);
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
