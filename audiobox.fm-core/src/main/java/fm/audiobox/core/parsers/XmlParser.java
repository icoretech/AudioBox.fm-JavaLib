package fm.audiobox.core.parsers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IEntity;


public class XmlParser extends DefaultHandler {

  private static final Logger log = LoggerFactory.getLogger(XmlParser.class);
  
  private IEntity entity;
  private IConfiguration config;
  
  private Stack<IEntity> stack;
  private StringBuffer bodyContent;
  
  public XmlParser(IEntity entity, IConfiguration config){
    this.entity = entity;
    this.config = config;
  }
  
  
  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    if ( this.bodyContent != null )
      this.bodyContent.append( new String(ch,start,length) );
    super.characters(ch, start, length);
  }

  @Override
  public void endDocument() throws SAXException {
    this.stack.clear();
    this.stack = null;
    this.bodyContent = null;
    super.endDocument();
  }

  @Override
  public void startDocument() throws SAXException {
    this.stack = new Stack<IEntity>();
    this.stack.push( this.entity );
    super.startDocument();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    IEntity _entity  = this.stack.peek();
    if ( _entity == null ){
      _entity = this.entity;
    }
    
    
    
    if ( localName.trim().equals("") ) {
      localName = qName;
    }
    
    
    Method setterMethod = null;
    try {
       setterMethod = _entity.getSetterMethod(localName);
    } catch (SecurityException e) {
      log.error("No accessible method found under key: " + localName, e);
    } catch (NoSuchMethodException e) {
      log.error("No method found under key: " + localName, e);
    }
    
    if ( setterMethod == null ){
      // no method found
      log.warn("No method found for tag: " + localName );
      this.stack.push(null);  // Prevent bug: we have to add an entity as null pointer
      return;
      
    } else if ( setterMethod.getParameterTypes().length == 1 ){
      // method found correctly
      Class<?> klass = setterMethod.getParameterTypes()[0]; 
      if (  klass.equals( IEntity.class )  ) {
        // method represents an Entity object
        
        @SuppressWarnings("unchecked")
        IEntity newEntity = this.config.getFactory().getEntity( (Class<? extends IEntity>) klass, this.config);
        try {
          
          // Invoke method passing new Entity to be set into parent!
          setterMethod.invoke(_entity, newEntity);

          // New entity becomes next root entity 
          this.stack.push( newEntity );
          
        } catch (IllegalArgumentException e) {
          log.error("An Error occurred while incoking method: " + setterMethod.toString(), e);
        } catch (IllegalAccessException e) {
          log.error("An Error occurred while incoking method: " + setterMethod.toString(), e);
        } catch (InvocationTargetException e) {
          log.error("An Error occurred while incoking method: " + setterMethod.toString(), e);
        }
        
      } else {
        /*
         *  Method represents a Entity field
         *  Only in this case we have to instance a new bodyContent ready to be populated by 'characters' method
         */
        this.bodyContent = new StringBuffer("");
        
        /*
         *  Prevent bug: we have to re-push the entity 
         *  because 'endElement' method always performs a 'pop' action on stack
         */
        this.stack.push( _entity );
      }
      
    } else {
      // Method has no arguments or more than 1 argument. In this case we have to skip the tag value
      log.info("Method seems to have no arguments or more than one argument");
      log.warn("No valid method found for tag: " + localName);
    }
    
  }
  
  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    
    IEntity currentEntity = this.stack.pop();
    String value = "";
    
    if ( this.bodyContent != null )
      value = this.bodyContent.toString().replace("\n","").replace("\r", "").replace("\t", "");
    
    if ( currentEntity != null ) {
      
      
      if ( localName.trim().equals("") ) {
        localName = qName;
      }
      
      
      Method setterMethod = null;
      try {
         setterMethod = currentEntity.getSetterMethod(localName);
      } catch (SecurityException e) {
        log.error("No accessible method found under key: " + localName, e);
      } catch (NoSuchMethodException e) {
        log.error("No method found under key: " + localName, e);
      }
      
      if ( setterMethod == null ){
        // no method found
        log.warn("No method found for tag: " + localName );
        this.stack.push(null);  // Prevent bug: we have to add an entity as null pointer
        return;
        
      } else if ( setterMethod.getParameterTypes().length == 1 ){
        
        Class<?> paramType = setterMethod.getParameterTypes()[0];
        try {
          
          if ( paramType.equals( int.class ) ){
            
              setterMethod.invoke(currentEntity, Integer.parseInt( value ) );
            
          } else if ( paramType.equals( long.class ) ){
            
            setterMethod.invoke(currentEntity, Long.parseLong( value ) );
            
          } else if ( paramType.equals( boolean.class ) ){
            
            setterMethod.invoke(currentEntity, Boolean.parseBoolean( value ) );
            
          } else if ( paramType.equals( String.class ) ){
            
            setterMethod.invoke(currentEntity, value );
            
          }
          
        } catch (NumberFormatException e) {
          log.warn("Method cannot be invoked for tag: " + localName, e);
        } catch (IllegalArgumentException e) {
          log.error("An error while invoking method '" + setterMethod + "' for tag: " + localName, e);
        } catch (IllegalAccessException e) {
          log.error("An error while invoking method '" + setterMethod + "' for tag: " + localName, e);
        } catch (InvocationTargetException e) {
          log.error("An error while invoking method '" + setterMethod + "' for tag: " + localName, e);
        }
          
      }
      
      
    } else {
      
      // An error might have occurred
      // do nothing
      log.debug("An error might have occurred while parsing tag: " + localName);
      
    }
    
    

    // blank bodyContent
    this.bodyContent = null;
    // this.stack.pop();  NOTE: already invoked
    super.endElement(uri, localName, qName);
  }
  

}
