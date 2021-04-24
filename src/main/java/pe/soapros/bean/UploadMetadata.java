package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UploadMetadata {
    @JsonProperty("Seguros")
    private Seguros seguros;
}
