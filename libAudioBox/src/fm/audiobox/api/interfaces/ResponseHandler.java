package fm.audiobox.api.interfaces;

import org.apache.http.HttpResponse;

public interface ResponseHandler {

    public Object handleResponse(HttpResponse response, String restAction);
}
