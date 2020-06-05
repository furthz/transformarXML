package pe.soapros.bean;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Header")
public class OfficialIDs {

	@JsonProperty("OfficialID")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<OfficialID> officialID;
	
}
