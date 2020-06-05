package pe.soapros.bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import pe.soapros.constants.Constants;
import pe.soapros.log.Log;
import pe.soapros.tasklet.TransformXmlTasklet;

@JacksonXmlRootElement(localName="Header")
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


	public void generateNominados() {
		
		List<AAHParty> nominados = new ArrayList<AAHParty>();
		List<AAHParty> no_nominados = new ArrayList<AAHParty>();
		
		//separado los nominados
		for(AAHParty party:this.aahpartys) {
		
			if(party.getInsuredType().getCode().equals(Constants.NOMINADOS)) {
				
				nominados.add(party);
			} else if(party.getInsuredType().getCode().equals(Constants.NO_NOMINADOS)){
				
				no_nominados.add(party);
			} else {
				log.info("No se reconoce tipo: " + party.getInsuredType().getCode());
			}
			
		}
		
		//buscar coverturas iguales para el grupo de nominados
		
		for(AAHParty party:nominados) {
			//verificar la cantidad de coverturas
			
			
		}
		
		
	}
	
	
}
