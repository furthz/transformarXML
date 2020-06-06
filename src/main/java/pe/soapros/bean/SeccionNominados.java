package pe.soapros.bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Header")
public class SeccionNominados {
	
	@JsonProperty("Seccion")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Seccion> seccion;

	public List<Seccion> getSeccion() {
		return seccion;
	}

	public void setSeccion(List<Seccion> seccion) {
		this.seccion = seccion;
	}

	public SeccionNominados() {
		this.seccion = new ArrayList<Seccion>();		
	}
	
	public void addSeccion(Seccion sec) {
		this.seccion.add(sec);
	}
	
}
