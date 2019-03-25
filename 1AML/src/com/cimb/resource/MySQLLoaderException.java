package com.cimb.resource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

public class MySQLLoaderException extends Exception {
	static ResourceBundle resourceBundle = ResourceBundle.getBundle("1aml");
	private static final long serialVersionUID = 1L;
	private static BufferedWriter bufferedWriter;
	private static final String filePath = resourceBundle.getString("EXCEPTIONFILEPATH");

	public static void writeExceptionInFile(String sqlloaderCmd, String exception) {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(filePath, true));
			bufferedWriter.newLine();
			//bufferedWriter.write(new Date().toString());
			//bufferedWriter.newLine();
			bufferedWriter.write("SQLLDR COMMAND :" + sqlloaderCmd);
			bufferedWriter.newLine();
			bufferedWriter.write("---------------------------------");
			bufferedWriter.newLine();
			bufferedWriter.write("SQLLDR EXCEPTION :" + exception);
			bufferedWriter.newLine();
			bufferedWriter.write("*********************************");
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeExceptionInDB(String sqlloaderCmd, String exception) {
		// create your db connection and insert in your db the details
	}
	
	public static void writeErrorRecordsInDB(List<String> errorList) {
		// create your db connection and insert error records in your db
	}

}
