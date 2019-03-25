package com.cimb.sqlloader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class GeneratePlainPassword {
	
	 public static final String AES = "AES";

	    @SuppressWarnings("unused")
		private static String byteArrayToHexString(byte[] b) {
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

	    private static byte[] hexStringToByteArray(String s) {
	        byte[] b = new byte[s.length() / 2];
	        for (int i = 0; i < b.length; i++) {
	            int index = i * 2;
	            int v = Integer.parseInt(s.substring(index, index + 2), 16);
	            b[i] = (byte) v;
	        }
	        return b;
	    }
	    
	    public static String getPassword(String key, String keypwd) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, FileNotFoundException, IOException{
	    		    	
	    	 byte[] bytekey = hexStringToByteArray(key);
		        SecretKeySpec sks = new SecretKeySpec(bytekey, GeneratePlainPassword.AES);
		        Cipher cipher = Cipher.getInstance(GeneratePlainPassword.AES);
		        cipher.init(Cipher.DECRYPT_MODE, sks);
		        byte[] decrypted = cipher.doFinal(hexStringToByteArray(keypwd));
		        String originalPassword = new String(decrypted);
		       /* System.out.println("****************  Original Password  ****************");
		        System.out.println(originalPassword);
		        System.out.println("****************  Original Password  ****************");*/
	    	return originalPassword;
	    }

	    public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, FileNotFoundException, IOException {
	    	ResourceBundle resourceBundle = ResourceBundle.getBundle("HostParam");
	        String tempkey = resourceBundle.getString("KEY");
	        String password = resourceBundle.getString("KEYPWD");
	        /*Properties prop = new Properties();
	        InputStream input = null;
	        input = new FileInputStream("c:/keypassword.properties");
	        // load a properties file
	        prop.load(input);
	        tempkey = prop.getProperty("Key");
	        password = prop.getProperty("Encrypted_Password");*/

	        byte[] bytekey = hexStringToByteArray(tempkey);
	        SecretKeySpec sks = new SecretKeySpec(bytekey, GeneratePlainPassword.AES);
	        Cipher cipher = Cipher.getInstance(GeneratePlainPassword.AES);
	        cipher.init(Cipher.DECRYPT_MODE, sks);
	        byte[] decrypted = cipher.doFinal(hexStringToByteArray(password));
	        String OriginalPassword = new String(decrypted);
	        System.out.println("****************  Original Password  ****************");
	        System.out.println(OriginalPassword);
	        System.out.println("****************  Original Password  ****************");

	    }

}
