/*
 * Decompiled with CFR 0_114.
 */
package com.cimb.icres.dao;

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





public class Dao {
    public static Connection connection;
    final static Logger logger = Logger.getLogger(Dao.class);
    public static final String AES = "AES";
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
             ResourceBundle resourceBundle = ResourceBundle.getBundle("ICRES");
              connection = null;
              	strURL = resourceBundle.getString("URL");
              	strIP = resourceBundle.getString("IP");
              	strPort = resourceBundle.getString("PORT");
              	strService = resourceBundle.getString("SERVICE");
              	strUserId = resourceBundle.getString("USERID");
              	//strPwd = resourceBundle.getString("PWD"); 
              	key = resourceBundle.getString("KEY");
              	keypwd = resourceBundle.getString("KEYPWD");
              	//logger.info("key  "+key);
              //	logger.info("keypwd  "+keypwd);
              	strPwd = getPassword(key, keypwd);
              	//logger.info("strPwd  "+strPwd);
              	strConnUrl = strURL + ":@" + strIP + ":" + strPort + ":" + strService;
 
            Class.forName("oracle.jdbc.driver.OracleDriver");
            if (connection == null) {
                connection = DriverManager.getConnection(strConnUrl, strUserId, strPwd);
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
	 logger.info(" I am in Dao getPassword...............!");
	 byte[] bytekey = hexStringToByteArray(key);
        SecretKeySpec sks = new SecretKeySpec(bytekey, Dao.AES);
        Cipher cipher = Cipher.getInstance(Dao.AES);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        byte[] decrypted = cipher.doFinal(hexStringToByteArray(keypwd));
        String originalPassword = new String(decrypted);
       /* System.out.println("****************  Original Password  ****************");
        System.out.println(originalPassword);
        System.out.println("****************  Original Password  ****************");*/
	return originalPassword;
}
}