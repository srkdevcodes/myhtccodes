package com.cimb.customer.out;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.sql.rowset.serial.SerialClob;

import org.apache.log4j.Logger;

public class CIFNumberINPUT  extends CIFNumberJDBCImpl{
	
	private final String FILE_ID = "INCLMSDM";
	private long totalFetchedRecords = 0;
	private static final SimpleDateFormat sdf  =  new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat time  =  new SimpleDateFormat("HHmmss");
	ResourceBundle resourceBundle = ResourceBundle.getBundle("HostParam");
	final static Logger logger = Logger.getLogger(CIFNumberINPUT.class);
	private static final String EMPTY_STRING = "";
	private static final String HEADER = "00";
	private static final String TRAILER = "99";
	private static final String CLMSDM = "CLMSDM";

	public static void main(String[] args) {
		CIFNumberINPUT cust = new CIFNumberINPUT();
		cust.writeCustomerCIFNumber();
	}

	private void writeCustomerCIFNumber() {
		String  header = generateDataHeader();
		List<String> cifList = generateData();
		String trailer = generateDataTrailer();
		writeToFile(header, cifList, trailer);
	}


	private String generateDataTrailer() {
		StringBuilder bd = new StringBuilder();
		bd.append(TRAILER).append("|");
		bd.append(totalFetchedRecords).append("|");
		bd.append(CLMSDM);
		System.out.println("Header  - "+bd.toString());
		return bd.toString();
	}

	private List<String> generateData() {
		
		List<String> list = getCifNumberOutData();
		SequenceGenerator gen = new SequenceGenerator();
		String sqno = "";
		//List<CustomerModel> cifList = new ArrayList<CustomerModel>();
		int i=0;
		List<String> cifList = new ArrayList<String>();
		for (String cifNo : list) {
			StringBuilder bd = new StringBuilder();
			int id = gen.nextUniqueInt();
			bd.append(String.format("%02d", id)).append("|").append(denullify(cifNo));
			System.out.println(bd.toString());
			cifList.add(bd.toString());
			totalFetchedRecords = id;
		}
		
		return cifList;
	}

	private String generateDataHeader() {
		
		StringBuilder bd = new StringBuilder();
		bd.append(HEADER).append("|");
		bd.append(sdf.format(new Date())).append("|");
		bd.append(sdf.format(new Date())).append("|");
		bd.append(time.format(new Date().getTime())).append("|");
		bd.append(CLMSDM);
		System.out.println("Header  - "+bd.toString());
		return bd.toString();
		
		
	}
	
	private String generate(String no){
		for(int i=1; i< 10; i+=2){
			no = String.format("%02d", i);
		}
		return no;
	}
	
	private void writeToFile(String header, List<String> cifList, String trailer) {
		
		String path = getBatchOutputDir();
		if (path == null) {
			throw new RuntimeException("No location specified for batch output directory");
		}
		
		String outputFile = "";
		
		outputFile = path + "/" + FILE_ID +sdf.format(new Date())+".txt";
		FileWriter fileWriter = null;
		try {
			System.out.println("File Path :"+outputFile);
			fileWriter = new FileWriter(outputFile);
			fileWriter.write(header);
			fileWriter.write(System.getProperty("line.separator"));
				int i = 0;
				for (String cifNo: cifList) {
					fileWriter.write(cifNo);
					fileWriter.write(System.getProperty("line.separator"));
				}
			//fileWriter.write(System.getProperty("line.separator"));				
			fileWriter.write(trailer);
			
		}
		catch (IOException e) {
			logger.error("End of processing records.");
			throw new RuntimeException(e);
		}finally {
			if (fileWriter != null) {
				try {
					fileWriter.flush();
					fileWriter.close();
				}
				catch (IOException e) {throw new RuntimeException(e);}
			}
		}
	}
	
	public String getBatchOutputDir() {
		String outputPath = resourceBundle.getString("GEN_FILE_DIR");
		
		if (outputPath != null) {
			File file = new File(outputPath);
			if (!file.exists()){
				file.mkdirs();
			}
		}
		else {
			outputPath = resourceBundle.getString("USER_DIR");
		}
		return outputPath;
	}
	
	private String denullify(String value) {
		return value == null ? EMPTY_STRING : value;
	}
	
	private Clob stringToClob(String source){
    	try {
    		return new SerialClob(source.toCharArray());
		} catch (Exception e) {
			logger.error("Cloud not convert string to CLOB...");
			return null;
		}
    	
    	
    }
}
