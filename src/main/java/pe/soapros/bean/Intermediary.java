package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Header")
public class Intermediary {
	
	@JsonProperty("ProducerCodeRole")
	private ProducerCodeRole producerCodeRole;
	@JsonProperty("Code")
	private String code;
	
	public ProducerCodeRole getProducerCodeRole() {
		return producerCodeRole;
	}
	public void setProducerCodeRole(ProducerCodeRole producerCodeRole) {
		this.producerCodeRole = producerCodeRole;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
}
