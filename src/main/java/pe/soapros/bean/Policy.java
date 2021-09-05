package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Policy")
public class Policy {

	@JsonProperty("CreateTime")
	private String createTime;
	@JsonProperty("FormattedCreation")
	private String formattedCreation;
	@JsonProperty("ProductCode")
	private String productCode;

	public String getCreateTime(){
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFormattedCreation() {
		return formattedCreation;
	}

	public void setFormattedCreation(String formattedCreation) {
		this.formattedCreation = formattedCreation;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
}
