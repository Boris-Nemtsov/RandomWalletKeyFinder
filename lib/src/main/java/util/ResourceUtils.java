package util;

import java.io.FileOutputStream;
import java.io.InputStream;

public class ResourceUtils {
	public static void copyResource(String resourceName, String outputPath) {
		InputStream resourceStream;
		FileOutputStream outputStream;
		
		try {
			resourceStream = ClassLoader.getSystemResourceAsStream(resourceName);
			outputStream = new FileOutputStream(outputPath);
			
			while (resourceStream.available() > 0) {
				outputStream.write(resourceStream.read());
			}
		} catch (Exception e) {
			System.out.println("Cannot create default config : " + resourceName);
			System.exit(-1);
		}
	}
}
