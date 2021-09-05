package pe.soapros.bean;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="AdditionalInterestType")
public class AdditionalInterestType {

    @JsonProperty("Code")
    private String code;
}
