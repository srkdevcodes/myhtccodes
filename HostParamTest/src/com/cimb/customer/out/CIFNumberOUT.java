package com.cimb.customer.out;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class CIFNumberOUT extends CIFNumberJDBCImpl{
	
	//private final String FILE_ID = "INCLMSDM";
	//private long totalFetchedRecords = 0;
	private static final SimpleDateFormat sdf  =  new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat time  =  new SimpleDateFormat("HHmmss");
	ResourceBundle resourceBundle = ResourceBundle.getBundle("HostParam");
	final static Logger logger = Logger.getLogger(CIFNumberINPUT.class);
	private static final String EMPTY_STRING = "";
	private static final String HEADER = "00";
	private static final String TRAILER = "99";
	private static final String CLMSDM = "CLMSDM";

	public static void main(String[] args) {
		CIFNumberOUT out = new CIFNumberOUT();
		out.readCustomerCIFNumberFile();
	}

	private void readCustomerCIFNumberFile() {
		try {
		String fileLine = null;
		String path = getBatchOutputDir();
		if (path == null) {
			throw new RuntimeException("No location specified for batch output directory");
		}
		String outputFile = "";
		outputFile = path + "/" + resourceBundle.getString("FILE_ID") +sdf.format(new Date())+".txt";
		FileReader messageFile = new FileReader(outputFile);
		
		List<CIFNumberModel> modelList = new ArrayList<CIFNumberModel>();
		BufferedReader messageFileReader = new BufferedReader(messageFile);
		try{
			 while((fileLine = messageFileReader.readLine()) != null) {
			//StringTokenizer st = new StringTokenizer(fileLine, "|");
				//fileLine = messageFileReader.readLine();
				System.out.println(fileLine);
				if(fileLine !=null){
				if(fileLine.startsWith("01")){
				String[] split = fileLine.split("\\|");
				CIFNumberModel model = new CIFNumberModel();
				model.setRecordType(denullify(split[0]));
				//System.out.println(split[1]);
				model.setCifNumner(denullify(split[1]));
				model.setStatus(denullify(split[2]));
				if(model.getStatus().equalsIgnoreCase("000")){
					model.setStatusDesc(null);
				}else{
					model.setStatusDesc(denullify(split[3]));
				}
				modelList.add(model);
				}
				}
			} 
			
			insertIntoTable(modelList);
			
		}catch (FileNotFoundException ex) {
			logger.error(ex.getStackTrace());
		}catch (IOException ex) {
			logger.error(ex.getStackTrace());
		}finally{
			try{
				if(messageFileReader !=null){
					messageFileReader.close();
				}
			}catch (IOException e) {
				logger.error(e.getStackTrace());
			}
		}
			
		
	}catch (IOException e) {
		logger.error(e.getStackTrace());
		e.printStackTrace();
	}
		
}
	
public String getBatchOutputDir() {
		String outputPath = resourceBundle.getString("CIF_FILE_PATH");
		
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
	/*private void addListToModel(String alist) {
		int tokenNumber = 0;
		if(alist !=null){
		List<CIFNumberModel> modelList = new ArrayList<CIFNumberModel>();
		StringTokenizer st = new StringTokenizer(alist,"|");
		while (st.hasMoreElements()){
			CIFNumberModel model = new CIFNumberModel();
			model.setRecordType(denullify(st.nextElement().toString()));
			model.setCifNumner(denullify(st.nextElement().toString()));
			model.setStatus(denullify(st.nextElement().toString()));
			if(model.getStatus().equalsIgnoreCase("000")){
				model.setStatusDesc(null);
			}else{
				model.setStatusDesc(denullify(st.nextElement().toString()));
			}
			modelList.add(model);
			tokenNumber++;
		}
		insertIntoTable(modelList);
		}
		//return modelList;
	}*/

	private void insertIntoTable(List<CIFNumberModel> modelList) {
		try {
			saveCIFFile(modelList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String denullify(String value) {
		return value == null ? EMPTY_STRING : value;
	}


}
