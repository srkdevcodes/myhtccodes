package com.cimb.customer.out;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * 
 * @author Rajkiran suram
 * @since 1 mar 2018
 *
 */

public class CIFNumberINPUT  extends CIFNumberJDBCImpl{
	
	private final String FILE_ID = "INCLMSDM";
	private long totalFetchedRecords = 0;
	private static final SimpleDateFormat sdf  =  new SimpleDateFormat("dd-MMM-yyyy");
	private static final SimpleDateFormat dateFormat  =  new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat time  =  new SimpleDateFormat("HHmmss");
	ResourceBundle resourceBundle = ResourceBundle.getBundle("Datamart");
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
		bd.append(String.format("%9s",totalFetchedRecords).replace(' ','0'));
	//	bd.append(CLMSDM);
		System.out.println("Trailer  - "+bd.toString());
		return bd.toString();
	}

	private List<String> generateData() {
		
		List<CifNoModel> list = getNewCifNumberOutData();
		SequenceGenerator gen = new SequenceGenerator();
		int countRow = 00;
		List<String> cifList = new ArrayList<String>();
		if(!list.isEmpty()){
		for (CifNoModel model : list) {
			StringBuilder bd = new StringBuilder();
			int intDfltNo = 01;
			int id = gen.nextUniqueInt();
			bd.append(String.format("%02d", intDfltNo)).append("|");
			bd.append(denullify(model.getCifNo())).append("|");
			try {
				if(sdf.format(model.getCreationDt()).compareTo(sdf.format(new Date())) == 0){
					bd.append(resourceBundle.getString("CIF_IND_N")).append("|");
				}else{
					bd.append(resourceBundle.getString("CIF_IND_O")).append("|");
				}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
			bd.append(denullify(dateFormat.format(model.getCreationDt())));
			System.out.println(bd.toString());
			cifList.add(bd.toString());
			countRow++;
		}
		}
		totalFetchedRecords = countRow;
		
		//commented the code 
		/*if(resourceBundle.getString("CIF_IND_O").equalsIgnoreCase("N")){
			try {
				changePropertyFilesInFolder();
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}
		}*/
		return cifList;
	}
	
	/*public void changePropertyFilesInFolder() throws IOException{
		try{
		File[] filelist = new File(resourceBundle.getString("PROP_FILE_DIR")).listFiles();
		for(File file: filelist){
			if(file.isFile() && file.getName().equalsIgnoreCase("Datamart.properties")){
				Properties prop = new Properties();
				FileInputStream instream = new FileInputStream(file);
				prop.load(instream);
				instream.close();
				prop.setProperty("CIF_IND_O", "O"); // change whatever you want here
				FileOutputStream outstream = new FileOutputStream(file);
				prop.store(outstream, "Updating CIF_IND_O indicator 'N' to 'O'... ");
				outstream.close();
			}
		}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}*/
	
	/*private Date getYesterdayDateString() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);    
      //  System.out.println(dateFormat.format(cal.getTime()));
        String returnDt = dateFormat.format(cal.getTime());
        return dateFormat.parse(returnDt);
	}*/

	private String generateDataHeader() {
		
		StringBuilder bd = new StringBuilder();
		bd.append(HEADER).append("|");
		bd.append(dateFormat.format(new Date())).append("|");
		bd.append(dateFormat.format(new Date())).append("|");
		bd.append(time.format(new Date().getTime())).append("|");
		bd.append(CLMSDM);
		System.out.println("Header  - "+bd.toString());
		return bd.toString();
		
		
	}
	
	/*private String generate(String no){
		for(int i=1; i< 10; i+=2){
			no = String.format("%02d", i);
		}
		return no;
	}*/
	
	private void writeToFile(String header, List<String> cifList, String trailer) {
		
		String path = getBatchOutputDir();
		if (path == null) {
			throw new RuntimeException("No location specified for batch output directory");
		}
		
		String outputFile = "";
		
		outputFile = path + "/" + FILE_ID +dateFormat.format(new Date())+".txt";
		FileWriter fileWriter = null;
		try {
			System.out.println("File Path :"+outputFile);
			fileWriter = new FileWriter(outputFile);
			fileWriter.write(header);
			fileWriter.write(System.getProperty("line.separator"));
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
	
}
