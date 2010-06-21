package fm.audiobox.api.interfaces;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

import fm.audiobox.api.exceptions.LoginException;

public interface ResponseHandler {

    public String handleResponse(HttpResponse response, String restAction) throws ClientProtocolException, IOException, IllegalStateException, LoginException;
    
}
