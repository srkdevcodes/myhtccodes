package com.cimb.resource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
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
	private String totalStr;
	private FileReader fr;
	String strFile[] = null;
	String strFileName="";
	String strCtlFilePath="";
	String strTxtFilePath="";
	ResourceBundle resourceBundle = ResourceBundle.getBundle("HostParam");
	final static Logger logger = Logger.getLogger(ModifyControlFile.class);
	public ModifyControlFile() {
		setControlFiles();
	}

	private void setControlFiles(){
		//Method to copy the previous day log details into Backup Log table
		FileProperties.insertLogBkpDtls();
		
		strCtlFilePath=resourceBundle.getString("CTL_FILE_PATH");
		strTxtFilePath=resourceBundle.getString("TXT_FILE_PATH");
		//strFileName=resourceBundle.getString("FILENAME");
		
		//Method for Loading only Files that have not run for the day
		arrFiles = FileProperties.getUnloadedFiles();

		File f = null;
		boolean bool = false;
		//logger.debug("Name of files added in List:: "+arrFiles.size());
		for(int i=0;i<arrFiles.size();i++){
			f = new File (strTxtFilePath+arrFiles.get(i)+"_"+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt");
			bool = f.exists();
			logger.debug("Bool value::"+bool);
			if (bool == true){
				
			//Applicable only for Thailand - Start
				try {
					logger.debug("File is :"+strTxtFilePath+arrFiles.get(i)+"_"+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt" );
					FileProperties.transform(new File(strTxtFilePath+arrFiles.get(i)+"_"+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt"), "TIS620", new File(strTxtFilePath+arrFiles.get(i)+"_"+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + "_tmp" + ".txt"), "UTF-8");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//Applicable only for Thailand - End
				
		//controlFileList.add(strCtlFilePath + strFile[i] + ".ctl" + "~" + strTxtFilePath+strFile[i]+"_"+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt");
		controlFileList.add(strCtlFilePath + arrFiles.get(i) + ".ctl" + "~" + strTxtFilePath+arrFiles.get(i)+"_"+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt");
			}
		
	}
}

	public Set<String> getControlFiles() {
		return controlFileList;
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
