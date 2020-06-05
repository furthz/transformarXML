package pe.soapros.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Header")
public class Intermediaries {
	
	@JsonProperty("Intermediary")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Intermediary> intermediary;

	public List<Intermediary> getIntermediary() {
		return intermediary;
	}

	public void setIntermediary(List<Intermediary> intermediary) {
		this.intermediary = intermediary;
	}

}
