package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmisionPoliza {
    @JsonProperty("EmisionDate")
    private String emisionDate;
    @JsonProperty("PrintingDate")
    private String printingDate;
    @JsonProperty("EndorsementType")
    private String endorsementType;
    @JsonProperty("EndorsementNumber")
    private String endorsementNumber;
    @JsonProperty("ProposalNumber")
    private String proposalNumber;
    @JsonProperty("Quotas")
    private String quotas;
    @JsonProperty("LicensePlate")
    private String licensePlate;
    @JsonProperty("Operative")
    private String operative;
    @JsonProperty("OperativeGroup")
    private String operativeGroup;
    @JsonProperty("SectionDocument")
    private String sectionDocument;
    @JsonProperty("EndorsementConcept")
    private String endorsementConcept;
    @JsonProperty("PolicyDescription")
    private String policyDescription;
}
