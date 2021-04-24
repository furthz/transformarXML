package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class File {
    @JsonProperty("DocumentMimeType")
    private String documentMimeType;
    @JsonProperty("DocumentName")
    private String documentName;
    @JsonProperty("FileName")
    private String fileName;
}
