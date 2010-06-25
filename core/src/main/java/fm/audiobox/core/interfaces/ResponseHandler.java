package fm.audiobox.core.interfaces;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import fm.audiobox.core.exceptions.LoginException;

/**
 * <p>ResponseHandler interface.</p>
 *
 * @author keytwo
 * @version $Id: $
 */
public interface ResponseHandler {

    /**
     * <p>handleResponse</p>
     *
     * @param response a {@link org.apache.http.HttpResponse} object.
     * @param restAction a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     * @throws org.apache.http.client.ClientProtocolException if any.
     * @throws java.io.IOException if any.
     * @throws java.lang.IllegalStateException if any.
     * @throws fm.audiobox.core.exceptions.LoginException if any.
     */
    public String handleResponse(HttpResponse response, String restAction) throws ClientProtocolException, IOException, IllegalStateException, LoginException;
    
}
