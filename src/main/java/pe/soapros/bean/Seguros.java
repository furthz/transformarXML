package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Seguros {
    @JsonProperty("Poliza")
    private Poliza poliza;
    @JsonProperty("EmisionPoliza")
    private EmisionPoliza emisionPoliza;
    @JsonProperty("CotizacionPoliza")
    private CotizacionPoliza cotizacionPoliza;
    @JsonProperty("InspeccionPreviaPoliza")
    private InspeccionPreviaPoliza inspeccionPreviaPoliza;
}
