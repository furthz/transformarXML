package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
@JacksonXmlRootElement(localName="CA7Line")
public class CA7Line {

    @JsonProperty("PolicyType")
    private PolicyType policyType;
    @JsonProperty("LegalEntity")
    private LegalEntity legalEntity;
    @JsonProperty("PrivatePassengers")
    private PrivatePassengers privatePassengers;



    public PrivatePassengers getPrivatePassengers() {
        return privatePassengers;
    }

    public void setPrivatePassengers(PrivatePassengers privatePassengers) {
        this.privatePassengers = privatePassengers;
    }
    public PolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }
    public LegalEntity getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(LegalEntity legalEntity) {
        this.legalEntity = legalEntity;
    }
}
