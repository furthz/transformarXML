package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="AggregationModelList")
public class AggregationModelList {
	@JsonProperty("AggregationModel")
	private AggregationModel aggregationModel;

	public AggregationModel getAggregationModel() {
		return aggregationModel;
	}

	public void setAggregationModel(AggregationModel aggregationModel) {
		this.aggregationModel = aggregationModel;
	}
}
