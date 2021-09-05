package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName="AAHParty")
public class AAHParty {

	@JsonProperty("PartyNumber")
	private String partyNumber;
	
	@JsonProperty("NumberOfRelatedPeople")
	private String numberOfRelatedPeople;
	
	@JsonProperty("AgeOrYearRange")
	private String ageOrYearRange;

	@JsonProperty("AgeRangeDisplayName")
	private String ageRangeDisplayName;

	@JsonProperty("InsuredType")
	private InsuredType insuredType;
	
	@JsonProperty("CoveredPerson")
	private CoveredPerson coveredPerson;

	@JsonProperty("PartyCoverages")
	private PartyCoverages partyCoverages;

	@JsonProperty("Activity")
	private Activity activity;

	@JsonProperty("Clasification")
	private Clasification clasification;

	@JsonProperty("InsurableGroup")
	private InsurableGroup insurableGroup;

	@JsonProperty("RiskCategory")
	private String RiskCategory;

	@JsonProperty("CoveredEvent")
	private CoveredEvent coveredEvent;

	@JsonProperty("ContractType")
	private ContractType contractType;

	@JsonProperty("AAHRiskCategory")
	private AAHRiskCategory aAHRiskCategory;

	@JsonProperty("EventStartDate")
	private String EventStartDate;

	@JsonProperty("EventEndDate")
	private String EventEndDate;

	@JsonProperty("LocationEvent")
	private String LocationEvent;

	/*
	public String getPartyNumber() {
		return partyNumber;
	}

	public void setPartyNumber(String partyNumber) {
		this.partyNumber = partyNumber;
	}
	

	public String getNumberOfRelatedPeople() {
		return numberOfRelatedPeople;
	}
	public void setNumberOfRelatedPeople(String numberOfRelatedPeople) {
		this.numberOfRelatedPeople = numberOfRelatedPeople;
	}

	public InsuredType getInsuredType() {
		return insuredType;
	}

	public void setInsuredType(InsuredType insuredType) {
		this.insuredType = insuredType;
	}

	public CoveredPerson getCoveredPerson() {
		return coveredPerson;
	}

	public void setCoveredPerson(CoveredPerson coveredPerson) {
		this.coveredPerson = coveredPerson;
	}

	public PartyCoverages getPartyCoverages() {
		return partyCoverages;
	}

	public void setPartyCoverages(PartyCoverages partyCoverages) {
		this.partyCoverages = partyCoverages;
	}

	public String getAgeOrYearRange() {
		return ageOrYearRange;
	}

	public void setAgeOrYearRange(String ageOrYearRange) {
		this.ageOrYearRange = ageOrYearRange;
	}
	*/

	public boolean getCoveredPersonExistence() {
		return coveredPerson == null;
	}

}

