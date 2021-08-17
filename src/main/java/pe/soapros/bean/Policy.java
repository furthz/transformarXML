package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Policy")
public class Policy {

	@JsonProperty("ProductCode")
	private String productCode;
	@JsonProperty("FormattedCreation")
	private String FormattedCreation;

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getFormattedCreation() {
		return FormattedCreation;
	}

	public void setFormattedCreation(String formattedCreation) {
		FormattedCreation = formattedCreation;
	}
}
