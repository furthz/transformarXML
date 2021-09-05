package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import pe.soapros.services.impl.TransformImpl;

@Data
@JacksonXmlRootElement(localName="PolicyPeriod")
public class PolicyPeriod {

	@JsonProperty("OfferCode")
	private String offerCode;
	// PC 11
	@JsonProperty("OfferName")
	private String offerName;
	// PC 11
	@JsonProperty("PolicyNumber")
	private String policyNumber;
	// PC 11
	@JsonProperty("OfficialPolicyNumber")
	private String officialPolicyNumber;
	// PC 11
	@JsonProperty("PolicyChangeNumber")
	private String policyChangeNumber;
	// PC 11
	@JsonProperty("PeriodStart")
	private String periodStart;
	// BORRAR PARA 11
	@JsonProperty("CancellationDate")
	private String cancellationDate;
	// PC 11
	@JsonProperty("PeriodEnd")
	private String periodEnd;
	// PC 11
	@JsonProperty("PaymentFrequency")
	private String paymentFrequency;
	// PC 11
	@JsonProperty("SystemDate")
	private String systemDate;
	// PC 11
	@JsonProperty("RamoCode")
	private RamoCode ramoCode;
	// PC 11
	@JsonProperty("Policy")
	private Policy policy;
	// PC 11
	@JsonProperty("PrimaryNamedInsured")
	private PrimaryNamedInsured primaryNamedInsured;
	@JsonProperty("SecondaryNamedInsured")
	private SecondaryNamedInsured secondaryNamedInsured;
	// PC 11
	@JsonProperty("Job")
	private Job job;
	// PC 11
	@JsonProperty("ReceiptInfo")
	private ReceiptInfo receiptInfo;
	// PC BORRAR 11
	@JsonProperty("AAHLine")
	private AAHLine aAHLine;
	@JsonProperty("CA7Line")
	private CA7Line cA7Line;
	// PC 11
	/*@JsonProperty("PrimaryNamedInsured")
	private PrimaryNamedInsured primaryNamedInsured;
	@JsonProperty("SecondaryNamedInsured")
	private SecondaryNamedInsured secondaryNamedInsured;*/
	// PC 11
	@JsonProperty("SancorInfo")
	private SancorInfo sancorInfo;
	//  BORRAR PC 11
	@JsonProperty("ProducerCode")
	private ProducerCode producerCode;
	//  BORRAR PC 11
	@JsonProperty("TotalParties")
	private String totalParties;
	//  PC 11
	@JsonProperty("OfferCommercialName")
	private String offerCommercialName;
	// PC 11
	@JsonProperty("EditEffectiveDate")
	private String editEffectiveDate;
	// PC 11
	@JsonProperty("Commissions")
	private Commissions commissions;

	// PC 11
	@JsonProperty("BusinessUnit")
	private BusinessUnit businessUnit;
	// PC 11
	@JsonProperty("BranchOffice")
	private BranchOffice branchOffice;
	//   PC 11
	@JsonProperty("TotalCostRPT")
	private TotalCostRPT totalCostRPT;
	//  BORRAR PC 11
	@JsonProperty("PartialCost")
	private PartialCost partialCost;

	/*@JsonProperty("CA7Line")
	private CA7Line cA7Line;*/
	/*  PC 11

    @JsonProperty("OfferCode")
	private String offerCode;

	@JsonProperty("CA7Line")
	private CA7Line cA7Line;

	@JsonProperty("SecondaryNamedInsured")
	private SecondaryNamedInsured secondaryNamedInsured;


	 */




	/*
	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	
	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getOfficialPolicyNumber() {
		return officialPolicyNumber;
	}

	public void setOfficialPolicyNumber(String officialPolicyNumber) {
		this.officialPolicyNumber = officialPolicyNumber;
	}

	public String getPolicyChangeNumber() {
		return policyChangeNumber;
	}

	public void setPolicyChangeNumber(String policyChangeNumber) {
		this.policyChangeNumber = policyChangeNumber;
	}

	public String getPeriodStart() {
		return periodStart;
	}

	public void setPeriodStart(String periodStart) {
		this.periodStart = periodStart;
	}

	public String getCancellationDate() {
		return cancellationDate;
	}

	public void setCancellationDate(String cancellationDate) {
		this.cancellationDate = cancellationDate;
	}

	public String getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(String periodEnd) {
		this.periodEnd = periodEnd;
	}

	public String getPaymentFrequency() {
		return paymentFrequency;
	}

	public void setPaymentFrequency(String paymentFrequency) {
		this.paymentFrequency = paymentFrequency;
	}

	public String getSystemDate() {
		return systemDate;
	}

	public void setSystemDate(String systemDate) {
		this.systemDate = systemDate;
	}

	public RamoCode getRamoCode() {
		return ramoCode;
	}

	public void setRamoCode(RamoCode ramoCode) {
		this.ramoCode = ramoCode;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public ReceiptInfo getReceiptInfo() {
		return receiptInfo;
	}

	public void setReceiptInfo(ReceiptInfo receiptInfo) {
		this.receiptInfo = receiptInfo;
	}


	public AAHLine getaAHLine() {
		return aAHLine;
	}

	public void setaAHLine(AAHLine aAHLine) {
		this.aAHLine = aAHLine;
	}

	public PrimaryNamedInsured getPrimaryNamedInsured() {
		return primaryNamedInsured;
	}

	public void setPrimaryNamedInsured(PrimaryNamedInsured primaryNamedInsured) {
		this.primaryNamedInsured = primaryNamedInsured;
	}

	public SancorInfo getSancorInfo() {
		return sancorInfo;
	}

	public void setSancorInfo(SancorInfo sancorInfo) {
		this.sancorInfo = sancorInfo;
	}

	public ProducerCode getProducerCode() {
		return producerCode;
	}

	public void setProducerCode(ProducerCode producerCode) {
		this.producerCode = producerCode;
	}

	public String getTotalParties() {
		return totalParties;
	}

	public void setTotalParties(String totalParties) {
		this.totalParties = totalParties;
	}
	*/
	public void sumAlllParties() {
		//System.out.println("sumAlllParties");
		int cantidadNominados, cantidadInnominados;
		cantidadInnominados = this.aAHLine.getAAHPartys().getSeccionInnominados().getSize();
		cantidadNominados = this.aAHLine.getAAHPartys().getSeccionNominados().getSize();
		//System.out.println(cantidadInnominados);
		//System.out.println(cantidadNominados);
		this.totalParties = String.valueOf(cantidadNominados + cantidadInnominados);
	}

	public void deletePro(){
		TransformImpl obj = new TransformImpl();
		String theme_id=String.valueOf(obj.ThemeVar);

	if(theme_id.equals("ARG_GW_PC_TarjetaAzul")) {
	/*	Properties p = new Properties();
		p.remove("BusinessUnit");
		System.out.println(p.propertyNames());*/

		PolicyPeriod p = PolicyPeriod.this;
		//commissions.finalize();
	}

	}

}
