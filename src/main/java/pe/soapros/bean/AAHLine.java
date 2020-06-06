package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="AAHLine")
public class AAHLine {
	
	@JsonProperty("PolicyType")
	private PolicyType policyType;
	
	@JsonProperty("AAHPartys")
	private AAHPartys aAHPartys;
	
	public PolicyType getPolicyType() {
		return this.policyType;
	}
	public void setPolicyType(PolicyType policyType) {
		this.policyType = policyType;
	}
	public AAHPartys getAAHPartys() {
		return aAHPartys;
	}
	public void setAAHPartys(AAHPartys aAHPartys) {
		this.aAHPartys = aAHPartys;
	}

	
}
