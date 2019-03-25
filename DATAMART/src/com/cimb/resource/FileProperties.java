package com.cimb.resource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cimb.dao.Dao;

public class FileProperties {
	public static Connection con = null;
	Statement stmt = null;
	public static PreparedStatement pstmt = null;
	public static PreparedStatement pstmtPrevRecs = null;
	public static PreparedStatement pstmtFile = null;
	public static PreparedStatement pstmtInsrtLogDtls = null;
	public static PreparedStatement pstmtUpdtFlg = null;
	public static PreparedStatement pstmtDel = null;
	public static PreparedStatement pstmtSrcInfo = null;
	public static CallableStatement cstmtExecParam = null;
	public static ResultSet rsPrevDtRecs = null;
	public static ResultSet rsFileNames = null;
	public static ResultSet rsSrcInfo = null;
	private static String strUpdtLogQry=""; 
	final static Logger logger = Logger.getLogger(FileProperties.class);

	private static java.sql.Date getCurrentDate() {
	    java.util.Date today = new java.util.Date();
	    return new java.sql.Date(today.getTime());
	}
	
	public static int getNoOfLine(String CompleteFilePath){
		
		int cnt = 0;
		LineNumberReader lnr = null;
		String lineRead = "";
		try {
			lnr = new LineNumberReader(new FileReader(new File(CompleteFilePath)));

			while ((lineRead = lnr.readLine()) != null) {
			}

			cnt = lnr.getLineNumber();
			
		} catch (IOException e) {
			MySQLLoaderException.writeExceptionInFile("FileProperties.getNoOfLine :", e.toString());
			logger.error("There is an IOException here is::"+e);
		} finally {
			try {
				if (lnr != null) {
					lnr.close();
				}
			} catch (IOException e) {
				MySQLLoaderException.writeExceptionInFile("FileProperties.getNoOfLine :", e.toString());
				logger.error("There is an IOException here is::"+e);
			}
		}
		return cnt;
	}

	//public static List<String> getErrorRecords(String CompleteFilePath) {
	
	  public static int getErrorRecordsCnt(String CompleteFilePath){
		
		List<String> errorList = new LinkedList<>();
		LineNumberReader lnr = null;
		String lineRead = "";
		int cnt=0;
		try {
			if (new File(CompleteFilePath + ".bad").isFile()) {
				lnr = new LineNumberReader(new FileReader(new File(CompleteFilePath + ".bad")));

				while ((lineRead = lnr.readLine()) != null) {
					//errorList.add(lineRead);
				}
				cnt = lnr.getLineNumber();
				logger.debug("Error through sql ldr is ::"+cnt);
			}

			if (new File(CompleteFilePath + ".dis").isFile()) {
				lnr = new LineNumberReader(new FileReader(new File(CompleteFilePath + ".dis")));

				while ((lineRead = lnr.readLine()) != null) {
					//errorList.add(lineRead);
				}
				cnt = lnr.getLineNumber();
				logger.debug("Count of records from bad file"+cnt);
			}

		} catch (IOException e) {
			//MySQLLoaderException.writeExceptionInFile("FileProperties.getErrorRecords :", e.toString());
			logger.error("There is an IOException here is::"+e);
		} finally {
			try {
				if (lnr != null) {
					lnr.close();
				}
			} catch (IOException e) {
				MySQLLoaderException.writeExceptionInFile("FileProperties.getErrorRecords :", e.toString());
			}
		}

		return cnt;
	}
	
