package com.cimb.resource;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.cimb.dao.Dao;

public class LoadDMProc {

	public static Connection con = null;
	Statement stmt = null;
	public static CallableStatement cstmtExecDMParam = null;
	final static Logger logger = Logger.getLogger(FilePropertiesBKUP.class);

	public static void execDMMainData(){
		logger.debug("Am inside Calling of generic Procedure execDMMainData for Datamart");

		try{
			con = Dao.createConnection();
			cstmtExecDMParam = con.prepareCall("{call CALL_GENERIC_PROC()}");
			cstmtExecDMParam.execute();
			
			logger.debug("Procedure for DM Target table executed successfully");
		}
		catch (SQLException e) 
		{
			logger.error("SQLException exception caught in Proc Call"+e);
		}
	}
	
	public static void main(String args[]) throws IOException, Exception {

		LoadDMProc loadDMProc = new LoadDMProc();
			
		LoadDMProc.execDMMainData();
		
		}
}
