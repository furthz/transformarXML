package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Header")
public class Contact {
	@JsonProperty("PrimaryAddress")
	private PrimaryAddress primaryAddress;
	@JsonProperty("DisplayName")
	private String displayName;
	@JsonProperty("AssociatedNumber")
	private String associatedNumber;
	@JsonProperty("OfficialIDs")
	private OfficialIDs officialIDs;
	
	public PrimaryAddress getPrimaryAddress() {
		return primaryAddress;
	}
	public void setPrimaryAddress(PrimaryAddress primaryAddress) {
		this.primaryAddress = primaryAddress;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getAssociatedNumber() {
		return associatedNumber;
	}
	public void setAssociatedNumber(String associatedNumber) {
		this.associatedNumber = associatedNumber;
	}
	public OfficialIDs getOfficialIDs() {
		return officialIDs;
	}
	public void setOfficialIDs(OfficialIDs officialIDs) {
		this.officialIDs = officialIDs;
	}

}
