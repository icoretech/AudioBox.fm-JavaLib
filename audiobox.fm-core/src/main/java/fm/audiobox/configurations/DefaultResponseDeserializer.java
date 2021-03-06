package fm.audiobox.configurations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.parsers.DownloadHandler;
import fm.audiobox.core.parsers.JParserStreaming;
import fm.audiobox.core.parsers.XmlParser;
import fm.audiobox.interfaces.IConfiguration.ContentFormat;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;



/**
 * This is the default response deserializer used for parsing the AudioBox.fm response body
 */
@SuppressWarnings("deprecation")
public class DefaultResponseDeserializer implements IResponseHandler  {

  private static Logger log = LoggerFactory.getLogger(DefaultResponseDeserializer.class);
  
  public String deserialize(InputStream in, IEntity destEntity, ContentFormat format ) throws ServiceException {
    
    destEntity.startLoading();
    
    if ( format == ContentFormat.XML ) {
        
      this.deserializeXml(in, destEntity );
    
    } else if ( format == ContentFormat.JSON ) {
      
      this.deserializeJson(in, destEntity );
      
    } else if ( format == ContentFormat.TXT ) {
      
      return this.deserializeText(in, destEntity );
      
    } else if ( format == ContentFormat.BINARY ) {
      
      this.deserializeBinary(in, destEntity );
      
    }
    
    destEntity.endLoading();
    
    return "";
      
  }
  

  public void deserializeXml(InputStream inputStream, IEntity destEntity) throws ServiceException {
    try {

      SAXParserFactory spf = SAXParserFactory.newInstance();
      SAXParser sp = spf.newSAXParser();
      XMLReader xr = sp.getXMLReader();
      xr.setContentHandler( new XmlParser(destEntity) );
      xr.parse( new InputSource( inputStream ) );

    } catch (ParserConfigurationException e) {
      log.error("An error occurred while instantiating XML Parser", e);
      throw new ServiceException(ServiceException.GENERIC_ERROR, e.getMessage() );
    } catch (SAXException e) {
      log.error("An error occurred while instantiating XML Parser", e);
      throw new ServiceException(ServiceException.GENERIC_ERROR, e.getMessage() );
    } catch (IllegalStateException e) {
      log.error("An error occurred while parsing response", e);
      throw new ServiceException(ServiceException.GENERIC_ERROR, e.getMessage() );
    } catch (IOException e) {
      log.error("An error occurred while parsing response", e);
      throw new ServiceException(ServiceException.GENERIC_ERROR, e.getMessage() );
    }
  }

  public void deserializeJson(InputStream inputStream, IEntity destEntity) throws ServiceException {
  
    JParserStreaming parser = new JParserStreaming(destEntity);
//    JParser parser = new JParser(destEntity);
    parser.parse(new InputStreamReader(inputStream));
  }

  public String deserializeText(InputStream inputStream, IEntity destEntity) throws ServiceException {
    try {
      return Response.streamToString(inputStream);
    } catch (IOException e) {
      log.error("An error occurred while parsing response content", e);
      throw new ServiceException( e.getMessage() );
    }
  }

  /**
   * This method is not implemented by this class
   * <br />
   * <b>Use {@link DownloadHandler} instead</b>
   */
  @Deprecated
  public void deserializeBinary(InputStream inputStream, IEntity destEntity) throws ServiceException {
    throw new ServiceException("Binary parser is not implemented yet");
  }
  
}
