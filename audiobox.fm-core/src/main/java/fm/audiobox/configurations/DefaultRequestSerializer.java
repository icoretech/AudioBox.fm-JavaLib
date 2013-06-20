package fm.audiobox.configurations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fm.audiobox.core.models.AbstractCollectionEntity;
import fm.audiobox.core.models.AbstractEntity;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.ContentFormat;
import fm.audiobox.interfaces.IEntity;
import fm.audiobox.interfaces.IRequestHandler;

/**
 * This is the default serializer used for sending data to AudioBox.fm server
 * <br />
 * <b>This class is not used yet</b>
 */
public class DefaultRequestSerializer implements IRequestHandler {
  
  private static final Logger log = LoggerFactory.getLogger( DefaultRequestSerializer.class );

  public String serialize(IEntity entity, ContentFormat format) {
    
    
    if ( format == IConfiguration.ContentFormat.JSON ) {
      this.serializeJson(entity);
      
    } else if ( format == IConfiguration.ContentFormat.TXT ) {
      this.serializeText(entity);
      
    } else if ( format == IConfiguration.ContentFormat.XML ) {
      this.serializeXml(entity);
      
    } else if ( format == IConfiguration.ContentFormat.BINARY ) {
      this.serializeBinary(entity);
      
    }
    
    
    return "";
  }

  @SuppressWarnings({ "unchecked", "deprecation" })
  public JsonElement serializeJson(IEntity entity) {
    JsonObject json = new JsonObject();
    
    if ( entity instanceof AbstractCollectionEntity ) {
      
      // Collection
      JsonArray jarray = new JsonArray();
      AbstractCollectionEntity<AbstractEntity> aEntities = (AbstractCollectionEntity<AbstractEntity>) entity;
      for( AbstractEntity e : aEntities ) {
        jarray.add( this.serializeJson( e ) );
      }
      return jarray;
      
    } else {
      
      // Single entity
      
      Map<String, Method> getterMethods = entity.getGetterMethods();
      
      Set<String> fields = getterMethods.keySet();
      Iterator<String> fieldsIterator = fields.iterator(); 
      while( fieldsIterator.hasNext() ) {
        
        String field = fieldsIterator.next();
        Method method = getterMethods.get( field );
        
        Class<?> returnType = method.getReturnType();
        
        try {
        
          if ( returnType.equals(int.class) ) {
            json.addProperty( field , (Integer) method.invoke(entity) );
          
          } else if ( returnType.equals(long.class) ) {
            
            json.addProperty( field , (Long) method.invoke(entity) );
            
          } else if ( returnType.equals(boolean.class) ) {
            
            json.addProperty( field , (Boolean) method.invoke(entity) );
            
          } else if ( returnType.equals(String.class) || returnType.isEnum() ) {
            
            Object res = method.invoke(entity);
            json.addProperty( field , res != null ? res.toString() : (String) res );
            
          } else if ( returnType.asSubclass( IEntity.class ) != null ) {
            
            IEntity subEntity = (IEntity) method.invoke(entity);
            if ( subEntity != null ) {
              json.add( field, this.serializeJson(subEntity) );
            }
            
          }
        
        } catch (IllegalArgumentException e) {
          log.warn("Cannot invoke method for '" + field + "': " + e.getMessage() );
        } catch (IllegalAccessException e) {
          log.warn("Cannot invoke method for '" + field + "': " + e.getMessage() );
        } catch (InvocationTargetException e) {
          log.warn("Cannot invoke method for '" + field + "': " + e.getMessage() );
        }
        
      }
    }
    
    return json;
  }

  public Object serializeXml(IEntity entity) {
    return null;
  }

  public String serializeText(IEntity entity) {
    return null;
  }

  public Object serializeBinary(IEntity entity) {
    return null;
  }

}
