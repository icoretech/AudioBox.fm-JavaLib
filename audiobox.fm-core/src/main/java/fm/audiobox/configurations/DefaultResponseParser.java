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
import fm.audiobox.core.parsers.JParser;
import fm.audiobox.core.parsers.XmlParser;
import fm.audiobox.interfaces.IConfiguration.ContentFormat;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IResponseHandler;

public class DefaultResponseParser implements IResponseHandler  {

  private static Logger log = LoggerFactory.getLogger(DefaultResponseParser.class);
  
  public String parse(InputStream in, IEntity destEntity, ContentFormat format ) throws ServiceException {
    
    if ( format == ContentFormat.XML ) {
        
      this.parseAsXml(in, destEntity );
    
    } else if ( format == ContentFormat.JSON ) {
      
      this.parseAsJson(in, destEntity );
      
    } else if ( format == ContentFormat.TXT ) {
      
      return this.parseAsText(in, destEntity );
      
    } else if ( format == ContentFormat.BINARY ) {
      
      this.parseAsBinary(in, destEntity );
      
    }
    
    return "";
      
  }
  

  @Override
  public void parseAsXml(InputStream inputStream, IEntity destEntity) throws ServiceException {
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

  @Override
  public void parseAsJson(InputStream inputStream, IEntity destEntity) throws ServiceException {
  
    JParser parser = new JParser(destEntity);
    parser.parse(new InputStreamReader(inputStream));
  }

  @Override
  public String parseAsText(InputStream inputStream, IEntity destEntity) throws ServiceException {
    try {
      return Response.streamToString(inputStream);
    } catch (IOException e) {
      log.error("An error occurred while parsing response content", e);
      throw new ServiceException( e.getMessage() );
    }
  }

  @Override
  public void parseAsBinary(InputStream inputStream, IEntity destEntity) throws ServiceException {
    throw new ServiceException("Json parser is not implemented yet");
  }
  
}
