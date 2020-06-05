package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Header")
public class AAHPartys {
	@JsonProperty("SeccionNominados")
	private SeccionNominados seccionNominados;
	@JsonProperty("SeccionInnominados")
	private SeccionInnominados seccionInnominados;

}