	@SuppressWarnings("static-access")
	public static void insertUploadDtls(String strCntrlFile,String strParamFile,String strUploadFlag,int intTotNoOfRecs,int intTotalUploadRecs,int intTotErrRecs,String strExcDtls,String strLogDtls){
		Dao newConn = new Dao();
		int intPrevDtRecs=0;
	try{
		logger.debug("Am inside Method to insert Record Details");
		con = Dao.createConnection();
		String strPrevDtRecsQry="SELECT TOTAL_NUMB_RECORDS FROM TBL_LOG_DTLS WHERE LOAD_DATE=trim(SYSDATE-1) AND CTRLFILENAME='"+strCntrlFile+"'";
		pstmtPrevRecs = con.prepareStatement(strPrevDtRecsQry);
		rsPrevDtRecs = pstmtPrevRecs.executeQuery();
		while(rsPrevDtRecs.next())
		{
			intPrevDtRecs = rsPrevDtRecs.getInt(1);
		}
		String strInsrtQuery = "insert into tbl_log_dtls values (log_slno.nextval,?,?,?,?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(strInsrtQuery);
		pstmt.setString(1,strParamFile);
		pstmt.setString(2, strCntrlFile);
		pstmt.setString(3,strUploadFlag);
		pstmt.setInt(4,intTotNoOfRecs);
		pstmt.setInt(5,intTotalUploadRecs);
		pstmt.setInt(6,intTotErrRecs);
		pstmt.setInt(7, intPrevDtRecs);
		pstmt.setDate(8,getCurrentDate());
		pstmt.setString(9,strExcDtls);
		pstmt.setString(10,strLogDtls);
		int intInsrtUpldDtls = pstmt.executeUpdate();
		if(intInsrtUpldDtls > 0)
			{
				con.commit();
			}
			else
			{
				con.rollback();
			}
		}
		catch(SQLException ex){
			logger.error("SQLException is here::"+ex);
		}
		catch(Exception e)
		{
			logger.error("Exception here is :: "+e);
		}
		finally
		{
			try{
				 if(rsPrevDtRecs != null)
				 	{
					 rsPrevDtRecs.close();
					 rsPrevDtRecs = null;
				 	}
				 if(pstmt != null)
					{
					 pstmt.close();
					 pstmt = null;
					}
				 if(pstmtPrevRecs != null)
					{
					 pstmtPrevRecs.close();
					 pstmtPrevRecs = null;
					}
				 if(con != null)
					{
					con.close();
					con=null;
					}
				}
				catch (SQLException e) 
				{
					logger.error("SQLException in finally block is :: "+e);
				}
		}

	}
	
	public static void updateUploadDtls(String strCntrlFile,String strParamFile,String strUploadFlag,int intTotNoOfRecs,int intTotalUploadRecs,int intTotErrRecs,String strExcDtls,String strLogDtls){
		logger.debug("Am inside Method to Update Record Details updateUpload"+strCntrlFile);
		String strSrcSys = "";
		String strUpdtTblQuery = "";
	try{
		con = Dao.createConnection();
		con.setAutoCommit(false);
		String strSrcInfo = "SELECT SOURCE_SYSTEM FROM CLMS_DATAMART_SUMMARY_DTLS WHERE STG_CTRL_FILENAME = ?";
		pstmtSrcInfo = con.prepareStatement(strSrcInfo);
		pstmtSrcInfo.setString(1,strCntrlFile);
		rsSrcInfo = pstmtSrcInfo.executeQuery();
		while(rsSrcInfo.next())
		{
			strSrcSys = rsSrcInfo.getString(1);
		}
		System.out.println("Source system is :: "+strSrcSys);
		if(strSrcSys.equals("CLMS")){
			System.out.println("Before update query for CLMS");
			strUpdtTblQuery = "UPDATE CLMS_DATAMART_SUMMARY_DTLS SET CLMS_LOAD_FLAG = ?,CLMS_TOTAL_NUMB_RECORDS=?,CLMS_NUMB_REC_LOADED=?,CLMS_NUMB_ERR_RECS=?,CLMS_LOAD_DATE=?,ERR_DESCRIPTION=? WHERE STG_CTRL_FILENAME = ?";
		}else{
			System.out.println("Before update query for non CLMS");
			strUpdtTblQuery = "UPDATE CLMS_DATAMART_SUMMARY_DTLS SET STG_FILE_LOAD_FLG = ?, STG_TOTAL_NUMB_RECS=?, STG_NUMB_RECS_LOADED=?, STG_NUMB_ERR_RECS=?, STG_LOAD_DATE=?, ERR_DESCRIPTION=? WHERE STG_CTRL_FILENAME = ?";
		}
		try{
		pstmt = con.prepareStatement(strUpdtTblQuery);
		}catch (Exception e) {
			logger.error( "error non clms update query :"+ e.getMessage());
		}
		pstmt.setString(1,strUploadFlag);
		pstmt.setInt(2,intTotNoOfRecs);
		pstmt.setInt(3,intTotalUploadRecs);
		pstmt.setInt(4,intTotErrRecs);
		//pstmt.setInt(5, intPrevDtRecs);
		pstmt.setDate(5,getCurrentDate());
		pstmt.setString(6,strExcDtls);
		pstmt.setString(7, strCntrlFile);
		int intInsrtUpldDtls = pstmt.executeUpdate();
		logger.debug("query result -> "+intInsrtUpldDtls);
		if(intInsrtUpldDtls > 0)
			{
				con.commit();
				logger.debug("committes -> "+intInsrtUpldDtls);
			}
			else
			{
				con.rollback();
			}
		}
		catch(SQLException ex){
			logger.error("This is a SQLException :: "+ex);
		}
		catch(Exception e)
		{
			logger.error("This is an Exception :: "+e);
		}
		finally
		{
			try{
				 if(pstmt != null)
					{
					 pstmt.close();
					 pstmt = null;
					}
				 if(pstmtSrcInfo != null)
					{
					 pstmtSrcInfo.close();
					 pstmtSrcInfo = null;
					}
				 if(rsSrcInfo != null)
					{
					 rsSrcInfo.close();
					 rsSrcInfo = null;
					}
				 if(con != null)
					{
					con.close();
					con=null;
					}
				}
				catch (SQLException e) 
				{
					logger.error("This is a SQLException exception :: "+e);
				}
		}

	}
	
