package com.cimb.resource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.log4j.Logger;

public class ModifyControlFile {

	private boolean replaceComplete;
	private String search;
	private String replacement;
	private final Set<String> controlFileList = new LinkedHashSet<>();
	private final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private final Calendar cal = Calendar.getInstance();
	private StringBuffer totalStrBfr = new StringBuffer();
	private ArrayList arrFiles = new ArrayList();
	private ArrayList arrCleanFiles = new ArrayList();
	private String totalStr;
	private FileReader fr;
	String strFile[] = null;
	String strFileName="";
	String strCtlFilePath="";
	String strTxtFilePath=""; 
	String strCountry="";
	ResourceBundle resourceBundle = ResourceBundle.getBundle("Datamart");
	final static Logger logger = Logger.getLogger(ModifyControlFile.class);
	
	public ModifyControlFile() {
		setControlFiles();
	}

	private void setControlFiles(){
		//Method to update the records to N status for loading data for sysdate
		FileProperties.insertLogBkpDtls();
		logger.debug("Am updating the flag to N");
		
		strCtlFilePath=resourceBundle.getString("CTL_FILE_PATH");
		strTxtFilePath=resourceBundle.getString("TXT_FILE_PATH");
		strCountry=resourceBundle.getString("IN_COUNTRY");
		//strFileName=resourceBundle.getString("FILENAME");
		
		//Method for Loading only Files that have not run for the day
		arrFiles = FileProperties.getUnloadedFiles();

		File f = null;
		boolean bool = false;
		//logger.debug("Name of files added in List:: "+arrFiles.size());
		for(int i=0;i<arrFiles.size();i++){
			//f = new File (strTxtFilePath+arrFiles.get(i)+"_"+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt");
			f = new File (strTxtFilePath+arrFiles.get(i) + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt");
			bool = f.exists();
			logger.debug("Bool value::"+bool+" File name: "+f);
			if (bool == true){
				//logger.debug("Country value::"+strCountry);
				logger.debug("Country value::"+strCountry);
				if(strCountry.equals("TH")){
			//Applicable only for Thailand - Start
				try {
					logger.debug("File is :"+strTxtFilePath+arrFiles.get(i) + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt" );
					FileProperties.transform(new File(strTxtFilePath+arrFiles.get(i) + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt"), "TIS620", new File(strTxtFilePath+arrFiles.get(i) + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "_tmp" + ".txt"), "UTF-8");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			//Applicable only for Thailand - End
					controlFileList.add(strCtlFilePath + arrFiles.get(i) + ".ctl" + "~" + strTxtFilePath+arrFiles.get(i) + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt");
		}
	}
}

	public Set<String> getControlFiles() {
		return controlFileList;
	}

	/*public void cleanTextfiles() throws ParseException{
		logger.debug("Im in cleaning TXT_FILES...!");
		strTxtFilePath=resourceBundle.getString("TXT_FILE_PATH");
		arrCleanFiles = FileProperties.getUploadedFiles();
		FileProperties.deleteTableLogFiles(daybeforeThreedays()); // deleting tbl_log_bkup_dtls records before 3days
		File f = null;
		boolean bool = false;
		//logger.debug("Name of files added in List:: "+arrFiles.size());
		for(int i=0;i<arrCleanFiles.size();i++){
			//logger.debug(strTxtFilePath+arrCleanFiles.get(i)+"_"+ new SimpleDateFormat("yyyyMMdd").format(daybeforeTwodays()) + ".txt");
			f = new File (strTxtFilePath+arrCleanFiles.get(i)+"_"+ new SimpleDateFormat("yyyyMMdd").format(daybeforeThreedays()) + ".txt");
			bool = f.exists();
			//logger.debug("Bool value::"+bool);
			if (bool == true){
				if(f.delete()){
					logger.debug(f.getName() + " is deleted!");
	    		}else{
	    			logger.debug("Delete operation is failed.");
	    		}
		
			}
		
	}
		
	}*/
	
	
	public void deleteOldFiles() throws ParseException{
		try {
		File directory = new File(resourceBundle.getString("TXT_FILE_PATH"));
		File[] fileList = directory.listFiles();
		for (File file : fileList) {
			String filename = file.getName();
			if(filename.contains(".txt")){
				//String[] strArray = filename.split("_");
				String strArray = filename.substring(6,filename.indexOf("."));				
				//logger.debug(strArray);
				String date = strArray;
				Date fileDate = dateFormat.parse(date);
				//logger.debug(fileDate);
				String checkDate = dateFormat.format(day());
				//logger.debug(checkDate);
				Date isDate = dateFormat.parse(checkDate);
				//logger.debug(fileDate.before(isDate));
				//logger.debug(fileDate.compareTo(isDate));
					if(fileDate.before(isDate)||fileDate.compareTo(isDate) == 0){
						if(file.delete()){
							logger.debug(file.getName() + " is deleted!");
			    		}else{
			    			logger.debug("Delete operation is failed.");
			    		}
					}
			}
		}
		//FileProperties.deleteTableLogFiles(day()); // deleting tbl_log_bkup_dtls records before 3days
				} catch (Exception e) {
					logger.error("SQLException exception caught"+e);
				}
	}
	
	
	private Date day() throws ParseException{
		 Calendar now = Calendar.getInstance();
		   // System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1) + "-"
		   //     + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));

		    // add days to current date using Calendar.add method
		 	
		    now.add(Calendar.DATE, Integer.parseInt(resourceBundle.getString("DAYS")));

		    /*logger.debug("date after one day : " + (now.get(Calendar.MONTH) + 1) + "-"
		        + now.get(Calendar.DATE) + "-" + now.get(Calendar.YEAR));
		    String sDate = now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH) + 1)+"-"+now.get(Calendar.DATE);*/
		   
			return now.getTime();
	}
		
		
	private void setFindAndReplace() {
		replacement = dateFormat.format(cal.getTime()).toString();
		cal.add(Calendar.DATE, -1);
		search = dateFormat.format(cal.getTime());
	}

	public boolean modifyFiles() {

		return true;
	}

}
