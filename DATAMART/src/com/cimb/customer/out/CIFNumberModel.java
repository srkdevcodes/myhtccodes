package com.cimb.customer.out;

import java.io.Serializable;

public class CIFNumberModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String recordType;
	private String cifNumner;
	private String status;
	private String statusDesc;
	
	
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public String getCifNumner() {
		return cifNumner;
	}
	public void setCifNumner(String cifNumner) {
		this.cifNumner = cifNumner;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	@Override
	public String toString() {
		return "CIFNumberModel [recordType=" + recordType + ", cifNumner=" + cifNumner + ", status=" + status
				+ ", statusDesc=" + statusDesc + "]";
	}
	
	
}
