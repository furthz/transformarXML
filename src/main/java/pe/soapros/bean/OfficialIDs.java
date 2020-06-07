package pe.soapros.bean;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="OfficialIDs")
public class OfficialIDs {

	@JsonProperty("OfficialID")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<OfficialID> officialID;

	public List<OfficialID> getOfficialID() {
		return officialID;
	}

	public void setOfficialID(List<OfficialID> officialID) {
		this.officialID = officialID;
	}
	
	public void discardOfficialID() {
		for (OfficialID officialid : this.officialID) {
			if(officialid.getPrimary().equals("true" )) {
				this.officialID.clear();
				this.officialID.add(officialid);
				break;
			}
		}

	}
}
