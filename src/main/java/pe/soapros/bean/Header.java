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
	
	@JsonProperty("ThemeID")
	private String themeID;
	
	@JsonProperty("SourceSystem")
	private String sourceSystem;
	
	@JsonProperty("Environment")
	private String environment;
	
	@JsonProperty("MultDownID")
	private String multDownID;
	
	@JsonProperty("UserName")
	private String userName;
	
	@JsonProperty("OcurrenceDate")
	private String ocurrenceDate;
	
	@JsonProperty("BusinessID")
	private String businessID;
	
	@JsonProperty("ForPrint")
	private String forPrint;
	
	@JsonProperty("ExternalChannel")
	private String externalChannel;
	
	@JsonProperty("GroupQuantity")
	private String groupQuantity;
	
	@JsonProperty("GeneralEnvironment")
	private String generalEnvironment;
	
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
	public String getThemeID() {
		return themeID;
	}
	public void setThemeID(String themeID) {
		this.themeID = themeID;
	}
	public String getSourceSystem() {
		return sourceSystem;
	}
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getMultDownID() {
		return multDownID;
	}
	public void setMultDownID(String multDownID) {
		this.multDownID = multDownID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOcurrenceDate() {
		return ocurrenceDate;
	}
	public void setOcurrenceDate(String ocurrenceDate) {
		this.ocurrenceDate = ocurrenceDate;
	}
	public String getBusinessID() {
		return businessID;
	}
	public void setBusinessID(String businessID) {
		this.businessID = businessID;
	}
	public String getForPrint() {
		return forPrint;
	}
	public void setForPrint(String forPrint) {
		this.forPrint = forPrint;
	}
	public String getExternalChannel() {
		return externalChannel;
	}
	public void setExternalChannel(String externalChannel) {
		this.externalChannel = externalChannel;
	}
	public String getGroupQuantity() {
		return groupQuantity;
	}
	public void setGroupQuantity(String groupQuantity) {
		this.groupQuantity = groupQuantity;
	}
	public String getGeneralEnvironment() {
		return generalEnvironment;
	}
	public void setGeneralEnvironment(String generalEnvironment) {
		this.generalEnvironment = generalEnvironment;
	}
	
	
	
	
}