	public static void insertLogBkpDtls(){
		logger.debug("Am inside insertlog bkup dtls");
		int intInsrt=0;
		int intUpdt = 0;
		String strUpdtClmsMainQry="";
		String strUpdtStgQry="";
		String strUpdtClmsQry="";
		
	try{
		con = Dao.createConnection();
		con.setAutoCommit(false);
				strUpdtStgQry = "UPDATE CLMS_DATAMART_SUMMARY_DTLS SET STG_FILE_LOAD_FLG = 'N',CLMS_LOAD_FLAG = 'N' WHERE TRIM(STG_LOAD_DATE) <> TRIM(SYSDATE)";
				pstmtUpdtFlg = con.prepareStatement(strUpdtStgQry);
				intUpdt = pstmtUpdtFlg.executeUpdate();
				logger.debug("intUpdt::::"+intUpdt);
				if(intUpdt > 0){
					con.commit();
				}else{
					con.rollback();
				}
				/*strUpdtClmsQry = "UPDATE CLMS_DATAMART_SUMMARY_DTLS SET CLMS_LOAD_FLAG = 'N' WHERE TRIM(CLMS_LOAD_DATE) <> TRIM(SYSDATE) and CLMS_LOAD_FLG IN ('Y','N') AND STG_FILE_LOAD_FLG='C'";
				pstmtUpdtFlg = con.prepareStatement(strUpdtLogQry);
				intUpdt = pstmtUpdtFlg.executeUpdate();
				if(intUpdt > 0){
					con.commit();
				}else{
					con.rollback();
				}*/
				/*strUpdtClmsMainQry = "UPDATE CLMS_DATAMART_SUMMARY_DTLS SET CLMS_LOAD_FLAG = 'C' WHERE TRIM(CLMS_LOAD_DATE) <> TRIM(SYSDATE) and CLMS_LOAD_FLG='Y' AND STG_FILE_LOAD_FLG IN ('N','Y')";
				pstmtUpdtFlg = con.prepareStatement(strUpdtLogQry);
				intUpdt = pstmtUpdtFlg.executeUpdate();
				if(intUpdt > 0){
					con.commit();
				}else{
					con.rollback();
				}*/
		}
		catch(SQLException ex){
			logger.error("This is a SQLException :: "+ex);
		}
		catch(Exception e)
		{
			logger.error("This is an Exception :: "+e);
		}
		finally
		{
			try{
				  if(pstmtInsrtLogDtls != null)
					{
					  pstmtInsrtLogDtls.close();
					  pstmtInsrtLogDtls = null;
					}
				  if(pstmtUpdtFlg != null)
					{
					  pstmtUpdtFlg.close();
					  pstmtUpdtFlg = null;
					}
				 if(con != null)
					{
					con.close();
					con=null;
					}
				}
				catch (SQLException e) 
				{
					logger.error("SQLException exception caught :: "+e);
				}
		}
	}
	
