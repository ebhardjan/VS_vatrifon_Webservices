package ch.ethz.inf.vs.a2.sensor;

import android.util.Log;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import ch.ethz.inf.vs.a2.http.RemoteServerConfiguration;
import ch.ethz.inf.vs.a2.http.SimpleHttpClientFactory;

/**
 * Created by jan on 15.10.15.
 */
public class XmlSensor extends AbstractSensor {
    @Override
    protected void setHttpClient() {
        this.httpClient = SimpleHttpClientFactory.getInstance(SimpleHttpClientFactory.Type.LIB);
    }

    @Override
    public double parseResponse(String response) {
        Log.d("###", "parseResponse");
        Log.d("###", "res:"+response);
        return 0;
    }

    @Override
    public void getTemperature() throws NullPointerException {
        String host = RemoteServerConfiguration.HOST;
        int port = RemoteServerConfiguration.SOAP_PORT;
        String path = RemoteServerConfiguration.SPOT_3_URL;

        HttpPost post = new HttpPost("http://"+host+":"+port+path);

        StringBuilder sb = new StringBuilder();
        // todo: do this later better in an actual xml!
        // http://stackoverflow.com/questions/2559948/android-sending-xml-via-http-post-soap
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        sb.append("S:Header/>");
        sb.append("<S:Body>");
        sb.append("<ns2:getSpot xmlns:ns2 = \"http://webservices.vslecture.vs.inf.ethz.ch/\">");
        sb.append("<id>Spot3</id>");
        sb.append("</ns2:getSpot>");
        sb.append("</S:Body>");
        sb.append("</S:Envelope>");
        StringEntity se = null;
        try {
            se = new StringEntity(sb.toString(), HTTP.UTF_8);
            se.setContentType("text/xml");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.setHeader("Content-Type", "application/soap+xml;charset=UTF-8");
        post.addHeader("Accept", "text/xml");
        post.setEntity(se);
        Log.d("###", post.toString());
        Log.d("###", post.getURI().toString());
        Log.d("###", Arrays.deepToString(post.getAllHeaders()));

        new AsyncWorker().execute(post);
    }
}