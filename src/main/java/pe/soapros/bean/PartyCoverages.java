package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import pe.soapros.services.impl.TransformImpl;

import java.util.List;

@JacksonXmlRootElement(localName="PartyCoverages")
public class PartyCoverages {

	@JsonProperty("PartyCoverage")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<PartyCoverage> partyCoverage;

	private String coverages;

	public List<PartyCoverage> getPartyCoverage() {
		return partyCoverage;
	}

	public void setPartyCoverage(List<PartyCoverage> partyCoverage) {
		this.partyCoverage = partyCoverage;
	}



	public String getCoverages() {
		String valor = "";
		TransformImpl obj = new TransformImpl();

		String theme_id=String.valueOf(obj.ThemeVar);

		for(PartyCoverage party: this.partyCoverage) {
			if (theme_id.equals("ARG_GW_PC_CotizacionAP_Colectiva")) {

			//	System.out.println("Entro en la condifcional");
				String description = party.getDescription();
				String LimNombre = party.getLimNombre();
				String DedValor = party.getDedValor();
				String LimValorDV = party.getLimValorDV();
				String LimValorDA = party.getLimValorDA();
				String LimValor = party.getLimValor();
				String DedNombre = party.getDedNombre();
				String Suma = party.getSuma();
				String EdadMinCant = party.getEdadMinCant();
				String EdadMinUnid = party.getEdadMinUnid();
				String EdadMaxIng = party.getEdadMaxIng();
				String EdadMaxPerm = party.getEdadMaxPerm();
				String DedModelo = party.getDedModelo();
				String LimModelo = party.getLimModelo();
				String SubNombre = party.getSubNombre();
				String SubValor = party.getSubValor();
				String SubModelo = party.getSubModelo();
				String SubExcl = party.getSubExcl();

				if (LimNombre != null) {
					LimNombre = party.getLimNombre().toLowerCase().replaceAll("\\s+", "");
				}
				if (DedValor != null) {
					DedValor = party.getDedValor().toLowerCase().replaceAll("\\s+", "");
				}
				if (LimValorDV != null) {
					LimValorDV = party.getLimValorDV().toLowerCase().replaceAll("\\s+", "");
				}
				if (LimValorDA != null) {
					LimValorDA = party.getLimValorDA().toLowerCase().replaceAll("\\s+", "");
				}
				if (LimValor != null) {
					LimValor = party.getLimValor().toLowerCase().replaceAll("\\s+", "");
				}
				if (DedNombre != null) {
					DedNombre = party.getDedNombre().toLowerCase().replaceAll("\\s+", "");
				}
				if (Suma != null) {
					Suma = party.getSuma().toLowerCase().replaceAll("\\s+", "");
				}
				if (EdadMinCant != null) {
					EdadMinCant = party.getEdadMinCant().toLowerCase().replaceAll("\\s+", "");
				}
				if (EdadMinUnid != null) {
					EdadMinUnid = party.getEdadMinUnid().toLowerCase().replaceAll("\\s+", "");
				}
				if (EdadMaxIng != null) {
					EdadMaxIng = party.getEdadMaxIng().toLowerCase().replaceAll("\\s+", "");
				}
				if (EdadMaxPerm != null) {
					EdadMaxPerm = party.getEdadMaxPerm().toLowerCase().replaceAll("\\s+", "");
				}
				if (DedModelo != null) {
					DedModelo = party.getDedModelo().toLowerCase().replaceAll("\\s+", "");
				}
				if (LimModelo != null) {
					LimModelo = party.getLimModelo().toLowerCase().replaceAll("\\s+", "");
				}
				if (SubNombre != null) {
					SubNombre = party.getSubNombre().toLowerCase().replaceAll("\\s+", "");
				}
				if (SubValor != null) {
					SubValor = party.getSubValor().toLowerCase().replaceAll("\\s+", "");
				}
				if (SubModelo != null) {
					SubModelo = party.getSubModelo().toLowerCase().replaceAll("\\s+", "");
				}
				if (SubExcl != null) {
					SubExcl = party.getSubExcl().toLowerCase().replaceAll("\\s+", "");
				}
				if (description != null) {
					description = party.getDescription().toLowerCase().replaceAll("\\s+", "");
				//	System.out.println(description + " /////");
				}
				valor = valor  +description+ LimNombre + DedValor + LimValorDV + LimValorDA + LimValor + DedNombre + Suma + EdadMinCant + EdadMinUnid + EdadMaxIng + EdadMaxPerm + DedModelo + LimModelo + SubNombre + SubValor + SubModelo + SubExcl;

				//System.out.println(description + LimValor);

			} else {
				String description = party.getDescription();
				String suma = party.getSuma();
				String dednombre = party.getDedNombre();
				String dedmodelo = party.getDedModelo();
				String dedvalor = party.getDedValor();

				if (description != null) {
					description = party.getDescription().toLowerCase().replaceAll("\\s+", "");
				}
				if (suma != null) {
					suma = party.getSuma().toLowerCase().replaceAll("\\s+", "");
				}
				if (dedmodelo != null) {
					dedmodelo = party.getDedModelo().toLowerCase().replaceAll("\\s+", "");
				}
				if (dednombre != null) {
					dednombre = party.getDedNombre().toLowerCase().replaceAll("\\s+", "");
				}
				if (dedvalor != null) {
					dedvalor = party.getDedValor().toLowerCase().replaceAll("\\s+", "");
				}
				valor = valor + description + suma + dedmodelo + dednombre + dedvalor;
				//System.out.println("PartyCoverages " + description);
			}
		}
		this.coverages = valor;

		return this.coverages;
	}



















}
