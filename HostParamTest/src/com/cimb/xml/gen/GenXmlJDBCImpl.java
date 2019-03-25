package com.cimb.xml.gen;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialClob;

import org.apache.log4j.Logger;

import com.cimb.dao.Dao;

import oracle.jdbc.driver.OracleConnection;

public class GenXmlJDBCImpl  extends Dao{
	public static Connection con = null;
	public static PreparedStatement pstmt = null;
	private final static String CIF_OUT_FILE = "SELECT * FROM TEMP_TABLE WHERE NOTIFY_FLAG ='N' ";
	final static Logger logger = Logger.getLogger(GenXmlJDBCImpl.class);
	public static PreparedStatement pstmtPrevRecs = null;
	public static ResultSet rs = null;
	private static final String EMPTY_STRING = "";
	
	OracleConnection conn;
	
	private static final SimpleDateFormat sdf  =  new SimpleDateFormat("yyyyMMdd");
	
	public List<TempModel> getTempTableData() throws ParseException, IOException
	{
		
				
		logger.debug("Am inside execute temptable query");
		List<TempModel> arrFileNames = new ArrayList<TempModel>();
		try{
			con = Dao.createConnection();
			pstmtPrevRecs = con.prepareStatement(CIF_OUT_FILE);
			rs = pstmtPrevRecs.executeQuery();
			
			while (rs.next()){
				TempModel model = new TempModel();
				String rquId = denullify(rs.getString("RQUID"));
				logger.debug(rquId);
				model.setRquId(rquId);
				
				String claimId = denullify(rs.getString("CLAIM_ID"));
				//logger.debug(claimId);
				model.setClaimId(claimId);
				
				String notifyFalg = denullify(rs.getString("NOTIFY_FLAG"));
				//logger.debug(notifyFalg);
				model.setNotifyFalg(notifyFalg);
				
				String creationDate = denullify(rs.getString("CREATION_DATE"));
				//logger.debug(creationDate);
				model.setCreationDate(sdf.parse(creationDate));
				
				String lastUpdateDdate = denullify(rs.getString("LAST_UPDATED_DATE"));
				model.setLastUpdateDdate(sdf.parse(lastUpdateDdate));
				
				String customerName = denullify(rs.getString("CUSTOMER_NAME"));
				model.setCustomerName(customerName);
				
				String  TcusomterName= denullify(rs.getString("CIMB_TCUSTOMERNAME"));
				model.setTcusomterName(TcusomterName);
				
				String  customerNumber= denullify(rs.getString("CIMB_CUSTOMERNUMBER"));
				//logger.debug(customerNumber);
				model.setCustomerNumber(customerNumber);
				
				String sectorCode = denullify(rs.getString("CIMB_SECTORCODE"));
				//logger.debug(sectorCode);
				model.setSectorCode(sectorCode);
				
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
				
				arrFileNames.add(model);
			}
			logger.debug("SIZE ---"+arrFileNames.size());
		}catch(SQLException e){
			logger.error("SQLException exception caught"+e);
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
	
	
	
	public void upateTemptable(String message, String rquId, String customerNo) throws Exception {
		logger.info("Im in updating temptable....!");
	    PreparedStatement pstmt = null;
	    int intUpdt = 0;
	    try {
	             
	        String sql = "UPDATE TEMP_TABLE  set CIMB_REQ = ?, NOTIFY_FLAG ='Y', RQUID = ?  WHERE CIMB_CUSTOMERNUMBER ="+customerNo;
	        logger.info("Message :"+message);
	        StringReader clob = new StringReader(message);
	        logger.info("clob :"+clob);
	        con = Dao.createConnection();
	        logger.info("Query"+ sql);
	        pstmt = con.prepareStatement(sql); 
	       // pstmt.setString(1, "'Y'"); 
	        pstmt.setNString(1, message); 
	        pstmt.setString(2, rquId);
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
	
	
	
	private Clob stringToClob(String source){
    	try {
    		return new SerialClob(source.toCharArray());
		} catch (Exception e) {
			logger.error("Cloud not convert string to CLOB...");
			return null;
		}
    	
    	
    	
    	
    	
    }

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


}
