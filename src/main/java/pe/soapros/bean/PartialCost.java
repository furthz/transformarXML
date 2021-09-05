package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="PartialCost")
public class PartialCost {
    @JsonProperty("Premio")
    private Premio premio;

}
