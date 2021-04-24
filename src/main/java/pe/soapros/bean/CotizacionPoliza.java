package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CotizacionPoliza {
    @JsonProperty("Quota")
    private String quota;
    @JsonProperty("Master")
    private String master;
    @JsonProperty("PolicyHolderName")
    private String policyHolderName;
    @JsonProperty("ProducerFantasyName")
    private String producerFantasyName;
    @JsonProperty("RequestStatus")
    private String requestStatus;
}
