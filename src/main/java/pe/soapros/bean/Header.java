package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="Header")
public class Header {

	@JsonProperty("EventIDGLOBAL")
	private String eventIDGLOBAL;
	
	@JsonProperty("EventName")
	private String eventName;
	
	@JsonProperty("FormCode")
	private String formCode;
	
	@JsonProperty("FlowID")
	private String flowID;
	
	
	public String getEventIDGLOBAL() {
		return this.eventIDGLOBAL;
	}
	public void setEventIDGLOBAL(String eventIDGLOBAL) {
		this.eventIDGLOBAL = eventIDGLOBAL;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getFormCode() {
		return formCode;
	}
	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}
	public String getFlowID() {
		return flowID;
	}
	public void setFlowID(String flowID) {
		this.flowID = flowID;
	}
	
	
	
	
}
