package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName="Content")
public class Content {

	@JsonProperty("PolicyPeriod")
	private PolicyPeriod policyPeriod;
/*
	public PolicyPeriod getPolicyPeriod() {
		return policyPeriod;
	}

	public void setPolicyPeriod(PolicyPeriod policyPeriod) {
		this.policyPeriod = policyPeriod;
	}
*/
}
