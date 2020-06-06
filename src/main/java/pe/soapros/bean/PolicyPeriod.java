package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="PolicyPeriod")
public class PolicyPeriod {
	
	@JsonProperty("OfferName")
	private String offerName;
	
	@JsonProperty("PolicyNumber")
	private String policyNumber;
	
	@JsonProperty("OfficialPolicyNumber")
	private String officialPolicyNumber;
	
	@JsonProperty("PolicyChangeNumber")
	private String policyChangeNumber;
	
	@JsonProperty("PeriodStart")
	private String periodStart;
	
	@JsonProperty("CancellationDate")
	private String cancellationDate;
	
	@JsonProperty("PeriodEnd")
	private String periodEnd;
	
	@JsonProperty("PaymentFrequency")
	private String paymentFrequency;
	
	@JsonProperty("SystemDate")
	private String systemDate;

	@JsonProperty("RamoCode")
	private RamoCode ramoCode;
	
	@JsonProperty("Policy")
	private Policy policy;
	
	@JsonProperty("Job")
	private Job job;
	
	@JsonProperty("ReceiptInfo")
	private ReceiptInfo receiptInfo;
	
	@JsonProperty("AAHLine")
	private AAHLine aAHLine;
	
	@JsonProperty("PrimaryNamedInsured")
	private PrimaryNamedInsured primaryNamedInsured;
	
	@JsonProperty("SancorInfo")
	private SancorInfo sancorInfo;
	
	@JsonProperty("ProducerCode")
	private ProducerCode producerCode;
	
	@JsonProperty("TotalParties")
	private String totalParties;
	
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
	
	public void sumAlllParties() {
		int cantidadNominados, cantidadInnominados;
		cantidadInnominados = this.aAHLine.getaAHPartys().getSeccionInnominados().getSize();
		cantidadNominados = this.aAHLine.getaAHPartys().getSeccionInnominados().getSize();

		this.totalParties = String.valueOf(cantidadNominados + cantidadInnominados);
	}
	
}
