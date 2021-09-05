package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName="Intermediary")
public class Intermediary {
	
	@JsonProperty("ProducerCodeRole")
	private ProducerCodeRole producerCodeRole;
	@JsonProperty("Code")
	private String code;
	@JsonProperty("ProducerCodeID")
	private ProducerCodeID producerCodeID;

	/*      PC 11
	@JsonProperty("CommissionPercentage")
	private String commissionPercentage;

	@JsonProperty("IntermediaryPercentage")
	private String intermediaryPercentage;

	//ELIMINAR CODE Y PRODUCERCODEROLE

	* */


	/*
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
	*/
	
}
