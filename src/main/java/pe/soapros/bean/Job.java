package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName="Job")
public class Job {

	@JsonProperty("Subtype")
	private Subtype subtype;

	@JsonProperty("JobNumber")
	private String jobNumber;

	/*public Subtype getSubtype() {
		return subtype;
	}

	public void setSubtype(Subtype subtype) {
		this.subtype = subtype;
	}*/

}
