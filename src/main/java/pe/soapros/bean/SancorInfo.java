package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="SancorInfo")
public class SancorInfo {

	/*      PC 11

	@JsonProperty("Name")
	private String name;


	  */

	@JsonProperty("Name")
	private String name;
	@JsonProperty("PrimaryAddress")
	private PrimaryAddress primaryAddress;

	/*      PC 11

	public String getName() {
		return primaryAddress;
	}

	public void setName(String name) {
		this.name = name;
	}


	  */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public PrimaryAddress getPrimaryAddress() {
		return primaryAddress;
	}

	public void setPrimaryAddress(PrimaryAddress primaryAddress) {
		this.primaryAddress = primaryAddress;
	}
	
}
