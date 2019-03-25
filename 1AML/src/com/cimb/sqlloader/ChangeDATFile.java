package com.cimb.sqlloader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.cimb.resource.ModifyControlFile;

public class ChangeDATFile {
	
	
	String strFileName="";
	String strCtlFilePath="";
	String strTxtFilePath="";
	String strDATFilePath="";
	ResourceBundle resourceBundle = ResourceBundle.getBundle("1aml");
	final static Logger logger = Logger.getLogger(ChangeDATFile.class);
	
	
	public static void main(String[] args) throws SQLException, Exception {
		
		ChangeDATFile changeFile  = new ChangeDATFile();
		
		try {
			changeFile.changeFileFormat();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	// changing DAT FILE FORMAT to TEXT FILE 	
		public void changeFileFormat() throws IOException{
			logger.debug("Im in convert .DAT file to text file...!");
			try {
			strDATFilePath=resourceBundle.getString("DAT_FILE_PATH");
			strTxtFilePath=resourceBundle.getString("TXT_FILE_PATH");
			String inputPath=strDATFilePath+resourceBundle.getString("FILENAME")+".DAT";
			String outputPath = strTxtFilePath+resourceBundle.getString("FILENAME")+"_"+ new SimpleDateFormat("yyyyMMdd").format(new Date())+".txt";
			//System.out.println(inputPath);
			Path pathin = Paths.get(inputPath);
			Path pathout = Paths.get(outputPath);
			Charset charset = StandardCharsets.ISO_8859_1;
			String content = new String(Files.readAllBytes(pathin), charset);
			content = content.replaceAll(resourceBundle.getString("IN_SYMBOL"), resourceBundle.getString("OUT_SYMBOL"));
			Files.write(pathout, content.getBytes(charset));
			logger.debug("Text file converted ---------!"+pathout);
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
		}

}
