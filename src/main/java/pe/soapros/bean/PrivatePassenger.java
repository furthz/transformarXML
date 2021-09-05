package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName="PrivatePassenger")
public class PrivatePassenger {


    @JsonProperty("ChassisNumber")
    private String chassisNumber;

    @JsonProperty("EngineNumber")
    private String engineNumber;

    @JsonProperty("LicensePlate")
    private String licensePlate;

    @JsonProperty("MakeName")
    private String makeName;

    @JsonProperty("ModelName")
    private String modelName;

    @JsonProperty("VehicleNumber")
    private String vehicleNumber;

    @JsonProperty("ModelYear")
    private String modelYear;

    @JsonProperty("InternationalCovType")
    private InternationalCovType internationalCovType;

    @JsonProperty("AdditionalInterestDetails")
    private AdditionalInterestDetails additionalInterestDetails;



    public AdditionalInterestDetails getAdditionalInterestDetails() {

        AdditionalInterestDetails newAdditionalInterestDetails=  new AdditionalInterestDetails();
        List<AdditionalInterestDetail> newAdditionalInterestDetail=  new ArrayList<AdditionalInterestDetail>();
        Contact newContact = new Contact();
        //TaxID newTaxID = new TaxID();
        // newAdditionalInterestDetail = new ArrayList<>();
        if(additionalInterestDetails!=null){
            return  additionalInterestDetails;
        }else{

            newAdditionalInterestDetails.setAdditionalInterestDetai(newAdditionalInterestDetail);

           // newAdditionalInterestDetail.setContact(newContact);
            newAdditionalInterestDetails.getAdditionalInterestDetail();

            additionalInterestDetails= newAdditionalInterestDetails;
           // System.out.println("NEW VAR: "+additionalInterestDetails);
            return additionalInterestDetails;
        }
       // return additionalInterestDetails;
    }

    public void setAdditionalInterestDetails(AdditionalInterestDetails additionalInterestDetails) {
        this.additionalInterestDetails = additionalInterestDetails;
    }
    public InternationalCovType getInternationalCovType() {
        return internationalCovType;
    }
    public void setInternationalCovType(InternationalCovType internationalCovType) {
        this.internationalCovType = internationalCovType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }
    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getEngineNumber() {
        return engineNumber;
    }
    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getModelYear() {
        return modelYear;
    }
    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getMakeName() {
        return makeName;
    }
    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }


}
