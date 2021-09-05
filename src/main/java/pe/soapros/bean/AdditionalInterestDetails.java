package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName="AdditionalInterestDetails")
public class AdditionalInterestDetails {

    @JsonProperty("AdditionalInterestDetail")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<AdditionalInterestDetail> additionalInterestDetail;

    public List<AdditionalInterestDetail> getAdditionalInterestDetail() {
        AdditionalInterestDetail newAdditionalInterestDetail=  new AdditionalInterestDetail();
        Contact newContact = new Contact();
       //TaxID newTaxID = new TaxID();
       // newAdditionalInterestDetail = new ArrayList<>();
        if(additionalInterestDetail!=null){
            return  additionalInterestDetail;
        }else{
         newAdditionalInterestDetail.setContact(newContact);
           additionalInterestDetail.add(newAdditionalInterestDetail);
      return additionalInterestDetail;
    }
    }

    public void setAdditionalInterestDetail(List<AdditionalInterestDetail> additionalInterestDetail) {
      //  this.additionalInterestDetail = additionalInterestDetail;

        AdditionalInterestDetail newAdditionalInterestDetail=  new AdditionalInterestDetail();
        Contact newContact = new Contact();

     //  if( PrivatePassenger.additionalInterestDetails==null){ }else{}
       // additionalInterestDetails = new PrivatePassenger();

       // newPrivatePassenger.getAdditionalInterestDetails();
      //  System.out.println("VAL: " + PrivatePassenger.additionalInterestDetails);
        //TaxID newTaxID = new TaxID();
        // newAdditionalInterestDetail = new ArrayList<>();
        if(additionalInterestDetail!=null){
         /*   if(AdditionalInterestDetails==null){
                System.out.println("ENTRO BIEN");
                newAdditionalInterestDetail.setContact(newContact);
                additionalInterestDetail.add(newAdditionalInterestDetail);
                this.additionalInterestDetail = additionalInterestDetail;
            }else{

                this.additionalInterestDetail = additionalInterestDetail;} */
         /*   if( PrivatePassenger.additionalInterestDetails==null){newAdditionalInterestDetail.setContact(newContact);
                additionalInterestDetail.add(newAdditionalInterestDetail);
                System.out.println("ENtro : " +additionalInterestDetail);
                this.additionalInterestDetail = additionalInterestDetail;
            }else{ this.additionalInterestDetail = additionalInterestDetail;} */

        //    this.additionalInterestDetail = additionalInterestDetail;

          /*  newAdditionalInterestDetail.setContact(newContact);
            additionalInterestDetail.add(newAdditionalInterestDetail);
            System.out.println("ENtro : " +additionalInterestDetail); */
            this.additionalInterestDetail = additionalInterestDetail;

        }else{
            System.out.println("ENTRO BIEN");
            newAdditionalInterestDetail.setContact(newContact);
            additionalInterestDetail.add(newAdditionalInterestDetail);
            this.additionalInterestDetail = additionalInterestDetail;
        }



    }

    public void setAdditionalInterestDetai(List<AdditionalInterestDetail> additionalInterestDetail) {
        //  this.additionalInterestDetail = additionalInterestDetail;

        AdditionalInterestDetail newAdditionalInterestDetail=  new AdditionalInterestDetail();
        Contact newContact = new Contact();

        //  if( PrivatePassenger.additionalInterestDetails==null){ }else{}
        // additionalInterestDetails = new PrivatePassenger();

        // newPrivatePassenger.getAdditionalInterestDetails();
        //  System.out.println("VAL: " + PrivatePassenger.additionalInterestDetails);
        //TaxID newTaxID = new TaxID();
        // newAdditionalInterestDetail = new ArrayList<>();
        if(additionalInterestDetail!=null){
          newAdditionalInterestDetail.setContact(newContact);
            additionalInterestDetail.add(newAdditionalInterestDetail);
            System.out.println("ENtro : " +additionalInterestDetail);
            this.additionalInterestDetail = additionalInterestDetail;

        }else{
            System.out.println("ENTRO BIEN");
            newAdditionalInterestDetail.setContact(newContact);
            additionalInterestDetail.add(newAdditionalInterestDetail);
            this.additionalInterestDetail = additionalInterestDetail;
        }



    }

}
