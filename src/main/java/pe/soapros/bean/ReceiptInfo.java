package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName="ReceiptInfo")
public class ReceiptInfo {
	@JsonProperty("LastReceiptDueDate")
	private String lastReceiptDueDate;
	@JsonProperty("LastReceiptCoverageEndDate")
	private String lastReceiptCoverageEndDate;

	//public String getLastReceiptDueDate() {
	//	return lastReceiptDueDate;
	//}

	//public void setLastReceiptDueDate(String lastReceiptDueDate) {
	//	this.lastReceiptDueDate = lastReceiptDueDate;
	//}
	
}
