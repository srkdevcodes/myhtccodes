package com.cimb.sqlloader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

//import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.Logger;
import org.jboss.security.plugins.PBEUtils;
import org.jboss.security.vault.SecurityVaultException;
import org.picketbox.plugins.vault.PicketBoxSecurityVault;
public class GetPassword {
	private static final Logger LOGGER = Logger.getLogger(GetPassword.class);
	public static String getPass(){
		Map<String, Object> data = new HashMap<String, Object>();
		String Password=null;
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("/appl1/taonet/fpx-batchjob/fpx-batchjob-vault.properties");		
			prop.load(input);
			String keystoreLocation = prop.getProperty("fpxdb.keystore-location");
			String keystorePassword = prop.getProperty("fpxdb.keystore-maskpwd");
			String keystoreSalt = prop.getProperty("fpxdb.keystore-salt");
			String keystoreIteration = prop.getProperty("fpxdb.keystore-iteration");
			String keystoreAlias = prop.getProperty("fpxdb.keystore-alias");
			String vaultDirectory = prop.getProperty("fpxdb.vault-directory");
			String vaultLocation = prop.getProperty("fpxdb.vault-location");
			
			data.put("KEYSTORE_URL", keystoreLocation);
			data.put("KEYSTORE_PASSWORD", keystorePassword);
			data.put("SALT", keystoreSalt);
			data.put("ITERATION_COUNT", keystoreIteration);
			data.put("KEYSTORE_ALIAS", keystoreAlias);
			data.put("ENC_FILE_DIR", vaultDirectory);

			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEwithMD5andDES");
			char[] password = "somearbitrarycrazystringthatdoesnotmatter".toCharArray();
			PBEParameterSpec cipherSpec = new PBEParameterSpec(((String) data.get("SALT")).getBytes(), Integer.valueOf((String) data.get("ITERATION_COUNT")));
			PBEKeySpec keySpec = new PBEKeySpec(password);
			SecretKey cipherKey = factory.generateSecret(keySpec);
			String decodedValue = PBEUtils.decode64(((String) data.get("KEYSTORE_PASSWORD")).substring("MASK-".length()), "PBEwithMD5andDES", cipherKey, cipherSpec);

			PicketBoxSecurityVault meuPick = new PicketBoxSecurityVault();
			meuPick.init(data);
			
			for (String s : meuPick.keyList()) {
			    String block = s.split(":")[0];
			    String attribute = s.split(":")[2];
			    char[] senha = meuPick.retrieve(block, attribute,null);
			    Password= String.valueOf(senha);
			}
		} catch (NumberFormatException e) {
			LOGGER.error("Exception Occured in GetPassword"+e.getLocalizedMessage());
		} catch (FileNotFoundException e) {
			LOGGER.error("Exception Occured in GetPassword"+e.getLocalizedMessage());
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Exception Occured in GetPassword"+e.getLocalizedMessage());
		} catch (InvalidKeySpecException e) {
			LOGGER.error("Exception Occured in GetPassword"+e.getLocalizedMessage());
		} catch (SecurityVaultException e) {
			LOGGER.error("Exception Occured in GetPassword"+e.getLocalizedMessage());
		} catch (IOException e) {
			LOGGER.error("Exception Occured in GetPassword"+e.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.error("Exception Occured in GetPassword"+e.getLocalizedMessage());
		}
		
		return Password;
	}

}
