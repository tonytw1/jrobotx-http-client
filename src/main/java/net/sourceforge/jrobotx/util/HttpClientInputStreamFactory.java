package net.sourceforge.jrobotx.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

public class HttpClientInputStreamFactory implements URLInputStreamFactory {

	private static final Logger LOG = Logger.getLogger(HttpClientInputStreamFactory.class);

	private static final int HTTP_TIMEOUT = 10000;
		
	public InputStream openStream(URL url) throws IOException {		
		return fetchUrl(url);
	}

	private InputStream fetchUrl(URL url) throws IOException, ClientProtocolException {
		HttpClient httpClient = setupClient();
		
		HttpGet get = new HttpGet(url.toString());
		HttpResponse response = httpClient.execute(get);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			LOG.info("Fetching url: " + url);
			return response.getEntity().getContent();			
		} else {
			LOG.warn("Request failed: " + response.getStatusLine().getStatusCode());
		}
		return null;
	}

	private HttpClient setupClient() {
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter("http.socket.timeout", new Integer(HTTP_TIMEOUT));
		return httpClient;
	}
	
}
