package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName="PrivatePassengers")
public class PrivatePassengers {

    @JsonProperty("PrivatePassenger")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<PrivatePassenger> privatePassenger;

    public List<PrivatePassenger> getPrivatePassenger() {
        return privatePassenger;
    }

    public void setPartyCoverage(List<PrivatePassenger> privatePassenger) {
        this.privatePassenger = privatePassenger;
    }
}
