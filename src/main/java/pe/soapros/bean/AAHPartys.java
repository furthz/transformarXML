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
             CoveredPerson coveredP = party.getCoveredPerson();
            if (coveredP != null) {
                for (OfficialID officialID: coveredP.getOfficialIDs().getOfficialID()) {
                    if (officialID.getPrimary().equalsIgnoreCase("true")) {
                        //System.out.println(1);
                        coveredP.setTipoDocum(officialID.getType().getUnlocalizedName());
                    } else {
                        System.out.println(2);
                        continue;
                    }
                }
            } else {
                continue;
            }

        }
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
            System.out.println(covertura);
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
                    if(elementosSeccion > 1) {
                        for (PartyCoverage coverage : party.getPartyCoverages().getPartyCoverage()) {
                            coverage.setDescription("");
                        }
                    }
                    secTemp.addAAHParty(party);

                }
                System.out.println(party.getPartyCoverages().getCoverages());
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

                if (party.getPartyCoverages().getCoverages().toLowerCase().equals(key.toLowerCase())) {
                    elementosSeccion++;

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
            String currentCoverage = party.getPartyCoverages().getCoverages().toLowerCase().replaceAll("\\s+", "");
            if (conteo_no_nominados == 0) {
                coverages = currentCoverage;
            }
            System.out.println(conteo_no_nominados + "#");
            //System.out.println(coverages + "*");
            System.out.println(currentCoverage + "+");
            System.out.println(!coverages.equals(currentCoverage) +"....");
            System.out.println("------------------------------------------------------");
            // ISTRUE??
            if (!coverages.equals(currentCoverage) && conteo_no_nominados > 0) {
                isTrue = false;
                System.out.println("ENTRO FUNC");
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
            try {
                party.getActivity().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando activity: null");
                activity = null;
            }

            //Try-Catch: Clasification
            try {
                party.getClasification().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando clasification: null");
                clasification = null;
                //System.out.println(party.getCoveredEvent().getDisplayName());
            }

            //RiskCategory
            riskCategory = party.getRiskCategory();

            //Try-Catch: insurableGroup
            try {
                party.getInsurableGroup().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando insurableGroup: null");
                insurableGroup = null;
                //System.out.println(party.getCoveredEvent().getDisplayName());
            }

            //Try-Catch: coveredEvent
            try {
                party.getCoveredEvent().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando coveredEvent: null");
                coveredEvent = null;
                //System.out.println(party.getCoveredEvent().getDisplayName());
            }

            //Try-Catch: ContractType
            try {
                party.getContractType().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando contractType: null");
                contractType = null;
                //System.out.println(party.getContractType().getDisplayName());
            }

            //Try-Catch: AAHRiskCategory
            try {
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
                activity = party.getActivity().getDisplayName().toLowerCase().replaceAll("\\s+", "");
            }
            if (clasification != null) {
                clasification = party.getClasification().getDisplayName().toLowerCase().replaceAll("\\s+", "");
            }
            if (riskCategory != null) {
                riskCategory = party.getRiskCategory().toLowerCase().replaceAll("\\s+", "");
            }
            if (insurableGroup != null) {
                insurableGroup = party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+", "");
            }
            if (coveredEvent != null) {
                coveredEvent = party.getCoveredEvent().getDisplayName().toLowerCase().replaceAll("\\s+", "");
            }
            if (contractType != null) {
                contractType = party.getContractType().getDisplayName().toLowerCase().replaceAll("\\s+", "");
            }
            if (aAHRiskCategory != null) {
                aAHRiskCategory = party.getAAHRiskCategory().getDisplayName().toLowerCase().replaceAll("\\s+", "");
            }
            if (eventStartDate != null) {
                eventStartDate = party.getEventStartDate().toLowerCase().replaceAll("\\s+", "");
            }
            if (eventEndDate != null) {
                eventEndDate = party.getEventEndDate().toLowerCase().replaceAll("\\s+", "");
            }
            if (locationEvent != null) {
                locationEvent = party.getLocationEvent().toLowerCase().replaceAll("\\s+", "");
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

            String currentCoverage = party.getPartyCoverages().getCoverages().toLowerCase().replaceAll("\\s+", "");
            if (conteo == 0) {
                //System.out.println(conteo);
                coverages = currentCoverage;
            }
            // ISTRUE??
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
            try {
                party.getActivity().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando activity: null");
                activity = null;
            }

            //Try-Catch: Clasification
            try {
                party.getClasification().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando clasification: null");
                clasification = null;
                //System.out.println(party.getCoveredEvent().getDisplayName());
            }

            //RiskCategory
            riskCategory = party.getRiskCategory();

            //Try-Catch: insurableGroup
            try {
                party.getInsurableGroup().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando insurableGroup: null");
                insurableGroup = null;
                //System.out.println(party.getCoveredEvent().getDisplayName());
            }

            //Try-Catch: coveredEvent
            try {
                party.getCoveredEvent().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando coveredEvent: null");
                coveredEvent = null;
                //System.out.println(party.getCoveredEvent().getDisplayName());
            }

            //Try-Catch: ContractType
            try {
                party.getContractType().getDisplayName();

            } catch (NullPointerException n) {
                //System.out.println("Asignando contractType: null");
                contractType = null;
                //System.out.println(party.getContractType().getDisplayName());
            }

            //Try-Catch: AAHRiskCategory
            try {
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
                activity = party.getActivity().getDisplayName().toLowerCase().replaceAll("\\s+", "");
            }
            if (clasification != null) {
                clasification = party.getClasification().getDisplayName().toLowerCase().replaceAll("\\s+", "");
            }
            if (riskCategory != null) {
                riskCategory = party.getRiskCategory().toLowerCase().replaceAll("\\s+", "");
            }
            if (insurableGroup != null) {
                insurableGroup = party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+", "");
            }
            if (coveredEvent != null) {
                coveredEvent = party.getCoveredEvent().getDisplayName().toLowerCase().replaceAll("\\s+", "");
            }
            if (contractType != null) {
                contractType = party.getContractType().getDisplayName().toLowerCase().replaceAll("\\s+", "");
            }
            if (aAHRiskCategory != null) {
                aAHRiskCategory = party.getAAHRiskCategory().getDisplayName().toLowerCase().replaceAll("\\s+", "");
            }
            if (eventStartDate != null) {
                eventStartDate = party.getEventStartDate().toLowerCase().replaceAll("\\s+", "");
            }
            if (eventEndDate != null) {
                eventEndDate = party.getEventEndDate().toLowerCase().replaceAll("\\s+", "");
            }
            if (locationEvent != null) {
                locationEvent = party.getLocationEvent().toLowerCase().replaceAll("\\s+", "");
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
        boolean isEquals2 = false;
        boolean isEquals3 = false;
        boolean isEquals4 = false;
        boolean isEquals5 = false;
        boolean isEquals6 = false;
        boolean isEquals7 = false;
        boolean isEquals8 = false;
        boolean isEquals9 = false;
        boolean isEquals10 = false;

        boolean isEqualsIn = false;
        boolean isEqualsIn2 = false;
        boolean isEqualsIn3 = false;
        boolean isEqualsIn4 = false;
        boolean isEqualsIn5 = false;
        boolean isEqualsIn6 = false;
        boolean isEqualsIn7 = false;
        boolean isEqualsIn8 = false;
        boolean isEqualsIn9 = false;
        boolean isEqualsIn10 = false;

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
            System.out.println("conteoIterator: " + conteoIterator);
            Map.Entry mentry = (Map.Entry) iterator.next();
            String key = "";
            if (mentry.getValue() != null) {
                key = mentry.getValue().toString();
            } else {
                key = null;
            }
            //key = mentry.getValue().toString();
            System.out.println("Key: " + key);
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
                try {
                    party.getActivity().getDisplayName().toLowerCase().replaceAll("\\s+", "");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando activity: null");
                    currentActivity = null;
                    //System.out.println(party.getCoveredEvent().getDisplayName());
                }
                if (currentActivity != null) {
                    currentActivity = party.getActivity().getDisplayName().toLowerCase().replaceAll("\\s+", "");
                }

                //Try-Catch: Clasification
                try {
                    party.getClasification().getDisplayName().toLowerCase().replaceAll("\\s+", "");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando Clasification: null");
                    currentClasification = null;
                    //System.out.println(party.getCoveredEvent().getDisplayName());
                }
                if (currentClasification != null) {
                    currentClasification = party.getClasification().getDisplayName().toLowerCase().replaceAll("\\s+", "");
                }

                //RiskCategory
                currentRiskCategory = party.getRiskCategory();
                if (currentRiskCategory != null) {
                    currentRiskCategory = party.getRiskCategory().toLowerCase().replaceAll("\\s+", "");
                }

                //Try-Catch: currentInsurableGroup
                try {
                    party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+", "");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando Clasification: null");
                    currentInsurableGroup = null;
                    //System.out.println(party.getCoveredEvent().getDisplayName());
                }
                if (currentInsurableGroup != null) {
                    currentInsurableGroup = party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+", "");
                }

                //Try-Catch: currentCoveredEvent
                try {
                    party.getCoveredEvent().getDisplayName();
                    //party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando currentCoveredEvent: null");
                    currentCoveredEvent = null;
                }
                if (currentCoveredEvent != null) {
                    currentCoveredEvent = party.getCoveredEvent().getDisplayName().toLowerCase().replaceAll("\\s+", "");
                }

                //Try-Catch: currentContractType
                try {
                    party.getContractType().getDisplayName();
                    //party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando currentCoveredEvent: null");
                    currentContractType = null;
                }
                if (currentContractType != null) {
                    currentContractType = party.getContractType().getDisplayName().toLowerCase().replaceAll("\\s+", "");
                }

                //Try-Catch: currentAAHRiskCategory
                try {
                    party.getAAHRiskCategory().getDisplayName();
                    //party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando currentCoveredEvent: null");
                    currentAAHRiskCategory = null;
                }
                if (currentAAHRiskCategory != null) {
                    currentAAHRiskCategory = party.getAAHRiskCategory().getDisplayName().toLowerCase().replaceAll("\\s+", "");
                }

                //EventStartDate
                currentEventStartDate = party.getEventStartDate();
                if (currentEventStartDate != null) {
                    currentEventStartDate = party.getEventStartDate().toLowerCase().replaceAll("\\s+", "");
                }

                //EventEndDate
                currentEventEndDate = party.getEventEndDate();
                if (currentEventEndDate != null) {
                    currentEventEndDate = party.getEventEndDate().toLowerCase().replaceAll("\\s+", "");
                }

                //LocalEvent
                currentLocalEvent = party.getLocationEvent();
                if (currentLocalEvent != null) {
                    currentLocalEvent = party.getLocationEvent().toLowerCase().replaceAll("\\s+", "");
                }

                if (conteoIterator == 1) {

                    if (currentActivity != null) {
                        if (key != null) {
                            if (currentActivity.equals(key.toLowerCase())) {
                                if (count == nominados.size()) {
                                    isEquals = true;
                                }
                            } else {
                                isDifferent = true;
                            }
                        } else {
                            isEquals = false;
                            isDifferent = true;
                        }
                    } else {
                        if (key != null) {
                            isDifferent = true;
                        } else {
                            if (count == nominados.size()) {
                                isEquals = true;
                            }
                        }
                    }
                } else if (conteoIterator == 2) {

                    if (currentClasification != null) {
                        if (key != null) {
                            if (currentClasification.equals(key.toLowerCase())) {
                                // CASO 1: Activity !=    =>   isDifferent= True
                                if ((count == nominados.size() && isEquals) || (count == nominados.size() && isDifferent)) {
                                    isEquals2 = true;
                                     /*   if(isDifferent==true){
                                            secCoverage = new Seccion();
                                            secCoverage.addAAHParty(party);
                                            secNo.addSeccion(secCoverage);
                                        }*/
                                }
                                        /*else{
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                    }*/
                            } else {
                                if ((count == nominados.size() && isEquals) || (count == nominados.size() && isDifferent)) {
                                    isDifferent2 = true;
                                    //   secCoverage = new Seccion();
                                    //   secCoverage.addAAHParty(party);
                                    //   secNo.addSeccion(secCoverage);
                                }
                            }
                        } else {
                            if ((count == nominados.size() && isEquals) || (count == nominados.size() && isDifferent)) {
                                isDifferent2 = true;
                                //  secCoverage = new Seccion();
                                //  secCoverage.addAAHParty(party);
                                //  secNo.addSeccion(secCoverage);
                            }
                        }
                    } else {
                        if (key != null) {
                            if ((count == nominados.size() && isEquals) || (count == nominados.size() && isDifferent)) {
                                isDifferent2 = true;
                                //  secCoverage = new Seccion();
                                // secCoverage.addAAHParty(party);
                                //  secNo.addSeccion(secCoverage);
                            }
                        } else {
                            if ((count == nominados.size() && isEquals) || (count == nominados.size() && isDifferent)) {
                                isEquals2 = true;
                            }
                        }
                    }

                } else if (conteoIterator == 3) {

                    if (currentRiskCategory != null) {
                        if (key != null) {
                            if (currentRiskCategory.equals(key.toLowerCase())) {
                                //CASO2: Clasification != => isDifferent2= true || Activity!= =>isDifferent=true
                                if ((count == nominados.size() && isEquals && isDifferent2) || (count == nominados.size() && isDifferent && isDifferent2) || (count == nominados.size() && isEquals && isEquals2) || (count == nominados.size() && isDifferent && isEquals2)) {
                                    isEquals3 = true;
                                      /*  if((isDifferent==true)||(isDifferent2==true)){
                                            secCoverage = new Seccion();
                                            secCoverage.addAAHParty(party);
                                            secNo.addSeccion(secCoverage);
                                        }*/
                                }
                                    /*else{
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                    }*/
                            } else {
                                if ((count == nominados.size() && isEquals && isDifferent2) || (count == nominados.size() && isDifferent && isDifferent2) || (count == nominados.size() && isEquals && isEquals2) || (count == nominados.size() && isDifferent && isEquals2)) {
                                    isDifferent3 = true;

                                          /*  secCoverage = new Seccion();
                                            secCoverage.addAAHParty(party);
                                            secNo.addSeccion(secCoverage);*/
                                }
                            }
                        } else {
                            if ((count == nominados.size() && isEquals && isDifferent2) || (count == nominados.size() && isDifferent && isDifferent2) || (count == nominados.size() && isEquals && isEquals2) || (count == nominados.size() && isDifferent && isEquals2)) {
                                isDifferent3 = true;
                                 /*   if((isDifferent==true)||(isDifferent2==true)){
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                    }*/
                            }
                        }

                    } else {
                        if (key != null) {

                            if ((count == nominados.size() && isEquals && isDifferent2) || (count == nominados.size() && isDifferent && isDifferent2) || (count == nominados.size() && isEquals && isEquals2) || (count == nominados.size() && isDifferent && isEquals2)) {
                                isDifferent3 = true;
/*
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
*/
                            }
                        } else {
                            if ((count == nominados.size() && isEquals && isDifferent2) || (count == nominados.size() && isDifferent && isDifferent2) || (count == nominados.size() && isEquals && isEquals2) || (count == nominados.size() && isDifferent && isEquals2)) {
                                isEquals3 = true;
                            }
                        }
                    }
                } else if (conteoIterator == 4) {

                    if (currentInsurableGroup != null) {
                        if (key != null) {
                            if (currentInsurableGroup.equals(key.toLowerCase())) {
                                /*    //CASO1: Activity =!
                                    if (count == nominados.size() && isDifferent && isEquals2 && isEquals3) {
                                        isEquals4 = true;
                                    }

                                    //CASO2: Clasification !=
                                    if (count == nominados.size() && isEquals && isDifferent2 && isEquals3) {
                                         isEquals4 = true;
                                        }

                                    //CASO3: RiskCategory !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isDifferent3) {
                                       isEquals4 = true;
                                    }

                                    //Activity != && Clasification !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isEquals3) {
                                        isEquals4 = true;
                                    }

                                    //Activity != && Clasification != && RiskCategory !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3) {
                                        isEquals4 = true;
                                      }

                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3) {
                                        isEquals4 = true;
                                    }*/

                                if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3) || (count == nominados.size() && isEquals && isEquals2 && isEquals3)) {
                                    isEquals4 = true;
                                       /* if((isDifferent==true)||(isDifferent2==true)||(isDifferent3==true)){
                                            secCoverage = new Seccion();
                                            secCoverage.addAAHParty(party);
                                            secNo.addSeccion(secCoverage);
                                        }*/
                                }
                                    /*else{
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                    }*/

                            } else {
                                    /*
                                    secTemp6.addAAHParty(party);
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3) {
                                        isDifferent4 = true;
                                    }

                                    //CASO3: InsurableGroup !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3) {
                                        isDifferent4 = true;
                                    }

                                    //CASO4: Activity != && InsurableGroup =!
                                    if (count == nominados.size() && isDifferent && isEquals2 && isEquals3) {
                                        isDifferent4 = true;
                                    }

                                    //CASO5: RiskCategory != && InsurableGroup !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isDifferent3) {
                                        isDifferent4 = true;
                                    }*/
                                if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3) || (count == nominados.size() && isEquals && isEquals2 && isEquals3)) {
                                    isDifferent4 = true;
                                        /*secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                }
                            }
                        } else {
                               /* if (count == nominados.size()) {
                                    secTemp3 = new Seccion();
                                    secTemp3.addAAHParty(party);
                                    isEquals4 = false;
                                    isDifferent4 = true;
                                }*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3) || (count == nominados.size() && isEquals && isEquals2 && isEquals3)) {
                                isDifferent4 = true;

                                        /*secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                        */
                            }
                        }
                    } else {
                        if (key != null) {
                            //isDifferent4 = true;
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3) || (count == nominados.size() && isEquals && isEquals2 && isEquals3)) {
                                isDifferent4 = true;
                                  /*  secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);*/
                            }
                        } else {
                              /*  //secTemp.addAAHParty(party);
                                secTemp5.addAAHParty(party);
                                secTemp2 = secTemp5;
                                if (count == nominados.size()) {
                                    isEquals4 = true;
                                }*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3) || (count == nominados.size() && isEquals && isEquals2 && isEquals3)) {
                                isEquals4 = true;
                            }
                        }
                    }
                } else if (conteoIterator == 5) {   //isDifferent, isDifferent2, isDifferent3, isDifferent4, setTemp2, setTemp3
                       /* boolean val1 = (count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4);
                        boolean val2 = (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4);
                        boolean val3 = (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4);
                        boolean val4 = (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4);
                        boolean val5 = (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4);
                        boolean val6 = (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4);
                        boolean val7 = (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4);
                        boolean val8 = (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4);
*/
                    if (currentCoveredEvent != null) {
                        if (key != null) {
                            if (currentCoveredEvent.equals(key.toLowerCase())) {
                                /*   //CASO1: Activity !=
                                    if (count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4) {
                                        isEquals5 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }
                                    //CASO2: Clasification !=
                                    if (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4) {
                                        isEquals5 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }
                                    //CASO3: RiskCategory !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4) {
                                        //System.out.println("CASO2: RiskCategory =!");
                                        isEquals5 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }
                                    //CASO4: InsurableGroup !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4) {
                                        //System.out.println("CASO2: RiskCategory =!");
                                        isEquals5 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }
                                    //Activity != && Clasification !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4) {
                                        isEquals5 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }
                                    //Activity != && Clasification != && RiskCategory !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4) {
                                        isEquals5 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }
                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4) {
                                        isEquals5 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4) {
                                        isEquals5 = true;
                                        //secNo.addSeccion(secTemp7);
                                    }*/
                                if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4)) {
                                    isEquals5 = true;
                                      /*  if((isDifferent==true)||(isDifferent2==true)||(isDifferent3==true)||(isDifferent4==true)){
                                            secCoverage = new Seccion();
                                            secCoverage.addAAHParty(party);
                                            secNo.addSeccion(secCoverage);
                                        }*/
                                }
                                    /*else{
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                    }*/

                                  /*  if((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4)||(count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4)){
                                        isEquals5 = true;
                                    }else{if((count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4)||(count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4)){
                                        isEquals5 = true;
                                    }else{if((count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4)||(count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4)){
                                        isEquals5 = true;
                                    }else{if((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4)||(count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4)){
                                        isEquals5 = true;*/

                            } else {
                                  /* //CASO5: CoveredEvent !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4) {
                                        secTemp2 = secTemp7;
                                        secTemp3 = secTemp8;
                                        isDifferent5 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }

                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4) {
                                        isDifferent5 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                        //secNo.addSeccion(secTemp3);
                                    }*/
                                    /*if((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4)||(count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4)){
                                        isDifferent5 = true;
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                    }else{if((count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4)||(count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4)){
                                        isDifferent5 = true;
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                    }else{if((count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4)||(count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4)){
                                        isDifferent5 = true;
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                    }else{if((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4)||(count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4)){
                                        isDifferent5 = true;
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                          }
                                       }
                                     }
                                    }*/
                                if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4)) {
                                    isDifferent5 = true;
                                     /*   secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                }
                            }
                        } else {
                              /*  //isDifferent, isDifferent2, isDifferent3, isDifferent4, setTemp2, setTemp3, secTemp3, secTemp3, secTemp3
                                if (count == nominados.size()) {
                                    secTemp3 = new Seccion();
                                    secTemp3.addAAHParty(party);
                                    isEquals5 = false;
                                    isDifferent5 = true;
                                }
                                if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5) {
                                    //secNo.addSeccion(secTemp2);
                                    //secNo.addSeccion(secTemp3);
                                }
                                if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5) {
                                    //secNo.addSeccion(secTemp2);
                                    //secNo.addSeccion(secTemp3);
                                }*/
                              /*  if((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4)||(count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4)){
                                    isDifferent5 = true;
                                    secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);
                                }else{if((count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4)||(count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4)){
                                    isDifferent5 = true;
                                    secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);
                                }else{if((count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4)||(count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4)){
                                    isDifferent5 = true;
                                    secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);
                                }else{if((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4)||(count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4)){
                                    isDifferent5 = true;
                                    secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);
                                        }
                                    }
                                  }
                                }*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4)) {
                                isDifferent5 = true;
                                   /* secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);*/
                            }
                        }
                    } else {
                        if (key != null) {
                               /* //secTemp.addAAHParty(party);
                                secTemp8.addAAHParty(party);
                                secTemp2 = secTemp7;
                                secTemp3 = secTemp8;
                                isDifferent5 = true;
*/
                               /* if((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4)||(count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4)){
                                    isDifferent5 = true;
                                    secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);
                                }else{if((count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4)||(count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4)){
                                    isDifferent5 = true;
                                    secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);
                                }else{if((count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4)||(count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4)){
                                    isDifferent5 = true;
                                    secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);
                                }else{if((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4)||(count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4)){
                                    isDifferent5 = true;
                                    secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);
                                        }
                                      }
                                   }
                                }*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4)) {
                                isDifferent5 = true;
                                   /* secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);*/
                            }
                        } else {
                               /* //secTemp.addAAHParty(party);
                                secTemp7.addAAHParty(party);
                                secTemp2 = secTemp7;
                                if (count == nominados.size()) {
                                    isEquals5 = true;
                                }*/
                             /*   if((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4)||(count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4)){
                                    isEquals5 = true;

                                }else{if((count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4)||(count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4)){
                                    isEquals5 = true;

                                }else{if((count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4)||(count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4)){
                                    isEquals5 = true;

                                }else{if((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4)||(count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4)){
                                    isEquals5 = true;
                                        }
                                      }
                                   }
                                }*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4) || ((key == null && currentCoveredEvent == null))) {
                                isEquals5 = true;
                            }
                                /*if(key==null && currentCoveredEvent==null){
                                    secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);
                                }*/
                        }
                    }


                } else if (conteoIterator == 6) {

                    if (currentContractType != null) {
                        if (key != null) {
                            if (currentContractType.equals(key.toLowerCase())) {
                                /*    secTemp9.addAAHParty(party);

                                    //CASO1: Activity =!
                                    if (count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5) {
                                        isEquals6 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }

                                    //CASO2: Clasification !=
                                    if (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5) {
                                        isEquals6 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }

                                    //CASO3: RiskCategory !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5) {
                                        isEquals6 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }

                                    //CASO4: InsurableGroup !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5) {
                                        isEquals6 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }

                                    //CASO5: CoveredEvent !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5) {
                                        isEquals6 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }

                                    //Activity != && Clasification !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5) {
                                        isEquals6 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }

                                    //Activity != && Clasification != && RiskCategory !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5) {
                                        isEquals6 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5) {
                                        isEquals6 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }

                                    //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent !=
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5) {
                                        isEquals6 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }

                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3&& isEquals4 && isEquals5) {
                                        isEquals6 = true;
                                        //secNo.addSeccion(secTemp5);
                                    }*/
                                if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5)) {
                                    isEquals6 = true;
                                        /*if((isDifferent==true)||(isDifferent2==true)||(isDifferent3==true)||(isDifferent4==true)||(isDifferent5==true)){
                                            secCoverage = new Seccion();
                                            secCoverage.addAAHParty(party);
                                            secNo.addSeccion(secCoverage);
                                        }*/
                                } else {
                                    if ((count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5)) {
                                        isEquals6 = true;
                                        /*if((isDifferent==true)||(isDifferent2==true)||(isDifferent3==true)||(isDifferent4==true)||(isDifferent5==true)){
                                            secCoverage = new Seccion();
                                            secCoverage.addAAHParty(party);
                                            secNo.addSeccion(secCoverage);
                                        }*/
                                    }
                                    /*else{
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                        }*/
                                }

                            } else {
                                 /*   secTemp10.addAAHParty(party);

                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5) {
                                        isDifferent6 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                        //secNo.addSeccion(secTemp3);
                                    }

                                    //CASO6: ContractType !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5) {
                                        secTemp2 = secTemp9;
                                        secTemp3 = secTemp10;
                                        isDifferent6 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }*/
                                if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5)) {
                                    isDifferent6 = true;
                                        /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                } else {
                                    if ((count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5)) {
                                        isDifferent6 = true;
                                        /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                    }
                                }
                            }
                        } else {
                              /*  if (count == nominados.size()) {
                                    secTemp3 = new Seccion();
                                    secTemp3.addAAHParty(party);
                                    isEquals6 = false;
                                    isDifferent6 = true;
                                }*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5)) {
                                isDifferent6 = true;
                                    /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                            } else {
                                if ((count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5)) {
                                    isDifferent6 = true;
                                    /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                }
                            }
                        }
                    } else {
                        if (key != null) {
                             /*   //secTemp.addAAHParty(party);
                                secTemp10.addAAHParty(party);
                                secTemp2 = secTemp9;
                                secTemp3 = secTemp10;
                                isDifferent6 = true; */
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5)) {
                                isDifferent6 = true;
                                    /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                            } else {
                                if ((count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5)) {
                                    isDifferent6 = true;
                                    /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                }
                            }

                        } else {
                             /*   //secTemp.addAAHParty(party);
                                secTemp9.addAAHParty(party);
                                secTemp2 = secTemp9;
                                if (count == nominados.size()) {
                                    isEquals6 = true;
                                }*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5)) {
                                isEquals6 = true;
                            } else {
                                if ((count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5)) {
                                    isEquals6 = true;
                                }
                            }
                        }
                    }
                } else if (conteoIterator == 7) {

                    if (currentAAHRiskCategory != null) {
                        if (key != null) {
                            if (currentAAHRiskCategory.equals(key.toLowerCase())) {
                                  /*  secTemp11.addAAHParty(party);
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
                                      }*/
                                if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6)) {
                                    isEquals7 = true;
                                } else {
                                    if ((count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6)) {
                                        isEquals7 = true;
                                    }
                                    /*else{
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                        }*/
                                }
                            } else {
                                  /*  secTemp12.addAAHParty(party);

                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6) {
                                        isDifferent7 = true;
                                       }
                                    //CASO7: AAHRiskCategory !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) {
                                        isDifferent7 = true;
                                     }*/
                                if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6)) {
                                    isDifferent7 = true;
                                        /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                } else {
                                    if ((count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6)) {
                                        isDifferent7 = true;
                                        /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                    }
                                }
                            }
                        } else {
                                /*if (count == nominados.size()) {
                                    secTemp3 = new Seccion();
                                    secTemp3.addAAHParty(party);
                                    isEquals7 = false;
                                    isDifferent7 = true;
                                }*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6)) {
                                isDifferent7 = true;
                                   /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                            } else {
                                if ((count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6)) {
                                    isDifferent7 = true;
                                    /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                }
                            }
                        }
                    } else {
                        if (key != null) {
                             /*   //secTemp.addAAHParty(party);
                                secTemp12.addAAHParty(party);
                                secTemp2 = secTemp11;
                                secTemp3 = secTemp12;
                                isDifferent7 = true;*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6)) {
                                isDifferent7 = true;
                                    /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                            } else {
                                if ((count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6)) {
                                    isDifferent7 = true;
                                   /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                }
                            }
                        } else {
                               /* //secTemp.addAAHParty(party);
                                secTemp11.addAAHParty(party);
                                secTemp2 = secTemp11;
                                if (count == nominados.size()) {
                                    isEquals7 = true;
                                }*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6)) {
                                isEquals7 = true;
                            } else {
                                if ((count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6)) {
                                    isEquals7 = true;
                                }
                            }
                        }
                    }
                } else if (conteoIterator == 8) {

                    if (currentEventStartDate != null) {
                        if (key != null) {
                            if (currentEventStartDate.equals(key.toLowerCase())) {
                                   /* //CASO1: Activity =!
                                    if (count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) {
                                        isEquals8 = true;;
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
                                    }*/
                                if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7)) {
                                    isEquals8 = true;
                                } else {
                                    if ((count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7)) {
                                        isEquals8 = true;
                                    } else {
                                        if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7)) {
                                            isEquals8 = true;
                                        }
                                    /*else{
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                             }*/
                                    }
                                }
                            } else {
                                   /* secTemp14.addAAHParty(party);
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7) {
                                        isDifferent8 = true;
                                    }
                                    //CASO8: EventStartDate !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) {
                                        isDifferent8 = true;
                                     }*/
                                if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7)) {
                                    isDifferent8 = true;
                                        /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                } else {
                                    if ((count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7)) {
                                        isDifferent8 = true;
                                        /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                    } else {
                                        if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7)) {
                                            isDifferent8 = true;
                                        /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                        }
                                    }
                                }
                            }
                        } else {
                                /*if (count == nominados.size()) {
                                    secTemp3 = new Seccion();
                                    secTemp3.addAAHParty(party);
                                    isEquals8 = false;
                                    isDifferent8 = true;
                                }*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7)) {
                                isDifferent8 = true;
                                    /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                            } else {
                                if ((count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7)) {
                                    isDifferent8 = true;
                                    /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                } else {
                                    if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7)) {
                                        isDifferent8 = true;
                                    /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                    }
                                }
                            }
                        }
                    } else {
                        if (key != null) {
                               /* //secTemp.addAAHParty(party);
                                secTemp14.addAAHParty(party);
                                secTemp2 = secTemp13;
                                secTemp3 = secTemp14;
                                isDifferent8 = true;*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7)) {
                                isDifferent8 = true;
                                    /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                            } else {
                                if ((count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7)) {
                                    isDifferent8 = true;
                                    /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                } else {
                                    if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7)) {
                                        isDifferent8 = true;
                                    /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                    }
                                }
                            }
                        } else {
                               /* //secTemp.addAAHParty(party);
                                secTemp13.addAAHParty(party);
                                secTemp2 = secTemp13;
                                if (count == nominados.size()) {
                                    isEquals8 = true;
                                }*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7)) {
                                isEquals8 = true;
                            } else {
                                if ((count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7)) {
                                    isEquals8 = true;
                                } else {
                                    if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7)) {
                                        isEquals8 = true;
                                    }
                                }
                            }
                        }
                    }
                } else if (conteoIterator == 9) {
                    if (currentEventEndDate != null) {
                        if (key != null) {
                            if (currentEventEndDate.equals(key.toLowerCase())) {
                                  /*  //CASO1: Activity =!
                                    if (count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) {
                                        isEquals9 = true;
                                    }
                                    //CASO2: Clasification !=
                                    if (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) {
                                        isEquals9 = true;
                                    }//CASO3: RiskCategory !=
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
                                    }*/

                                if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8)) {
                                    isEquals9 = true;
                                } else {
                                    if ((count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isDifferent8) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8)) {
                                        isEquals9 = true;
                                    } else {
                                        if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8)) {
                                            isEquals9 = true;
                                        }
                                    /*else{
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                        }*/
                                    }
                                }
                            } else {
                                   /* secTemp16.addAAHParty(party);
                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8) {
                                        isDifferent9 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                        //secNo.addSeccion(secTemp3);
                                    }
                                    //CASO9: EventEndDate !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) {
                                        secTemp2 = secTemp15;
                                        secTemp3 = secTemp16;
                                        isDifferent9 = true;
                                        //secNo.addSeccion(secTemp2);
                                        //secNo.addSeccion(secTemp3);
                                    }*/
                                if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8)) {
                                    isDifferent9 = true;
                                        /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                } else {
                                    if ((count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isDifferent8) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8)) {
                                        isDifferent9 = true;
                                        /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                    } else {
                                        if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8)) {
                                            isDifferent9 = true;
                                       /* secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);*/
                                        }
                                    }
                                }
                            }
                        } else {
                                /*if (count == nominados.size()) {
                                    secTemp3 = new Seccion();
                                    secTemp3.addAAHParty(party);
                                    isEquals9 = false;
                                    isDifferent9 = true;
                                }*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8)) {
                                isDifferent9 = true;
                                   /* secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);*/
                            } else {
                                if ((count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isDifferent8) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8)) {
                                    isDifferent9 = true;
                                   /* secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);*/
                                } else {
                                    if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8)) {
                                        isDifferent9 = true;
                                    /*secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);*/
                                    }
                                }
                            }
                        }
                    } else {
                        if (key != null) {
                               /* //secTemp.addAAHParty(party);
                                secTemp16.addAAHParty(party);
                                secTemp2 = secTemp15;
                                secTemp3 = secTemp16;
                                isDifferent9 = true;*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8)) {
                                isDifferent9 = true;
                                    /*secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);*/
                            } else {
                                if ((count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isDifferent8) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8)) {
                                    isDifferent9 = true;
                                   /* secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);*/
                                } else {
                                    if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8)) {
                                        isDifferent9 = true;
                                    /*secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);*/
                                    }
                                }
                            }

                        } else {
                                /*//secTemp.addAAHParty(party);
                                secTemp15.addAAHParty(party);
                                secTemp2 = secTemp15;
                                if (count == nominados.size()) {
                                    isEquals9 = true;
                                }*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8)) {
                                isEquals9 = true;
                            } else {
                                if ((count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7 && isEquals8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isDifferent8) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8)) {
                                    isEquals9 = true;
                                } else {
                                    if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isEquals8) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8)) {
                                        isEquals9 = true;
                                    }
                                }
                            }
                        }
                    }
                } else if (conteoIterator == 10) {

                    if (currentLocalEvent != null) {
                        if (key != null) {
                            if (currentLocalEvent.equals(key.toLowerCase())) {
                                   /* if (isTrue) {
                                       //CASO1: Activity =!
                                        if (count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                        }
                                        //CASO2: Clasification !=
                                        if (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                        }
                                        //CASO3: RiskCategory !=
                                        if (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                         }
                                        //CASO4: InsurableGroup !=
                                        if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                         }
                                        //CASO5: CoveredEvent !=
                                        if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                        }
                                        //CASO6: ContractType !=
                                        if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                        }
                                        //CASO7: AAHRiskCategory !=
                                        if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                        }
                                        //CASO8: EventStartDate !=
                                        if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isDifferent8 && isEquals9) {
                                            isEquals10 = true;
                                         }
                                        //CASO9: EventEndDate !=
                                        if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isDifferent9) {
                                            isEquals10 = true;
                                         }
                                        //Activity != && Clasification !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                        }
                                        //Activity != && Clasification != && RiskCategory !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                        }
                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                        }
                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                        }
                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                        }
                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType != && AAHRiskCategory !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                         }
                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType != && AAHRiskCategory != && EventStartDate !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isEquals9) {
                                            isEquals10 = true;
                                        }
                                        //Activity != && Clasification != && RiskCategory != && InsurableGroup != && CoveredEvent != && ContractType != && AAHRiskCategory != && EventStartDate != && EventEndDate !=
                                        if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isDifferent9) {
                                            isEquals10 = true;
                                        }
                                        if (count == nominados.size() && isEquals && isEquals2 && isEquals3&& isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                            isEquals10 = true;
                                        }

                                    } else {
                                        if ( count == nominados.size() || count!= nominados.size() ) {
                                            secCoverage = new Seccion();
                                            secCoverage.addAAHParty(party);
                                            secNo.addSeccion(secCoverage);
                                        }

                                    }
                                */
                                if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8 && isEquals9)) {
                                    isEquals10 = true;
                                    if ((isDifferent == true) || (isDifferent2 == true) || (isDifferent3 == true) || (isDifferent4 == true) || (isDifferent5 == true) || (isDifferent6 == true) || (isDifferent7 == true) || (isDifferent8 == true) || (isDifferent9 == true) ) {
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                    }
                                } else {
                                    if ((count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isDifferent8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isDifferent9) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9)) {
                                        isEquals10 = true;
                                        if ((isDifferent == true) || (isDifferent2 == true) || (isDifferent3 == true) || (isDifferent4 == true) || (isDifferent5 == true) || (isDifferent6 == true) || (isDifferent7 == true) || (isDifferent8 == true) || (isDifferent9 == true)) {
                                            secCoverage = new Seccion();
                                            secCoverage.addAAHParty(party);
                                            secNo.addSeccion(secCoverage);
                                        }
                                    } else {
                                        if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isEquals8 && isEquals9)) {
                                            isEquals10 = true;
                                            if ((isDifferent == true) || (isDifferent2 == true) || (isDifferent3 == true) || (isDifferent4 == true) || (isDifferent5 == true) || (isDifferent6 == true) || (isDifferent7 == true) || (isDifferent8 == true) || (isDifferent9 == true)) {
                                                secCoverage = new Seccion();
                                                secCoverage.addAAHParty(party);
                                                secNo.addSeccion(secCoverage);
                                            }
                                        } else {
                                            if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isDifferent9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9)) {
                                                isEquals10 = true;
                                                if ((isDifferent == true) || (isDifferent2 == true) || (isDifferent3 == true) || (isDifferent4 == true) || (isDifferent5 == true) || (isDifferent6 == true) || (isDifferent7 == true) || (isDifferent8 == true) || (isDifferent9 == true)) {
                                                    secCoverage = new Seccion();
                                                    secCoverage.addAAHParty(party);
                                                    secNo.addSeccion(secCoverage);
                                                }
                                            } else {
                                                secCoverage = new Seccion();
                                                secCoverage.addAAHParty(party);
                                                secNo.addSeccion(secCoverage);
                                            }
                                        }
                                    }
                                }

                            } else {
                                  /*  //secTemp18.addAAHParty(party);

                                    if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isDifferent9) {
                                        isDifferent10 = true;
                                       // secNo.addSeccion(secTemp2);
                                       // secNo.addSeccion(secTemp3);
                                    }

                                    //CASO10: LocalEvent !=
                                    if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) {
                                       // secTemp2 = secTemp17;
                                        //secTemp3 = secTemp18;
                                        isDifferent10 = true;
                                       // secNo.addSeccion(secTemp2);
                                      //  secNo.addSeccion(secTemp3);
                                    }
                                    secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);*/
                                if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8 && isEquals9)) {
                                    isDifferent10 = true;
                                    secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);
                                } else {
                                    if ((count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isDifferent8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isDifferent9) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9)) {
                                        isDifferent10 = true;
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                    } else {
                                        if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isEquals8 && isEquals9)) {
                                            isDifferent10 = true;
                                            secCoverage = new Seccion();
                                            secCoverage.addAAHParty(party);
                                            secNo.addSeccion(secCoverage);
                                        } else {
                                            if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isDifferent9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) ) {
                                                isDifferent10 = true;
                                                secCoverage = new Seccion();
                                                secCoverage.addAAHParty(party);
                                                secNo.addSeccion(secCoverage);
                                            }
                                        }
                                    }
                                }

                            }
                        } else {
                              /*  if (count == nominados.size()) {
                                   // secTemp3 = new Seccion();
                                  //  secTemp3.addAAHParty(party);
                                    isEquals10 = false;
                                    isDifferent10 = true;

                                }
                                if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9 && isDifferent10) {
                                    //secNo.addSeccion(secTemp2);
                                   // secNo.addSeccion(secTemp3);
                                    isDifferent10 = true;
                                }
                                if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isDifferent9 && isDifferent10) {
                                   // secNo.addSeccion(secTemp2);
                                   // secNo.addSeccion(secTemp3);
                                    isDifferent10 = true;
                                }*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8 && isEquals9)) {
                                isDifferent10 = true;
                                secCoverage = new Seccion();
                                secCoverage.addAAHParty(party);
                                secNo.addSeccion(secCoverage);
                            } else {
                                if ((count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isDifferent8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isDifferent9) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9)) {
                                    isDifferent10 = true;
                                    secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);
                                } else {
                                    if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isEquals8 && isEquals9)) {
                                        isDifferent10 = true;
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                    } else {
                                        if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isDifferent9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9)) {
                                            isDifferent10 = true;
                                            secCoverage = new Seccion();
                                            secCoverage.addAAHParty(party);
                                            secNo.addSeccion(secCoverage);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (key != null) {
                                /*isDifferent10 = true;
                                secCoverage = new Seccion();
                                secCoverage.addAAHParty(party);
                                secNo.addSeccion(secCoverage);*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8 && isEquals9)) {
                                isDifferent10 = true;
                                secCoverage = new Seccion();
                                secCoverage.addAAHParty(party);
                                secNo.addSeccion(secCoverage);
                            } else {
                                if ((count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isDifferent8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isDifferent9) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9)) {
                                    isDifferent10 = true;
                                    secCoverage = new Seccion();
                                    secCoverage.addAAHParty(party);
                                    secNo.addSeccion(secCoverage);
                                } else {
                                    if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isEquals8 && isEquals9)) {
                                        isDifferent10 = true;
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                    } else {
                                        if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isDifferent9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9)) {
                                            isDifferent10 = true;
                                            secCoverage = new Seccion();
                                            secCoverage.addAAHParty(party);
                                            secNo.addSeccion(secCoverage);
                                        }
                                    }
                                }
                            }
                        } else {
                                /*if (isTrue) {
                                    //secTemp.addAAHParty(party);
                                    //secTemp17.addAAHParty(party);
                                   // secTemp2 = secTemp17;
                                    if (count == nominados.size()) {
                                        isEquals10 = true;
                                    }
                                } else {
                                    //CSMBIE
                                    if ( count == nominados.size() || count!= nominados.size() ) {
                                        secCoverage = new Seccion();
                                        secCoverage.addAAHParty(party);
                                        secNo.addSeccion(secCoverage);
                                    }
                                }*/
                            if ((count == nominados.size() && isDifferent && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8 && isEquals9)) {
                                isEquals10 = true;

                            } else {
                                if ((count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isDifferent6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isDifferent7 && isEquals8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isDifferent8 && isEquals9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isDifferent9) || (count == nominados.size() && isDifferent && isDifferent2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9)) {
                                    isEquals10 = true;

                                } else {
                                    if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isEquals6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isEquals7 && isEquals8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isEquals8 && isEquals9)) {
                                        isEquals10 = true;

                                    } else {
                                        if ((count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isEquals9) || (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isDifferent9) || (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9)) {
                                            isEquals10 = true;

                                        }
                                    }
                                }
                            }

                        }

                           /* if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9 && isEquals10) {
                                secNo.addSeccion(secTemp2);
                            }

                            if (count == nominados.size() && isEquals && isEquals2 && isEquals3 && isEquals4 && isEquals5 && isEquals6 && isEquals7 && isEquals8 && isEquals9 && isDifferent10) {
                                secNo.addSeccion(secTemp2);
                                secNo.addSeccion(secTemp3);
                            }

                            if (count == nominados.size() && isDifferent && isDifferent2 && isDifferent3 && isDifferent4 && isDifferent5 && isDifferent6 && isDifferent7 && isDifferent8 && isDifferent9 && isDifferent10) {
                                secNo.addSeccion(secTemp2);
                                secNo.addSeccion(secTemp3);
                            }*/

                    }
                }
                //}
            }

            XmlMapper xmlMapper = new XmlMapper();

            // Eliminando Secciones vacias
            int sizeASecciones = secNo.getSeccion().size();
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
            System.out.println("conteoIteratorIn: " + conteoIteratorIn);
            Map.Entry mentry = (Map.Entry) iteratorIn.next();
            String key = "";
            System.out.println(mentry);
            if (mentry.getValue() != null) {
                key = mentry.getValue().toString();
            } else {
                key = null;
            }
            //key = mentry.getValue().toString();
            System.out.println("Key: " + key);
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
              /*  if (!coverages1.equals(currentCoverage1)) {
                     boolean isTrueIN = false;

                  System.out.println(currentCoverage1 + "5555555555");
                }*/
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
                try {
                    party.getActivity().getDisplayName().toLowerCase().replaceAll("\\s+", "");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando activity: null");
                    currentActivity = null;
                    //System.out.println(party.getCoveredEvent().getDisplayName());
                }
                if (currentActivity != null) {
                    currentActivity = party.getActivity().getDisplayName().toLowerCase().replaceAll("\\s+", "");
                }

                //Try-Catch: Clasification
                try {
                    party.getClasification().getDisplayName().toLowerCase().replaceAll("\\s+", "");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando Clasification: null");
                    currentClasification = null;
                    //System.out.println(party.getCoveredEvent().getDisplayName());
                }
                if (currentClasification != null) {
                    currentClasification = party.getClasification().getDisplayName().toLowerCase().replaceAll("\\s+", "");
                }

                //RiskCategory
                currentRiskCategory = party.getRiskCategory();
                if (currentRiskCategory != null) {
                    currentRiskCategory = party.getRiskCategory().toLowerCase().replaceAll("\\s+", "");
                }

                //Try-Catch: currentInsurableGroup
                try {
                    party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+", "");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando Clasification: null");
                    currentInsurableGroup = null;
                    //System.out.println(party.getCoveredEvent().getDisplayName());
                }
                if (currentInsurableGroup != null) {
                    currentInsurableGroup = party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+", "");
                }

                //Try-Catch: currentCoveredEvent
                try {
                    party.getCoveredEvent().getDisplayName();
                    //party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando currentCoveredEvent: null");
                    currentCoveredEvent = null;
                }
                if (currentCoveredEvent != null) {
                    currentCoveredEvent = party.getCoveredEvent().getDisplayName().toLowerCase().replaceAll("\\s+", "");
                }

                //Try-Catch: currentContractType
                try {
                    party.getContractType().getDisplayName();
                    //party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando currentCoveredEvent: null");
                    currentContractType = null;
                }
                if (currentContractType != null) {
                    currentContractType = party.getContractType().getDisplayName().toLowerCase().replaceAll("\\s+", "");
                }

                //Try-Catch: currentAAHRiskCategory
                try {
                    party.getAAHRiskCategory().getDisplayName();
                    //party.getInsurableGroup().getDisplayName().toLowerCase().replaceAll("\\s+","");

                } catch (NullPointerException n) {
                    //System.out.println("Asignando currentCoveredEvent: null");
                    currentAAHRiskCategory = null;
                }
                if (currentAAHRiskCategory != null) {
                    currentAAHRiskCategory = party.getAAHRiskCategory().getDisplayName().toLowerCase().replaceAll("\\s+", "");
                }

                //EventStartDate
                currentEventStartDate = party.getEventStartDate();
                if (currentEventStartDate != null) {
                    currentEventStartDate = party.getEventStartDate().toLowerCase().replaceAll("\\s+", "");
                }

                //EventEndDate
                currentEventEndDate = party.getEventEndDate();
                if (currentEventEndDate != null) {
                    currentEventEndDate = party.getEventEndDate().toLowerCase().replaceAll("\\s+", "");
                }

                //LocalEvent
                currentLocalEvent = party.getLocationEvent();
                if (currentLocalEvent != null) {
                    currentLocalEvent = party.getLocationEvent().toLowerCase().replaceAll("\\s+", "");
                }

                if (conteoIteratorIn == 1) {
                    if (currentActivity != null) {
                        if (key != null) {
                            if (currentActivity.equals(key.toLowerCase())) {
                                if (count == no_nominados.size()) {
                                    isEqualsIn = true;
                                }
                            } else {
                                isDifferentIn = true;
                            }
                        } else {
                            isEqualsIn = false;
                            isDifferentIn = true;
                        }
                    } else {
                        if (key != null) {
                            isDifferentIn = true;
                        } else {
                            if (count == no_nominados.size()) {
                                isEqualsIn = true;
                            }
                        }
                    }
                } else if (conteoIteratorIn == 2) {
                    if (currentClasification != null) {
                        if (key != null) {
                            if (currentClasification.equals(key.toLowerCase())) {

                                if ((count == no_nominados.size() && isEqualsIn) || (count == no_nominados.size() && isDifferentIn)) {
                                    isEqualsIn2 = true;
                                 /*   if(isDifferent==true){
                                        secCoverageIn = new Seccion();
                                        secCoverageIn.addAAHParty(party);
                                        secIn.addSeccion(secCoverageIn);
                                    }*/
                                }

                            } else {

                                if ((count == no_nominados.size() && isEqualsIn) || (count == no_nominados.size() && isDifferentIn)) {
                                    isDifferentIn2 = true;
                                   /* secCoverageIn = new Seccion();
                                    secCoverageIn.addAAHParty(party);
                                    secIn.addSeccion(secCoverageIn);*/
                                }
                            }
                        } else {

                            if ((count == no_nominados.size() && isEqualsIn) || (count == no_nominados.size() && isDifferentIn)) {
                                isDifferentIn2 = true;
                              /*  secCoverageIn = new Seccion();
                                secCoverageIn.addAAHParty(party);
                                secIn.addSeccion(secCoverageIn);*/
                            }
                        }
                    } else {
                        if (key != null) {

                            if ((count == no_nominados.size() && isEqualsIn) || (count == no_nominados.size() && isDifferentIn)) {
                                isDifferentIn2 = true;
                            /*    secCoverageIn = new Seccion();
                                secCoverageIn.addAAHParty(party);
                                secIn.addSeccion(secCoverageIn);*/
                            }
                        } else {

                            if ((count == no_nominados.size() && isEqualsIn) || (count == no_nominados.size() && isDifferentIn)) {
                                isEqualsIn2 = true;
                            }
                        }
                    }

                } else if (conteoIteratorIn == 3) {
                    if (currentRiskCategory != null) {
                        if (key != null) {
                            if (currentRiskCategory.equals(key.toLowerCase())) {

                                if ((count == no_nominados.size() && isEqualsIn && isDifferentIn2) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2) || (count == no_nominados.size() && isDifferentIn && isEqualsIn2)) {
                                    isEqualsIn3 = true;
                                }

                            } else {

                                if ((count == no_nominados.size() && isEqualsIn && isDifferentIn2) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2) || (count == no_nominados.size() && isDifferentIn && isEqualsIn2)) {
                                    isDifferentIn3 = true;
                                }

                            }
                        } else {

                            if ((count == no_nominados.size() && isEqualsIn && isDifferentIn2) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2) || (count == no_nominados.size() && isDifferentIn && isEqualsIn2)) {
                                isDifferentIn3 = true;
                            }
                        }

                    } else {
                        if (key != null) {

                            if ((count == no_nominados.size() && isEqualsIn && isDifferentIn2) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2) || (count == no_nominados.size() && isDifferentIn && isEqualsIn2)) {
                                isDifferentIn3 = true;
                            }
                        } else {

                            if ((count == no_nominados.size() && isEqualsIn && isDifferentIn2) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2) || (count == no_nominados.size() && isDifferentIn && isEqualsIn2)) {
                                isEqualsIn3 = true;
                            }
                        }
                    }
                } else if (conteoIteratorIn == 4) {
                    if (currentInsurableGroup != null) {
                        if (key != null) {
                            if (currentInsurableGroup.equals(key.toLowerCase())) {

                                if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3)) {
                                    isEqualsIn4 = true;
                                }
                            } else {

                                if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3)) {
                                    isDifferentIn4 = true;
                                }
                            }
                        } else {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3)) {
                                isDifferentIn4 = true;
                            }
                        }

                    } else {
                        if (key != null) {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3)) {
                                isDifferentIn4 = true;
                            }
                        } else {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3)) {
                                isEqualsIn4 = true;
                            }
                        }
                    }
                } else if (conteoIteratorIn == 5) {   //isDifferentIn, isDifferentIn2, isDifferentIn3, isDifferentIn4, setTemp2, setTemp3
                    if (currentCoveredEvent != null) {
                        if (key != null) {
                            if (currentCoveredEvent.equals(key.toLowerCase())) {

                                if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4)) {
                                    isEqualsIn5 = true;
                                }
                            } else {

                                if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4)) {
                                    isDifferentIn5 = true;
                                }

                            }
                        } else {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4)) {
                                isDifferentIn5 = true;
                            }
                        }

                    } else {
                        if (key != null) {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4)) {
                                isDifferentIn5 = true;
                            }
                        } else {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4)) {
                                isEqualsIn5 = true;
                            }
                        }
                    }
                } else if (conteoIteratorIn == 6) {
                    if (currentContractType != null) {
                        if (key != null) {
                            if (currentContractType.equals(key.toLowerCase())) {

                                if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5)) {
                                    isEqualsIn6 = true;

                                } else {
                                    if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5)) {
                                        isEqualsIn6 = true;

                                    }
                                }
                            } else {

                                if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5)) {
                                    isDifferentIn6 = true;
                                } else {
                                    if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5)) {
                                        isDifferentIn6 = true;
                                    }
                                }
                            }
                        } else {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5)) {
                                isDifferentIn6 = true;
                            } else {
                                if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5)) {
                                    isDifferentIn6 = true;
                                }
                            }
                        }
                    } else {
                        if (key != null) {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5)) {
                                isDifferentIn6 = true;
                            } else {
                                if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5)) {
                                    isDifferentIn6 = true;
                                }
                            }
                        } else {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5)) {
                                isEqualsIn6 = true;
                            } else {
                                if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5)) {
                                    isEqualsIn6 = true;
                                }
                            }
                        }
                    }
                } else if (conteoIteratorIn == 7) {
                    if (currentAAHRiskCategory != null) {
                        if (key != null) {
                            if (currentAAHRiskCategory.equals(key.toLowerCase())) {

                                if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6)) {
                                    isEqualsIn7 = true;
                                } else {
                                    if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6)) {
                                        isEqualsIn7 = true;
                                    }
                                }
                            } else {

                                if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6)) {
                                    isDifferentIn7 = true;
                                } else {
                                    if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6)) {
                                        isDifferentIn7 = true;
                                    }
                                }
                            }
                        } else {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6)) {
                                isDifferentIn7 = true;
                            } else {
                                if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6)) {
                                    isDifferentIn7 = true;
                                }
                            }
                        }
                    } else {
                        if (key != null) {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6)) {
                                isDifferentIn7 = true;
                            } else {
                                if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6)) {
                                    isDifferentIn7 = true;
                                }
                            }
                        } else {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6)) {
                                isEqualsIn7 = true;
                            } else {
                                if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6)) {
                                    isEqualsIn7 = true;
                                }
                            }
                        }
                    }
                } else if (conteoIteratorIn == 8) {
                    if (currentEventStartDate != null) {
                        if (key != null) {
                            if (currentEventStartDate.equals(key.toLowerCase())) {

                                if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7)) {
                                    isEqualsIn8 = true;
                                } else {
                                    if ((count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7)) {
                                        isEqualsIn8 = true;
                                    } else {
                                        if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7)) {
                                            isEqualsIn8 = true;
                                        }
                                    }
                                }

                            } else {

                                if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7)) {
                                    isDifferentIn8 = true;
                                } else {
                                    if ((count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7)) {
                                        isDifferentIn8 = true;
                                    } else {
                                        if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7)) {
                                            isDifferentIn8 = true;
                                        }
                                    }
                                }
                            }
                        } else {
                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7)) {
                                isDifferentIn8 = true;
                            } else {
                                if ((count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7)) {
                                    isDifferentIn8 = true;
                                } else {
                                    if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7)) {
                                        isDifferentIn8 = true;
                                    }
                                }
                            }
                        }
                    } else {
                        if (key != null) {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7)) {
                                isDifferentIn8 = true;
                            } else {
                                if ((count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7)) {
                                    isDifferentIn8 = true;
                                } else {
                                    if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7)) {
                                        isDifferentIn8 = true;
                                    }
                                }
                            }

                        } else {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7)) {
                                isEqualsIn8 = true;
                            } else {
                                if ((count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7)) {
                                    isEqualsIn8 = true;
                                } else {
                                    if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7)) {
                                        isEqualsIn8 = true;
                                    }
                                }
                            }
                        }
                    }
                } else if (conteoIteratorIn == 9) {
                    if (currentEventEndDate != null) {
                        if (key != null) {
                            if (currentEventEndDate.equals(key.toLowerCase())) {

                                if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8)) {
                                    isEqualsIn9 = true;
                                } else {
                                    if ((count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isDifferentIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8)) {
                                        isEqualsIn9 = true;
                                    } else {
                                        if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8)) {
                                            isEqualsIn9 = true;
                                        }
                                    }
                                }

                            } else {

                                if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8)) {
                                    isDifferentIn9 = true;
                                } else {
                                    if ((count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isDifferentIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8)) {
                                        isDifferentIn9 = true;
                                    } else {
                                        if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8)) {
                                            isDifferentIn9 = true;
                                        }
                                    }
                                }
                            }
                        } else {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8)) {
                                isDifferentIn9 = true;
                            } else {
                                if ((count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isDifferentIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8)) {
                                    isDifferentIn9 = true;
                                } else {
                                    if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8)) {
                                        isDifferentIn9 = true;
                                    }
                                }
                            }
                        }
                    } else {
                        if (key != null) {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8)) {
                                isDifferentIn9 = true;
                            } else {
                                if ((count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isDifferentIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8)) {
                                    isDifferentIn9 = true;
                                } else {
                                    if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8)) {
                                        isDifferentIn9 = true;
                                    }
                                }
                            }
                        } else {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8)) {
                                isEqualsIn9 = true;
                            } else {
                                if ((count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7 && isEqualsIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isDifferentIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8)) {
                                    isEqualsIn9 = true;
                                } else {
                                    if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isEqualsIn8) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8)) {
                                        isEqualsIn9 = true;
                                    }
                                }
                            }
                        }
                    }
                } else if (conteoIteratorIn == 10) {
                    if (currentLocalEvent != null) {
                        if (key != null) {
                            if (currentLocalEvent.equals(key.toLowerCase())) {

                                if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9)) {
                                    isEqualsIn10 = true;
                                    if ((isDifferentIn == true) || (isDifferentIn2 == true) || (isDifferentIn3 == true) || (isDifferentIn4 == true) || (isDifferentIn5 == true) || (isDifferentIn6 == true) || (isDifferentIn7 == true) || (isDifferentIn8 == true) || (isDifferentIn9 == true)) {
                                        secCoverageIn = new Seccion();
                                        secCoverageIn.addAAHParty(party);
                                        secIn.addSeccion(secCoverageIn);
                                    }
                                } else {
                                    if ((count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isDifferentIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isDifferentIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9)) {
                                        isEqualsIn10 = true;
                                        if ((isDifferentIn == true) || (isDifferentIn2 == true) || (isDifferentIn3 == true) || (isDifferentIn4 == true) || (isDifferentIn5 == true) || (isDifferentIn6 == true) || (isDifferentIn7 == true) || (isDifferentIn8 == true) || (isDifferentIn9 == true)) {
                                            secCoverageIn = new Seccion();
                                            secCoverageIn.addAAHParty(party);
                                            secIn.addSeccion(secCoverageIn);
                                        }
                                    } else {
                                        if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isEqualsIn8 && isEqualsIn9)) {
                                            isEqualsIn10 = true;
                                            if ((isDifferentIn == true) || (isDifferentIn2 == true) || (isDifferentIn3 == true) || (isDifferentIn4 == true) || (isDifferentIn5 == true) || (isDifferentIn6 == true) || (isDifferentIn7 == true) || (isDifferentIn8 == true) || (isDifferentIn9 == true)) {
                                                secCoverageIn = new Seccion();
                                                secCoverageIn.addAAHParty(party);
                                                secIn.addSeccion(secCoverageIn);
                                            }
                                        } else {
                                            if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8 && isDifferentIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9)) {
                                                isEqualsIn10 = true;
                                                if ((isDifferentIn == true) || (isDifferentIn2 == true) || (isDifferentIn3 == true) || (isDifferentIn4 == true) || (isDifferentIn5 == true) || (isDifferentIn6 == true) || (isDifferentIn7 == true) || (isDifferentIn8 == true) || (isDifferentIn9 == true)) {
                                                    secCoverageIn = new Seccion();
                                                    secCoverageIn.addAAHParty(party);
                                                    secIn.addSeccion(secCoverageIn);
                                                }
                                            } else {
                                                secCoverageIn = new Seccion();
                                                secCoverageIn.addAAHParty(party);
                                                secIn.addSeccion(secCoverageIn);
                                            }
                                        }
                                    }
                                }

                            } else {

                                if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9)) {
                                    isDifferentIn10 = true;
                                    secCoverageIn = new Seccion();
                                    secCoverageIn.addAAHParty(party);
                                    secIn.addSeccion(secCoverageIn);
                                } else {
                                    if ((count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isDifferentIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isDifferentIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9)) {
                                        isDifferentIn10 = true;
                                        secCoverageIn = new Seccion();
                                        secCoverageIn.addAAHParty(party);
                                        secIn.addSeccion(secCoverageIn);
                                    } else {
                                        if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isEqualsIn8 && isEqualsIn9)) {
                                            isDifferentIn10 = true;
                                            secCoverageIn = new Seccion();
                                            secCoverageIn.addAAHParty(party);
                                            secIn.addSeccion(secCoverageIn);
                                        } else {
                                            if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8 && isDifferentIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9)) {
                                                isDifferentIn10 = true;
                                                secCoverageIn = new Seccion();
                                                secCoverageIn.addAAHParty(party);
                                                secIn.addSeccion(secCoverageIn);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9)) {
                                isDifferentIn10 = true;
                                secCoverageIn = new Seccion();
                                secCoverageIn.addAAHParty(party);
                                secIn.addSeccion(secCoverageIn);
                            } else {
                                if ((count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isDifferentIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isDifferentIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9)) {
                                    isDifferentIn10 = true;
                                    secCoverageIn = new Seccion();
                                    secCoverageIn.addAAHParty(party);
                                    secIn.addSeccion(secCoverageIn);
                                } else {
                                    if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isEqualsIn8 && isEqualsIn9)) {
                                        isDifferentIn10 = true;
                                        secCoverageIn = new Seccion();
                                        secCoverageIn.addAAHParty(party);
                                        secIn.addSeccion(secCoverageIn);
                                    } else {
                                        if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8 && isDifferentIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9)) {
                                            isDifferentIn10 = true;
                                            secCoverageIn = new Seccion();
                                            secCoverageIn.addAAHParty(party);
                                            secIn.addSeccion(secCoverageIn);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (key != null) {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9)) {
                                isDifferentIn10 = true;
                                secCoverageIn = new Seccion();
                                secCoverageIn.addAAHParty(party);
                                secIn.addSeccion(secCoverageIn);
                            } else {
                                if ((count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isDifferentIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isDifferentIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9)) {
                                    isDifferentIn10 = true;
                                    secCoverageIn = new Seccion();
                                    secCoverageIn.addAAHParty(party);
                                    secIn.addSeccion(secCoverageIn);
                                } else {
                                    if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isEqualsIn8 && isEqualsIn9)) {
                                        isDifferentIn10 = true;
                                        secCoverageIn = new Seccion();
                                        secCoverageIn.addAAHParty(party);
                                        secIn.addSeccion(secCoverageIn);
                                    } else {
                                        if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8 && isDifferentIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9)) {
                                            isDifferentIn10 = true;
                                            secCoverageIn = new Seccion();
                                            secCoverageIn.addAAHParty(party);
                                            secIn.addSeccion(secCoverageIn);
                                        }
                                    }
                                }
                            }
                        } else {

                            if ((count == no_nominados.size() && isDifferentIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9)) {
                                isEqualsIn10 = true;

                            } else {
                                if ((count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isDifferentIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isDifferentIn8 && isEqualsIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isDifferentIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9)) {
                                    isEqualsIn10 = true;

                                } else {
                                    if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isEqualsIn8 && isEqualsIn9)) {
                                        isEqualsIn10 = true;

                                    } else {
                                        if ((count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8 && isEqualsIn9) || (count == no_nominados.size() && isDifferentIn && isDifferentIn2 && isDifferentIn3 && isDifferentIn4 && isDifferentIn5 && isDifferentIn6 && isDifferentIn7 && isDifferentIn8 && isDifferentIn9) || (count == no_nominados.size() && isEqualsIn && isEqualsIn2 && isEqualsIn3 && isEqualsIn4 && isEqualsIn5 && isEqualsIn6 && isEqualsIn7 && isEqualsIn8 && isEqualsIn9)) {
                                            isEqualsIn10 = true;

                                        }
                                    }
                                }
                            }

                        }

                    }

                    if ((!coverages.equals(currentCoverage1)&& isEquals10==true)) {
                        System.out.println(coverages + " ----");
                        System.out.println(currentCoverage1 + " .........");
                        secCoverageIn = new Seccion();
                        secCoverageIn.addAAHParty(party);
                        secIn.addSeccion(secCoverageIn);
                    }
                }

                XmlMapper xmlMapper = new XmlMapper();
                // Eliminando Secciones vacias
                int sizeASecciones = secIn.getSeccion().size();
                for (int i = 0; i < sizeASecciones; i++) {
                    if (secIn.getSeccion().get(i).getSize() == 0) {

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
