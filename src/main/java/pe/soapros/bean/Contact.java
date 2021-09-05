package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Contact")
public class Contact {
	@JsonProperty("DisplayName")
	private String displayName;
	@JsonProperty("TaxID")
	private String taxID;
	@JsonProperty("AssociatedNumber")
	private String associatedNumber;
	@JsonProperty("OfficialIDs")
	private OfficialIDs officialIDs;
	@JsonProperty("PrimaryAddress")
	private PrimaryAddress primaryAddress;




	// ************************pc 11*********************
	    // SOLO PRIMARYADRESS Y TAXID
	/*@JsonProperty("TaxID")
	private String taxID;*/


	// *********************************************
	public PrimaryAddress getPrimaryAddress() {
		return primaryAddress;
	}
	public void setPrimaryAddress(PrimaryAddress primaryAddress) {
		this.primaryAddress = primaryAddress;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getAssociatedNumber() {
		return associatedNumber;
	}
	public void setAssociatedNumber(String associatedNumber) {
		this.associatedNumber = associatedNumber;
	}
	public OfficialIDs getOfficialIDs() {
		return officialIDs;
	}
	public void setOfficialIDs(OfficialIDs officialIDs) {
		this.officialIDs = officialIDs;
	}
	
	public void descartarOfficialID() {
		this.officialIDs.discardOfficialID();
	}
	// VALIDACION DE ETIQUETA SI ES NULA COLOCAR VALOR PARA PC 11

	public String getTaxID(){
		if(taxID!=null){ return  taxID;}else{ return  taxID="PC 11";}
	}

	public void setTaxID(String taxID) {
		if(taxID!=null){ this.taxID = taxID;}else{
			this.taxID = "PC 11";
		}
		//this.taxID = taxID;
	}

}
