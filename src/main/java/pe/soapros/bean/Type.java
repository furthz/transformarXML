package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Header")
public class Type {
	@JsonProperty("UnlocalizedName")
	private String unlocalizedName;

	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	public void setUnlocalizedName(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
	}
	
	
}
