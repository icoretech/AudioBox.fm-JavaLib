package fm.audiobox.api.interfaces;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

public interface ResponseHandler {

    public Object handleResponse(HttpResponse response, String restAction) throws ClientProtocolException, IOException, IllegalStateException, SAXException, ParserConfigurationException;
    
}
