package pe.soapros.bean;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="TotalCostRPT")
public class TotalCostRPT {

    @JsonProperty("DisplayAmount")
    private String displayAmount;

    public String getDisplayAmount() {
        return displayAmount;
    }

    public void setDisplayAmount(String displayAmount) {
        this.displayAmount = displayAmount;
    }
}
