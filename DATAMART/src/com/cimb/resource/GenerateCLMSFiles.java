package com.cimb.resource;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.cimb.dao.Dao;

public class GenerateCLMSFiles {
	public static Connection con = null;
	Statement stmt = null;
	public static CallableStatement cstmtExecClmsProc = null;
	final static Logger logger = Logger.getLogger(FilePropertiesBKUP.class);
	
	public static void execCLMSProc(){
		logger.debug("Am inside execCLMSProc Procedure for DM");

		try{
			con = Dao.createCLMSConnection();
			cstmtExecClmsProc = con.prepareCall("{call MAIN_RDM_FILECREATOR()}");
			cstmtExecClmsProc.execute();
		logger.debug("Procedure for generating clms flat files executed successfully");
		}
		catch (SQLException e) 
		{
			logger.error("SQLException exception caught in Proc Call"+e);
		}
	}
	
	public static void main(String args[]) throws IOException, Exception {

		GenerateCLMSFiles genCLMSProc = new GenerateCLMSFiles();
			
		GenerateCLMSFiles.execCLMSProc();
		
		}
}
