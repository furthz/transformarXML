package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName="AAHLine")
public class AAHLine {
	
	@JsonProperty("PolicyType")
	private PolicyType policyType;
	
	@JsonProperty("AAHPartys")
	private AAHPartys aAHPartys;
	/*
	public PolicyType getPolicyType() {
		return this.policyType;
	}
	public void setPolicyType(PolicyType policyType) {
		this.policyType = policyType;
	}
	public AAHPartys getaAHPartys() {
		return aAHPartys;
	}
	public void setaAHPartys(AAHPartys aAHPartys) {
		this.aAHPartys = aAHPartys;
	}
	*/
}
