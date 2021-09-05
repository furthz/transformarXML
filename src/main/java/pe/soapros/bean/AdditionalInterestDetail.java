package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="AdditionalInterestDetail")
public class AdditionalInterestDetail {

    @JsonProperty("Contact")
    private Contact contact;

    @JsonProperty("AdditionalInterestType")
    private AdditionalInterestType additionalInterestType;

    public Contact getContact() {

        return contact;
    }
    public void setContact(Contact contact) {
        this.contact = contact;
    }
    public AdditionalInterestType getAdditionalInterestType() {
        return additionalInterestType;
    }
    public void setAdditionalInterestType(AdditionalInterestType additionalInterestType) {
        this.additionalInterestType = additionalInterestType;
    }



}
