package pe.soapros.bean;



import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName="Seccion")
public class Seccion {
	
	@JsonProperty("AAHParty")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<AAHParty> aAHParty;

	public List<AAHParty> getaAHParty() {
		return aAHParty;
	}

	public void setaAHParty(ArrayList<AAHParty> aAHParty) {
		this.aAHParty = aAHParty;
	}

	public Seccion () {
		this.aAHParty = new ArrayList<AAHParty>();
	}
	
	public void addAAHParty(AAHParty p) {
		this.aAHParty.add(p);
	}
	
	public int getSize() {
		return this.aAHParty.size();
		//AAHPartys obj = new AAHPartys();
		//int cantidad_Sec= obj.cantSeccion;
		//return  cantidad_Sec;
	}
	
}
