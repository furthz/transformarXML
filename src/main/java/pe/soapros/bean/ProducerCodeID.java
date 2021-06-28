package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName="ProducerCodeID")
public class ProducerCodeID {

    @JsonProperty("Code")
    private String code;

    @JsonProperty("Role")
    private Role role;
}
