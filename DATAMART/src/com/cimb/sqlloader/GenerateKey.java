package com.cimb.sqlloader;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class GenerateKey {
	
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

    @SuppressWarnings("unused")
	private static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static void main(String args[]) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(GenerateKey.AES);
        keyGen.init(128);
        SecretKey sk = keyGen.generateKey();
        String key = byteArrayToHexString(sk.getEncoded());
        System.out.println("key:" + key);
    }

}
