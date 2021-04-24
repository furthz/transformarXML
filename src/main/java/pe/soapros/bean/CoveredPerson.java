package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName="CoveredPerson")
public class CoveredPerson {

	@JsonProperty("DisplayName")
	private String displayName;
	@JsonProperty("DateOfBirth")
	private String dateOfBirth;
	@JsonProperty("OfficialIDs")
	private OfficialIDs officialIDs;
	@JsonProperty("TipoDocum")
	private String tipoDocum;
	/*
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public OfficialIDs getOfficialIDs() {
		return officialIDs;
	}
	public void setOfficialIDs(OfficialIDs officialIDs) {
		this.officialIDs = officialIDs;
	}
	
	*/
	
}
