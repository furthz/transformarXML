package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import pe.soapros.constants.Constants;
import pe.soapros.log.Log;

import java.util.*;
@Data
@JacksonXmlRootElement(localName = "AAHPartys")
public class AAHPartys {
    public static int cantSeccion;
    private static final Log log = Log.getInstance(AAHPartys.class);

    @JsonProperty("SeccionNominados")
    private SeccionNominados seccionNominados;

    @JsonProperty("SeccionInnominados")
    private SeccionInnominados seccionInnominados;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("AAHParty")
    private List<AAHParty> aahpartys;


    public void calculateTipoDocum() {
        //System.out.println("calculateTipoDocum");

        //try {
        for (AAHParty party : aahpartys) {
            //System.out.println(party);
            CoveredPerson coveredP = party.getCoveredPerson();
            //System.out.println(coveredP);
            if (coveredP != null) {
                for (OfficialID officialID: coveredP.getOfficialIDs().getOfficialID()) {
                    //System.out.println("*******************************************************");
                    //System.out.println(officialID);
                    if (officialID.getPrimary().equalsIgnoreCase("true")) {
                        //System.out.println(1);
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


    /**
     * Método que ordena nominados y no nominados y los agrupa por sus coberturas
     */
    public void generateNominados() {

        List<AAHParty> nominados = new ArrayList<AAHParty>();
        List<AAHParty> no_nominados = new ArrayList<AAHParty>();

        // separado los nominados y los no nominados
        for (AAHParty party : this.aahpartys) {
            if (party.getInsuredType().getCode().equals(Constants.NOMINADOS)) {
                nominados.add(party);
            } else if (party.getInsuredType().getCode().equals(Constants.NO_NOMINADOS)) {
                no_nominados.add(party);
            } else {
                log.error("No se reconoce tipo: " + party.getInsuredType().getCode());
            }

        }

        // ordenar parties concatenarlos y meterlos en un diccionario
        HashMap<String, String> hmap = new HashMap<String, String>();
        for (AAHParty party : nominados) {
            String covertura = party.getPartyCoverages().getCoverages();    // Valor concatenado de los Atributos <Code> de cada <PartyCoverage> por cada <AAHParty>
            hmap.put(covertura, "");                                        // Valores concatenados creados lo metemos dentro de un {}, si hay 2 properties con el mismo nombre creará uno
        }

        HashMap<String, String> hmapNO = new HashMap<String, String>();
        for (AAHParty party : no_nominados) {
            String covertura = party.getPartyCoverages().getCoverages();
            hmapNO.put(covertura, "");  // { covertura : ""}
        }

        SeccionNominados secNo = new SeccionNominados();

        // recorrer el diccionario que diran cuantos grupos hay
        Set set = hmap.entrySet();      // [ covertura = ""]
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
			
            String key = mentry.getKey().toString();    // key: Valor Concatenado -> covertura
            Seccion secTemp = new Seccion();
            int elementosSeccion = 0;

            // Recorriendo Arreglo de <AAHParty> nominados
            for (AAHParty party : nominados) {
                // Si el Valor concatenado de los Atributos <Code> de cada <PartyCoverage> por cada <AAHParty> es igual al "key" agregamos a la <Seccion>
                if (party.getPartyCoverages().getCoverages().toLowerCase().equals(key.toLowerCase())) {
                    elementosSeccion++;
                    //Según necesidad se coloca la descripcion vacia a partir del segundo elemento de cada seccion
                  /* if(elementosSeccion > 1) {
                        for (PartyCoverage coverage : party.getPartyCoverages().getPartyCoverage()) {
                            coverage.setDescription("");
                        }
                    }*/
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

            for (AAHParty party : no_nominados) {
                //System.out.println("NO NOMINADOS");
                if (party.getPartyCoverages().getCoverages().toLowerCase().equals(key.toLowerCase())) {
                    elementosSeccion++;
                    //Según necesidad se coloca la descripcion vacia a partir del segundo elemento de cada seccion
                  /*  if(elementosSeccion > 1) {
                        for (PartyCoverage coverage : party.getPartyCoverages().getPartyCoverage()) {
                            coverage.setDescription("");
                        }
                    }*/
                    secTemp.addAAHParty(party);
                }
            }

            secIn.addSeccion(secTemp);
        }

        this.seccionNominados = secNo;
        this.seccionInnominados = secIn;

        //System.out.println("hola");

    }

    public void calculateActiv() {
        List<AAHParty> nominados = new ArrayList<AAHParty>();
        List<AAHParty> no_nominados = new ArrayList<AAHParty>();

        // separado los nominados y los no nominados
        for (AAHParty party : this.aahpartys) {
            if (party.getInsuredType().getCode().equals(Constants.NOMINADOS)) {
                nominados.add(party);
            } else if (party.getInsuredType().getCode().equals(Constants.NO_NOMINADOS)) {
                no_nominados.add(party);
            } else {
                log.error("No se reconoce tipo: " + party.getInsuredType().getCode());
            }

        }
        LinkedHashMap<String, String> hmap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> hmap_no_nominados = new LinkedHashMap<String, String>();
        //HashMap<String, String> hmap = new HashMap<String, String>();
        String current = "";
        int conteo = 0;
        int conteo_no_nominados = 0;
        String coverages = "";
        boolean isTrue = true;

        for (AAHParty party : no_nominados) {
            String currentCoverage = party.getPartyCoverages().getCoverages().toLowerCase().replaceAll("\\s+","");
           // System.out.println(currentCoverage);
            if (conteo_no_nominados == 0) {
                //System.out.println(conteo);
                coverages = currentCoverage;
            }
            if (!coverages.equals(currentCoverage) && conteo_no_nominados > 0) {
                isTrue = false;
              //  System.out.println("entra function");
            }

            String activity = "";
            String clasification = "";
            String riskCategory = "";
            String insurableGroup = "";
            String coveredEvent = "";
            String contractType = "";
            String aAHRiskCategory = "";
            String eventStartDate = "";
            String eventEndDate = "";
            String locationEvent = "";

            //Try-Catch: Activity
            try{
                party.getActivity().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando activity: null");
                activity = null;
            }

            //Try-Catch: Clasification
            try{
                party.getClasification().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando clasification: null");
                clasification = null;
                //System.out.println(party.getCoveredEvent().getDisplayName());
            }

            //RiskCategory
            riskCategory = party.getRiskCategory();

            //Try-Catch: insurableGroup
            try{
                party.getInsurableGroup().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando insurableGroup: null");
                insurableGroup = null;
                //System.out.println(party.getCoveredEvent().getDisplayName());
            }

            //Try-Catch: coveredEvent
            try{
                party.getCoveredEvent().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando coveredEvent: null");
                coveredEvent = null;
                //System.out.println(party.getCoveredEvent().getDisplayName());
            }

            //Try-Catch: ContractType
            try{
                party.getContractType().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando contractType: null");
                contractType = null;
                //System.out.println(party.getContractType().getDisplayName());
            }

            //Try-Catch: AAHRiskCategory
            try{
                party.getAAHRiskCategory().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando contractType: null");
                aAHRiskCategory = null;
                //System.out.println(party.getContractType().getDisplayName());
            }

            //EventStartDate
            eventStartDate = party.getEventStartDate();

            //EventEndDate
            eventEndDate = party.getEventEndDate();

            //LocationEvent
            locationEvent = party.getLocationEvent();

            if (activity != null) {
                activity = party.getActivity().getDisplayName().toLowerCase().replaceAll("\\s+","");
            }
            if (clasification != null) {
                clasification = party.getClasification().getDisplayName().toLowerCase().replaceAll("\\s+","");
            }
            if (riskCategory != null) {
                riskCategory = party.getRiskCategory().toLowerCase().replaceAll("\\s+","");
            }
            if (insurableGroup != null) {
                insurableGroup = party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");
            }
            if (coveredEvent != null) {
                coveredEvent = party.getCoveredEvent().getDisplayName().toLowerCase().replaceAll("\\s+","");
            }
            if (contractType != null) {
                contractType = party.getContractType().getDisplayName().toLowerCase().replaceAll("\\s+","");
            }
            if (aAHRiskCategory != null) {
                aAHRiskCategory = party.getAAHRiskCategory().getDisplayName().toLowerCase().replaceAll("\\s+","");
            }
            if (eventStartDate != null) {
                eventStartDate = party.getEventStartDate().toLowerCase().replaceAll("\\s+","");
            }
            if (eventEndDate != null) {
                eventEndDate = party.getEventEndDate().toLowerCase().replaceAll("\\s+","");
            }
            if (locationEvent != null) {
                locationEvent = party.getLocationEvent().toLowerCase().replaceAll("\\s+","");
            }

            if (conteo_no_nominados == 0) {
                hmap_no_nominados.put("activity", activity);
                hmap_no_nominados.put("clasification", clasification);
                hmap_no_nominados.put("riskcategory", riskCategory);
                hmap_no_nominados.put("insurableGroup", insurableGroup);
                hmap_no_nominados.put("coveredEvent", coveredEvent);
                hmap_no_nominados.put("contractType", contractType);
                hmap_no_nominados.put("aAHRiskCategory", aAHRiskCategory);
                hmap_no_nominados.put("eventStartDate", eventStartDate);
                hmap_no_nominados.put("eventEndDate", eventEndDate);
                hmap_no_nominados.put("locationEvent", locationEvent);   //CREACION DE VALIDACION


                conteo_no_nominados++;
                continue;
            }
        }
        for (AAHParty party : nominados) {

            String currentCoverage = party.getPartyCoverages().getCoverages().toLowerCase().replaceAll("\\s+","");
            if (conteo == 0) {
                //System.out.println(conteo);
                coverages = currentCoverage;
            }

            if (!coverages.equals(currentCoverage) && conteo > 0) {
                isTrue = false;
            }

            String activity = "";
            String clasification = "";
            String riskCategory = "";
            String insurableGroup = "";
            String coveredEvent = "";
            String contractType = "";
            String aAHRiskCategory = "";
            String eventStartDate = "";
            String eventEndDate = "";
            String locationEvent = "";

            //Try-Catch: Activity
            try{
                party.getActivity().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando activity: null");
                activity = null;
            }

            //Try-Catch: Clasification
            try{
                party.getClasification().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando clasification: null");
                clasification = null;
                //System.out.println(party.getCoveredEvent().getDisplayName());
            }

            //RiskCategory
            riskCategory = party.getRiskCategory();

            //Try-Catch: insurableGroup
            try{
                party.getInsurableGroup().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando insurableGroup: null");
                insurableGroup = null;
                //System.out.println(party.getCoveredEvent().getDisplayName());
            }

            //Try-Catch: coveredEvent
            try{
                party.getCoveredEvent().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando coveredEvent: null");
                coveredEvent = null;
                //System.out.println(party.getCoveredEvent().getDisplayName());
            }

            //Try-Catch: ContractType
            try{
                party.getContractType().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando contractType: null");
                contractType = null;
                //System.out.println(party.getContractType().getDisplayName());
            }

            //Try-Catch: AAHRiskCategory
            try{
                party.getAAHRiskCategory().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando contractType: null");
                aAHRiskCategory = null;
                //System.out.println(party.getContractType().getDisplayName());
            }

            //EventStartDate
            eventStartDate = party.getEventStartDate();

            //EventEndDate
            eventEndDate = party.getEventEndDate();

            //LocationEvent
            locationEvent = party.getLocationEvent();

            if (activity != null) {
                activity = party.getActivity().getDisplayName().toLowerCase().replaceAll("\\s+","");
            }
            if (clasification != null) {
                clasification = party.getClasification().getDisplayName().toLowerCase().replaceAll("\\s+","");
            }
            if (riskCategory != null) {
                riskCategory = party.getRiskCategory().toLowerCase().replaceAll("\\s+","");
            }
            if (insurableGroup != null) {
                insurableGroup = party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");
            }
            if (coveredEvent != null) {
                coveredEvent = party.getCoveredEvent().getDisplayName().toLowerCase().replaceAll("\\s+","");
            }
            if (contractType != null) {
                contractType = party.getContractType().getDisplayName().toLowerCase().replaceAll("\\s+","");
            }
            if (aAHRiskCategory != null) {
                aAHRiskCategory = party.getAAHRiskCategory().getDisplayName().toLowerCase().replaceAll("\\s+","");
            }
            if (eventStartDate != null) {
                eventStartDate = party.getEventStartDate().toLowerCase().replaceAll("\\s+","");
            }
            if (eventEndDate != null) {
                eventEndDate = party.getEventEndDate().toLowerCase().replaceAll("\\s+","");
            }
            if (locationEvent != null) {
                locationEvent = party.getLocationEvent().toLowerCase().replaceAll("\\s+","");
            }

            if (conteo == 0) {
                hmap.put("activity", activity);
                hmap.put("clasification", clasification);
                hmap.put("riskcategory", riskCategory);
                hmap.put("insurableGroup", insurableGroup);
                hmap.put("coveredEvent", coveredEvent);
                hmap.put("contractType", contractType);
                hmap.put("aAHRiskCategory", aAHRiskCategory);
                hmap.put("eventStartDate", eventStartDate);
                hmap.put("eventEndDate", eventEndDate);
                hmap.put("locationEvent", locationEvent);   //CREACION DE VALIDACION

                conteo++;
                continue;
            }
        }
        //hmap.put(current, "");
        SeccionNominados secNo = new SeccionNominados();
        SeccionInnominados secIn = new SeccionInnominados();
        //System.out.println(hmap);
        Set set = hmap.entrySet();
        Set setIn = hmap_no_nominados.entrySet();
        //System.out.println(set.size());
        Iterator iterator = set.iterator();
        Iterator iteratorIn = setIn.iterator();
        int conteoIterator = 0;
        int conteoIteratorIn = 0;

        boolean isEquals = false;
        boolean isEquals2= false;
        boolean isEquals3= false;
        boolean isEquals4= false;
        boolean isEquals5= false;
        boolean isEquals6= false;
        boolean isEquals7= false;
        boolean isEquals8= false;
        boolean isEquals9= false;
        boolean isEquals10= false;

        boolean isEqualsIn = false;
        boolean isEqualsIn2= false;
        boolean isEqualsIn3= false;
        boolean isEqualsIn4= false;
        boolean isEqualsIn5= false;
        boolean isEqualsIn6= false;
        boolean isEqualsIn7= false;
        boolean isEqualsIn8= false;
        boolean isEqualsIn9= false;
        boolean isEqualsIn10= false;

        boolean isDifferent = false;
        boolean isDifferent2 = false;
        boolean isDifferent3 = false;
        boolean isDifferent4 = false;
        boolean isDifferent5 = false;
        boolean isDifferent6 = false;
        boolean isDifferent7 = false;
        boolean isDifferent8 = false;
        boolean isDifferent9 = false;
        boolean isDifferent10 = false;

        boolean isDifferentIn = false;
        boolean isDifferentIn2 = false;
        boolean isDifferentIn3 = false;
        boolean isDifferentIn4 = false;
        boolean isDifferentIn5 = false;
        boolean isDifferentIn6 = false;
        boolean isDifferentIn7 = false;
        boolean isDifferentIn8 = false;
        boolean isDifferentIn9 = false;
        boolean isDifferentIn10 = false;

        Seccion secTemp2 = new Seccion();
        Seccion secTemp3 = new Seccion();
        Seccion secTemp2In = new Seccion();
        Seccion secTemp3In = new Seccion();
        Seccion secCoverage = new Seccion();
        Seccion secCoverageIn = new Seccion();
        while (iterator.hasNext()) {
            conteoIterator++;
            System.out.println("conteoIterator: "+conteoIterator);
            Map.Entry mentry = (Map.Entry) iterator.next();
            String key = "";
            if (mentry.getValue() != null) {
                key = mentry.getValue().toString();
            } else {
                key = null;
            }
            //key = mentry.getValue().toString();
            System.out.println("Key: "+key);
            Seccion secTemp = new Seccion();
            Seccion secTemp1 = new Seccion();
            Seccion secTemp4 = new Seccion();
            Seccion secTemp5 = new Seccion();
            Seccion secTemp6 = new Seccion();
            Seccion secTemp7 = new Seccion();
            Seccion secTemp8 = new Seccion();
            Seccion secTemp9 = new Seccion();
            Seccion secTemp10 = new Seccion();
            Seccion secTemp11 = new Seccion();
            Seccion secTemp12 = new Seccion();
            Seccion secTemp13 = new Seccion();
            Seccion secTemp14 = new Seccion();
            Seccion secTemp15 = new Seccion();
            Seccion secTemp16 = new Seccion();
            Seccion secTemp17 = new Seccion();
            Seccion secTemp18 = new Seccion();

            int count = 0;

            for (AAHParty party : nominados) {
                //AGREGO COVERTURAS
                String coverages3 = "";
                String currentCoverage2 = party.getPartyCoverages().getCoverages().toLowerCase().replaceAll("\\s+", "");
                if (conteo_no_nominados == 0) {
                    coverages3 = currentCoverage2;
                }
                count++;
                //System.out.println(nominados.size());

                String currentActivity = "";
                String currentClasification = "";
                String currentRiskCategory = "";
                String currentInsurableGroup = "";
                String currentCoveredEvent = "";
                String currentContractType = "";
                String currentAAHRiskCategory = "";
                String currentEventStartDate = "";
                String currentEventEndDate = "";
                String currentLocalEvent = "";

                //Try-Catch: Activity
                try{
                    party.getActivity().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando activity: null");
                    currentActivity = null;
                    //System.out.println(party.getCoveredEvent().getDisplayName());
                }
                if (currentActivity != null) {
                    currentActivity = party.getActivity().getDisplayName().toLowerCase().replaceAll("\\s+","");
                }

                //Try-Catch: Clasification
                try{
                    party.getClasification().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando Clasification: null");
                    currentClasification = null;
                    //System.out.println(party.getCoveredEvent().getDisplayName());
                }
                if (currentClasification != null) {
                    currentClasification = party.getClasification().getDisplayName().toLowerCase().replaceAll("\\s+","");
                }

                //RiskCategory
                currentRiskCategory = party.getRiskCategory();
                if (currentRiskCategory != null) {
                    currentRiskCategory = party.getRiskCategory().toLowerCase().replaceAll("\\s+","");
                }

                //Try-Catch: currentInsurableGroup
                try{
                    party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando Clasification: null");
                    currentInsurableGroup = null;
                    //System.out.println(party.getCoveredEvent().getDisplayName());
                }
                if (currentInsurableGroup != null) {
                    currentInsurableGroup = party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");
                }

                //Try-Catch: currentCoveredEvent
                try{
                    party.getCoveredEvent().getDisplayName();
                    //party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando currentCoveredEvent: null");
                    currentCoveredEvent = null;
                }
                if (currentCoveredEvent != null) {
                    currentCoveredEvent = party.getCoveredEvent().getDisplayName().toLowerCase().replaceAll("\\s+","");
                }

                //Try-Catch: currentContractType
                try{
                    party.getContractType().getDisplayName();
                    //party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando currentCoveredEvent: null");
                    currentContractType = null;
                }
                if (currentContractType != null) {
                    currentContractType = party.getContractType().getDisplayName().toLowerCase().replaceAll("\\s+","");
                }

                //Try-Catch: currentAAHRiskCategory
                try{
                    party.getAAHRiskCategory().getDisplayName();
                    //party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando currentCoveredEvent: null");
                    currentAAHRiskCategory = null;
                }
                if (currentAAHRiskCategory != null) {
                    currentAAHRiskCategory = party.getAAHRiskCategory().getDisplayName().toLowerCase().replaceAll("\\s+","");
                }

                //EventStartDate
                currentEventStartDate = party.getEventStartDate();
                if (currentEventStartDate != null) {
                    currentEventStartDate = party.getEventStartDate().toLowerCase().replaceAll("\\s+","");
                }

                //EventEndDate
                currentEventEndDate = party.getEventEndDate();
                if (currentEventEndDate != null) {
                    currentEventEndDate = party.getEventEndDate().toLowerCase().replaceAll("\\s+","");
                }

                //LocalEvent
                currentLocalEvent = party.getLocationEvent();
                if (currentLocalEvent != null) {
                    currentLocalEvent = party.getLocationEvent().toLowerCase().replaceAll("\\s+","");
                }

                if (conteoIterator == 1) {

                    if (currentActivity != null) {
                        if (key != null) {
                            if (currentActivity.equals(key.toLowerCase())) {
                                secTemp2.addAAHParty(party);
                                if (count == nominados.size()) {
                                    isEquals = true;
                                }
                            } else {
                                secTemp3.addAAHParty(party);
                                isDifferent = true;
                            }
                        } else {
                            secTemp3.addAAHParty(party);
                            isDifferent = true;
                        }
                    } else {
                        if (key != null) {
                            secTemp3.addAAHParty(party);
                            isDifferent = true;
                        } else {
                            secTemp2.addAAHParty(party);
                            if (count == nominados.size()) {
                                isEquals = true;
                            }
                        }
                    }
                }else if (conteoIterator == 2) {

                        if (currentClasification != null) {
                            if (key != null) {
                                if (currentClasification.equals(key.toLowerCase())) {
                                   secTemp.addAAHParty(party);

                                    //CASO 1: Activity !=
                                    if (count == nominados.size() && isDifferent) {
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                        isEquals2 = true;
                                    }

                                    if (count == nominados.size() && isEquals) {
                                        //secNo.addSeccion(secTemp2);
                                        isEquals2 = true;
                                    }
                                } else {
                                    secTemp1.addAAHParty(party);
                                    if (count == nominados.size() && isDifferent) {
                                        //secNo.addSeccion(secTemp3);
                                        isDifferent2 = true;
                                    }

                                    if (count == nominados.size() && isEquals) {
                                        //secNo.addSeccion(secTemp2);
                                       secTemp2 = secTemp;
                                       secTemp3 = secTemp1;
                                       isDifferent2 = true;
                                    }

                                }
                            } else {
                                if (count == nominados.size()) {
                                    secTemp3 = new Seccion();
                                    secTemp3.addAAHParty(party);
                                    isEquals2 = false;
                                    isDifferent2 = true;
                                }
                            }
                        } else {
                            if (key != null) {
                                //secTemp.addAAHParty(party);
                                secTemp1.addAAHParty(party);
                                secTemp2 = secTemp;
                                secTemp3 = secTemp1;
                                isDifferent2 = true;
                            } else {
                                secTemp.addAAHParty(party);
                                secTemp2 = secTemp;
                                if (count == nominados.size()) {
                                    isEquals2 = true;
                                }
                            }
                        }

                    } else if (conteoIterator == 3) {

                        if (currentRiskCategory != null) {
                            if (key != null) {
                                if (currentRiskCategory.equals(key.toLowerCase())) {
                                    secTemp4.addAAHParty(party);

                                    //CASO2: Clasification !=
                                    if (count == nominados.size() && isEquals && isDifferent2) {
                                        /*secNo.addSeccion(secTemp2);
                                        secNo.addSeccion(secTemp3);*/
                                        isEquals3 = true;
                                    }
                                    //Activity != && Clasification !=
                                    if (count == nominados.size() && isDifferent && isDifferent2) {
                                        isEquals3 = true;
                                    }
                                    if (count == nominados.size() && isEquals && isEquals2) {
                                        isEquals3 = true;
                                      }
                                    if (count == nominados.size() && isDifferent && isEquals2) {
                                        isEquals3 = true;
                                    }
                                } else {
                                    secTemp1.addAAHParty(party);
                                    if (count == nominados.size() && isDifferent && isDifferent2) {
                                        isDifferent3 = true;
                                        //secNo.addSeccion(secTemp3);
                                    }
                                    //CASO3: RiskCategory !=
                                    if (count == nominados.size() && isEquals && isEquals2) {
                                        secTemp2 = secTemp4;
                                        secTemp3 = secTemp1;
                                        isDifferent3 = true;
                                        //secNo.addSeccion(secTemp2);
                                    }
                                }
                            } else {
                                if (count == nominados.size()) {
                                    secTemp3 = new Seccion();
                                    secTemp3.addAAHParty(party);
                                    isEquals3 = false;
                                    isDifferent3 = true;
                                }
                            }

                        } else {
                            //isEquals3 = true;

                            if (key != null) {
                                //secTemp.addAAHParty(party);
                                secTemp1.addAAHParty(party);
                                secTemp2 = secTemp4;
                                secTemp3 = secTemp1;
                                isDifferent3 = true;
                            } else {
                                secTemp4.addAAHParty(party);
                                secTemp2 = secTemp4;
                                if (count == nominados.size()) {
                                    isEquals3 = true;
                                }
                            }
                        }
                    } else if (conteoIterator == 4) {

                        if (currentInsurableGroup != null) {
                            if (key != null) {
                                if (currentInsurableGroup.equals(key.toLowerCase())) {
                                    secTemp5.addAAHParty(party);
                                    //CASO1: Activity =!
                                    if (count == nominados.size() && isDifferent && isEquals2 && isEquals3) {
                                        isEquals4 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }
                                    //CASO2: Clasification !=
                                    if (count == nominados.size() && isEquals && isDifferent2 && isEquals3) {
                                        isEquals4 = true;
                                        /*secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);*/
                                    }
                                    //CASO3: RiskCategory !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isDifferent3) {
                                        isEquals4 = true;
                                       /* //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);*/
                                    }
                                    //Activity != && Clasification !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isEquals3) {
                                        isEquals4 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3) {
                                        isEquals4 = true;
                                    }
                                    //Activity !=  && RiskCategory !=
                                    if (count == nominados.size() && isDifferent && isEquals2 && isDifferent3) {
                                        isEquals4 = true;
                                    }

                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3) {
                                        isEquals4 = true;
                                    }
                                 } else {
                                    secTemp6.addAAHParty(party);
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3) {
                                        isDifferent4 = true;
                                        //secNo.addSeccion(secTemp3);
                                    }
                                    //CASO3: InsurableGroup !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3) {
                                        secTemp2 = secTemp5;
                                        secTemp3 = secTemp6;
                                        isDifferent4 = true;
                                    }
                                    //CASO4: Activity != && InsurableGroup =!
                                    if (count == nominados.size() && isDifferent && isEquals2 && isEquals3) {
                                        isDifferent4 = true;
                                    }
                                    //CASO5: RiskCategory != && InsurableGroup !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isDifferent3) {
                                        isDifferent4 = true;
                                    }
                                    //CASO6: Clasification != && InsurableGroup !=
                                    if (count == nominados.size() && isEquals && isDifferent2 && isEquals3) {
                                        isDifferent4 = true;
                                    }
                                }
                            } else {
                                if (count == nominados.size()) {
                                    secTemp3 = new Seccion();
                                    secTemp3.addAAHParty(party);
                                    isEquals4 = false;
                                    isDifferent4 = true;
                                }
                            }

                        } else {
                            if (key != null) {
                                secTemp6.addAAHParty(party);
                                secTemp2 = secTemp5;
                                secTemp3 = secTemp6;
                                isDifferent4 = true;
                            } else {
                                secTemp5.addAAHParty(party);
                                secTemp2 = secTemp5;
                                if (count == nominados.size()) {
                                    isEquals4 = true;
                                }
                            }
                        }
                    } else if (conteoIterator == 5) {
                         if (currentCoveredEvent != null) {
                            if (key != null) {
                                if (currentCoveredEvent.equals(key.toLowerCase())) {
                                    secTemp7.addAAHParty(party);
                                    //CASO1: Activity !=
                                    if (count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4) {
                                        isEquals5 = true;
                                    }
                                    //CASO2: Clasification !=
                                    if (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4) {
                                        isEquals5 = true;
                                    }
                                    //CASO3: RiskCategory !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4) {
                                       isEquals5 = true;
                                    }
                                    //CASO4: InsurableGroup !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4) {
                                        isEquals5 = true;
                                    }
                                    //Activity != && Clasification !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4) {
                                        isEquals5 = true;
                                    }
                                    //Activity !=  && RiskCategory !=
                                    if (count == nominados.size() && isDifferent && isEquals2 && isDifferent3 && isEquals4) {
                                        isEquals5 = true;
                                    }
                                    //Activity !=  && InsurableGroup !=
                                    if (count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isDifferent4) {
                                        isEquals5 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4) {
                                        isEquals5 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4) {
                                        isEquals5 = true;
                                    }
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4) {
                                        isEquals5 = true;
                                    }

                                } else {
                                    secTemp8.addAAHParty(party);
                                    //CASO5: CoveredEvent !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4) {
                                        secTemp2 = secTemp7;
                                        secTemp3 = secTemp8;
                                        isDifferent5 = true;
                                    }
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4) {
                                        isDifferent5 = true;
                                    }

                                }
                            } else {
                                //isDifferent, isDifferent2, isDifferent3, isDifferent4, setTemp2, setTemp3, secTemp3, secTemp3, secTemp3
                                if (count == nominados.size()) {
                                    secTemp3 = new Seccion();
                                    secTemp3.addAAHParty(party);
                                    isEquals5 = false;
                                    isDifferent5 = true;
                                }
                                if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5) {
                                  /*  //secNo.addSeccion(secTemp2);
                                    //secNo.addSeccion(secTemp3);*/
                                    isDifferent5 = true;
                                }
                                if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5) {
                                  /*  //secNo.addSeccion(secTemp2);
                                    //secNo.addSeccion(secTemp3);*/
                                    isDifferent5 = true;
                                }
                            }

                        } else {
                            //isEquals5 = true;

                            if (key != null) {
                                secTemp8.addAAHParty(party);
                                secTemp2 = secTemp7;
                                secTemp3 = secTemp8;
                                isDifferent5 = true;
                            } else {
                                secTemp7.addAAHParty(party);
                                secTemp2 = secTemp7;
                                if (count == nominados.size()) {
                                    isEquals5 = true;
                                }
                            }
                        }
                    } else if (conteoIterator == 6) {
                        if (currentContractType != null) {
                            if (key != null) {
                                if (currentContractType.equals(key.toLowerCase())) {
                                    secTemp9.addAAHParty(party);
                                    //CASO1: Activity =!
                                    if (count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5) {
                                        isEquals6 = true;
                                    }
                                    //CASO2: Clasification !=
                                    if (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5) {
                                        isEquals6 = true;
                                    }
                                    //CASO3: RiskCategory !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5) {
                                        isEquals6 = true;
                                    }
                                    //CASO4: InsurableGroup !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5) {
                                        isEquals6 = true;
                                    }
                                    //CASO5: CoveredEvent !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5) {
                                        isEquals6 = true;
                                    }
                                    //Activity != && Clasification !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5) {
                                        isEquals6 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5) {
                                        isEquals6 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5) {
                                        isEquals6 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5) {
                                        isEquals6 = true;
                                    }
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3&& isEquals4 && isEquals5) {
                                        isEquals6 = true;
                                    }
                                } else {
                                    secTemp10.addAAHParty(party);
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5) {
                                        isDifferent6 = true;
                                    }
                                    //CASO6: ContractType !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5) {
                                        secTemp2 = secTemp9;
                                        secTemp3 = secTemp10;
                                        isDifferent6 = true;
                                    }
                                }
                            } else {
                                if (count == nominados.size()) {
                                    secTemp3 = new Seccion();
                                    secTemp3.addAAHParty(party);
                                    isEquals6 = false;
                                    isDifferent6 = true;
                                }
                            }
                        } else {
                            if (key != null) {
                                secTemp10.addAAHParty(party);
                                secTemp2 = secTemp9;
                                secTemp3 = secTemp10;
                                isDifferent6 = true;

                            } else {
                                secTemp9.addAAHParty(party);
                                secTemp2 = secTemp9;
                                if (count == nominados.size()) {
                                    isEquals6 = true;
                                }
                            }
                        }
                    } else if (conteoIterator == 7) {

                        if (currentAAHRiskCategory != null) {
                            if (key != null) {
                                if (currentAAHRiskCategory.equals(key.toLowerCase())) {
                                    secTemp11.addAAHParty(party);
                                    //CASO1: Activity =!
                                    if (count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) {
                                        isEquals7 = true;
                                    }
                                    //CASO2: Clasification !=
                                    if (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) {
                                        isEquals7 = true;
                                    }
                                    //CASO3: RiskCategory !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6) {
                                        isEquals7 = true;
                                    }
                                    //CASO4: InsurableGroup !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6) {
                                        isEquals7 = true;
                                    }
                                    //CASO5: CoveredEvent !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6) {
                                        isEquals7 = true;
                                    }
                                    //CASO6: AAHRiskCategory !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6) {
                                        isEquals7 = true;
                                    }
                                    //Activity != && Clasification !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) {
                                        isEquals7 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6) {
                                        isEquals7 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6) {
                                        isEquals7 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6) {
                                        isEquals7 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6) {
                                        isEquals7 = true;
                                    }
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3&& isEquals4 && isEquals5 && isEquals6) {
                                        isEquals7 = true;
                                    }

                                } else {
                                    secTemp12.addAAHParty(party);

                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6) {
                                        isDifferent7 = true;
                                    }
                                    //CASO7: AAHRiskCategory !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) {
                                        secTemp2 = secTemp11;
                                        secTemp3 = secTemp12;
                                        isDifferent7 = true;
                                    }
                                }
                            } else {
                                if (count == nominados.size()) {
                                    secTemp3 = new Seccion();
                                    secTemp3.addAAHParty(party);
                                    isEquals7 = false;
                                    isDifferent7 = true;
                                }
                            }
                        } else {
                            if (key != null) {
                                secTemp12.addAAHParty(party);
                                secTemp2 = secTemp11;
                                secTemp3 = secTemp12;
                                isDifferent7 = true;

                            } else {
                                secTemp11.addAAHParty(party);
                                secTemp2 = secTemp11;
                                if (count == nominados.size()) {
                                    isEquals7 = true;
                                }
                            }
                        }
                    } else if (conteoIterator == 8) {
                        if (currentEventStartDate != null) {
                            if (key != null) {
                                if (currentEventStartDate.equals(key.toLowerCase())) {
                                    secTemp13.addAAHParty(party);
                                    //CASO1: Activity =!
                                    if (count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) {
                                        isEquals8 = true;
                                    }
                                    //CASO2: Clasification !=
                                    if (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) {
                                        isEquals8 = true;
                                    }
                                    //CASO3: RiskCategory !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) {
                                        isEquals8 = true;
                                    }
                                    //CASO4: InsurableGroup !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7) {
                                        isEquals8 = true;
                                    }
                                    //CASO5: CoveredEvent !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7) {
                                        isEquals8 = true;
                                    }
                                    //CASO6: ContractType !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7) {
                                        isEquals8 = true;
                                    }
                                    //CASO7: AAHRiskCategory !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7) {
                                        isEquals8 = true;
                                    }
                                    //Activity != && Clasification !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) {
                                        isEquals8 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) {
                                        isEquals8 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7) {
                                        isEquals8 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7) {
                                        isEquals8 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7) {
                                        isEquals8 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType != && AAHRiskCategory !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7) {
                                        isEquals8 = true;
                                    }
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3&& isEquals4 && isEquals5 && isEquals6 && isEquals7) {
                                        isEquals8 = true;
                                    }

                                } else {
                                    secTemp14.addAAHParty(party);
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7) {
                                        isDifferent8 = true;
                                    }
                                    //CASO8: EventStartDate !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) {
                                        secTemp2 = secTemp13;
                                        secTemp3 = secTemp14;
                                        isDifferent8 = true;
                                    }
                                }
                            } else {
                                if (count == nominados.size()) {
                                    secTemp3 = new Seccion();
                                    secTemp3.addAAHParty(party);
                                    isEquals8 = false;
                                    isDifferent8 = true;
                                }
                            }
                        } else {
                            if (key != null) {
                                secTemp14.addAAHParty(party);
                                secTemp2 = secTemp13;
                                secTemp3 = secTemp14;
                                isDifferent8 = true;

                            } else {
                                secTemp13.addAAHParty(party);
                                secTemp2 = secTemp13;
                                if (count == nominados.size()) {
                                    isEquals8 = true;
                                }
                            }
                        }
                    } else if (conteoIterator == 9) {

                        if (currentEventEndDate != null) {
                            if (key != null) {
                                if (currentEventEndDate.equals(key.toLowerCase())) {
                                    secTemp15.addAAHParty(party);
                                    //CASO1: Activity =!
                                    if (count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) {
                                        isEquals9 = true;
                                    }
                                    //CASO2: Clasification !=
                                    if (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) {
                                        isEquals9 = true;
                                    }
                                    //CASO3: RiskCategory !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) {
                                        isEquals9 = true;
                                    }
                                    //CASO4: InsurableGroup !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) {
                                        isEquals9 = true;
                                    }
                                    //CASO5: CoveredEvent !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8) {
                                        isEquals9 = true;
                                    }
                                    //CASO6: ContractType !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7 && isEquals8) {
                                        isEquals9 = true;
                                    }
                                    //CASO7: AAHRiskCategory !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7 && isEquals8) {
                                        isEquals9 = true;
                                    }
                                    //CASO8: EventStartDate !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isDifferent8) {
                                        isEquals9 = true;
                                    }
                                    //Activity != && Clasification !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) {
                                        isEquals9 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) {
                                        isEquals9 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) {
                                        isEquals9 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8) {
                                        isEquals9 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7 && isEquals8) {
                                        isEquals9 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType != && AAHRiskCategory !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isEquals8) {
                                        isEquals9 = true;
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType != && AAHRiskCategory != && EventStartDate !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8) {
                                        isEquals9 = true;
                                    }
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3&& isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) {
                                        isEquals9 = true;
                                    }

                                } else {
                                    secTemp16.addAAHParty(party);
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8) {
                                        isDifferent9 = true;
                                    }
                                    //CASO9: EventEndDate !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) {
                                        secTemp2 = secTemp15;
                                        secTemp3 = secTemp16;
                                        isDifferent9 = true;
                                    }
                                }
                            } else {
                                if (count == nominados.size()) {
                                    secTemp3 = new Seccion();
                                    secTemp3.addAAHParty(party);
                                    isEquals9 = false;
                                    isDifferent9 = true;
                                }
                            }
                        } else {
                            if (key != null) {
                                secTemp16.addAAHParty(party);
                                secTemp2 = secTemp15;
                                secTemp3 = secTemp16;
                                isDifferent9 = true;

                            } else {
                                secTemp15.addAAHParty(party);
                                secTemp2 = secTemp15;
                                if (count == nominados.size()) {
                                    isEquals9 = true;
                                }
                            }
                        }
                    } else if (conteoIterator == 10) {

                        if (currentLocalEvent != null) {
                            if (key != null) {
                                if (currentLocalEvent.equals(key.toLowerCase())) {
                                    if (isTrue) {
                                  //  if (!coverages.equals(currentCoverage2)) {
                                       secTemp17.addAAHParty(party);
                                        //CASO1: Activity =!
                                        if (count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                            secNo.addSeccion(secTemp2);
                                            secNo.addSeccion(secTemp3);
                                        }
                                        //CASO2: Clasification !=
                                        if (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                            secNo.addSeccion(secTemp2);
                                            secNo.addSeccion(secTemp3);
                                        }
                                        //CASO3: RiskCategory !=
                                        if (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                            secNo.addSeccion(secTemp2);
                                            secNo.addSeccion(secTemp3);
                                        }
                                        //CASO4: InsurableGroup !=
                                        if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                            secNo.addSeccion(secTemp2);
                                            secNo.addSeccion(secTemp3);
                                        }
                                        //CASO5: CoveredEvent !=
                                        if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                            secNo.addSeccion(secTemp2);
                                            secNo.addSeccion(secTemp3);
                                        }
                                        //CASO6: ContractType !=
                                        if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                            secNo.addSeccion(secTemp2);
                                            secNo.addSeccion(secTemp3);
                                        }
                                        //CASO7: AAHRiskCategory !=
                                        if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                            secNo.addSeccion(secTemp2);
                                            secNo.addSeccion(secTemp3);
                                        }
                                        //CASO8: EventStartDate !=
                                        if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isDifferent8 && isEquals9) {
                                            isEquals10 = true;
                                            secNo.addSeccion(secTemp2);
                                            secNo.addSeccion(secTemp3);
                                        }
                                        //CASO9: EventEndDate !=
                                        if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isDifferent9) {
                                            isEquals10 = true;
                                            secNo.addSeccion(secTemp2);
                                            secNo.addSeccion(secTemp3);
                                        }
                                        //Activity != && Clasification !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                            secNo.addSeccion(secTemp2);
                                            secNo.addSeccion(secTemp3);
                                        }
                                        //Activity != && Clasification != && RiskCategory !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                            secNo.addSeccion(secTemp2);
                                            secNo.addSeccion(secTemp3);
                                        }
                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                            secNo.addSeccion(secTemp2);
                                            secNo.addSeccion(secTemp3);
                                        }
                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                            secNo.addSeccion(secTemp2);
                                            secNo.addSeccion(secTemp3);
                                        }
                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                           secNo.addSeccion(secTemp2);
                                           secNo.addSeccion(secTemp3);
                                        }
                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType != && AAHRiskCategory !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                           secNo.addSeccion(secTemp2);
                                           secNo.addSeccion(secTemp3);
                                        }
                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType != && AAHRiskCategory != && EventStartDate !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isEquals9) {
                                            isEquals10 = true;
                                             secNo.addSeccion(secTemp2);
                                             secNo.addSeccion(secTemp3);
                                        }
                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType != && AAHRiskCategory != && EventStartDate != && EventEndDate !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isDifferent9) {
                                            isEquals10 = true;
                                            secNo.addSeccion(secTemp2);
                                            secNo.addSeccion(secTemp3);
                                        }
                                        if (count == nominados.size() && isEquals && isEquals2 && isEquals3&& isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                            secNo.addSeccion(secTemp17);
                                        }
                                    } else {
                                        if ( count == nominados.size() || count!= nominados.size() ) {
                                            secCoverage = new Seccion();
                                            secCoverage.addAAHParty(party);
                                            secNo.addSeccion(secCoverage);
                                        }
                                    }
                                } else {
                                    secTemp18.addAAHParty(party);

                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isDifferent9) {
                                        isDifferent10 = true;
                                       secNo.addSeccion(secTemp2);
                                       secNo.addSeccion(secTemp3);
                                    }
                                    //CASO10: LocalEvent !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                        secTemp2 = secTemp17;
                                        secTemp3 = secTemp18;
                                        isDifferent10 = true;
                                        secNo.addSeccion(secTemp2);
                                        secNo.addSeccion(secTemp3);
                                    }
                                    /*secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);*/
                                }
                            } else {
                                if (count == nominados.size()) {
                                    secTemp3 = new Seccion();
                                    secTemp3.addAAHParty(party);
                                    isEquals10 = false;
                                    isDifferent10 = true;

                                }
                                if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9 && isDifferent10) {
                                    secNo.addSeccion(secTemp2);
                                    secNo.addSeccion(secTemp3);
                                    isDifferent10 = true;
                                }
                                if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isDifferent9 && isDifferent10) {
                                    secNo.addSeccion(secTemp2);
                                    secNo.addSeccion(secTemp3);
                                    isDifferent10 = true;
                                }
                            }
                        } else {
                            if (key != null) {
                                secTemp18.addAAHParty(party);
                                secTemp2 = secTemp17;
                                secTemp3 = secTemp18;
                                isDifferent10 = true;

                            } else {
                                if (isTrue) {
                                //if (!coverages.equals(currentCoverage2)) {
                                    //secTemp.addAAHParty(party);
                                  secTemp17.addAAHParty(party);
                                  secTemp2 = secTemp17;
                                    if (count == nominados.size()) {
                                        isEquals10 = true;
                                    }
                                } else {
                                    if ( count == nominados.size() || count!= nominados.size() ) {
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                    }
                                }

                            }

                            if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9 && isEquals10) {
                                secNo.addSeccion(secTemp2);
                            }

                            if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9 && isDifferent10) {
                                secNo.addSeccion(secTemp2);
                                secNo.addSeccion(secTemp3);
                            }

                            if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isDifferent9 && isDifferent10) {
                                secNo.addSeccion(secTemp2);
                                secNo.addSeccion(secTemp3);
                            }

                        }
                    }
                //}
            }

            XmlMapper xmlMapper = new XmlMapper();

            // Eliminando Secciones vacias
            int sizeASecciones = secNo.getSeccion().size();
           // int sizeASecciones1 = secNo.size();
          System.out.println(sizeASecciones);
            //System.out.println(sizeASecciones);
            for (int i = 0; i < sizeASecciones; i++) {
                //System.out.println(secNo.getSeccion().get(i).getSize());
                if (secNo.getSeccion().get(i).getSize() == 0) {
                    //secNo.getSeccion().remove(0);
                }
            }
        }

        while (iteratorIn.hasNext()) {
            conteoIteratorIn++;
            System.out.println("conteoIteratorIn: "+conteoIteratorIn);
            Map.Entry mentry = (Map.Entry) iteratorIn.next();
            String key = "";
            System.out.println(mentry);
            if (mentry.getValue() != null) {
                key = mentry.getValue().toString();
            } else {
                key = null;
            }
            //key = mentry.getValue().toString();
            System.out.println("Key: "+key);
            Seccion secTemp = new Seccion();
            Seccion secTemp1 = new Seccion();
            Seccion secTemp4 = new Seccion();
            Seccion secTemp5 = new Seccion();
            Seccion secTemp6 = new Seccion();
            Seccion secTemp7 = new Seccion();
            Seccion secTemp8 = new Seccion();
            Seccion secTemp9 = new Seccion();
            Seccion secTemp10 = new Seccion();
            Seccion secTemp11 = new Seccion();
            Seccion secTemp12 = new Seccion();
            Seccion secTemp13 = new Seccion();
            Seccion secTemp14 = new Seccion();
            Seccion secTemp15 = new Seccion();
            Seccion secTemp16 = new Seccion();
            Seccion secTemp17 = new Seccion();
            Seccion secTemp18 = new Seccion();

            int count = 0;

            for (AAHParty party : no_nominados) {
                //AGREGO COVERTURAS
                String coverages1 = "";
                String currentCoverage1 = party.getPartyCoverages().getCoverages().toLowerCase().replaceAll("\\s+", "");
                if (conteo_no_nominados == 0) {
                    coverages1 = currentCoverage1;

                }
                count++;
                //System.out.println(no_nominados.size());

                String currentActivity = "";
                String currentClasification = "";
                String currentRiskCategory = "";
                String currentInsurableGroup = "";
                String currentCoveredEvent = "";
                String currentContractType = "";
                String currentAAHRiskCategory = "";
                String currentEventStartDate = "";
                String currentEventEndDate = "";
                String currentLocalEvent = "";

                //Try-Catch: Activity
                try{
                    party.getActivity().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando activity: null");
                    currentActivity = null;
                    //System.out.println(party.getCoveredEvent().getDisplayName());
                }
                if (currentActivity != null) {
                    currentActivity = party.getActivity().getDisplayName().toLowerCase().replaceAll("\\s+","");
                }

                //Try-Catch: Clasification
                try{
                    party.getClasification().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando Clasification: null");
                    currentClasification = null;
                    //System.out.println(party.getCoveredEvent().getDisplayName());
                }
                if (currentClasification != null) {
                    currentClasification = party.getClasification().getDisplayName().toLowerCase().replaceAll("\\s+","");
                }

                //RiskCategory
                currentRiskCategory = party.getRiskCategory();
                if (currentRiskCategory != null) {
                    currentRiskCategory = party.getRiskCategory().toLowerCase().replaceAll("\\s+","");
                }

                //Try-Catch: currentInsurableGroup
                try{
                    party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando Clasification: null");
                    currentInsurableGroup = null;
                    //System.out.println(party.getCoveredEvent().getDisplayName());
                }
                if (currentInsurableGroup != null) {
                    currentInsurableGroup = party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");
                }

                //Try-Catch: currentCoveredEvent
                try{
                    party.getCoveredEvent().getDisplayName();
                    //party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando currentCoveredEvent: null");
                    currentCoveredEvent = null;
                }
                if (currentCoveredEvent != null) {
                    currentCoveredEvent = party.getCoveredEvent().getDisplayName().toLowerCase().replaceAll("\\s+","");
                }

                //Try-Catch: currentContractType
                try{
                    party.getContractType().getDisplayName();
                    //party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando currentCoveredEvent: null");
                    currentContractType = null;
                }
                if (currentContractType != null) {
                    currentContractType = party.getContractType().getDisplayName().toLowerCase().replaceAll("\\s+","");
                }

                //Try-Catch: currentAAHRiskCategory
                try{
                    party.getAAHRiskCategory().getDisplayName();
                    //party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando currentCoveredEvent: null");
                    currentAAHRiskCategory = null;
                }
                if (currentAAHRiskCategory != null) {
                    currentAAHRiskCategory = party.getAAHRiskCategory().getDisplayName().toLowerCase().replaceAll("\\s+","");
                }

                //EventStartDate
                currentEventStartDate = party.getEventStartDate();
                if (currentEventStartDate != null) {
                    currentEventStartDate = party.getEventStartDate().toLowerCase().replaceAll("\\s+","");
                }

                //EventEndDate
                currentEventEndDate = party.getEventEndDate();
                if (currentEventEndDate != null) {
                    currentEventEndDate = party.getEventEndDate().toLowerCase().replaceAll("\\s+","");
                }

                //LocalEvent
                currentLocalEvent = party.getLocationEvent();
                if (currentLocalEvent != null) {
                    currentLocalEvent = party.getLocationEvent().toLowerCase().replaceAll("\\s+","");
                }

                    if (conteoIteratorIn == 1) {
                       if (currentActivity != null) {
                            if (key != null) {
                                if (currentActivity.equals(key.toLowerCase())) {
                                    secTemp2In.addAAHParty(party);
                                    if (count == no_nominados.size()) {
                                        isEqualsIn = true;
                                    }
                                } else {
                                    secTemp3In.addAAHParty(party);
                                    isDifferentIn = true;
                                }
                            } else {
                                secTemp3In.addAAHParty(party);
                                isEqualsIn = false;
                                isDifferentIn = true;
                            }
                        } else {
                            if (key != null) {
                                secTemp3In.addAAHParty(party);
                                isDifferentIn = true;
                            } else {
                                secTemp2In.addAAHParty(party);
                                if (count == no_nominados.size()) {
                                    isEqualsIn = true;
                                }
                            }
                        }
                    } else if (conteoIteratorIn == 2) {
                       if (currentClasification != null) {
                            if (key != null) {
                                if (currentClasification.equals(key.toLowerCase())) {
                                    secTemp.addAAHParty(party);

                                    //CASO 1: Activity !=
                                    if (count == no_nominados.size() && isDifferentIn) {
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                        isEqualsIn2 = true;
                                    }

                                    if (count == no_nominados.size() && isEqualsIn) {
                                        //secIn.addSeccion(secTemp2In);
                                        isEqualsIn2 = true;
                                    }
                                } else {
                                    secTemp1.addAAHParty(party);
                                    if (count == no_nominados.size() && isDifferentIn) {
                                        //secIn.addSeccion(secTemp3In);
                                        isDifferentIn2 = true;
                                    }

                                    if (count == no_nominados.size() && isEqualsIn) {
                                        //secIn.addSeccion(secTemp2In);
                                        secTemp2In = secTemp;
                                        secTemp3In = secTemp1;
                                        isDifferentIn2 = true;
                                    }
                                    //if (count == no_nominados.size()) {
                                    //    secIn.addSeccion(secTemp);
                                    //    secIn.addSeccion(secTemp1);
                                    //}
                                }
                            } else {
                                if (count == no_nominados.size()) {
                                    secTemp3In = new Seccion();
                                    secTemp3In.addAAHParty(party);
                                    isEqualsIn2 = false;
                                    isDifferentIn2 = true;
                                }
                            }
                        } else {
                            if (key != null) {
                                //secTemp.addAAHParty(party);
                                secTemp1.addAAHParty(party);
                                secTemp2In = secTemp;
                                secTemp3In = secTemp1;
                                isDifferentIn2 = true;
                            } else {
                                //secTemp.addAAHParty(party);
                                secTemp.addAAHParty(party);
                                secTemp2In = secTemp;
                                if (count == no_nominados.size()) {
                                    isEqualsIn2 = true;
                                }
                            }
                        }

                    } else if (conteoIteratorIn == 3) {
                        if (currentRiskCategory != null) {
                            if (key != null) {
                                if (currentRiskCategory.equals(key.toLowerCase())) {
                                    secTemp4.addAAHParty(party);

                                    //CASO2: Clasification !=
                                    if (count == no_nominados.size() && isEqualsIn && isDifferentIn2) {
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                        isEqualsIn3 = true;
                                    }
                                    //Activity != && Clasification !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2) {
                                        isEqualsIn3 = true;
                                    }

                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2) {
                                        //System.out.println("ENTRO AQUI");
                                        isEqualsIn3 = true;
                                        //secIn.addSeccion(secTemp2In);
                                    }

                                    if (count == no_nominados.size() && isDifferentIn && isEqualsIn2) {
                                        isEqualsIn3 = true;
                                    }

                                } else {

                                    secTemp1.addAAHParty(party);
                                    //System.out.println("AQUI ENTRARA LA SEGUNDA VEZ");
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2) {
                                        isDifferentIn3 = true;
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO3: RiskCategory !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2) {
                                        secTemp2In = secTemp4;
                                        secTemp3In = secTemp1;
                                        isDifferentIn3 = true;
                                        //secIn.addSeccion(secTemp2In);
                                    }
                                    //if (count == no_nominados.size()) {
                                    //    secIn.addSeccion(secTemp4);
                                    //    secIn.addSeccion(secTemp1);
                                    //}
                                }
                            } else {
                                if (count == no_nominados.size()) {
                                    secTemp3In = new Seccion();
                                    secTemp3In.addAAHParty(party);
                                    isEqualsIn3 = false;
                                    isDifferentIn3 = true;
                                }
                            }

                        } else {
                            //isEqualsIn3 = true;

                            if (key != null) {
                                //secTemp.addAAHParty(party);
                                secTemp1.addAAHParty(party);
                                secTemp2In = secTemp4;
                                secTemp3In = secTemp1;
                                isDifferentIn3 = true;
                            } else {
                                //secTemp.addAAHParty(party);
                                //if (count == no_nominados.size()) {
                                //    isEqualsIn3 = true;
                                //}
                                secTemp4.addAAHParty(party);
                                secTemp2In = secTemp4;
                                if (count == no_nominados.size()) {
                                    isEqualsIn3 = true;
                                }
                            }
                        }
                    } else if (conteoIteratorIn == 4) {
                        if (currentInsurableGroup != null) {
                            if (key != null) {
                                if (currentInsurableGroup.equals(key.toLowerCase())) {
                                    secTemp5.addAAHParty(party);

                                    //CASO1: Activity =!
                                    if (count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3) {
                                        //System.out.println("Llenando");
                                        isEqualsIn4 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO2: Clasification !=
                                    if (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3) {
                                        //System.out.println("Llenando");
                                        isEqualsIn4 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO3: RiskCategory !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3) {
                                        //System.out.println("CASO2: RiskCategory =!");
                                        isEqualsIn4 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3) {
                                        isEqualsIn4 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3) {
                                        isEqualsIn4 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3) {
                                        isEqualsIn4 = true;
                                        //secIn.addSeccion(secTemp5);
                                    }



                                } else {
                                    secTemp6.addAAHParty(party);

                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3) {
                                        isDifferentIn4 = true;
                                        //secIn.addSeccion(secTemp3In);
                                    }

                            /*
                            if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4) {
                                secIn.addSeccion(secTemp5);
                                secIn.addSeccion(secTemp6);
                            }
                            */

                                    //CASO3: InsurableGroup !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3) {
                                        secTemp2In = secTemp5;
                                        secTemp3In = secTemp6;
                                        isDifferentIn4 = true;
                                        //secIn.addSeccion(secTemp5);
                                        //secIn.addSeccion(secTemp6);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO4: Activity != && InsurableGroup =!
                                    if (count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3) {
                                        isDifferentIn4 = true;
                                        //secIn.addSeccion(secTemp5);
                                        //secIn.addSeccion(secTemp6);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO5: RiskCategory != && InsurableGroup !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3) {
                                        isDifferentIn4 = true;
                                        //secIn.addSeccion(secTemp5);
                                        //secIn.addSeccion(secTemp6);
                                        //secIn.addSeccion(secTemp3In);
                                    }
                                }
                            } else {
                                if (count == no_nominados.size()) {
                                    secTemp3In = new Seccion();
                                    secTemp3In.addAAHParty(party);
                                    isEqualsIn4 = false;
                                    isDifferentIn4 = true;
                                }
                            }

                        } else {
                            //isEqualsIn4 = true;

                            if (key != null) {
                                //secTemp.addAAHParty(party);
                                secTemp6.addAAHParty(party);
                                secTemp2In = secTemp5;
                                secTemp3In = secTemp6;
                                isDifferentIn4 = true;
                            } else {
                                //secTemp.addAAHParty(party);
                                secTemp5.addAAHParty(party);
                                secTemp2In = secTemp5;
                                if (count == no_nominados.size()) {
                                    isEqualsIn4 = true;
                                }
                            }
                        }
                    } else if (conteoIteratorIn == 5) {   //isDifferentIn, isDifferentIn2, isDifferentIn3, isDifferentIn4, setTemp2, setTemp3
                         if (currentCoveredEvent != null) {
                            if (key != null) {
                                if (currentCoveredEvent.equals(key.toLowerCase())) {
                                    secTemp7.addAAHParty(party);

                                    //CASO1: Activity !=
                                    if (count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4) {
                                        isEqualsIn5 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO2: Clasification !=
                                    if (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4) {
                                        isEqualsIn5 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO3: RiskCategory !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4) {
                                        //System.out.println("CASO2: RiskCategory =!");
                                        isEqualsIn5 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO4: InsurableGroup !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4) {
                                        //System.out.println("CASO2: RiskCategory =!");
                                        isEqualsIn5 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4) {
                                        isEqualsIn5 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4) {
                                        isEqualsIn5 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4) {
                                        isEqualsIn5 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4) {
                                        isEqualsIn5 = true;
                                        //secIn.addSeccion(secTemp7);
                                    }

                                } else {
                                    secTemp8.addAAHParty(party);

                                    //CASO5: CoveredEvent !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4) {
                                        secTemp2In = secTemp7;
                                        secTemp3In = secTemp8;
                                        isDifferentIn5 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4) {
                                        isDifferentIn5 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                }
                            } else {
                                //isDifferentIn, isDifferentIn2, isDifferentIn3, isDifferentIn4, setTemp2, setTemp3, secTemp3In, secTemp3In, secTemp3In
                                if (count == no_nominados.size()) {
                                    secTemp3In = new Seccion();
                                    secTemp3In.addAAHParty(party);
                                    isEqualsIn5 = false;
                                    isDifferentIn5 = true;
                                }
                                if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5) {
                                    //secIn.addSeccion(secTemp2In);
                                    //secIn.addSeccion(secTemp3In);
                                }
                                if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5) {
                                    //secIn.addSeccion(secTemp2In);
                                    //secIn.addSeccion(secTemp3In);
                                }
                            }

                        } else {
                            //isEqualsIn5 = true;

                            if (key != null) {
                                //secTemp.addAAHParty(party);
                                secTemp8.addAAHParty(party);
                                secTemp2In = secTemp7;
                                secTemp3In = secTemp8;
                                isDifferentIn5 = true;


                            } else {
                                //secTemp.addAAHParty(party);
                                secTemp7.addAAHParty(party);
                                secTemp2In = secTemp7;
                                if (count == no_nominados.size()) {
                                    isEqualsIn5 = true;
                                }
                            }
                        }


                    } else if (conteoIteratorIn == 6) {
                        if (currentContractType != null) {
                            if (key != null) {
                                if (currentContractType.equals(key.toLowerCase())) {
                                    secTemp9.addAAHParty(party);

                                    //CASO1: Activity =!
                                    if (count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) {
                                        isEqualsIn6 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO2: Clasification !=
                                    if (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) {
                                        isEqualsIn6 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO3: RiskCategory !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5) {
                                        isEqualsIn6 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO4: InsurableGroup !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5) {
                                        isEqualsIn6 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO5: CoveredEvent !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5) {
                                        isEqualsIn6 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) {
                                        isEqualsIn6 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5) {
                                        isEqualsIn6 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5) {
                                        isEqualsIn6 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5) {
                                        isEqualsIn6 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3&& isEqualsIn4 && isEqualsIn5) {
                                        isEqualsIn6 = true;
                                        //secIn.addSeccion(secTemp5);
                                    }

                                } else {
                                    secTemp10.addAAHParty(party);

                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5) {
                                        isDifferentIn6 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO6: ContractType !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) {
                                        secTemp2In = secTemp9;
                                        secTemp3In = secTemp10;
                                        isDifferentIn6 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }
                                }
                            } else {
                                if (count == no_nominados.size()) {
                                    secTemp3In = new Seccion();
                                    secTemp3In.addAAHParty(party);
                                    isEqualsIn6 = false;
                                    isDifferentIn6 = true;
                                }
                            }
                        } else {
                            if (key != null) {
                                //secTemp.addAAHParty(party);
                                secTemp10.addAAHParty(party);
                                secTemp2In = secTemp9;
                                secTemp3In = secTemp10;
                                isDifferentIn6 = true;

                            } else {
                                //secTemp.addAAHParty(party);
                                secTemp9.addAAHParty(party);
                                secTemp2In = secTemp9;
                                if (count == no_nominados.size()) {
                                    isEqualsIn6 = true;
                                }
                            }
                        }
                    } else if (conteoIteratorIn == 7) {
                       if (currentAAHRiskCategory != null) {
                            if (key != null) {
                                if (currentAAHRiskCategory.equals(key.toLowerCase())) {
                                    secTemp11.addAAHParty(party);

                                    //CASO1: Activity =!
                                    if (count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) {
                                        isEqualsIn7 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO2: Clasification !=
                                    if (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) {
                                        isEqualsIn7 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO3: RiskCategory !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) {
                                        isEqualsIn7 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO4: InsurableGroup !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6) {
                                        isEqualsIn7 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO5: CoveredEvent !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6) {
                                        isEqualsIn7 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO6: AAHRiskCategory !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6) {
                                        isEqualsIn7 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) {
                                        isEqualsIn7 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) {
                                        isEqualsIn7 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6) {
                                        isEqualsIn7 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6) {
                                        isEqualsIn7 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6) {
                                        isEqualsIn7 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3&& isEqualsIn4 && isEqualsIn5 && isEqualsIn6) {
                                        isEqualsIn7 = true;
                                        //secIn.addSeccion(secTemp5);
                                    }

                                } else {
                                    secTemp12.addAAHParty(party);

                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6) {
                                        isDifferentIn7 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO7: AAHRiskCategory !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) {
                                        secTemp2In = secTemp11;
                                        secTemp3In = secTemp12;
                                        isDifferentIn7 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }
                                }
                            } else {
                                if (count == no_nominados.size()) {
                                    secTemp3In = new Seccion();
                                    secTemp3In.addAAHParty(party);
                                    isEqualsIn7 = false;
                                    isDifferentIn7 = true;
                                }
                            }
                        } else {
                            if (key != null) {
                                //secTemp.addAAHParty(party);
                                secTemp12.addAAHParty(party);
                                secTemp2In = secTemp11;
                                secTemp3In = secTemp12;
                                isDifferentIn7 = true;

                            } else {
                                //secTemp.addAAHParty(party);
                                secTemp11.addAAHParty(party);
                                secTemp2In = secTemp11;
                                if (count == no_nominados.size()) {
                                    isEqualsIn7 = true;
                                }
                            }
                        }
                    } else if (conteoIteratorIn == 8) {
                        if (currentEventStartDate != null) {
                            if (key != null) {
                                if (currentEventStartDate.equals(key.toLowerCase())) {
                                    secTemp13.addAAHParty(party);

                                    //CASO1: Activity =!
                                    if (count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) {
                                        isEqualsIn8 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO2: Clasification !=
                                    if (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) {
                                        isEqualsIn8 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO3: RiskCategory !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) {
                                        isEqualsIn8 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO4: InsurableGroup !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) {
                                        isEqualsIn8 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO5: CoveredEvent !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7) {
                                        isEqualsIn8 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO6: ContractType !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7) {
                                        isEqualsIn8 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO7: AAHRiskCategory !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7) {
                                        isEqualsIn8 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) {
                                        isEqualsIn8 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) {
                                        isEqualsIn8 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) {
                                        isEqualsIn8 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7) {
                                        isEqualsIn8 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7) {
                                        isEqualsIn8 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType != && AAHRiskCategory !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7) {
                                        isEqualsIn8 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3&& isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) {
                                        isEqualsIn8 = true;
                                        //secIn.addSeccion(secTemp5);
                                    }

                                } else {
                                    secTemp14.addAAHParty(party);

                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7) {
                                        isDifferentIn8 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO8: EventStartDate !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) {
                                        secTemp2In = secTemp13;
                                        secTemp3In = secTemp14;
                                        isDifferentIn8 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }
                                }
                            } else {
                                if (count == no_nominados.size()) {
                                    secTemp3In = new Seccion();
                                    secTemp3In.addAAHParty(party);
                                    isEqualsIn8 = false;
                                    isDifferentIn8 = true;
                                }
                            }
                        } else {
                            if (key != null) {
                                //secTemp.addAAHParty(party);
                                secTemp14.addAAHParty(party);
                                secTemp2In = secTemp13;
                                secTemp3In = secTemp14;
                                isDifferentIn8 = true;

                            } else {
                                //secTemp.addAAHParty(party);
                                secTemp13.addAAHParty(party);
                                secTemp2In = secTemp13;
                                if (count == no_nominados.size()) {
                                    isEqualsIn8 = true;
                                }
                            }
                        }
                    } else if (conteoIteratorIn == 9) {
                        //System.out.println("===================================");
                        //System.out.println("CONTEO 9");
                        //System.out.println("currentEventEndDate: "+currentEventEndDate);
                        //System.out.println("key: "+key);
                        if (currentEventEndDate != null) {
                            if (key != null) {
                                if (currentEventEndDate.equals(key.toLowerCase())) {
                                    secTemp15.addAAHParty(party);

                                    //CASO1: Activity =!
                                    if (count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO2: Clasification !=
                                    if (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO3: RiskCategory !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO4: InsurableGroup !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO5: CoveredEvent !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO6: ContractType !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO7: AAHRiskCategory !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7 && isEqualsIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO8: EventStartDate !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isDifferentIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType != && AAHRiskCategory !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isEqualsIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType != && AAHRiskCategory != && EventStartDate !=
                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3&& isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) {
                                        isEqualsIn9 = true;
                                        //secIn.addSeccion(secTemp5);
                                    }

                                } else {
                                    secTemp16.addAAHParty(party);

                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8) {
                                        isDifferentIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                        //secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO9: EventEndDate !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) {
                                        secTemp2In = secTemp15;
                                        secTemp3In = secTemp16;
                                        isDifferentIn9 = true;
                                        //secIn.addSeccion(secTemp2In);
                                        //secIn.addSeccion(secTemp3In);
                                    }
                                }
                            } else {
                                if (count == no_nominados.size()) {
                                    secTemp3In = new Seccion();
                                    secTemp3In.addAAHParty(party);
                                    isEqualsIn9 = false;
                                    isDifferentIn9 = true;
                                }
                            }
                        } else {
                            if (key != null) {
                                //secTemp.addAAHParty(party);
                                secTemp16.addAAHParty(party);
                                secTemp2In = secTemp15;
                                secTemp3In = secTemp16;
                                isDifferentIn9 = true;

                            } else {
                                //secTemp.addAAHParty(party);
                                secTemp15.addAAHParty(party);
                                secTemp2In = secTemp15;
                                if (count == no_nominados.size()) {
                                    isEqualsIn9 = true;
                                }
                            }
                        }
                    } else if (conteoIteratorIn == 10) {
                       if (currentLocalEvent != null) {
                            if (key != null) {
                                if (currentLocalEvent.equals(key.toLowerCase())) {
                                    if (isTrue) {
                                   //  if (coverages.equals(currentCoverage1)){
                                        secTemp17.addAAHParty(party);

                                        //CASO1: Activity =!
                                        if (count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //CASO2: Clasification !=
                                        if (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //CASO3: RiskCategory !=
                                        if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //CASO4: InsurableGroup !=
                                        if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //CASO5: CoveredEvent !=
                                        if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //CASO6: ContractType !=
                                        if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //CASO7: AAHRiskCategory !=
                                        if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7 && isEqualsIn8 && isEqualsIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //CASO8: EventStartDate !=
                                        if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isDifferentIn8 && isEqualsIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //CASO9: EventEndDate !=
                                        if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isDifferentIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //Activity != && Clasification !=
                                        if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //Activity != && Clasification != && RiskCategory !=
                                        if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup !=
                                        if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent !=
                                        if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType !=
                                        if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType != && AAHRiskCategory !=
                                        if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isEqualsIn8 && isEqualsIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType != && AAHRiskCategory != && EventStartDate !=
                                        if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8 && isEqualsIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType != && AAHRiskCategory != && EventStartDate != && EventEndDate !=
                                        if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8 && isDifferentIn9) {
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp2In);
                                            secIn.addSeccion(secTemp3In);
                                        }

                                        if(count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3&& isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) {
                                           if(!(coverages.equals(currentCoverage1))){
                                               secTemp18.addAAHParty(party);
                                               secTemp2In = secTemp17;
                                               secTemp3In = secTemp18;
                                             //  isDifferentIn10 = true;
                                               secIn.addSeccion(secTemp2In);
                                               secIn.addSeccion(secTemp3In);
                                           }else{
                                            isEqualsIn10 = true;
                                            secIn.addSeccion(secTemp17);}
                                        }
                                    } else {

                                        if (count != no_nominados.size() || count == no_nominados.size()) {
                                            secCoverageIn = new Seccion();
                                            secCoverageIn.addAAHParty(party);
                                            secIn.addSeccion(secCoverageIn);
                                        }
                                    }


                                } else {
                                    secTemp18.addAAHParty(party);

                                    if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8 && isDifferentIn9) {
                                        isDifferentIn10 = true;
                                        secIn.addSeccion(secTemp2In);
                                        secIn.addSeccion(secTemp3In);
                                    }

                                    //CASO10: LocalEvent !=
                                    if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) {
                                        secTemp2In = secTemp17;
                                        secTemp3In = secTemp18;
                                        isDifferentIn10 = true;
                                        secIn.addSeccion(secTemp2In);
                                        secIn.addSeccion(secTemp3In);
                                    }
                                }
                            } else {
                                if (count == no_nominados.size()) {
                                    secTemp3In = new Seccion();
                                    secTemp3In.addAAHParty(party);
                                    isEqualsIn10 = false;
                                    isDifferentIn10 = true;
                                }
                                if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9 && isDifferentIn10) {
                                    secIn.addSeccion(secTemp2In);
                                    secIn.addSeccion(secTemp3In);
                                }
                                if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8 && isDifferentIn9 && isDifferentIn10) {
                                    secIn.addSeccion(secTemp2In);
                                    secIn.addSeccion(secTemp3In);
                                }
                            }
                        } else {
                            if (key != null) {
                                //secTemp.addAAHParty(party);
                                secTemp18.addAAHParty(party);
                                secTemp2In = secTemp17;
                                secTemp3In = secTemp18;
                                isDifferentIn10 = true;

                            } else {
                                if (isTrue) {
                               // if(coverages.equals(currentCoverage1)){
                                    //secTemp.addAAHParty(party);
                                    secTemp17.addAAHParty(party);
                                    secTemp2In = secTemp17;
                                    if (count == no_nominados.size()) {
                                        isEqualsIn10 = true;
                                    }
                                } else {
                                    if (count != no_nominados.size() || count == no_nominados.size()) {
                                        secCoverageIn = new Seccion();
                                        secCoverageIn.addAAHParty(party);
                                        secIn.addSeccion(secCoverageIn);
                                    }
                                }

                            }

                            if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9 && isEqualsIn10) {
                                secIn.addSeccion(secTemp2In);
                            }

                            if (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9 && isDifferentIn10) {
                                secIn.addSeccion(secTemp2In);
                                secIn.addSeccion(secTemp3In);
                            }

                            if (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8 && isDifferentIn9 && isDifferentIn10) {
                                secIn.addSeccion(secTemp2In);
                                secIn.addSeccion(secTemp3In);
                            }
                        }

                    }
            }

            XmlMapper xmlMapper = new XmlMapper();
            //System.out.println("seccionNominados");
            //System.out.println(xmlMapper.valueToTree(secIn));
            // Eliminando Secciones vacias
           int sizeASecciones = secIn.getSeccion().size();
           cantSeccion=sizeASecciones;
            System.out.println(sizeASecciones + " ----");
            //System.out.println(sizeASecciones);
            for (int i = 0; i < sizeASecciones; i++) {
                //System.out.println(secIn.getSeccion().get(i).getSize());
                if (secIn.getSeccion().get(i).getSize() == 0) {
                    //secIn.getSeccion().remove(0);
                }
            }
        }



        XmlMapper xmlMapper = new XmlMapper();
        //System.out.println("seccionNominados");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println(xmlMapper.valueToTree(secNo));
        System.out.println(xmlMapper.valueToTree(secIn));
        //seccionNominados.getSeccion().get(0).getaAHParty().get(0).getActivity().getDisplayName();
        this.seccionNominados = secNo;
        this.seccionInnominados = secIn;
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

}



