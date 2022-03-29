package util;

import java.io.File;

import com.google.common.base.Charsets;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

public class Log {
	public static void writeFoundWallet(String message) {
		try {
			Files.asCharSink(new File("log/FOUND_WALLET.log"), Charsets.UTF_8, FileWriteMode.APPEND).write(message + "\n");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to save found wallet's address. Message : \n" + message);
		}
	}
}
