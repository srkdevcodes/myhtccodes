package com.cimb.xml.gen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class ReadResponseXml {
	
	public static void main(String[] args) throws IOException{
		
		String hostname = "localhost";
		 Socket sock = null;
		try {
			sock = new Socket(hostname, 80);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		// Response
		BufferedReader rd = new BufferedReader(new InputStreamReader(sock.getInputStream()));
					String line;
					FileOutputStream fos = new FileOutputStream(new File("D:\\ICRES\\ICRES1\\ICRES\\response.xml"));
					
					while ((line = rd.readLine()) != null){
					System.out.println(line);
					byte [] b = line.getBytes();
					fos.write(b);
					}
		
	}
	

}
