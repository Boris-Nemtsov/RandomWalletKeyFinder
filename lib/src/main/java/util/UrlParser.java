package util;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;

public class UrlParser {
	private static Scanner openAndGetParser(String targetUrl, int retryCount) {
		URL url = null;
		URLConnection connection = null;
		
		try {
			url = new URL(targetUrl);
			connection = url.openConnection();
			connection.connect();
		
			return new Scanner(connection.getInputStream(), "UTF-8");
		} catch (Exception e) {
			if (retryCount <= 0) {
				e.printStackTrace();
				return new Scanner("");
			}
			
			return openAndGetParser(targetUrl, retryCount--);
		}
	}

	@SuppressWarnings("unchecked")
	private static <ReturnType extends Response<String>> ReturnType openAndGetParser(Request<?, ReturnType> provider, int retryCount) {
		try {
			return provider.send();
		} catch (Exception e) {
			if (retryCount <= 0) {
				Response<String> resp = new Response<String>();
				resp.setResult("0");
				
				return (ReturnType) resp;
			}
			
			return openAndGetParser(provider, retryCount--);
		}
	}
	
	public static Scanner openAndGetParser(String targetUrl) {
		return openAndGetParser(targetUrl, 5);
	}

	public static <ReturnType extends Response<String>> ReturnType openAndGetParser(Request<?, ReturnType> provider) {
		return openAndGetParser(provider, 5);
	}
}
