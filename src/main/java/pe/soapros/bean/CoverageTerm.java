package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName="CoverageTerm")
public class CoverageTerm {
	@JsonProperty("ModelType")
	private ModelType modelType;
	@JsonProperty("DisplayValue")
	private String displayValue;
	@JsonProperty("DisplayName")
	private String displayName;
	@JsonProperty("DisplayAmount")
	private String displayAmount;
	@JsonProperty("CodeIdentifier")
	private String codeIdentifier;
	@JsonProperty("AggregationModelList")
	private AggregationModelList aggregationModelList;
	@JsonProperty("ValueType")
	private ValueType valueType;


	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDisplayValue() {
		return displayValue;
	}
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
	public ModelType getModelType() {
		return modelType;
	}
	public void setModelType(ModelType modelType) {
		this.modelType = modelType;
	}
	
}
