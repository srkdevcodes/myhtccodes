package com.cimb.icres.req;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;

import javax.sql.rowset.serial.SerialClob;

import org.apache.log4j.Logger;

import com.cimb.icres.dao.Dao;

import oracle.jdbc.driver.OracleConnection;

public class GenXmlJDBCImpl  extends Dao{
	public static Connection con = null;
	public static PreparedStatement pstmt = null;
	private final static String CIF_OUT_FILE = "SELECT * FROM ( SELECT temp.*, ROW_NUMBER() OVER (PARTITION BY CIMB_CUSTOMERNUMBER ORDER BY LAST_UPDATED_DATE DESC) AS rn  FROM UPDATE_CUSTOMER_INFO_TEMP temp WHERE  temp.NOTIFY_FLAG = 'N' OR (temp.NOTIFY_FLAG = 'Y' and temp.UPDT_FLAG='N') AND COUNTER < 5 ) WHERE rn =1";
	final static Logger logger = Logger.getLogger(GenXmlJDBCImpl.class);
	public static PreparedStatement pstmtPrevRecs = null;
	public static ResultSet rs = null;
	public static ResultSet rsno = null;
	private static final String EMPTY_STRING = "";
	
	OracleConnection conn;
	
	private static final SimpleDateFormat sdf  =  new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	
	public HashSet<TempModel> getTempTableData() throws ParseException, IOException
	{
		
				
		logger.debug("Am inside execute temptable query");
		HashSet<TempModel> arrFileNames = new HashSet<TempModel>();
		try{
			con = Dao.createConnection();
			pstmtPrevRecs = con.prepareStatement(CIF_OUT_FILE);
			rs = pstmtPrevRecs.executeQuery();
			
			while (rs.next()){
				
				TempModel model = new TempModel();
				String id = denullify(rs.getString("ID"));
				logger.debug(id);
				model.setId(Integer.parseInt(id));
				
				String rquId = denullify(rs.getString("RQUID"));
				logger.debug(rquId);
				model.setRquId(rquId);
				
				String claimId = denullify(rs.getString("CLAIM_ID"));
				//logger.debug(claimId);
				model.setClaimId(claimId);
				
				String notifyFalg = denullify(rs.getString("NOTIFY_FLAG"));
				//logger.debug(notifyFalg);
				model.setNotifyFalg(notifyFalg);
				
				Timestamp creationDate = rs.getTimestamp("CREATION_DATE");
				//logger.debug(creationDate);
				model.setCreationDate(creationDate);
				
				Timestamp lastUpdateDdate = rs.getTimestamp("LAST_UPDATED_DATE");
				model.setLastUpdateDdate(lastUpdateDdate);
				
				String customerName = denullify(rs.getString("CUSTOMER_NAME"));
				model.setCustomerName(customerName);
				
				String  TcusomterName= denullify(rs.getString("CIMB_TCUSTOMERNAME"));
				model.setTcusomterName(TcusomterName);
				
				String  customerNumber= denullify(rs.getString("CIMB_CUSTOMERNUMBER"));
				//logger.debug(customerNumber);
				model.setCustomerNumber(customerNumber);
				
				String sectorCode = denullify(rs.getString("CIMB_SECTORCODE"));
				if(!sectorCode.isEmpty()){
					if(sectorCode.contains("-")){
					String[] secArray = sectorCode.split("-");
					model.setSectorCode(secArray[0].replaceAll("\\s",""));
					//logger.debug(model.getSectorCode());
					}else{
						model.setSectorCode(sectorCode);
					}
				}
				
				String businessType = denullify(rs.getString("CIMB_BUSINESSTYPE"));
				//logger.debug(businessType);
				model.setBusinessType(businessType);
				
				String cifId = denullify(rs.getString("CIMB_CIFID"));
				model.setCifId(cifId);
				
				String updateFlag = denullify(rs.getString("UPDT_FLAG"));
				model.setUpdateFlag(updateFlag);
				
				/*Clob xmlRequest = rs.getClob("XML_RESPONSE");
				model.setXmlRequest(xmlRequest);
				
				Clob xmlResponse = rs.getClob("XML_REQUEST");
				model.setXmlResponse(xmlResponse);*/
				
				String remarks = denullify(rs.getString("EXCEPTION_REMARK"));
				model.setRemarks(remarks);
				
				String customerId = denullify(rs.getString("CUSTOMER_ID"));
				model.setCustomerId(customerId);
				
				String counterStr = denullify(rs.getString("COUNTER"));
				model.setCounter(Integer.valueOf(counterStr));
				
				//if(!arrFileNames.contains(model.getCustomerNumber())){
					//logger.debug(model.getCustomerNumber());
				arrFileNames.add(model);
				//}
			}
			logger.debug("SIZE ---"+arrFileNames.size());
		}catch(SQLException e){
			logger.error("SQLException exception caught"+e.getMessage());
		}
		return arrFileNames;
	}
	
