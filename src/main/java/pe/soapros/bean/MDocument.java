package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.List;

@Data
public class MDocument {
    @JsonProperty("UserApplication")
    private String userApplication;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("ContentTypeDef")
    private List<String> contentTypeDef;
    @JsonProperty("EntityID")
    private String entityID;
    @JsonProperty("EntityName")
    private String entityName;
    @JsonProperty("BusinessBranch")
    private String businessBranch;
    @JsonProperty("ProcessName")
    private String processName;
    @JsonProperty("CreateBy")
    private String createBy;
    @JsonProperty("CountryName")
    private String countryName;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("DocumentCode")
    private String documentCode;
    @JsonProperty("DocumentTypes")
    private String documentTypes;
    @JsonProperty("CreateDate")
    private String createDate;
    @JsonProperty("AgrupationData")
    private String agrupationData;
    @JsonProperty("BupID")
    private String bupID;
    @JsonProperty("PersonType")
    private String personType;
    @JsonProperty("FantasyName")
    private String fantasyName;
    @JsonProperty("TaxIdentificationType")
    private String taxIdentificationType;
    @JsonProperty("IdentificationType")
    private String identificationType;
    @JsonProperty("IdentificationNumber")
    private String identificationNumber;
    @JsonProperty("SourceSystem")
    private String sourceSystem;
    @JsonProperty("Environment")
    private String environment;

    @JsonProperty("File")
    private File file;
    @JsonProperty("UploadMetadata")
    private UploadMetadata uploadMetadata;
}
