package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InspeccionPreviaPoliza {
    @JsonProperty("PriorInspectionNumber")
    private String priorInspectionNumber;
    @JsonProperty("PriorInspectionDate")
    private String priorInspectionDate;
    @JsonProperty("PriorInspectionDescription")
    private String priorInspectionDescription;
}
