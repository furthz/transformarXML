package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName="Clasification")
public class Clasification {
    @JsonProperty("DisplayName")
    private String displayName;
/*
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName() {
        this.displayName = displayName;
    }*/
}

