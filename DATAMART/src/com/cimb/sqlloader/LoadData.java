package com.cimb.sqlloader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.cimb.resource.FileProperties;
import com.cimb.resource.ModifyControlFile;

public class LoadData {
	private ModifyControlFile modifyControlFile;
	private Runtime rt = Runtime.getRuntime();
	private Process proc;
	private boolean ifModified;
	private Set<String> controlFileList;
	private String sqlloaderCmd;
	private String line;
	private String sqlloaderError;
	private final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private final Calendar cal = Calendar.getInstance();
	final static Logger logger = Logger.getLogger(LoadData_bkup.class);
	ResourceBundle resourceBundle = ResourceBundle.getBundle("Datamart");
	public static final String AES = "AES";
	public LoadData() throws SQLException,Exception {
		modifyControlFile = new ModifyControlFile();
		modifyCntrlfiles();
	}
	
	private void modifyCntrlfiles() {
		ifModified = modifyControlFile.modifyFiles();
	}

	private void executeCommand(){
	try {
		String strService = resourceBundle.getString("SERVICE");
      	String strUserId = resourceBundle.getString("USERID");
      	//String strPwd = resourceBundle.getString("PWD");
      	String key = resourceBundle.getString("KEY");
      	String keypwd = resourceBundle.getString("KEYPWD");
      	String strPwd = this.getPassword(key,keypwd);
      	String strAbsPath = resourceBundle.getString("ABSOLUTE_PATH");
      	String strLogFilePath = resourceBundle.getString("LOG_FILE_PATH");
      	String strBadFilePath = resourceBundle.getString("BAD_FILE_PATH");
      	String strDisFilePath = resourceBundle.getString("DIS_FILE_PATH");
      	String strURL = strUserId + "/" + strPwd + "@" + strService;
			for (String controlFileNameInFile : controlFileList) {
				logger.debug("controlFileNameInFile::::"+controlFileNameInFile);
				String controlFileName = controlFileNameInFile.substring(0, controlFileNameInFile.indexOf("~"));
				String inFileName = controlFileNameInFile.substring(controlFileNameInFile.indexOf("~") + 1);
				logger.debug("inFileName"+inFileName);
				//SIT-Localhost
				String strCntrlFile = controlFileNameInFile.substring(controlFileNameInFile.indexOf("_")+7,controlFileNameInFile.indexOf("~"));
				//UAT
				//String strCntrlFile = controlFileNameInFile.substring(controlFileNameInFile.indexOf("CTL_FILES")+10,controlFileNameInFile.indexOf("~"));
				//SIT-Localhost
				//String strParamFile = inFileName.substring(inFileName.indexOf("_")+7,inFileName.indexOf(".")-9);
				String strParamFile = inFileName.substring(inFileName.indexOf("_")+7,inFileName.indexOf(".")-8);
				//UAT
				//tring strParamFile = inFileName.substring(inFileName.indexOf("TXT_FILES")+10);
				//String strParamFileName = strParamFile.substring(0,strParamFile.indexOf("_"));
				
				logger.debug("Begin ---tABLE NAME CLMS-------"+strParamFile);
				logger.debug("The Ctl File NAme is : " +strCntrlFile);
				logger.debug("The tEXT File NAme PATH is : " +inFileName);
				int intTotalUploadRecs=0;
				String strExcDtls="";
				String strLogDtls="";
				
				
				//Method to get Number of Records in the Parameter File
				int intNoOfRecs= FileProperties.getNoOfLine(inFileName);
				logger.debug("No Of Records in the File is : " +intNoOfRecs);
				//Delete the Param Table before loading new parameters for the day
				//FileProperties.delParamRecs(strParamFileName);
				
				sqlloaderError = "";
				sqlloaderCmd = ""+strAbsPath+"sqlldr control= " + controlFileName + " data=" + inFileName + " LOG=" + strLogFilePath + strCntrlFile
						+ ".log " + "USERID=" + strURL + " BAD=" + strBadFilePath + strCntrlFile + ".bad ERRORS=999999999 "
						+ "SKIP=0 DISCARD=" + strDisFilePath + strCntrlFile + ".dis";

				int exitVal=0;
				try{
				
				logger.info(sqlloaderCmd);
				proc = rt.exec(sqlloaderCmd);
				logger.debug("Am after the process execution");
				StringBuffer readBuffer = new StringBuffer();
				BufferedReader isr = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String buff = new String("");
				while ((buff = isr.readLine()) != null){
					readBuffer.append(buff);
					System.out.println(buff);
					}
					
				exitVal = proc.waitFor();
				logger.debug("Am after the exitval process");
				}
				catch(Exception e){
					logger.debug("message"+e);
				}
				int intTotErrRecs = 0;
				logger.debug("Am after the initiation");
				BufferedReader br = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
				logger.debug("Am after the process error stream");
				logger.debug("Exitval for File Execution :"+exitVal);
				String strErrFilePath = strBadFilePath + strCntrlFile;
				if(exitVal == 0){
					logger.debug("EXITVAL := "+exitVal);
					//Method to Update Record Load Details into summary table
					FileProperties.updateUploadDtls(strCntrlFile,strParamFile,"Y",intNoOfRecs,intNoOfRecs,intTotErrRecs,strExcDtls,"TEST");
				}
				if (br != null && exitVal != 0) {
					if (exitVal == 1) {
						logger.debug("EXITVAL := "+exitVal);
						while ((line = br.readLine()) != null) {
							sqlloaderError = sqlloaderError + " + " + line;
						}
						//logger.debug("strErrFilePath inside 1:::"+strErrFilePath);
						//Method to get the Number of Error Records
						intTotErrRecs = FileProperties.getErrorRecordsCnt(strErrFilePath);
						intTotalUploadRecs = intNoOfRecs-intTotErrRecs;
						//logger.debug("intTotErrRecs inside 1:::"+intTotErrRecs);
						//logger.debug("intTotalUploadRecs inside 1:::"+intTotalUploadRecs);
						//Method to Update Record Load Details into TBL_LOG_DTLS table
						logger.debug("EXITVAL := "+exitVal);
						FileProperties.updateUploadDtls(strCntrlFile,strParamFile,"N",intNoOfRecs,intTotalUploadRecs,intTotErrRecs,sqlloaderError,"TEST");
					} else if (exitVal == 2) {
						sqlloaderError = "All or some rows rejected + All or some rows discarded + Discontinued load";
						logger.debug("EXITVAL := "+exitVal);
						//Method to get the Number of Error Records
						//logger.debug("strErrFilePath inside 2:::"+strErrFilePath);
						intTotErrRecs = FileProperties.getErrorRecordsCnt(strErrFilePath);
						//logger.debug("intTotErrRecs inside 2:::"+intTotErrRecs);
						intTotalUploadRecs = intNoOfRecs-intTotErrRecs;
						//logger.debug("intTotalUploadRecs inside 2:::"+intTotalUploadRecs);
						//Method to Update Record Load Details into TBL_LOG_DTLS table
						FileProperties.updateUploadDtls(strCntrlFile,strParamFile,"N",intNoOfRecs,intTotalUploadRecs,intTotErrRecs,sqlloaderError,"TEST");
					} else {
						while ((line = br.readLine()) != null) {
							logger.debug("EXITVAL := "+exitVal);
							sqlloaderError = sqlloaderError + " + " + line;
						}
						intTotErrRecs = intNoOfRecs;
						intTotalUploadRecs = intNoOfRecs-intTotErrRecs;
						FileProperties.updateUploadDtls(strCntrlFile,strParamFile,"N",intNoOfRecs,intTotalUploadRecs,intTotErrRecs,sqlloaderError,"TEST");
					}
					
					logger.debug("Error through sql ldr is ::"+sqlloaderError);
				}
				//logger.debug("Execution completed---------"+strParamFileName);
				logger.debug("______________________________________________");
				
			}
			
		} catch (Exception e) {
			logger.error("Exception caught is "+e.toString());
		}
		finally{
			
			//if(br != null){
				//br.close();
				//br=null;
			}
		}

	//}

	public static void main(String args[]) throws IOException, Exception {

		LoadData loadData = new LoadData();

		System.out.println(loadData.ifModified);

		if (loadData.ifModified == true) {
			loadData.controlFileList = loadData.modifyControlFile.getControlFiles();
			loadData.executeCommand();
		//	loadData.modifyControlFile.deleteOldFiles();
		}

	}
	
	@SuppressWarnings("unused")
	private  String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    private  byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }
	
	 public String getPassword(String key, String keypwd) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, FileNotFoundException, IOException{
		// logger.info(" I am in LoadData getPassword...............!");
    	 byte[] bytekey = hexStringToByteArray(key);
	        SecretKeySpec sks = new SecretKeySpec(bytekey, LoadData_bkup.AES);
	        Cipher cipher = Cipher.getInstance(LoadData_bkup.AES);
	        cipher.init(Cipher.DECRYPT_MODE, sks);
	        byte[] decrypted = cipher.doFinal(hexStringToByteArray(keypwd));
	        String originalPassword = new String(decrypted);
	       /* System.out.println("****************  Original Password  ****************");
	        System.out.println(originalPassword);
	        System.out.println("****************  Original Password  ****************");*/
    	return originalPassword;
    }

}
