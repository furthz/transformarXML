package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import java.util.List;
@Data
@JacksonXmlRootElement(localName="Commissions")
public class Commissions {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("Intermediary")
    private List<Intermediary> intermediary;
}
