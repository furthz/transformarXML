package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Header")
public class PartyCoverage {
	@JsonProperty("Description")
	private String description;
	@JsonProperty("Category")
	private String category;
	@JsonProperty("CoverageTerms")
	private CoverageTerms coverageTerms;
	@JsonProperty("Code")
	private String code;
	
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
	
	
}
