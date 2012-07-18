package fm.audiobox.core.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.configurations.DefaultResponseParser;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.interfaces.IConnector;
import fm.audiobox.interfaces.IEntity;

public class DownloadHandler extends DefaultResponseParser {

  private static final Logger log = LoggerFactory.getLogger( DownloadHandler.class );
  
  protected FileOutputStream fileOutput;
  private int chunk = IConnector.DEFAULT_CHUNK;
  
  public DownloadHandler(File file, int chunk) {
    try {
      this.fileOutput = new FileOutputStream(file);
    } catch (FileNotFoundException e) {
      // Silently fail (we throw exception in method below)
      log.error("File not found: " + file.getAbsolutePath() );
    }
    this.chunk = chunk;
  }
  
  public DownloadHandler(FileOutputStream fos, int chunk) {
    this.fileOutput = fos;
    this.chunk = chunk;
  }
  
  
  public void parseAsBinary(InputStream inputStream, IEntity destEntity) throws ServiceException {
    
    if ( this.fileOutput == null ) {
      throw new ServiceException("No output file found, maybe doesn't exist");
    }
    
    byte[] buf = new byte[ this.chunk ];
    int len = 0;
    try {
      boolean canDownload = true;
      while( canDownload && (len = inputStream.read(buf) ) > 0 ){
        canDownload = this.write(buf, len, destEntity );
        if ( !canDownload ) {
          log.warn("Download has been interrupted");
        }
      }
      this.fileOutput.flush();
      
    } catch (IOException e) {
      throw new ServiceException( e );
    
    } finally {
      
      try {
        this.fileOutput.close();
      } catch (IOException e) {
        log.error("Unable to close the output stream on the file", e);
      }
      try {
        inputStream.close();
      } catch (IOException e) {
        log.error("Unable to close the input stream", e);
      }
      
    }
  }
  
  
  public synchronized boolean write( byte[] buffer, int length, IEntity destEntity ) throws IOException {
    this.fileOutput.write( buffer, 0, length );
    return true;
  }
  
}
