package com.cimb.icres.req;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.Timestamp;
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
	
	private Timestamp creationDate;
	
	private Timestamp lastUpdateDdate;
	
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
	
	private String customerId;
	
	private Integer id;
	
	private Integer counter;
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

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

	/*public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdateDdate() {
		return lastUpdateDdate;
	}

	public void setLastUpdateDdate(Date lastUpdateDdate) {
		this.lastUpdateDdate = lastUpdateDdate;
	}*/

	public String getCustomerName() {
		return customerName;
	}

	public Timestamp getLastUpdateDdate() {
		return lastUpdateDdate;
	}

	public void setLastUpdateDdate(Timestamp lastUpdateDdate) {
		this.lastUpdateDdate = lastUpdateDdate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
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
	
	public Integer getCounter() {
		return counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((TcusomterName == null) ? 0 : TcusomterName.hashCode());
		result = prime * result + ((businessType == null) ? 0 : businessType.hashCode());
		result = prime * result + ((cifId == null) ? 0 : cifId.hashCode());
		result = prime * result + ((claimId == null) ? 0 : claimId.hashCode());
		//result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((customerName == null) ? 0 : customerName.hashCode());
		result = prime * result + ((customerNumber == null) ? 0 : customerNumber.hashCode());
		//result = prime * result + ((lastUpdateDdate == null) ? 0 : lastUpdateDdate.hashCode());
		result = prime * result + ((notifyFalg == null) ? 0 : notifyFalg.hashCode());
		result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
		result = prime * result + ((rquId == null) ? 0 : rquId.hashCode());
		result = prime * result + ((sectorCode == null) ? 0 : sectorCode.hashCode());
		result = prime * result + ((updateFlag == null) ? 0 : updateFlag.hashCode());
		result = prime * result + ((xmlRequest == null) ? 0 : xmlRequest.hashCode());
		result = prime * result + ((xmlResponse == null) ? 0 : xmlResponse.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((counter == null) ? 0 : counter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TempModel other = (TempModel) obj;
		if (TcusomterName == null) {
			if (other.TcusomterName != null)
				return false;
		} else if (!TcusomterName.equals(other.TcusomterName))
			return false;
		if (businessType == null) {
			if (other.businessType != null)
				return false;
		} else if (!businessType.equals(other.businessType))
			return false;
		if (cifId == null) {
			if (other.cifId != null)
				return false;
		} else if (!cifId.equals(other.cifId))
			return false;
		if (claimId == null) {
			if (other.claimId != null)
				return false;
		} else if (!claimId.equals(other.claimId))
			return false;
//		if (creationDate == null) {
//			if (other.creationDate != null)
//				return false;
//		} else if (!creationDate.equals(other.creationDate))
//			return false;
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		if (customerNumber == null) {
			if (other.customerNumber != null)
				return false;
		} else if (!customerNumber.equals(other.customerNumber))
			return false;
//		if (lastUpdateDdate == null) {
//			if (other.lastUpdateDdate != null)
//				return false;
//		} else if (!lastUpdateDdate.equals(other.lastUpdateDdate))
//			return false;
		if (notifyFalg == null) {
			if (other.notifyFalg != null)
				return false;
		} else if (!notifyFalg.equals(other.notifyFalg))
			return false;
		if (remarks == null) {
			if (other.remarks != null)
				return false;
		} else if (!remarks.equals(other.remarks))
			return false;
		if (rquId == null) {
			if (other.rquId != null)
				return false;
		} else if (!rquId.equals(other.rquId))
			return false;
		if (sectorCode == null) {
			if (other.sectorCode != null)
				return false;
		} else if (!sectorCode.equals(other.sectorCode))
			return false;
		if (updateFlag == null) {
			if (other.updateFlag != null)
				return false;
		} else if (!updateFlag.equals(other.updateFlag))
			return false;
		if (xmlRequest == null) {
			if (other.xmlRequest != null)
				return false;
		} else if (!xmlRequest.equals(other.xmlRequest))
			return false;
		if (xmlResponse == null) {
			if (other.xmlResponse != null)
				return false;
		} else if (!xmlResponse.equals(other.xmlResponse))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (counter == null) {
			if (other.counter != null)
				return false;
		} else if (!counter.equals(other.counter))
			return false;
		return true;
	}
	
	
	
	
	
	
	
	

}
