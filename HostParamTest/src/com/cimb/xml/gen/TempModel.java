package com.cimb.xml.gen;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;
/**
 * 
 * @author Rajkiran
 * @since 03 Aug 2017
 *
 */

public class TempModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String rquId;
	
	private String claimId;
	
	private String notifyFalg;
	
	private Date creationDate;
	
	private Date lastUpdateDdate;
	
	private String customerName;
	
	private String TcusomterName;
	
	private String customerNumber;
	
	private String sectorCode;
	
	private String businessType;
	
	private String cifId;
	
	private String updateFlag;
	
	private Clob xmlRequest;
	
	private Clob xmlResponse;
	
	private String remarks;
	
	

	public String getRquId() {
		return rquId;
	}

	public void setRquId(String rquId) {
		this.rquId = rquId;
	}

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public String getNotifyFalg() {
		return notifyFalg;
	}

	public void setNotifyFalg(String notifyFalg) {
		this.notifyFalg = notifyFalg;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdateDdate() {
		return lastUpdateDdate;
	}

	public void setLastUpdateDdate(Date lastUpdateDdate) {
		this.lastUpdateDdate = lastUpdateDdate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTcusomterName() {
		return TcusomterName;
	}

	public void setTcusomterName(String tcusomterName) {
		TcusomterName = tcusomterName;
	}

	public String getSectorCode() {
		return sectorCode;
	}

	public void setSectorCode(String sectorCode) {
		this.sectorCode = sectorCode;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getCifId() {
		return cifId;
	}

	public void setCifId(String cifId) {
		this.cifId = cifId;
	}

	public String getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}

	public Clob getXmlRequest() {
		return xmlRequest;
	}

	public void setXmlRequest(Clob xmlRequest) {
		this.xmlRequest = xmlRequest;
	}

	public Clob getXmlResponse() {
		return xmlResponse;
	}

	public void setXmlResponse(Clob xmlResponse) {
		this.xmlResponse = xmlResponse;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	
	
	
	
	
	
	
	

}
