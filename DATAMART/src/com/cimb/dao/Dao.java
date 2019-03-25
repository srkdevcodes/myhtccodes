/*
 * Decompiled with CFR 0_114.
 */
package com.cimb.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.cimb.sqlloader.LoadData_bkup;


public class Dao {
    public static Connection connection;
    public static Connection connClms;
    final static Logger logger = Logger.getLogger(Dao.class);
    static ResourceBundle resourceBundle = ResourceBundle.getBundle("Datamart");
    public static Connection createConnection(){
        try {
            String strURL = "";
            String strIP = "";
            String strPort = "";
            String strService = "";
            String strUserId = "";
            String strPwd = "";
            String strConnUrl = "";
            String key = "";
            String keypwd = "";
             connection = null; 
             System.out.println("Am inside DM conn");
              	strURL = resourceBundle.getString("URL");
              	strIP = resourceBundle.getString("IP");
              	strPort = resourceBundle.getString("PORT");
              	strService = resourceBundle.getString("SERVICE");
              	strUserId = resourceBundle.getString("USERID");
              	key = resourceBundle.getString("KEY");
              	keypwd = resourceBundle.getString("KEYPWD");
              	strPwd = getPassword(key, keypwd);
              	System.out.println("strPwd - : " + strPwd);
              	 Class.forName("oracle.jdbc.driver.OracleDriver");
              	strConnUrl = strURL + ":@" + strIP + ":" + strPort + ":" + strService;
              	logger.debug("strConnUrl  :"+strConnUrl);
            if (connection == null) {
                connection = DriverManager.getConnection(strConnUrl, strUserId.trim(), strPwd.trim());
               // connection = DriverManager.getConnection("jdbc:oracle:thin:@@10.104.122.91:1527:CLUATMY", strUserId, strPwd);
            }
        }
        catch (SQLException s) {
            logger.error("SQL Exception in getConnection :::" + s);
        }
        catch (Exception e) {
            logger.error("Error in getConnection ::::" + e);
        }
        return connection;
    }
    
    
    public static Connection createCLMSConnection(){
        try {
            String strCMURL = "";
            String strCMIP = "";
            String strCMPort = "";
            String strCMService = "";
            String strCMUserId = "";
            String strCMPwd = "";
            String strCMConnUrl = "";
            String keyCM = "";
            String keypwdCM = "";
                connClms = null;
               	System.out.println("Am inside CLMS conn");
              	strCMURL = resourceBundle.getString("URL_CM");
              	strCMIP = resourceBundle.getString("IP_CM");
              	strCMPort = resourceBundle.getString("PORT_CM");
              	strCMService = resourceBundle.getString("SERVICE_CM");
              	strCMUserId = resourceBundle.getString("USERID_CM");
              	keyCM = resourceBundle.getString("KEY_CM");
              	keypwdCM = resourceBundle.getString("KEYPWD_CM");
              	strCMPwd = getPassword(keyCM, keypwdCM);
              	 Class.forName("oracle.jdbc.driver.OracleDriver");
              	 
              	strCMConnUrl = strCMURL + ":@" + strCMIP + ":" + strCMPort + ":" + strCMService;
              	 if (connClms == null) {
                 	connClms = DriverManager.getConnection(strCMConnUrl, strCMUserId, strCMPwd);
                 }       
        }
        catch (SQLException s) {
            logger.error("SQL Exception in getConnection :::" + s);
        }
        catch (Exception e) {
            logger.error("Error in getConnection ::::" + e);
        }
        return connClms;
    }
    
    @SuppressWarnings("unused")
	private  String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    private static  byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }
    
    public static String getPassword(String key, String keypwd) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, FileNotFoundException, IOException{
   	// logger.info(" I am in Dao getPassword...............!");
   	 byte[] bytekey = hexStringToByteArray(key);
           SecretKeySpec sks = new SecretKeySpec(bytekey, LoadData_bkup.AES);
           Cipher cipher = Cipher.getInstance(LoadData_bkup.AES);
           cipher.init(Cipher.DECRYPT_MODE, sks);
           byte[] decrypted = cipher.doFinal(hexStringToByteArray(keypwd));
           String originalPassword = new String(decrypted);
          /* System.out.println("****************  Original Password  ****************");
           System.out.println(originalPassword);
           System.out.println("****************  Original Password  ****************");*/
   	return originalPassword;
   }
    
    
}