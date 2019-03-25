package com.cimb.resource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ResourceBundle;


public class MyLogger {
	static ResourceBundle resourceBundle = ResourceBundle.getBundle("HostParam");
	private static BufferedWriter bufferedWriter;
	private static final String filePath = resourceBundle.getString("LOGFILEPATH");

	public static void logInFile(String logCmd) {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(filePath, true));
			bufferedWriter.newLine();
			//bufferedWriter.write(new Date().toString());
			//bufferedWriter.newLine();
			bufferedWriter.write(logCmd);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logInDB(String sqlloaderCmd, String exception) {
		// create your db connection and insert in your db the details
	}

}
