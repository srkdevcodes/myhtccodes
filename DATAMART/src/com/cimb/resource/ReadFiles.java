package com.cimb.resource;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
public class ReadFiles {
	
	public ReadFiles() {
        try {
            
        }
        catch (Exception e) {
            System.out.println("Exception in FileRead():: Initialization :: " + e.getMessage());
            e.printStackTrace();
        }
    }
	
	public void FTPFilePull(){
	
	FTPClient client;
	client=new FTPClient();
	try {
		FTPFile[] arrfTPFile = client.listFiles();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