	private String denullify(String value) {
		return value == null ? EMPTY_STRING : value;
	}
	
	/*public void upateTemptable(Clob source, String cifId) throws SQLException
	{
		StringBuffer UPDATE_SQL =  new StringBuffer("");
		UPDATE_SQL.append("UPDATE TEMP_TABLE SET NOTIFY_FLAG='Y', SET LASTUPDATED_DATE=SYSFDATE , SET XML_REQUEST ="+source+"  "  );
		UPDATE_SQL.append("WHERE CIMB_CIFID ="+cifId );
		pstmt = con.prepareStatement(UPDATE_SQL.toString());
		System.out.println(UPDATE_SQL.toString());
		int intInsrtUpldDtls = pstmt.executeUpdate();
		if(intInsrtUpldDtls > 0)
			{
				con.commit();
			}
			else
			{
				con.rollback();
			}
	}*/
	
	
	
	public void upateTemptable(String message, String rquId, Integer id) throws Exception {
		logger.info("Im in updating temptable....!");
	    PreparedStatement pstmt = null;
	    int intUpdt = 0;
	    try {
	             
	        String sql = "UPDATE UPDATE_CUSTOMER_INFO_TEMP  set CIMB_REQ = ?, NOTIFY_FLAG ='Y',  CLAIM_ID =?, RQUID = ?, LAST_UPDATED_DATE=SYSDATE WHERE ID ='"+id+"'";
	       // logger.info("Message :"+message);
	        StringReader clob = new StringReader(message);
	       // logger.info("clob :"+clob);
	        con = Dao.createConnection();
	        //logger.info("Query"+ sql);
	        pstmt = con.prepareStatement(sql); 
	       // pstmt.setString(1, "'Y'"); 
	        pstmt.setNString(1, message); 
	        pstmt.setString(2, rquId);
	        pstmt.setString(3, rquId);
	        intUpdt = pstmt.executeUpdate();
	        if(intUpdt > 0){
	        	logger.info("commit");
				con.commit();
			}else{
				logger.info("rollback");
				con.rollback();
			}
	    } catch (SQLException e) {
	    	System.out.println("SQL exception: " + e.toString());
	    } catch (Exception ex) {
	      System.out.println("Unexpected exception: " + ex.toString());
	    } finally {
	        if (pstmt != null)pstmt.close();
	    }
	}
	
	public void upateCustNotifyFlag(String id) throws Exception {
		logger.info("Im in updating counter temptable....!");
	    PreparedStatement pstmt = null;
	    String sql = "";
	    int intUpdt = 0;
	    try {
	        
	        sql = "UPDATE UPDATE_CUSTOMER_INFO_TEMP set NOTIFY_FLAG ='Y', LAST_UPDATED_DATE=SYSDATE WHERE CUSTOMER_ID ='"+id+"'";	
	        
	        con = Dao.createConnection();
	       // logger.info("Query"+ sql);
	        pstmt = con.prepareStatement(sql); 
	        intUpdt = pstmt.executeUpdate();
	        if(intUpdt > 0){
	        	logger.info("commit");
				con.commit();
			}else{
				logger.info("rollback");
				con.rollback();
			}
	    } catch (SQLException e) {
	    	System.out.println("SQL exception: " + e.toString());
	    } catch (Exception ex) {
	      System.out.println("Unexpected exception: " + ex.toString());
	    } finally {
	        if (pstmt != null)pstmt.close();
	    }
	}
	
	public void upateResTemptable(String message,String flag, String statusDesc, Integer id,Integer count) throws Exception {
		logger.info("Im in updating response into temptable....!");
	    PreparedStatement pstmt = null;
	    int intUpdt = 0;
	    try {
	             
	        String sql = "UPDATE UPDATE_CUSTOMER_INFO_TEMP  set CIMB_RES = ?, UPDT_FLAG = ?, EXCEPTION_REMARK = ?, COUNTER = ?, LAST_UPDATED_DATE=SYSDATE  WHERE ID ="+id;
	       // logger.info("Message :"+message);
	        StringReader clob = new StringReader(message);
	       // logger.info("clob :"+clob);
	        con = Dao.createConnection();
	       // logger.info("Query"+ sql);
	        pstmt = con.prepareStatement(sql); 
	       // pstmt.setString(1, "'Y'"); 
	        pstmt.setNString(1, message); 
	        pstmt.setString(2, flag);
	        pstmt.setString(3, statusDesc);
	        pstmt.setInt(4, count+1);
	        //pstmt.executeUpdate(); 
	        intUpdt = pstmt.executeUpdate();
	        if(intUpdt > 0){
	        	logger.info("commit");
				con.commit();
			}else{
				logger.info("rollback");
				con.rollback();
			}
	    } catch (SQLException e) {
	    	System.out.println("SQL exception: " + e.toString());
	    } catch (Exception ex) {
	      System.out.println("Unexpected exception: " + ex.toString());
	    } finally {
	        if (pstmt != null)pstmt.close();
	    }
	}
	
	
	@SuppressWarnings("unused")
	private Clob stringToClob(String source){
    	try {
    		return new SerialClob(source.toCharArray());
		} catch (Exception e) {
			logger.error("Cloud not convert string to CLOB...");
			return null;
		}
    }
	
