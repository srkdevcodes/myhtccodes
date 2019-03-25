package com.cimb.sqlloader;

import com.cimb.dao.Dao;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

public class LoadAMLProc
{
// public LoadAMLProc() {}
  
  public static Connection con = null;
  Statement stmt = null;
  public static CallableStatement cstmtExecAMLParam = null;
  static final Logger logger = Logger.getLogger(LoadAMLProc.class);
  
  public void execAMLParam() {
    logger.debug("Am inside execParamDownload Procedure for AML");
   // Dao newConn = new Dao();
    try {
      con = Dao.createConnection();
      cstmtExecAMLParam = con.prepareCall("{call AML_RISK_PROC()}");
      cstmtExecAMLParam.execute();
      
      logger.debug("Procedure for AML executed successfully");
    }
    catch (SQLException e)
    {
      logger.error("SQLException exception caught in Proc Call" + e);
    }
  }
  
  public static void main(String[] args) throws IOException, Exception
  {
    LoadAMLProc loadAMLProc = new LoadAMLProc();
    loadAMLProc.execAMLParam();
  }
}
