package com.cimb.sqlloader;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class GenerateEncryptionPassword {
	public static final String AES = "AES";

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

    public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
        String key = "565632200CF48054F3CDF889287524AD";
        //String password = "clmssitmy17"; /*FC92512579F618D87C97C52E951B03F6*/
       // String password = "CLMSSITSG2";/*D9A1EEA0ED9FCD665BB855D094A568DB*/
        //String password = "clmssitth";/*5029C312BD2B4F8C5D0ED364223B2FC7*/
        //String password = "clmssitrptmy";/*849D0D136B5E7D1020332A889F2C90E7*/
        //String password = "clmssitrptsg";/*DCF7AD1DBEAB332F7A200D08F60465AE*/
        //String password = "clmssitrptth";/*F5B5CD8BB5E2E8922F815C88CE728BE7*/
        
        //String password = "clmslosmyc2";/*AE7D1CD8FD37AF39DF748D2E84C34535*/
        //String password = "clmslossgc2";/*2B5E4CACAE719DBD558251953B026A13*/
        //String password = "clmslosthc2";/*39AA8CC5A45D205543C90F955AB8E350*/
        //String password = "clmsuatrptmy";/*2D8C431C9290AFB9A18945E98212D95A*/
        //String password = "clmsuatrptsg";/*206E572430FB077BBED3901433806525*/
        String password = "password123";/*8DE20F557C8B67C353B2C7FD4EF25366*/

//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please Enter Key:");
//        String key = scanner.next();
//        System.out.println("Please Enter Plain Text Password:");
//        String password = scanner.next();

        byte[] bytekey = hexStringToByteArray(key);
        SecretKeySpec sks = new SecretKeySpec(bytekey, GenerateEncryptionPassword.AES);
        Cipher cipher = Cipher.getInstance(GenerateEncryptionPassword.AES);
        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
        byte[] encrypted = cipher.doFinal(password.getBytes());
        String encryptedpwd = byteArrayToHexString(encrypted);
        System.out.println("****************  Encrypted Password  ****************");
        System.out.println(encryptedpwd);
        System.out.println("****************  Encrypted Password  ****************");

    }
}
