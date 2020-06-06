package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Job")
public class Job {

	@JsonProperty("Subtype")
	private Subtype subtype;

	public Subtype getSubtype() {
		return subtype;
	}

	public void setSubtype(Subtype subtype) {
		this.subtype = subtype;
	}
	
}
