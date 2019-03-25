package com.cimb.resource;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;


public class ReadDATFile {
	
	private static String strDATFilePath="";
	private static String strTxtFilePath="";
	static ResourceBundle resourceBundle = ResourceBundle.getBundle("1aml");
	final static Logger logger = Logger.getLogger(ReadDATFile.class);
	
	
	public static void main(String[] args) throws IOException {
		strDATFilePath=resourceBundle.getString("DAT_FILE_PATH");
		strTxtFilePath=resourceBundle.getString("TXT_FILE_PATH");
		String inputPath=strDATFilePath+resourceBundle.getString("FILENAME")+".DAT";
		String outputPath = strTxtFilePath+resourceBundle.getString("FILENAME")+"_"+ new SimpleDateFormat("yyyyMMdd").format(new Date())+".txt";;
		//System.out.println(inputPath);
		Path pathin = Paths.get(inputPath);
		Path pathout = Paths.get(outputPath);
		Charset charset = StandardCharsets.UTF_8;
		String content = new String(Files.readAllBytes(pathin), charset);
		content = content.replaceAll(resourceBundle.getString("IN_SYMBOL"), resourceBundle.getString("OUT_SYMBOL"));
		Files.write(pathout, content.getBytes(charset));
	}
	
	/*public void readDATFile() throws IOException{
				
		strTxtFilePath=resourceBundle.getString("TXT_FILE_PATH");
		String inputPath = strTxtFilePath+fileName;
		
		System.out.println(inputPath);
		byte[] data = Files.readAllBytes(Paths.get(inputPath));
        String myString = new String(data, StandardCharsets.UTF_8  set correct required charset );
        // convert/transform here string if required
        Files.write(Paths.get(outputPath), myString.getBytes(StandardCharsets.UTF_8));
		
	}*/
	
	
	/*public static void writeCsvFile(String fileName, ArrayList<String> data) {
		
		ArrayList<String> students =data; 		
		FileWriter fileWriter = null;		
		try {			
		fileWriter = new FileWriter(fileName);
		for (String student111 : students) {
		String datafinal[]=student111.split("§");
		for(String dat1:datafinal)				
		{
		fileWriter.append(dat1);
		fileWriter.append(COMMA_DELIMITER);
		}				
		fileWriter.append(NEW_LINE_SEPARATOR);
		}			
		System.out.println("CSV file was created successfully !!!");
		} catch (Exception e) {
		System.out.println("Error in CsvFileWriter !!!");
		e.printStackTrace();		
		} finally {
		try {
		fileWriter.flush();	
		fileWriter.close();
		} catch (IOException e) 
		{				
		System.out.println("Error while flushing/closing fileWriter !!!");
		e.printStackTrace();			
		}		
		}	
		}	*/
}
