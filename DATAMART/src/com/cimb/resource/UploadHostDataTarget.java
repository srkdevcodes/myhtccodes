package com.cimb.resource;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

import com.cimb.dao.Dao;
import com.cimb.sqlloader.LoadData_bkup;

public class UploadHostDataTarget {
	public static Connection con = null;
	static ResourceBundle resourceBundle = ResourceBundle.getBundle("Datamart");
	final static Logger logger = Logger.getLogger(UploadHostDataTarget.class);
	public static PreparedStatement pstmtValUpldRecs = null;
	public static ResultSet rsValUpldRecs = null;

	public static void main(String args[]) throws IOException, Exception {
		//CallableStatement cstmtExecUpldProc = new CallableStatement();
		logger.debug("Am inside Main Method");
		Dao newConn = new Dao();
		try{
		con = Dao.createConnection();
		String strSrcSystem = resourceBundle.getString("SRCSYSTEM");
		String strSelectLogQry="SELECT CASE WHEN (SELECT COUNT(*) FROM CLMS_DATAMART_SUMMARY_DTLS where SOURCE_SYSTEM = ?)=(SELECT COUNT(*) FROM CLMS_DATAMART_SUMMARY_DTLS WHERE STG_FILE_LOAD_FLG = 'Y' AND SOURCE_SYSTEM = ?) THEN 1 ELSE 0 END AS FLAG FROM DUAL";
		pstmtValUpldRecs = con.prepareStatement(strSelectLogQry);
		pstmtValUpldRecs.setString(1,strSrcSystem);
		pstmtValUpldRecs.setString(2, strSrcSystem);
		rsValUpldRecs = pstmtValUpldRecs.executeQuery();
		//Temp value set for testing
		strSelectLogQry = "1";
		if(strSelectLogQry.equals("1"))
		{
			//con = Dao.createConnection();
			CallableStatement cstmtExecUpldProc = con.prepareCall("{call CALL_GENERIC_PROC()}");
			cstmtExecUpldProc.execute();
			logger.debug("Procedure for Upload into Target Tables executed successfully");
		}else{
		logger.info("call genric procedure not calling....");
		}
		}
		catch (SQLException e) 
		{
			logger.error("SQLException exception caught in Proc Call"+e);
		}
		
		}

	}

