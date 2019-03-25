package com.cimb.customer.out;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author Rajkiran Suram
 * @since 12 April 2018
 */

public class CifNoModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String cifNo;
	private String ind; 
	private Date creationDt;
	
	
	public String getCifNo() {
		return cifNo;
	}
	public void setCifNo(String cifNo) {
		this.cifNo = cifNo;
	}
	public String getInd() {
		return ind;
	}
	public void setInd(String ind) {
		this.ind = ind;
	}
	public Date getCreationDt() {
		return creationDt;
	}
	public void setCreationDt(Date creationDt) {
		this.creationDt = creationDt;
	}
	
	
	
	

	
	

}
