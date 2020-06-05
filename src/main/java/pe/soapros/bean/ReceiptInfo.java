package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Header")
public class ReceiptInfo {
	@JsonProperty("LastReceiptDueDate")
	private String lastReceiptDueDate;

	public String getLastReceiptDueDate() {
		return lastReceiptDueDate;
	}

	public void setLastReceiptDueDate(String lastReceiptDueDate) {
		this.lastReceiptDueDate = lastReceiptDueDate;
	}
	
}
