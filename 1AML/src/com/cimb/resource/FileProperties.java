package com.cimb.resource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
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
	public static CallableStatement cstmtExecParam = null;
	public static CallableStatement cstmtExecLosParam = null;
	public static ResultSet rsPrevDtRecs = null;
	public static ResultSet rsFileNames = null;
	private static String strInsertLogQry="";
	private static String strUpdtLogQry=""; 
	final static Logger logger = Logger.getLogger(FileProperties.class);
	private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
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
			//if (new File(CompleteFilePath + ".bad").isFile()) {
				lnr = new LineNumberReader(new FileReader(new File(CompleteFilePath + ".bad")));

				while ((lineRead = lnr.readLine()) != null) {
					//errorList.add(lineRead);
				}
				cnt = lnr.getLineNumber();
				logger.debug("Error through sql ldr is ::"+cnt);
			//}

			if (new File(CompleteFilePath + ".dis").isFile()) {
				lnr = new LineNumberReader(new FileReader(new File(CompleteFilePath + ".dis")));

				while ((lineRead = lnr.readLine()) != null) {
					//errorList.add(lineRead);
				}
				cnt = lnr.getLineNumber();
				logger.debug("Count of records from bad file"+cnt);
			}

		} catch (IOException e) {
			MySQLLoaderException.writeExceptionInFile("FileProperties.getErrorRecords :", e.toString());
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
		
	
	public static void updateUploadDtls(String strCntrlFile,String strParamFile,String strUploadFlag,int intTotNoOfRecs,int intTotalUploadRecs,int intTotErrRecs,String strExcDtls,String strLogDtls,String fileName){
		logger.debug("Am inside Method to Update Record Details");
		Dao newConn = new Dao();
		int intPrevDtRecs=0;
		
	try{
		con = Dao.createConnection();
		String strPrevDtRecsQry="SELECT TOTAL_NUMB_RECORDS FROM TBL_LOG_BKUP_DTLS WHERE LOAD_DATE=trim(SYSDATE-1) AND CTRLFILENAME='"+strCntrlFile+"'";
		pstmtPrevRecs = con.prepareStatement(strPrevDtRecsQry);
		rsPrevDtRecs = pstmtPrevRecs.executeQuery();
		while(rsPrevDtRecs.next())
		{
			intPrevDtRecs = rsPrevDtRecs.getInt(1);
		}
		logger.debug("Update Record Details   -:"+fileName);
		String strUpdtTblQuery = "UPDATE TBL_LOG_DTLS SET FILE_LOAD_FLG ='"+strUploadFlag+"',TOTAL_NUMB_RECORDS="+intTotNoOfRecs+", NUMB_RECORDS_LOADED="+intTotalUploadRecs+","
				+ "NUMB_ERR_RECS="+intTotErrRecs+",PREV_TOT_RECORDS="+intPrevDtRecs+",LOAD_DATE=?, EXCEPTION_DTLS='"+strExcDtls+"',"
				+ "LOG_DTLS='"+strLogDtls+"' WHERE CTRLFILENAME ='"+strCntrlFile+"' AND PARAM_FILENAME='"+fileName+"'";
		pstmt = con.prepareStatement(strUpdtTblQuery);
		pstmt.setDate(1,getCurrentDate());
		int intInsrtUpldDtls = pstmt.executeUpdate();
		logger.debug("intInsrtUpldDtls   -:"+intInsrtUpldDtls);
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
			logger.error("This is a SQLException :: "+ex);
		}
		catch(Exception e)
		{
			logger.error("This is an Exception :: "+e);
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
					logger.error("This is a SQLException exception :: "+e);
				}
		}

	}
	
	public static void insertLogBkpDtls(String fileName){
		logger.debug("Am inside insertlog bkup dtls");
		Dao newConn = new Dao();
		int intInsrt=0;
		int intUpdt = 0;
	try{
		con = Dao.createConnection();
		//strInsertLogQry="INSERT INTO TBL_LOG_BKUP_DTLS (SELECT * FROM TBL_LOG_DTLS WHERE TRIM(LOAD_DATE)= TRIM(SYSDATE-1))";
		
		strInsertLogQry="INSERT INTO TBL_LOG_BKUP_DTLS (SELECT * FROM TBL_LOG_DTLS WHERE FILE_LOAD_FLG = 'Y' AND PARAM_FILENAME='"+fileName+"' AND TRIM(LOAD_DATE) <> TRIM(SYSDATE))";
		
		pstmtInsrtLogDtls = con.prepareStatement(strInsertLogQry);
		intInsrt = pstmtInsrtLogDtls.executeUpdate();
			if(intInsrt > 0)
			{
				con.commit();
				strUpdtLogQry = "UPDATE TBL_LOG_DTLS SET FILE_LOAD_FLG = 'N' WHERE AND PARAM_FILENAME='"+fileName+"' TRIM(LOAD_DATE) <> TRIM(SYSDATE) and FILE_LOAD_FLG='Y'";
				pstmtUpdtFlg = con.prepareStatement(strUpdtLogQry);
				intUpdt = pstmtUpdtFlg.executeUpdate();
				if(intUpdt > 0){
					con.commit();
				}else{
					con.rollback();
				}
			}else{
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
	
	public static ArrayList getUnloadedFiles(String fileName) 	{
		logger.debug("Am inside getunloaded files details dtls");
		Dao newConn = new Dao();
		ArrayList<String> arrFileNames = new ArrayList<String>();
		String strFileQuery="";
		try{
			strFileQuery = "SELECT PARAM_FILENAME FROM TBL_LOG_DTLS WHERE FILE_LOAD_FLG = 'N' AND PARAM_FILENAME ='"+fileName+"'";
			con = Dao.createConnection();
			pstmtFile = con.prepareStatement(strFileQuery);
			rsFileNames = pstmtFile.executeQuery();
			while (rsFileNames.next())
			{
				arrFileNames.add(rsFileNames.getString(1));
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

	
	public static void execParamDownload(){
		logger.debug("Am inside execParamDownload Procedure");
		Dao newConn = new Dao();
		try{
			con = Dao.createConnection();
			cstmtExecParam = con.prepareCall("{call CLMS_MASTER_PARAMETER_DOWNLOAD()}");
			cstmtExecParam.execute();
			cstmtExecLosParam = con.prepareCall("{call CLMS_LOS_MASTER_PARAM_DOWNLOAD()}");
			cstmtExecLosParam.execute();
		}
		catch (SQLException e) 
		{
			logger.error("SQLException exception caught in Proc Call"+e);
		}
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
