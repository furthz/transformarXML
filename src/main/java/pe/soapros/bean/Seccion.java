package pe.soapros.bean;



import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Header")
public class Seccion {
	@JsonProperty("AAHParty")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<AAHParty> aAHParty;

	public List<AAHParty> getaAHParty() {
		return aAHParty;
	}

	public void setaAHParty(ArrayList<AAHParty> aAHParty) {
		this.aAHParty = aAHParty;
	}



	
}
