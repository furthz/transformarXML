package pe.soapros.bean;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.Data;
import pe.soapros.constants.Constants;
import pe.soapros.log.Log;
@Data
@JacksonXmlRootElement(localName = "AAHPartys")
public class AAHPartys {

    private static final Log log = Log.getInstance(AAHPartys.class);

    @JsonProperty("SeccionNominados")
    private SeccionNominados seccionNominados;

    @JsonProperty("SeccionInnominados")
    private SeccionInnominados seccionInnominados;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("AAHParty")
    private List<AAHParty> aahpartys;


    public void calculateTipoDocum() {
        System.out.println("calculateTipoDocum");

        //try {
        for (AAHParty party : aahpartys) {
            System.out.println(party);
            CoveredPerson coveredP = party.getCoveredPerson();
            System.out.println(coveredP);
            if (coveredP != null) {
                for (OfficialID officialID: coveredP.getOfficialIDs().getOfficialID()) {
                    System.out.println("*******************************************************");
                    System.out.println(officialID);
                    if (officialID.getPrimary().equalsIgnoreCase("true")) {
                        System.out.println(1);
                        coveredP.setTipoDocum(officialID.getType().getUnlocalizedName());
                        //for (PartyCoverage partyCoverage: party.getPartyCoverages().getPartyCoverage()) {
                        //	partyCoverage.setTipoDocum(officialID.getType().getUnlocalizedName());
                        //}
                        //party.getPartyCoverages().set= officialID.getType().getUnlocalizedName();
                        //} else {
                        //for (PartyCoverage partyCoverage: party.getPartyCoverages().getPartyCoverage()) {
                        //	partyCoverage.setTipoDocum("");
                        //}
                        //coveredP.setTipoDocum("");
                    } else {
                        System.out.println(2);
                        continue;
                    }
                }
            } else {
                continue;
            }

        }
        //} catch (Exception e) {
        //	System.out.println(e);
        //}
    }

    public void calculateActiv() {
        String current = "";
        for (AAHParty party : aahpartys) {
            current += party.getActivity().getDisplayName().toLowerCase().replaceAll("\\s+","");
        }
    }

/*
	public SeccionNominados getSeccionNominados() {
		return seccionNominados;
	}

	public void setSeccionNominados(SeccionNominados seccionNominados) {
		this.seccionNominados = seccionNominados;
	}

	public SeccionInnominados getSeccionInnominados() {
		return seccionInnominados;
	}

	public void setSeccionInnominados(SeccionInnominados seccionInnominados) {
		this.seccionInnominados = seccionInnominados;
	}

	public List<AAHParty> getAahpartys() {
		return aahpartys;
	}

	public void setAahpartys(List<AAHParty> aahpartys) {
		this.aahpartys = aahpartys;
	}
*/
    /**
     * Método que ordena nominados y no nomiados y los agrupa por sus coberturas
     */
    public void generateNominados() {

        List<AAHParty> nominados = new ArrayList<AAHParty>();
        List<AAHParty> no_nominados = new ArrayList<AAHParty>();

        // separado los nominados y los no nominados
        for (AAHParty party : this.aahpartys) {
            if (party.getInsuredType().getCode().equals(Constants.NOMINADOS)) {
                System.out.println("NOMINADOS");
                System.out.println(party.getPartyCoverages().getCoverages());
                nominados.add(party);
            } else if (party.getInsuredType().getCode().equals(Constants.NO_NOMINADOS)) {
                System.out.println("NO_NOMINADOS");
                System.out.println(party.getPartyCoverages().getCoverages());
                no_nominados.add(party);
            } else {
                log.error("No se reconoce tipo: " + party.getInsuredType().getCode());
            }

        }

        // ordenar parties concatenarlos y meterlos en un diccionario
        HashMap<String, String> hmap = new HashMap<String, String>();
        for (AAHParty party : nominados) {
            String covertura = party.getPartyCoverages().getCoverages();
            hmap.put(covertura, "");
        }

        HashMap<String, String> hmapNO = new HashMap<String, String>();
        for (AAHParty party : no_nominados) {
            String covertura = party.getPartyCoverages().getCoverages();
            hmapNO.put(covertura, "");
        }

        SeccionNominados secNo = new SeccionNominados();

        // recorrer el diccionario que diran cuantos grupos hay
        Set set = hmap.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();

            String key = mentry.getKey().toString();
            Seccion secTemp = new Seccion();
            int elementosSeccion = 0;

            System.out.println("VALOR DEL KEY - NOMINADOS");
            System.out.println(key);
            for (AAHParty party : nominados) {
                System.out.println("NOMINADOS");
                if (party.getPartyCoverages().getCoverages().toLowerCase().equals(key.toLowerCase())) {
                    elementosSeccion++;
                    System.out.println("elementosSeccion");
                    System.out.println(elementosSeccion);
                    System.out.println(party.getPartyCoverages().getCoverages());
                    //Según necesidad se coloca la descripcion vacia a partir del segundo elemento de cada seccion
                    if(elementosSeccion > 1) {
                        for (PartyCoverage coverage : party.getPartyCoverages().getPartyCoverage()) {
                            coverage.setDescription("");
                        }
                    }
                    secTemp.addAAHParty(party);
                }
            }

            secNo.addSeccion(secTemp);
        }

