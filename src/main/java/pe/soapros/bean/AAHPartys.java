package pe.soapros.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import pe.soapros.constants.Constants;
import pe.soapros.log.Log;

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

	public List<AAHParty> getAahparty() {
		return aahpartys;
	}

	public void setAahparty(List<AAHParty> aahparty) {
		this.aahpartys = aahparty;
	}

	/**
	 * Método que ordena nominados y no nomiados y los agrupa por sus coberturas
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

			for (AAHParty party : nominados) {
				if (party.getPartyCoverages().getCoverages().toLowerCase().equals(key.toLowerCase())) {
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

			for (AAHParty party : no_nominados) {
				if (party.getPartyCoverages().getCoverages().toLowerCase().equals(key.toLowerCase())) {
					secTemp.addAAHParty(party);
				}
			}

			secIn.addSeccion(secTemp);
		}

		this.seccionNominados = secNo;
		this.seccionInnominados = secIn;
		
		//System.out.println("hola");

	}

}
