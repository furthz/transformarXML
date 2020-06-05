package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Header")
public class SancorInfo {
	@JsonProperty("PrimaryAddress")
	private PrimaryAddress primaryAddress;

	public PrimaryAddress getPrimaryAddress() {
		return primaryAddress;
	}

	public void setPrimaryAddress(PrimaryAddress primaryAddress) {
		this.primaryAddress = primaryAddress;
	}
	
}
