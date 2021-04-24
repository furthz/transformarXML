package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName="AAHRiskCategory")
public class AAHRiskCategory {
    @JsonProperty("DisplayName")
    private String displayName;
}
