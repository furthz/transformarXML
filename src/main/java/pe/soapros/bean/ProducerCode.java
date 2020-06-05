package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Header")
public class ProducerCode {
	
	@JsonProperty("Intermediaries")
	private Intermediaries intermediaries;
	@JsonProperty("BusinessUnit")
	private BusinessUnit businessUnit;
	@JsonProperty("BranchOffice")
	private BranchOffice branchOffice;
	
	public Intermediaries getIntermediaries() {
		return intermediaries;
	}
	public void setIntermediaries(Intermediaries intermediaries) {
		this.intermediaries = intermediaries;
	}
	public BusinessUnit getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(BusinessUnit businessUnit) {
		this.businessUnit = businessUnit;
	}
	public BranchOffice getBranchOffice() {
		return branchOffice;
	}
	public void setBranchOffice(BranchOffice branchOffice) {
		this.branchOffice = branchOffice;
	}

	
}
