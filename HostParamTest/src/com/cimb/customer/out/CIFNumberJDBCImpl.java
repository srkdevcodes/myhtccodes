package com.cimb.customer.out;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cimb.dao.Dao;

public class CIFNumberJDBCImpl  extends Dao{
	public static Connection con = null;
	private static final String EMPTY_STRING = "";
	private static final BigDecimal DEF_BIG_DECIMAL = new BigDecimal(0);
	private final static String CIF_OUT_FILE = "SELECT CIF_NUMBER FROM SML_CUSTOMER WHERE DEPRECATED='N'";
	private final static String NEW_CIF_OUT_FILE = "SELECT CIF_NUMBER FROM SML_CUSTOMER WHERE DEPRECATED='N' AND CREATION_DATE <= trunc(sysdate)";
	final static Logger logger = Logger.getLogger(CIFNumberJDBCImpl.class);
	public static PreparedStatement pstmtPrevRecs = null;
	public static ResultSet rsFileNames = null;
	
	public List<String> getCifNumberOutData()
	{
		StringBuilder querySb = new StringBuilder(CIF_OUT_FILE);
				
		logger.debug("Am inside execute customer wuery");
		List<String> arrFileNames = new ArrayList<String>();
		try{
			con = createConnection();
			pstmtPrevRecs = con.prepareStatement(querySb.toString());
			rsFileNames = pstmtPrevRecs.executeQuery();
			while (rsFileNames.next())
			{
				arrFileNames.add(rsFileNames.getString(1));
			}
			
		}catch(SQLException e){
			logger.error("SQLException exception caught"+e);
		}
		return arrFileNames;
	}
	
	public List<String> getNewCifNumberOutData()
	{
		StringBuilder querySb = new StringBuilder(NEW_CIF_OUT_FILE);
				
		logger.debug("Am inside execute customer wuery");
		List<String> arrFileNames = new ArrayList<String>();
		try{
			con = createConnection();
			pstmtPrevRecs = con.prepareStatement(querySb.toString());
			rsFileNames = pstmtPrevRecs.executeQuery();
			while (rsFileNames.next())
			{
				arrFileNames.add(rsFileNames.getString(1));
			}
			
		}catch(SQLException e){
			logger.error("SQLException exception caught"+e);
		}
		return arrFileNames;
	}
	
	public void saveCIFFile(List<CIFNumberModel> modelList) throws SQLException{
		SequenceGenerator gen = new SequenceGenerator();
		String query = " insert into DM_CIFNUMBER_FILE (ID,RECORD_TYPE, CIF_NUMBER, STATUS, DESCRIPTION,CREATION_DATE)"
	               + " values (?, ?, ?, ?, ?, SYSDATE)";
		
		 // declare the preparedstatement reference
		  //PreparedStatement preparedStmt = null;
		  try
		  {
		    // create the preparedstatement before the loop
			con = createConnection();
			pstmtPrevRecs = con.prepareStatement(query);

		    // now loop through nearly 1,500 nodes in the list
		    for (CIFNumberModel n : modelList)
		    {	
		    	pstmtPrevRecs.setInt(1, gen.nextUniqueInt());
		    	pstmtPrevRecs.setString(2, n.getRecordType());
		    	pstmtPrevRecs.setString(3, n.getCifNumner());
		    	pstmtPrevRecs.setString(4, n.getStatus());
		    	pstmtPrevRecs.setString(5, n.getStatusDesc());
		    	pstmtPrevRecs.execute();           // the INSERT happens here
		    }
		  }
		  catch (SQLException se)
		  {
		    se.printStackTrace();
		    throw se;
		  }
		  finally
		  {
		    // close the statement when all the INSERT's are finished
			  pstmtPrevRecs.close();
		  }
	}
}
