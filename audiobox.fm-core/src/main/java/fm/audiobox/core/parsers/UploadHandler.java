package fm.audiobox.core.parsers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.protocol.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.configurations.MimeTypes;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IConnector.IConnectionMethod;


/**
 * This class is used for uploading media file to AudioBox.fm Cloud
 */
public class UploadHandler extends FileBody {
  
  private static final Logger log = LoggerFactory.getLogger( UploadHandler.class );
  private int chunk = IConnector.DEFAULT_CHUNK;
  
  protected volatile transient IConnectionMethod req = null;
  
  public UploadHandler(File file) {
    super( file, MimeTypes.getMime( file )  );
  }
  
  public UploadHandler(File file, int chunk) {
    this(file);
    this.chunk = chunk;
  }
  
  
  public void setRequestMethod(IConnectionMethod r) {
    this.req = r;
  }
  
  public void writeTo(OutputStream out) throws IOException {
    InputStream in = this.getInputStream();

    byte[] buf = new byte[ this.chunk ];
    int len = 0, step = 0;
    try {
      boolean canUpload = true;
      while( canUpload && (len = in.read(buf) ) > 0 ){
        
        if ( step++ < 2 ) {
          boolean hasResponse = ((org.apache.http.HttpClientConnection)this.req.getRequestContext().getAttribute(ExecutionContext.HTTP_CONNECTION)).isResponseAvailable(2000);
          if ( hasResponse ){
            break;
          }
        }
        canUpload = this.write(out, buf, len );
        if ( !canUpload ) {
          log.warn("Upload has been interrupted");
        }
      }
      
      out.flush();
    
    } catch (IOException e) {
    
      throw new ServiceException( e );
    
    } finally {
      // The output stream is automatically closed
      //  out.close();
      in.close();
    }
  }

  
  /**
   * This method writes given buffer to {@link OutputStream}
   * <br />
   * You can overwrite this method in order to simply manage the {@code upload} by yourself.
   * 
   * @param out the {@link OutputStream} which write data to
   * @param buffer the {@code byte array} to be write
   * @param length the {@code number of bytes} will be write
   * 
   * @return {@code boolean}. Returning {@code false} <b>it stops the upload</b>
   * 
   * @throws IOException if any operation error occurs
   */
  public synchronized boolean write(OutputStream out, byte[] buffer, int length) throws IOException {
    out.write( buffer, 0, length );
    return true;
  }

}
