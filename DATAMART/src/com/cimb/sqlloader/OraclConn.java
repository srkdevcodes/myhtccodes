package com.cimb.sqlloader;
import java.sql.*;  

	
public class OraclConn {

	
	public static void main(String args[]){  
	try{  
	Class.forName("oracle.jdbc.OracleDriver");  
	String dbURL = "jdbc:oracle:thin:CLMSUATRPTMY/uatrptmy@10.104.122.91:1527:CLUATMY";
	Connection con= DriverManager.getConnection(dbURL);  
	//here sonoo is database name, root is username and password  
	Statement stmt=con.createStatement();  
	ResultSet rs=stmt.executeQuery("select * from sml_business");  
	while(rs.next())  
	System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
	con.close();  
	}catch(Exception e){ System.out.println(e);
	}  
	}  
	  

}
