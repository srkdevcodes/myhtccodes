package com.cimb.resource;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.cimb.dao.Dao;

public class LoadDiffData {
	public static Connection con = null;
	Statement stmt = null;
	public static CallableStatement cstmtExecDiffData = null;
	final static Logger logger = Logger.getLogger(FilePropertiesBKUP.class);

	public static void execDiffTblLoad(){
		logger.debug("Am inside execDiffTblLoad Procedure for LOS");
		
		try{
			con = Dao.createConnection();
			cstmtExecDiffData = con.prepareCall("{call DATAMART_UPDATE_GENRIC_PROC()}");
			cstmtExecDiffData.execute();
		logger.debug("Procedure for Updating differential table data executed successfully");
		}
		catch (SQLException e) 
		{
			logger.error("SQLException exception caught in Proc Call"+e);
		}
	}
	
	public static void main(String args[]) throws IOException, Exception {

		LoadDiffData loadMonthlyProc = new LoadDiffData();
			
		LoadDiffData.execDiffTblLoad();
		
		}
}
