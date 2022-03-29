package util;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

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
	
	public static Scanner openAndGetParser(String targetUrl) {
		return openAndGetParser(targetUrl, 5);
	}
}
