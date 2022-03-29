package util;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.sqlite.SQLiteConnection;

public class Peeked {
	private static Object fileCreateLock = new Object();
	
	private SQLiteConnection sqlConnection;
	
	public Peeked(String dbFileName) {
		try {
			dbFileName = "db/" + dbFileName;
			Class.forName(org.sqlite.JDBC.class.getName());
			
			synchronized (fileCreateLock) {
				if (new File("db").exists() == false) {
					Files.createDirectory(Paths.get("db"));
				}
				
				if (new File(dbFileName).exists() == false) {
					Files.copy(Paths.get("db/DEFAULT_TABLE.db"), Paths.get(dbFileName), StandardCopyOption.REPLACE_EXISTING);
				}
			}

			sqlConnection = (SQLiteConnection)DriverManager.getConnection("jdbc:sqlite:" + dbFileName);
			sqlConnection.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to load " + dbFileName + "\n" + dbFileName + " won't save anymore.");
		}
	}

	public void savePeeked(BigInteger privateKey) {
		PreparedStatement insertJob = null;
		
		try {
			insertJob = sqlConnection.prepareStatement("INSERT INTO T (PRK) VALUES (?)");
			insertJob.setObject(1, privateKey.toString());
			insertJob.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (insertJob != null) {
					insertJob.close();
				}
			} catch (Exception e) {}			
		}
	}

	public boolean isPeeked(BigInteger privateKey) {
		PreparedStatement selectJob = null;
		
		try {
			selectJob = sqlConnection.prepareStatement("SELECT PRK FROM T WHERE PRK=? LIMIT 1");
			selectJob.setObject(1, privateKey.toString());
			
			return selectJob.executeQuery().next();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (selectJob != null) {
					selectJob.close();
				}
			} catch (Exception e) {}
		}
		
		return false;
	}
}
