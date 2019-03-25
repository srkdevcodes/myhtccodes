package com.cimb.customer.out;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.cimb.dao.Dao;

public class CIFNumberJDBCImpl  extends Dao{
	public static Connection con = null;
	private final static String CIF_OUT_FILE = "SELECT CIF_NUMBER FROM SML_CUSTOMER WHERE DEPRECATED='N'";
	private final static String NEW_CIF_OUT_FILE = "SELECT CIF_NUMBER,CREATION_DATE FROM SML_CUSTOMER WHERE DEPRECATED='N' AND CREATION_DATE <= trunc(sysdate) AND LENGTH(CIF_NUMBER)  >= 14 AND CIF_NUMBER IS NOT NULL GROUP BY CIF_NUMBER,CREATION_DATE";
	final static Logger logger = Logger.getLogger(CIFNumberJDBCImpl.class);
	public static PreparedStatement pstmtPrevRecs = null;
	public static PreparedStatement pstmtDel = null;
	public static ResultSet rsFileNames = null;
	private static final String EMPTY_STRING = "";
	private static final SimpleDateFormat sdf  =  new SimpleDateFormat("dd-MMM-yyyy");
	public List<String> getCifNumberOutData()
	{
		StringBuilder querySb = new StringBuilder(CIF_OUT_FILE);
				
		logger.debug("Am inside execute customer wuery");
		List<String> arrFileNames = new ArrayList<String>();
		try{
			con = createCLMSConnection();
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
	
	public List<CifNoModel> getNewCifNumberOutData() 
	{
		StringBuilder querySb = new StringBuilder(NEW_CIF_OUT_FILE);
				
		logger.debug("Am inside execute customer wuery");
		List<CifNoModel> arrFileNames = new ArrayList<CifNoModel>();
		try{
			con = createCLMSConnection();
			pstmtPrevRecs = con.prepareStatement(querySb.toString());
			rsFileNames = pstmtPrevRecs.executeQuery();
			while (rsFileNames.next())
			{
				CifNoModel model = new CifNoModel();
				String cifNo = denullify(rsFileNames.getString("CIF_NUMBER"));
				if(isNumber(cifNo) == true){
				model.setCifNo(cifNo);
				Timestamp curDate = rsFileNames.getTimestamp("CREATION_DATE");
				Date sdate = null;
				sdate = new Date(curDate.getTime());
				model.setCreationDt(sdate);
				arrFileNames.add(model);
				}
			}
			
		}catch(SQLException e){
			logger.error("SQLException exception caught"+e);
		}
		return arrFileNames;
	}
	
	@SuppressWarnings("unused")
	private boolean isNumber(String cifNo){
		
		//String string = "-1234.15";
        boolean numeric = false;
        numeric = cifNo.matches("-?\\d+(\\.\\d+)?");
        if(numeric){
          //  System.out.println(cifNo + " is a number");
        	numeric = true;
        }
        else{
           // System.out.println(cifNo + " is not a number");
        numeric = false;
        }
        
		return numeric;
	}
	
	private String denullify(String value) {
		return value == null ? EMPTY_STRING : value;
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
	
	public void saveCIFFile(List<CIFNumberModel> modelList) throws SQLException{
		SequenceGenerator gen = new SequenceGenerator();
		int intDelParam=0;
		String strDelQry = "truncate table DM_CIFNUMBER_FILE";
		pstmtDel = con.prepareStatement(strDelQry);
		intDelParam = pstmtDel.executeUpdate();
			if(intDelParam > 0){
				con.commit();
			}else{
				con.rollback();
			}
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
