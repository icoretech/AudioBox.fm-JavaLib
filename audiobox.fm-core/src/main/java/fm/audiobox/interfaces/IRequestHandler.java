package fm.audiobox.interfaces;

public interface IRequestHandler {

  
  
  public String serialize(IEntity entity, IConfiguration.ContentFormat format);
  
  
  public String serializeJson(IEntity entity, IConfiguration.ContentFormat format);
  
  public String serializeXml(IEntity entity, IConfiguration.ContentFormat format);
  
  public String serializeText(IEntity entity, IConfiguration.ContentFormat format);
  
  public String serializeBinary(IEntity entity, IConfiguration.ContentFormat format);
  
}

