package pe.soapros.bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="CoverageTerms")
public class CoverageTerms {
	@JsonProperty("CoverageTerm")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<CoverageTerm> coverageTerm;
	private ArrayList<CoverageTerm> newCoverageTerm;


	public List<CoverageTerm> getCoverageTerm() {
		return coverageTerm;
	}

	public void setCoverageTerm(List<CoverageTerm> coverageTerm) {
		this.coverageTerm = coverageTerm;
	}
	
	public void discardCoverageTerms() {
		newCoverageTerm = new ArrayList<CoverageTerm>();
		for (CoverageTerm covTerm : this.coverageTerm) {
			if(covTerm.getModelType().getCode().equals("amount_limit_GSS") || covTerm.getModelType().getCode().equals("Deductible")) {
				newCoverageTerm.add(covTerm);
			}
		}
		this.coverageTerm = newCoverageTerm;
	}
	
}
