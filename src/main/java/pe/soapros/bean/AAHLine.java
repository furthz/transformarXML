package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Header")
public class AAHLine {
	@JsonProperty("PolicyType")
	private PolicyType policyType;
	@JsonProperty("AAHPartys")
	private AAHPartys AAHPartys;
	
	public PolicyType getPolicyType() {
		return policyType;
	}
	public void setPolicyType(PolicyType policyType) {
		this.policyType = policyType;
	}
	public AAHPartys getAAHPartys() {
		return AAHPartys;
	}
	public void setAAHPartys(AAHPartys aAHPartys) {
		AAHPartys = aAHPartys;
	}

	
}
