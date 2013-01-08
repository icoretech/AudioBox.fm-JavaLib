package fm.audiobox.core.parsers;

import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fm.audiobox.core.models.AbstractCollectionEntity;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IEntity;


/**
 * This is an utility class used for parsing a JSON string.
 * <br />
 * It also populates the {@link IEntity} associated with this instance
 */
public class JParser {

  private static Logger log = LoggerFactory.getLogger(JParser.class);

  private IEntity entity;

  private IConfiguration config;

  private long startParse = 0;

  public JParser(IEntity entity){
    this.entity = entity;
    this.config = entity.getConfiguration();
  }

  /**
   * Parses given string and returns the associated entity
   * 
   * @param jstr the JSON string
   * @return the {@link IEntity} associated with this instance
   */
  public IEntity parse ( String jstr ){
    startParse = System.currentTimeMillis();
    JsonObject jobj = new JsonParser().parse( jstr ).getAsJsonObject();
    
    parse( jobj );
    
    if (log.isDebugEnabled()) {
      log.debug("Json parsed in " + (System.currentTimeMillis() - startParse) + "ms (" + this.entity.getNamespace() + ")");
    }
    return entity;
  }
  
  
  /**
   * Parses given string and returns the associated entity
   * 
   * @param jstr the InputStreamReader representing a JSON string
   * @return the {@link IEntity} associated with this instance
   */
  public IEntity parse ( InputStreamReader isr ){
    startParse = System.currentTimeMillis();

    JsonObject jobj = new JsonParser().parse(isr).getAsJsonObject();

    parse(jobj);
    
    if (log.isDebugEnabled()) {
      log.debug("Json parsed in " + (System.currentTimeMillis() - startParse) + "ms (" + this.entity.getNamespace() + ")");
    }
    return entity;
  }
  
  
  /**
   * Parses given JsonObject and returns the associated entity
   * 
   * @param jstr the JsonObject
   * @return the {@link IEntity} associated with this instance
   */
  @SuppressWarnings("unchecked")
  public IEntity parse ( JsonObject jobj ){
    
    if ( log.isDebugEnabled() ) {
      log.debug("Response from server is: " + jobj.toString() );
    }
    
    
    if( jobj.has( entity.getTagName() ) ){
      if( jobj.get(entity.getTagName()).isJsonObject() ){
        fillJsonObject(jobj.get(entity.getTagName()).getAsJsonObject(), this.entity);
      } else if( jobj.get(entity.getTagName()).isJsonArray() ){
        fillJsonArray(jobj.get(entity.getTagName()).getAsJsonArray(), (AbstractCollectionEntity<IEntity>)this.entity);
      } else {
        log.error("'" + entity.getTagName() + "' is not an object nor an array");
      }
    } else {
      log.warn("json response doesn't contain the entity tag name '" + entity.getTagName() + "'" );
    }
    return entity;
  }
  
  
  
  @SuppressWarnings("unchecked")
  private IEntity fillJsonArray (JsonArray jarr,AbstractCollectionEntity<IEntity> entity){

    for( JsonElement jelm:jarr ){
      if( jelm.isJsonObject() ){
        IEntity subentity = this.config.getFactory().getEntity( entity.getSubTagName() , this.config);
        if( subentity != null){
          subentity = fillJsonObject( jelm.getAsJsonObject() , subentity);
          invokemethod(entity, entity.getSubTagName() , subentity);
        } else {
          log.warn( "The configuration doesn't contain the entity " + entity.getSubTagName() );
        }
      } else if( jelm.isJsonArray() ){
        IEntity subentity = this.config.getFactory().getEntity( entity.getSubTagName() , this.config ) ;
        if(subentity != null){
          subentity = fillJsonArray( jelm.getAsJsonArray() , (AbstractCollectionEntity<IEntity>) subentity );
          invokemethod(entity, entity.getSubTagName() , subentity);
        } else {
          log.warn( "The configuration doesn't contain the entity " + entity.getSubTagName() );
        }
      } else if( jelm.isJsonPrimitive() ){
        invokemethod(entity, entity.getSubTagName() , jelm);
      }
    }

    return entity;
  }

  
  @SuppressWarnings("unchecked")
  private IEntity fillJsonObject (JsonObject jobj,IEntity entity) {

    for(Entry<String, JsonElement> elem:jobj.entrySet()){
      if(elem.getValue().isJsonObject()){
        IEntity subentity = this.config.getFactory().getEntity(elem.getKey(), this.config);
        if(subentity != null){
          subentity = fillJsonObject(elem.getValue().getAsJsonObject(), subentity );
          invokemethod(entity, elem.getKey(), subentity);  
        } else {
          log.warn( "The configuration doesn't contain the entity " + elem.getKey() + " as object");
        }

      } else if( elem.getValue().isJsonArray() ){
        IEntity subentity = this.config.getFactory().getEntity( elem.getKey() , this.config);
        if(subentity != null){
          subentity = fillJsonArray( elem.getValue().getAsJsonArray() , (AbstractCollectionEntity<IEntity>) subentity );
          invokemethod(entity, elem.getKey() , subentity);
        } else {
          log.warn( "The configuration doesn't contain the entity " + elem.getKey()  + " as array" );
        }
      } else if(elem.getValue().isJsonPrimitive()){
        invokemethod(entity, elem.getKey(), elem.getValue());
      }

    }
    return entity;
  }

  @SuppressWarnings("deprecation")
  private void invokemethod(IEntity entity,String _method , Object value){
    Method method = null;
    try{
      method =  entity.getSetterMethod(_method);
    }catch (SecurityException e) {
      log.error("No accessible method found under key: " + _method, e);
      return;
    }
    if (method == null) {
      log.warn(entity.getClass().getName() + " doesn't contain the requested method for tag: " + _method);
      return;
    }

    try{
      
      if (method.getParameterTypes().length == 1) {
        Class<?> paramType = method.getParameterTypes()[0];

        if (paramType.equals(int.class)) {
          try{
            method.invoke(entity, ((JsonElement)value).getAsInt());
          }catch (NumberFormatException e) {
            method.invoke(entity, 0);
          }
        } else if (paramType.equals(long.class)) {
          try{
            method.invoke(entity, ((JsonElement)value).getAsLong());
          }catch (NumberFormatException e) {
            method.invoke(entity, 0);
          }
        } else if (paramType.equals(boolean.class)) {
          method.invoke(entity, ((JsonElement)value).getAsBoolean());
        } else if (paramType.equals(String.class)) {
          method.invoke(entity, ((JsonElement)value).getAsString());
        } else {
          /*
           * In this case we have to check if method parameter is an IEntity
           */
          boolean isEntity = false;
          try {
            isEntity = paramType.asSubclass(IEntity.class) != null;
          } catch (ClassCastException e) {
            ; // silent fail
          }

          if (isEntity) {
            /*
             * Method parameter seems to be an IEntity. We invoke method
             * passing current IEntity
             */
            method.invoke(entity, value);
          }

        }
      }
    }catch (IllegalArgumentException e) {
      log.error("An error while invoking method '" + method + "' for tag: " + _method, e);
    } catch (IllegalAccessException e) {
      log.error("An error while invoking method '" + method + "' for tag: " + _method, e);
    } catch (InvocationTargetException e) {
      log.error("An error while invoking method '" + method + "' for tag: " + _method, e);
    }
  }
}
