package fm.audiobox.core.parsers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.mime.content.FileBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.configurations.MimeTypes;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConnector;

public class UploadHandler extends FileBody {
  
  
  private static final Logger log = LoggerFactory.getLogger( UploadHandler.class );
  private int chunk = IConnector.DEFAULT_CHUNK;
  

  public UploadHandler(File file) {
    super( file, MimeTypes.getMime( file )  );
  }
  
  public UploadHandler(File file, int chunk) {
    this(file);
    this.chunk = chunk;
  }
  
  
  
  public void writeTo(OutputStream out) throws IOException {
    InputStream in = this.getInputStream();
    
    byte[] buf = new byte[ this.chunk ];
    int len = 0;
    try {
      boolean canUpload = true;
      while( canUpload && (len = in.read(buf) ) > 0 ){
        canUpload = this.write(out, buf, len );
        if ( !canUpload ) {
          log.warn("Upload has been interrupted");
        }
      }
      
      out.flush();
    
    } catch (IOException e) {
    
      throw new ServiceException( e );
    
    } finally {
      out.close();
      in.close();
    }
  }

  
  
  public synchronized boolean write(OutputStream out, byte[] buffer, int length) throws IOException {
    out.write( buffer, 0, length );
    return true;
  }

}
