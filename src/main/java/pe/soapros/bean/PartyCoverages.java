package pe.soapros.bean;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="PartyCoverages")
public class PartyCoverages {

	@JsonProperty("PartyCoverage")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<PartyCoverage> partyCoverage;

	private String coverages;
	
	public List<PartyCoverage> getPartyCoverage() {
		return partyCoverage;
	}

	public void setPartyCoverage(List<PartyCoverage> partyCoverage) {
		this.partyCoverage = partyCoverage;
	}
	
	
	public String getCoverages() {
		String valor = "";
		
		Collections.sort(partyCoverage);
		
		for(PartyCoverage party: this.partyCoverage) {
			valor = valor + party.getCode();
		}
		
		this.coverages = valor;
		
		return this.coverages;
	}
	
	
	
}