	/*Prashanti [21112017] Changed the String argument from rquId to strCustId since the argument was giving a different description as cust id needs to be passed*/
	
	/*public int getRaingId(String strCustId){
		int intUpdt = 0;
		//Random random = new Random();
		 try {
			 logger.info(" Customer ID ---> "+ strCustId);
		      //  String sql = "SELECT ID FROM SML_RATING_SELECTION WHERE CUSTOMER_ID ="+rquId+" AND STATUS IN ('COMPLETED','SUCCESS')";
			// String sql = "SELECT MAX(SELECTION_ID)  FROM SML_RATING_REPORT WHERE CUSTOMER_ID ="+rquId+"  AND EXTN_STATUS IN ('PROPOSED','EVALUATED','APPROVED') GROUP BY CUSTOMER_ID";
			 Prashanti [21112017] Commented the above query and changed to below query to fetch the Rqid of atleast one successful rating for a customer 
			 String sql = "SELECT ID FROM (SELECT TEMP.*,ROW_NUMBER() OVER (PARTITION BY CUSTOMER_ID ORDER BY LAST_UPDATE_DATE DESC) AS RN FROM SML_RATING_SELECTION TEMP WHERE CUSTOMER_ID ="+strCustId+"  AND STATUS <> 'FAILED') WHERE RN=1";
		        con = Dao.createConnection();
		       // logger.info("Query"+ sql);
		        pstmt = con.prepareStatement(sql); 
		        rsno = pstmt.executeQuery();
		        while (rsno.next()) {
		        	//String[] rqno = rsno.getString(1).split("MY");
		        	//intUpdt = Integer.parseInt(rsno.getString(1).replaceAll("[\\D]", ""));
		        	intUpdt = rsno.getInt(1);
		        	logger.info(" RATING SELECTION ID  --- "+ intUpdt);
				}
		       
		    } catch (SQLException e) {
		    	System.out.println("SQL exception: " + e.toString());
		    } catch (Exception ex) {
		      System.out.println("Unexpected exception: " + ex.toString());
		    } finally {
		        //if (pstmt != null)pstmt.close();
		    }
		
		return intUpdt;
		
	}*/

	@SuppressWarnings("unused")
	private String readFile(String fileName, Writer writerArg)
	        throws FileNotFoundException, IOException {
	
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    String nextLine = "";
	    StringBuffer sb = new StringBuffer();
	    while ((nextLine = br.readLine()) != null) {
	        System.out.println("Writing: " + nextLine);
	        writerArg.write(nextLine);
	        sb.append(nextLine);
	    }
	    // Convert the content into to a string
	    String clobData = sb.toString();
	
	    // Return the data.
	    return clobData;
	}

	public void updateAllCustomerRecords(String statusDesc,String CustId) throws Exception {
		logger.info("Im in updating customer records reponse is success ....!");
	    PreparedStatement pstmt = null;
	    int intUpdt = 0;
	    try {
	             
	        String sql = "UPDATE UPDATE_CUSTOMER_INFO_TEMP SET UPDT_FLAG ='Y', LAST_UPDATED_DATE=SYSDATE  WHERE CUSTOMER_ID ="+CustId;
	        con = Dao.createConnection();
	       // logger.info("Query"+ sql);
	        pstmt = con.prepareStatement(sql); 
	        //pstmt.setString(1, statusDesc);
	        intUpdt = pstmt.executeUpdate();
	        if(intUpdt > 0){
	        	logger.info("commit");
				con.commit();
			}else{
				logger.info("rollback");
				con.rollback();
			}
	    } catch (SQLException e) {
	    	System.out.println("SQL exception: " + e.toString());
	    } catch (Exception ex) {
	      System.out.println("Unexpected exception: " + ex.toString());
	    } finally {
	        if (pstmt != null)pstmt.close();
	    }
	}
}
