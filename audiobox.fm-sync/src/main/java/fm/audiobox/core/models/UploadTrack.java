package fm.audiobox.core.models;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;

public class UploadTrack extends Track {

    public UploadTrack() {
        super();
    }

    public UploadTrack( FileBody fb, AudioBoxClient abc ){
        super(fb);
        this.setConnector( abc.getMainConnector() );
    }

    public void upload() throws ServiceException, LoginException {
        pConnector.execute(Tracks.END_POINT , null, null, this, HttpPost.METHOD_NAME);
    }


    @Override
    public String parseResponse( InputStream input, Header contentType ) throws IOException {

        String response = "";

        byte[] bytes = new byte[CHUNK];
        int read;
        StringBuffer result = new StringBuffer();
        while( ( read = input.read(bytes) ) != -1  ){
            result.append( new String( bytes, 0, read )  );
        }

        response = result.toString().trim();

        this.setUuid( response );

        return response;
    }
}
