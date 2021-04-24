package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class Document {
	
	@JacksonXmlProperty(localName="Header")
    @JsonProperty("Header")
	private Header header;

	@JsonProperty("Metadata")
	private Metadata metadata;

    @JsonProperty("Content")
	private Content content;

	@JsonProperty("Delivery")
	private Delivery delivery;

	/*
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}
	*/
	
}
