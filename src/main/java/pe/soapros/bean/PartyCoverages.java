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

	@JsonProperty("AAHParty")
	private List<AAHParty> aahpartys;

	public String getCoverages() {
		String valor = "";
		TransformImpl obj = new TransformImpl();

		String theme_id=String.valueOf(obj.ThemeVar);

		for(PartyCoverage party: this.partyCoverage) {
			if (theme_id.equals("ARG_GW_PC_CotizacionAP_Colectiva")) {

			//	System.out.println("Entro en la condifcional");
				String description = party.getDescription();
				List<String> LimNombre = party.getLimNombre();
				String DedValor = party.getDedValor();
				List<String> LimValorDV = party.getLimValorDV();
				List<String> LimValorDA = party.getLimValorDA();
				List<String> LimValor = party.getLimValor();
				String DedNombre = party.getDedNombre();
				String Suma = party.getSuma();
				String EdadMinCant = party.getEdadMinCant();
				String EdadMinUnid = party.getEdadMinUnid();
				String EdadMaxIng = party.getEdadMaxIng();
				String EdadMaxPerm = party.getEdadMaxPerm();
				String DedModelo = party.getDedModelo();
				List<String> LimModelo = party.getLimModelo();
				List<String> SubNombre = party.getSubNombre();
				List<String> SubValor = party.getSubValor();
				List<String> SubModelo = party.getSubModelo();
				List<String> SubExcl = party.getSubExcl();

				/*if (LimNombre != null) {
					LimNombre = party.getLimNombre().toLowerCase().replaceAll("\\s+", "");
				}*/
				if (LimNombre != null) {
					LimNombre = party.getLimNombre();
				}
				if (DedValor != null) {
					DedValor = party.getDedValor().toLowerCase().replaceAll("\\s+", "");
				}
				if (LimValorDV != null) {
					//LimValorDV = party.getLimValorDV().toLowerCase().replaceAll("\\s+", "");
					LimValorDV = party.getLimValorDV();
				}
				if (LimValorDA != null) {
				//	LimValorDA = party.getLimValorDA().toLowerCase().replaceAll("\\s+", "");
					LimValorDA = party.getLimValorDA();
				}
				if (LimValor != null) {
					//LimValor = party.getLimValor().toLowerCase().replaceAll("\\s+", "");
					LimValor = party.getLimValor();
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
					LimModelo = party.getLimModelo();
					//LimModelo = party.getLimModelo().toLowerCase().replaceAll("\\s+", "");
				}
				if (SubNombre != null) {
				//	SubNombre = party.getSubNombre().toLowerCase().replaceAll("\\s+", "");
					SubNombre = party.getSubNombre();
				}
				if (SubValor != null) {
					SubValor = party.getSubValor();
				//	SubValor = party.getSubValor().toLowerCase().replaceAll("\\s+", "");
				}
				if (SubModelo != null) {
					//SubModelo = party.getSubModelo().toLowerCase().replaceAll("\\s+", "");
					SubModelo = party.getSubModelo();
				}
				if (SubExcl != null) {
				//	SubExcl = party.getSubExcl().toLowerCase().replaceAll("\\s+", "");
					SubExcl = party.getSubExcl();
				}
				if (description != null) {
					description = party.getDescription().toLowerCase().replaceAll("\\s+", "");
				//	System.out.println(description + " /////");
				}
				valor = valor  + description + LimNombre + DedValor + LimValorDV + LimValorDA + LimValor + DedNombre + Suma + EdadMinCant + EdadMinUnid + EdadMaxIng + EdadMaxPerm + DedModelo + LimModelo + SubNombre + SubValor + SubModelo + SubExcl;
				//valor = valor  + description  + DedValor + LimValorDV + LimValorDA + LimValor + DedNombre + Suma + EdadMinCant + EdadMinUnid + EdadMaxIng + EdadMaxPerm + DedModelo + LimModelo + SubNombre + SubValor + SubModelo + SubExcl;

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



/*	public String ConcatenadoAAHParty(){
				String concatenado = "";
				for(AAHParty party : this.aahpartys) {
					String activity = "";|
            		String activity1 = "";
            		String clasification = "";
            		String clasification1 = "";
           			String insurableGroup = "";
           			String insurableGroup1 = "";
            		String coveredEvent = "";
            		String coveredEvent1 = "";
            		String contractType = "";
           			String contractType1 = "";
            		String aAHRiskCategory = "";
            		String aAHRiskCategory1 = "";
            		String riskCategory = "";
            		String eventStartDate = "";
           			String eventEndDate = "";
            		String locationEvent = "";

					//Try-Catch: Activity
					try{
						party.getActivity().getDisplayName();
						System.out.println(party.getActivity().getDisplayName().toLowerCase()+ "---");
					} catch (NullPointerException n) {
						//System.out.println("Asignando activity: null");
						activity = null;
						activity1 = "";
					}

					if (activity != null) {
						activity = party.getActivity().getDisplayName().toLowerCase().replaceAll("\\s+","");
					}else{if(activity1 == null){ activity= party.getActivity().getDisplayName();}}

					//Try-Catch: Clasification
					try{
					   party.getClasification().getDisplayName();
						System.out.println(party.getClasification().getDisplayName().toLowerCase()+ "++++");
					} catch (NullPointerException n) {
						//System.out.println("Asignando clasification: null");
						clasification = null;
						clasification1 = "";

					}

					if (clasification != null )  {
						clasification = party.getClasification().getDisplayName().toLowerCase().replaceAll("\\s+","");
					}else{if(clasification1==null){ clasification = party.getClasification().getDisplayName(); }}


					//Try-Catch: insurableGroup
					try{
						party.getInsurableGroup().getDisplayName();
						System.out.println(party.getInsurableGroup().getDisplayName().toLowerCase()+ "++++");
					} catch (NullPointerException n) {
						//System.out.println("Asignando insurableGroup: null");
						insurableGroup = null;
						insurableGroup1 = "";
					}
					if (insurableGroup != null) {
						insurableGroup = party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");
					}else{if(insurableGroup1==null){ insurableGroup = party.getInsurableGroup().getDisplayName(); }}

					//Try-Catch: coveredEvent
					try{
						party.getCoveredEvent().getDisplayName();
						System.out.println(party.getCoveredEvent().getDisplayName().toLowerCase()+ "++++");
					} catch (NullPointerException n) {
						//System.out.println("Asignando coveredEvent: null");
						coveredEvent = null;
						coveredEvent1 = "";
					}

					if (coveredEvent != null) {
						coveredEvent = party.getCoveredEvent().getDisplayName().toLowerCase().replaceAll("\\s+","");
					}else{if(coveredEvent1==null){ coveredEvent = party.getCoveredEvent().getDisplayName(); }}

					//Try-Catch: ContractType
					try{
						party.getContractType().getDisplayName();
						System.out.println(party.getContractType().getDisplayName().toLowerCase()+ "++++");
					} catch (NullPointerException n) {
						//System.out.println("Asignando contractType: null");
						contractType = null;
						contractType1 = "";
					}
					if (contractType != null) {
						contractType = party.getContractType().getDisplayName().toLowerCase().replaceAll("\\s+","");
					}else{if(contractType1==null){ contractType = party.getContractType().getDisplayName(); }}


					//Try-Catch: AAHRiskCategory
					try{
						party.getAAHRiskCategory().getDisplayName();
						System.out.println(party.getAAHRiskCategory().getDisplayName().toLowerCase()+ "++++");
					} catch (NullPointerException n) {
						//System.out.println("Asignando contractType: null");
						aAHRiskCategory = null;
						aAHRiskCategory1 = "";
					}
					if (aAHRiskCategory != null) {
						aAHRiskCategory = party.getAAHRiskCategory().getDisplayName().toLowerCase().replaceAll("\\s+","");
					}else{if(aAHRiskCategory1==null){ aAHRiskCategory = party.getAAHRiskCategory().getDisplayName(); }}

					//RiskCategory
					riskCategory = party.getRiskCategory();
					if (riskCategory != null) {
						riskCategory = party.getRiskCategory().toLowerCase().replaceAll("\\s+","");
					}
					//EventStartDate
					eventStartDate = party.getEventStartDate();
					if (eventStartDate != null) {
						eventStartDate = party.getEventStartDate().toLowerCase().replaceAll("\\s+","");
					}
					//EventEndDate
					eventEndDate = party.getEventEndDate();
					if (eventEndDate != null) {
						eventEndDate = party.getEventEndDate().toLowerCase().replaceAll("\\s+","");
					}
					//LocationEvent
					locationEvent = party.getLocationEvent();
					if (locationEvent != null) {
						locationEvent = party.getLocationEvent().toLowerCase().replaceAll("\\s+","");
					}
					concatenado = concatenado + activity + clasification + insurableGroup+coveredEvent+contractType+aAHRiskCategory+riskCategory+eventStartDate+eventEndDate+locationEvent;

					System.out.println("CONCATENADO: " + concatenado);


				}
				//this.coverages = valor;

				return concatenado;
		}

*/











}