        SeccionInnominados secIn = new SeccionInnominados();
        // recorrer el diccionario que diran cuantos grupos hay
        Set set2 = hmapNO.entrySet();
        Iterator iterator2 = set2.iterator();
        while (iterator2.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator2.next();

            String key = mentry.getKey().toString();
            Seccion secTemp = new Seccion();
            int elementosSeccion = 0;

            System.out.println("VALOR DEL KEY - NO NOMINADOS");
            System.out.println(key);
            for (AAHParty party : no_nominados) {
                System.out.println("NO NOMINADOS");
                if (party.getPartyCoverages().getCoverages().toLowerCase().equals(key.toLowerCase())) {
                    elementosSeccion++;
                    System.out.println("elementosSeccion");
                    System.out.println(elementosSeccion);
                    System.out.println(party.getPartyCoverages().getCoverages());
                    //Según necesidad se coloca la descripcion vacia a partir del segundo elemento de cada seccion
                    if(elementosSeccion > 1) {
                        for (PartyCoverage coverage : party.getPartyCoverages().getPartyCoverage()) {
                            coverage.setDescription("");
                        }
                    }
                    secTemp.addAAHParty(party);
                }
            }

            secIn.addSeccion(secTemp);
        }

        this.seccionNominados = secNo;
        this.seccionInnominados = secIn;