	/*public static void delParamRecs(String strParamFileName) {
		logger.error("Am inside method delete param records");
		Dao newConn = new Dao();
		int intDelParam=0;
		try{
		con = Dao.createConnection();
		String strDelQry = "DELETE FROM "+strParamFileName+"";
		pstmtDel = con.prepareStatement(strDelQry);
		intDelParam = pstmtDel.executeUpdate();
			if(intDelParam > 0){
				con.commit();
			}else{
				con.rollback();
			}
		}
		catch(SQLException ex){
			logger.error("SQLException is :: "+ex);
		}
		catch(Exception e)
		{
			logger.error("Exception is:: "+e);
		}
		finally
		{
			try{
				  if(pstmtInsrtLogDtls != null)
					{
					  pstmtInsrtLogDtls.close();
					  pstmtInsrtLogDtls = null;
					}
				  if(pstmtUpdtFlg != null)
					{
					  pstmtUpdtFlg.close();
					  pstmtUpdtFlg = null;
					}
				 if(con != null)
					{
					con.close();
					con=null;
					}
				}
				catch (SQLException e) 
				{
					logger.error("SQLException exception caught"+e);
				}
		}
		
	}*/
	
	public static ArrayList getUnloadedFiles() 	{
		logger.debug("Am inside getunloaded files details dtls");
		Dao newConn = new Dao();
		ArrayList arrFileNames = new ArrayList();
		String strFileQuery="";
		try{
			//strFileQuery = "SELECT STG_PARAM_FILENAME FROM CLMS_DATAMART_SUMMARY_DTLS WHERE CLMS_LOAD_FLAG = 'N'";
			//strFileQuery = "SELECT STG_PARAM_FILENAME FROM CLMS_DATAMART_SUMMARY_DTLS WHERE CLMS_LOAD_FLAG = 'N' OR STG_FILE_LOAD_FLG = 'N'";
			strFileQuery = "SELECT STG_PARAM_FILENAME FROM CLMS_DATAMART_SUMMARY_DTLS WHERE STG_FILE_LOAD_FLG = 'N'";
			con = Dao.createConnection();
			//logger.debug("Connection established...."+Dao.createConnection());
			pstmtFile = con.prepareStatement(strFileQuery);
			rsFileNames = pstmtFile.executeQuery();
			while (rsFileNames.next())
			{
				arrFileNames.add(rsFileNames.getString(1));
				//logger.debug("Araay established...."+arrFileNames.size());
			}
			
		}catch(SQLException e){
			logger.error("SQLException exception caught"+e);
		}finally
		{
			try{
				 if(rsFileNames != null)
				 	{
					 rsFileNames.close();
					 rsFileNames = null;
				 	}
				 if(pstmtFile != null)
					{
					 pstmtFile.close();
					 pstmtFile = null;
					}
				 if(con != null)
					{
					con.close();
					con=null;
					}
				}
				catch (SQLException e) 
				{
					logger.error("SQLException exception caught"+e);
				}
		}
		
		return arrFileNames;
		
	}
	 // This method getting uploaded TextFile names for cleaning two (Date -3) Text files 
	public static ArrayList getUploadedFiles() 	{
		logger.debug("Am inside getUploaded files details dtls");
		Dao newConn = new Dao();
		ArrayList arrFileNames = new ArrayList();
		String strFileQuery="";
		try{
			//strFileQuery = "SELECT PARAM_FILENAME FROM TBL_LOG_DTLS ";
			strFileQuery = "SELECT PARAM_FILENAME FROM CLMS_DATAMART_SUMMARY_DTLS";
			con = Dao.createConnection();
			//logger.debug("Connection established...."+Dao.createConnection());
			pstmtFile = con.prepareStatement(strFileQuery);
			rsFileNames = pstmtFile.executeQuery();
			while (rsFileNames.next())
			{
				arrFileNames.add(rsFileNames.getString(1));
				//logger.debug("Araay established...."+arrFileNames.size());
			}
			
		}catch(SQLException e){
			logger.error("SQLException exception caught"+e);
		}finally
		{
			try{
				 if(rsFileNames != null)
				 	{
					 rsFileNames.close();
					 rsFileNames = null;
				 	}
				 if(pstmtFile != null)
					{
					 pstmtFile.close();
					 pstmtFile = null;
					}
				 if(con != null)
					{
					con.close();
					con=null;
					}
				}
				catch (SQLException e) 
				{
					logger.error("SQLException exception caught"+e);
				}
		}
		
		return arrFileNames;
		
	}
	
	//Applicable only for Thailand
	public static void transform(File source, String srcEncoding, File target, String tgtEncoding) throws IOException {
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(source), srcEncoding));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), tgtEncoding));) {
            char[] buffer = new char[16384];
            int read;
            while ((read = br.read(buffer)) != -1) {
                bw.write(buffer, 0, read);
            }
            br.close();
            bw.close();
            
            if(source.delete()){
                target.renameTo(source);
            }
        }
    }

}
