package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName="Metadata")
public class Metadata {
    @JsonProperty("BusinessDocument")
    private String businessDocument;
    @JsonProperty("BusinessDocumentName")
    private String businessDocumentName;
    @JsonProperty("DocumentSize")
    private String documentSize;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("Document")
    private MDocument mDocument;
}
