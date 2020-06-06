package pe.soapros.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="CoverageTerms")
public class CoverageTerms {
	@JsonProperty("CoverageTerm")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<CoverageTerm> coverageTerm;

	public List<CoverageTerm> getCoverageTerm() {
		return coverageTerm;
	}

	public void setCoverageTerm(List<CoverageTerm> coverageTerm) {
		this.coverageTerm = coverageTerm;
	}
	
	
}
