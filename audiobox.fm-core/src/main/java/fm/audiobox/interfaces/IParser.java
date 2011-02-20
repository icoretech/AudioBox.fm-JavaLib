package fm.audiobox.interfaces;

import java.io.InputStream;

public interface IParser {


  
  /**
   * Populates a given {@link IEntity} from a given {@link String}
   * @param entity
   * @param data
   */
  public void populateEntity(IEntity entity, String data);
  
  
  /**
   * Populates a given {@link IEntity} from a given {@link InputStream}
   * @param entity
   * @param data
   */
  public void populateEntity(IEntity entity, InputStream data);
  
  
  
}