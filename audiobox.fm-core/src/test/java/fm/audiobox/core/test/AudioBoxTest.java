package fm.audiobox.core.test;

import org.junit.Test;

import fm.audiobox.configurations.DefaultFactory;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;
import fm.audiobox.interfaces.IFactory;

public class AudioBoxTest extends AbxTestCase {
  
  @Test
  public void audioboxShouldBeCorrectlyInstantiated() {
    IConfiguration config = abx.getConfiguration();
    
    assertNotNull( config.getClass().asSubclass( IConfiguration.class ) );
    assertSame( config.getApplicationName(), APPLICATION_NAME );
    assertNotNull( config.getFactory().getClass().asSubclass( IFactory.class ) );
    assertEquals( config.getVersion(), "1.0.0" );
    assertNotNull( config.getHttpMethodType().asSubclass( IConnectionMethod.class ) );
    assertSame( config.getRequestFormat(), IConfiguration.ContentFormat.JSON );
    
    IFactory factory = config.getFactory();
    assertSame( factory.getClass(), DefaultFactory.class );
    assertNotNull( factory.getConnector().getClass().asSubclass( IConnector.class ) );
    
    assertNull( abx.getUser() );
  }

}