        //System.out.println("hola");

    }

    public void descartarOfficialID() {
        for (AAHParty party : this.aahpartys) {
            if(!party.getCoveredPersonExistence()) {
                party.getCoveredPerson().getOfficialIDs().discardOfficialID();
            }
        }
    }

    public void descartarCoverageTerms() {
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.getCoverageTerms().discardCoverageTerms();
            }
        }
    }

    public void calcularDetuctibleValue() {
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateDetuctibleValue();
            }
        }
    }

    public void calcularLimNombre() {
        System.out.println("IMPRIMIENDO CALCULARLIMNOMBRE");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateLimNombre();
            }
        }
    }

    public void calcularDedValor() {
        System.out.println("IMPRIMIENDO CALCULARDEDVALOR");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateDedValor();
            }
        }
    }

    public void calcularLimValorDV() {
        System.out.println("IMPRIMIENDO CALCULARLimValorDV");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateLimValorDV();
            }
        }
    }

    public void calcularLimValorDA() {
        System.out.println("IMPRIMIENDO CALCULARLimValorDA");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateLimValorDA();
            }
        }
    }

    public void calcularLimValor() {
        System.out.println("IMPRIMIENDO CALCULARLimValor");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateLimValor();
            }
        }
    }

    public void calcularDedNombre() {
        System.out.println("IMPRIMIENDO CALCULARDedNombre");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateDedNombre();
            }
        }
    }

    public void calcularSuma() {
        System.out.println("IMPRIMIENDO CALCULARSuma");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateSuma();
            }
        }
    }

    public void calcularEdadMinCant() {
        System.out.println("IMPRIMIENDO CALCULAREdadMinCant");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateEdadMinCant();
            }
        }
    }

    public void calcularEdadMinUnid() {
        System.out.println("IMPRIMIENDO CALCULAREdadMinUnid");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateEdadMinUnid();
            }
        }
    }

    public void calcularEdadMaxIng() {
        System.out.println("IMPRIMIENDO CALCULAREdadMaxIng");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateEdadMaxIng();
            }
        }
    }

    public void calcularEdadMaxPerm() {
        System.out.println("IMPRIMIENDO CALCULAREdadMaxPerm");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateEdadMaxPerm();
            }
        }
    }

    public void calcularDedModelo() {
        System.out.println("IMPRIMIENDO CALCULARDedModelo");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateDedModelo();
            }
        }
    }

    public void calcularLimModelo() {
        System.out.println("IMPRIMIENDO CALCULARLimModelo");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateLimModelo();
            }
        }
    }

    public void calcularSubNombre() {
        System.out.println("IMPRIMIENDO CALCULARSubNombre");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateSubNombre();
            }
        }
    }

    public void calcularSubValor() {
        System.out.println("IMPRIMIENDO CALCULARSubValor");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateSubValor();
            }
        }
    }

    public void calcularSubModelo() {
        System.out.println("IMPRIMIENDO CALCULARSubModelo");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateSubModelo();
            }
        }
    }

    public void calcularSubExcl() {
        System.out.println("IMPRIMIENDO CALCULARSubExcl");
        for (AAHParty party : this.aahpartys) {
            for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
                partyCov.calculateSubExcl();
            }
        }
    }
	/*
	public void calcularTipoDocum() {
		System.out.println("IMPRIMIENDO CALCULARTipoDocum");
		for (AAHParty party : this.aahpartys) {
			for (PartyCoverage partyCov : party.getPartyCoverages().getPartyCoverage()) {
				partyCov.calculateTipoDocum();
			}
		}
	}
	*/
}
/*
	public void calculateTipoDocum() {

		for (AAHParty party : this.aahpartys) {
			for (OfficialID partyCov : party.getCoveredPerson().getOfficialIDs().getOfficialID()) {
				//partyCov.getPrimary();
				if (partyCov.getPrimary().equalsIgnoreCase("true")) {
					System.out.println(1);
					this.tipoDocum = officialID.getType().getUnlocalizedName();
				} else {
					System.out.println(2);
					this.tipoDocum = "";
				}
			}
			;

		}

	//	document.getContent().getPolicyPeriod().getaAHLine().getaAHPartys().getAahpartys()
	/*
	for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			if (coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && coTerm.getValueType().getCode().equalsIgnoreCase("percent") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHAdditionalOfDeathDueTrafficAccidentRateLim") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHDeathOfTheSpouseByAccidentRateLim")) {
				this.subExcl = coTerm.getAggregationModelList().getAggregationModel().getCode();
			} else if (code.equalsIgnoreCase("AAHPharmaceuticalMedicalAssistanceCov")) {
				if (coTerm.getDisplayValue().equalsIgnoreCase("") || coTerm.getDisplayValue().equalsIgnoreCase("0%")) {
					this.subModelo = "";
					this.subValor = "";
					this.subExcl = "se excluyen";
				}
			} else if (code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")) {
				if (coTerm.getDisplayValue().equalsIgnoreCase("") || coTerm.getDisplayValue().equalsIgnoreCase("0%") || coTerm.getDisplayValue().equalsIgnoreCase("100%") ) {
					this.subExcl = "";
				}
			}
		}

	 */
