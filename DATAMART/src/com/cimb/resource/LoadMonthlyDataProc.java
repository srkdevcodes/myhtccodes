package com.cimb.resource;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.cimb.dao.Dao;

public class LoadMonthlyDataProc {
	public static Connection con = null;
	Statement stmt = null;
	public static CallableStatement cstmtExecMonthData = null;
	final static Logger logger = Logger.getLogger(FilePropertiesBKUP.class);

	public static void execMonthlyLoad(){
		logger.debug("Am inside execMonthlyLoad Procedure for LOS");
		
		try{
			con = Dao.createConnection();
			cstmtExecMonthData = con.prepareCall("{call DATAMART_GENRIC_MONTHLY_PROC()}");
			cstmtExecMonthData.execute();
		logger.debug("Procedure for Monthly data executed successfully");
		}
		catch (SQLException e) 
		{
			logger.error("SQLException exception caught in Proc Call"+e);
		}
	}
	
	public static void main(String args[]) throws IOException, Exception {

		LoadMonthlyDataProc loadMonthlyProc = new LoadMonthlyDataProc();
			
		LoadMonthlyDataProc.execMonthlyLoad();
		
		}
}
