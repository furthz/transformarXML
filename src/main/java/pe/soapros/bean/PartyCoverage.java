package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="PartyCoverage")
public class PartyCoverage implements Comparable<PartyCoverage>{
	
	@JsonProperty("Description")
	private String description;
	
	@JsonProperty("Category")
	private String category;
	
	@JsonProperty("CoverageTerms")
	private CoverageTerms coverageTerms;
	
	@JsonProperty("Code")
	private String code;
	
	@JsonProperty("DetuctibleValue")
	private String detuctibleValue;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public CoverageTerms getCoverageTerms() {
		return coverageTerms;
	}
	public void setCoverageTerms(CoverageTerms coverageTerms) {
		this.coverageTerms = coverageTerms;
	}
	public String getDetuctibleValue() {
		return detuctibleValue;
	}
	public void setDetuctibleValue(String detuctibleValue) {
		this.detuctibleValue = detuctibleValue;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	
	@Override
	public int compareTo(PartyCoverage arg0) {
		return this.code.compareTo(arg0.getCode());
	}
	
	public void calculateDetuctibleValue() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			// System.out.println("coTerm.getModelType().getCode() "+coTerm.getModelType().getCode());
			// System.out.println("coTerm.getDisplayValue() "+coTerm.getDisplayValue());
			if(coTerm.getModelType().getCode().equalsIgnoreCase("Deductible") && !coTerm.getDisplayValue().equalsIgnoreCase("0")) {
				this.detuctibleValue = coTerm.getDisplayValue();
			}
		}
	}
	
	
}
