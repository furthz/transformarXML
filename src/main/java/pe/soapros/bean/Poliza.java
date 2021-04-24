package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Poliza {
    @JsonProperty("ProductCode")
    private String productCode;
    @JsonProperty("ProductName")
    private String productName;
    @JsonProperty("BranchCode")
    private String branchCode;
    @JsonProperty("BranchName")
    private String branchName;
    @JsonProperty("CompanySSNCode")
    private String companySSNCode;
    @JsonProperty("CompanyName")
    private String companyName;
    @JsonProperty("ReferenceNumber")
    private String referenceNumber;
    @JsonProperty("CertificateNumber")
    private String certificateNumber;
    @JsonProperty("BusinessOfficeName")
    private String businessOfficeName;
    @JsonProperty("OfferingName")
    private String offeringName;
    @JsonProperty("ProducerCode")
    private String producerCode;
    @JsonProperty("AccountNumber")
    private String accountNumber;
    @JsonProperty("IntermediaryCode")
    private String intermediaryCode;
}
