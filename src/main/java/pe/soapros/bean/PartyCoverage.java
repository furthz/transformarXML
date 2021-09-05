package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JacksonXmlRootElement(localName="PartyCoverage")
public class PartyCoverage implements Comparable<PartyCoverage>{
	
	@JsonProperty("Description")
	private String description;
	
	@JsonProperty("Category")
	private String category;
	
	@JsonProperty("CoverageTerms")
	private CoverageTerms coverageTerms;
	
	@JsonProperty("Code")
	private String code;
	
	@JsonProperty("DetuctibleValue")
	private String detuctibleValue;

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("LimNombre")
	//private String limNombre;
	private List<String> limNombre;

	//private ArrayList<LimNombre> limNombre;
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("LimValorDV")
	private List<String> limValorDV;

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("LimValorDA")
	private List<String> limValorDA;

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("LimValor")
	private List<String> limValor;

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("LimModelo")
	private List<String> limModelo;

	@JsonProperty("DedValor")
	private String dedValor;

	@JsonProperty("DedNombre")
	private String dedNombre;

	@JsonProperty("DedModelo")
	private String dedModelo;

	@JsonProperty("Suma")
	private String suma;

	@JsonProperty("EdadMinCant")
	private String edadMinCant;

	@JsonProperty("EdadMinUnid")
	private String edadMinUnid;

	@JsonProperty("EdadMaxIng")
	private String edadMaxIng;

	@JsonProperty("EdadMaxPerm")
	private String edadMaxPerm;

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("SubNombre")
	private List<String> subNombre;

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("SubValor")
	private List<String> subValor;

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("SubModelo")
	private List<String> subModelo;

	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("SubExcl")
	private List<String> subExcl;


	//@JsonProperty("TipoDocum")
	//private String tipoDocum;

	//private AAHPartys aahpartys;

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public CoverageTerms getCoverageTerms() {
		return coverageTerms;
	}
	public void setCoverageTerms(CoverageTerms coverageTerms) {
		this.coverageTerms = coverageTerms;
	}
	public String getDetuctibleValue() {
		return detuctibleValue;
	}
	public void setDetuctibleValue(String detuctibleValue) {
		this.detuctibleValue = detuctibleValue;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	
	@Override
	public int compareTo(PartyCoverage arg0) {
		return this.code.compareTo(arg0.getCode());
	}
	
	public void calculateDetuctibleValue() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			//System.out.println("coTerm.getModelType().getCode() "+coTerm.getModelType().getCode());
			//System.out.println("coTerm.getDisplayValue() "+coTerm.getDisplayValue());

			String currentDisplayValue = "";

			try {
				coTerm.getDisplayValue();

			} catch (NullPointerException e) {
				currentDisplayValue = null;
			}
			if (currentDisplayValue != null) {
				currentDisplayValue = coTerm.getDisplayValue();
			}
			//System.out.println("--------------------------------------------------");
			//System.out.println(currentDisplayValue.equalsIgnoreCase("0"));
			if(coTerm.getModelType().getCode().equalsIgnoreCase("Deductible") && currentDisplayValue != null && !currentDisplayValue.equalsIgnoreCase("0")) {
				this.detuctibleValue = coTerm.getDisplayValue();
			}
		}
	}


	public void calculateSuma() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			if (coTerm.getModelType().getCode().equalsIgnoreCase("amount_limit_GSS")) {
				if (coTerm.getDisplayAmount() != null) {
					this.suma = coTerm.getDisplayAmount();
				} else {
					coTerm.setDisplayAmount(null);
					//this.suma = coTerm.getDisplayAmount();
				}
			}
		}
	}

	public void calculateEdadMinCant() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			String ValEdadMinCant = "";

			try {
				coTerm.getDisplayValue();

			} catch (NullPointerException e) {
				ValEdadMinCant = null;
			}
			if (ValEdadMinCant != null) {
				ValEdadMinCant = coTerm.getDisplayValue();
			}
			if ((coTerm.getModelType().getCode().equalsIgnoreCase("min_age_limit_GSS"))&& ValEdadMinCant != null) {
				this.edadMinCant = coTerm.getDisplayValue();
			}
		}
	}

	public void calculateEdadMinUnid() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			String ValEdadMinUnid = "";

			try {
				coTerm.getDisplayValue();

			} catch (NullPointerException e) {
				ValEdadMinUnid = null;
			}
			if (ValEdadMinUnid != null) {
				ValEdadMinUnid = coTerm.getDisplayValue();
			}
			if ((coTerm.getModelType().getCode().equalsIgnoreCase("min_age_type_limit_GSS"))&& ValEdadMinUnid != null) {
				this.edadMinUnid = coTerm.getDisplayValue();
			}
		}
	}

	public void calculateEdadMaxIng() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			String ValEdadMaxIng = "";

			try {
				coTerm.getDisplayValue();

			} catch (NullPointerException e) {
				ValEdadMaxIng = null;
			}
			if (ValEdadMaxIng != null) {
				ValEdadMaxIng = coTerm.getDisplayValue();
			}
			if ((coTerm.getModelType().getCode().equalsIgnoreCase("max_age_limit_GSS"))&& ValEdadMaxIng != null) {
				this.edadMaxIng = coTerm.getDisplayValue() + " años";
			}
		}
	}

	public void calculateEdadMaxPerm() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			String ValEdadMaxPerm = "";

			try {
				coTerm.getDisplayValue();

			} catch (NullPointerException e) {
				ValEdadMaxPerm = null;
			}
			if (ValEdadMaxPerm != null) {
				ValEdadMaxPerm = coTerm.getDisplayValue();
			}
			if ((coTerm.getModelType().getCode().equalsIgnoreCase("max_perm_age_limit_GSS"))&& ValEdadMaxPerm != null)  {
				this.edadMaxPerm = coTerm.getDisplayValue() + " años";
			}
		}
	}

	public void calculateDedNombre() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			String currentDedNombre = "";

			try {
				coTerm.getDisplayName();

			} catch (NullPointerException e) {
				currentDedNombre = null;
			}
			if (currentDedNombre != null) {
				//	currentDedNombre = coTerm.getValueType().getCode();
				currentDedNombre = coTerm.getDisplayName();
			}
			if (coTerm.getModelType().getCode().equalsIgnoreCase("Deductible") && currentDedNombre != null && coTerm.getValueType().getCode().equalsIgnoreCase("percent")) {
				this.dedNombre = coTerm.getDisplayName();
			}
		}
	}
	public void calculateDedValor() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {

			String currentDedValor = "";

			try {
				coTerm.getDisplayValue();

			} catch (NullPointerException e) {
				currentDedValor = null;
			}
			if (currentDedValor != null) {
				currentDedValor	 = coTerm.getDisplayValue();
			}
			if(coTerm.getModelType().getCode().equalsIgnoreCase("Deductible") && currentDedValor != null && coTerm.getValueType().getCode().equalsIgnoreCase("percent")) {
				this.dedValor = coTerm.getDisplayValue();
			}
		}
	}

	public void calculateDedModelo() {

		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			String ValDedModelo = "";

			try {
				coTerm.getAggregationModelList().getAggregationModel().getCode();
			}catch (NullPointerException e) {
				ValDedModelo = null;
			}
			if (ValDedModelo != null) {
				ValDedModelo = coTerm.getAggregationModelList().getAggregationModel().getCode();
			}
			if (coTerm.getModelType().getCode().equalsIgnoreCase("Deductible") && coTerm.getValueType().getCode().equalsIgnoreCase("percent")&&ValDedModelo != null) {
				if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("pc")) {
					this.dedModelo = "por cada siniestro";
				} else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("ag")) {
					this.dedModelo = "por vigencia";
				} else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("pd_GSS")) {
					this.dedModelo = "por día";
				}  else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("ppt_GSS")) {
					this.dedModelo = "por vigencia por póliza";
				}
			}
		}

	}

	public void calculateLimNombre() {
		//newCoverageTerm = new ArrayList<CoverageTerm>();
		ArrayList<String> newlimNombre;
		newlimNombre = new ArrayList<String>();
		for (CoverageTerm coTerm: coverageTerms.getCoverageTerm()) {
			String valLimNombre = "";

			try {
				coTerm.getDisplayName();

			} catch (NullPointerException e) {
				valLimNombre = null;
			}
			if (valLimNombre != null) {
				valLimNombre = coTerm.getDisplayName();
			}
			if(coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && valLimNombre != null&& !coTerm.getValueType().getCode().equalsIgnoreCase("percent")) {
				//this.limNombre = coTerm.getDisplayName();
				newlimNombre.add(coTerm.getDisplayName());
				//arrayList1 = coTerm.getDisplayName();
			}else{newlimNombre.add("");}

		}

		this.limNombre = newlimNombre;
	}


	public void calculateLimValorDV() {
		ArrayList<String> newlimValorDV;
		newlimValorDV = new ArrayList<String>();
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			String valLimValorDV = "";

			try {
				coTerm.getDisplayValue();

			} catch (NullPointerException e) {
				valLimValorDV = null;
			}
			if (valLimValorDV != null) {
				valLimValorDV = coTerm.getDisplayValue();
			}
			if(coTerm.getModelType().getCode().equalsIgnoreCase("Limit")&& valLimValorDV != null && !coTerm.getValueType().getCode().equalsIgnoreCase("percent")) {
				newlimValorDV.add(coTerm.getDisplayValue());
			}else{newlimValorDV.add("");}
		}
		this.limValorDV = newlimValorDV;
	}

	public void calculateLimValorDA() {
		ArrayList<String> newlimValorDA;
		newlimValorDA = new ArrayList<String>();
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			String valLimValorDA = "";

			try {
				coTerm.getDisplayAmount();

			} catch (NullPointerException e) {
				valLimValorDA = null;
			}
			if (valLimValorDA != null) {
				valLimValorDA = coTerm.getDisplayAmount();
			}

			if(coTerm.getModelType().getCode().equalsIgnoreCase("Limit")&& valLimValorDA != null && !coTerm.getValueType().getCode().equalsIgnoreCase("percent")) {
				newlimValorDA.add(coTerm.getDisplayAmount());
			}else{newlimValorDA.add("");}
		}
		this.limValorDA = newlimValorDA;
	}

	public void calculateLimValor() {
		ArrayList<String> newlimValor;
		newlimValor = new ArrayList<String>();
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			if (coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && !coTerm.getValueType().getCode().equalsIgnoreCase("percent") && coTerm.getValueType().getCode().equalsIgnoreCase("money")) {
				//this.limValor = limValorDA;
				newlimValor.add(coTerm.getDisplayAmount());
			} //else if (limValor != limValorDA){
				else {
				//	this.limValor = limValorDV;
				newlimValor.add(coTerm.getDisplayValue());
			}
		}
		this.limValor = newlimValor;
	}


	public void calculateLimModelo() {
		/*
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			if (coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && !coTerm.getValueType().getCode().equalsIgnoreCase("percent")) {
				if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("pc")) {
					this.limModelo = " por cada siniestro";
				} else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("ag")) {
					this.limModelo = " por vigencia";
				} else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("pd_GSS")) {
					this.limModelo = " por día";
				}  else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("ppt_GSS")) {
					this.limModelo = " por vigencia por póliza";
				}
			}
		}
		 */
		ArrayList<String> newlimModelo;
		newlimModelo = new ArrayList<String>();

		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			String ValLimModelo = "";

			try {
				coTerm.getAggregationModelList().getAggregationModel().getCode();
			}catch (NullPointerException e) {
				ValLimModelo = null;
			}
			if (ValLimModelo != null) {
				ValLimModelo = coTerm.getAggregationModelList().getAggregationModel().getCode();
			}
			String ValSubValor = "";

			try {
				coTerm.getDisplayValue();
			}catch (NullPointerException e) {
				ValSubValor = null;
			}
			if (ValSubValor != null) {
				ValSubValor = coTerm.getDisplayValue();
			}
			if (coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && !coTerm.getValueType().getCode().equalsIgnoreCase("percent")&&ValLimModelo!=null&& ((!code.equalsIgnoreCase("AAHPharmaceuticalMedicalAssistanceCov")&&ValSubValor != null)||(code.equalsIgnoreCase("AAHPharmaceuticalMedicalAssistanceCov")&&ValSubValor != null))&& ((!code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")&&ValSubValor != null)||(code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")&&ValSubValor != null)) ) {
				if (ValLimModelo.equalsIgnoreCase("pc")) {
					//this.limModelo = "por cada siniestro";
					newlimModelo.add("por cada siniestro");
				} else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("ag")) {
				//	this.limModelo = "por vigencia";
					newlimModelo.add("por vigencia");
				} else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("pd_GSS")) {
				//	this.limModelo = "por día";
					newlimModelo.add("por día");
				} else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("ppt_GSS")) {
				//	this.limModelo = "por vigencia por póliza";
					newlimModelo.add("por vigencia por póliza");
				}else{newlimModelo.add("");}
			}
		}
		this.limModelo = newlimModelo;
	}

	public void calculateSubNombre() {
		ArrayList<String> newSubNombre;
		newSubNombre = new ArrayList<String>();
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {

			String currentDisplayName = "";

			try {
				coTerm.getDisplayName();
			}catch (NullPointerException e) {
				currentDisplayName = null;
			}
			if (currentDisplayName != null) {
				currentDisplayName = coTerm.getDisplayName();
			}
			String ValSubValor = "";

			try {
				coTerm.getDisplayValue();
			}catch (NullPointerException e) {
				ValSubValor = null;
			}
			if (ValSubValor != null) {
				ValSubValor = coTerm.getDisplayValue();
			}
			if (coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && coTerm.getValueType().getCode().equalsIgnoreCase("percent") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHAdditionalOfDeathDueTrafficAccidentRateLim")&& !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHDeathOfTheSpouseByAccidentRateLim")&& currentDisplayName != null&& ((!code.equalsIgnoreCase("AAHPharmaceuticalMedicalAssistanceCov")&&((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%"))))||(code.equalsIgnoreCase("AAHPharmaceuticalMedicalAssistanceCov")&&((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%")))))&& ((!code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")&&((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%"))))||(code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")&&((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%")))))) {
			//	System.out.println(coTerm.getDisplayName() + "OKIIII");
				//this.subNombre = coTerm.getDisplayName();
				newSubNombre.add(coTerm.getDisplayName());
			}else{
			//	System.out.println("rufu");
			//	this.subNombre ="";
				newSubNombre.add("");} /*else if ( currentDisplayName != null && currentDisplayValue.equalsIgnoreCase("") || currentDisplayValue != null && currentDisplayValue.equalsIgnoreCase("0%") || currentDisplayValue != null && currentDisplayValue.equalsIgnoreCase("100%") ) {
					if (code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")) {
						this.subNombre = "";
					}
			}*/

		}
		this.subNombre = newSubNombre;
	}

	public void calculateSubValor() {
		ArrayList<String> newSubValor;
		newSubValor = new ArrayList<String>();
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			String ValSubValor = "";

			try {
				coTerm.getDisplayValue();
			}catch (NullPointerException e) {
				ValSubValor = null;
			}
			if (ValSubValor != null) {
				ValSubValor = coTerm.getDisplayValue();
			}
			if (coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && coTerm.getValueType().getCode().equalsIgnoreCase("percent") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHAdditionalOfDeathDueTrafficAccidentRateLim") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHDeathOfTheSpouseByAccidentRateLim")&& ((!code.equalsIgnoreCase("AAHPharmaceuticalMedicalAssistanceCov")&&((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%"))))||(code.equalsIgnoreCase("AAHPharmaceuticalMedicalAssistanceCov")&&((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%")))))&& ((!code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")&&((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%"))))||(code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")&&((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%")))))) {
				//this.subValor = coTerm.getDisplayValue();
				newSubValor.add(coTerm.getDisplayValue());
			}else {
				//this.subValor = "";
				newSubValor.add("");
			} /*else if (!code.equalsIgnoreCase("AAHPharmaceuticalMedicalAssistanceCov")) {
				this.subValor = "";
			}*/
		}
		this.subValor = newSubValor;
	}

	public void calculateSubModelo() {
		ArrayList<String> newSubModelo;
		newSubModelo = new ArrayList<String>();
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			/*String currentDisplayValue = "";

			try {
				coTerm.getDisplayValue();
			}catch (NullPointerException e) {
				currentDisplayValue = null;
			}
			if (currentDisplayValue != null) {
				currentDisplayValue = coTerm.getDisplayValue();
			}*/
			String ValSubModelo = "";

			try {
				coTerm.getAggregationModelList().getAggregationModel().getCode();
			}catch (NullPointerException e) {
				ValSubModelo = null;
			}
			if (ValSubModelo != null) {
				ValSubModelo = coTerm.getAggregationModelList().getAggregationModel().getCode();
			}
			String ValSubValor = "";

			try {
				coTerm.getDisplayValue();
			}catch (NullPointerException e) {
				ValSubValor = null;
			}
			if (ValSubValor != null) {
				ValSubValor = coTerm.getDisplayValue();
			}
			if (coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && coTerm.getValueType().getCode().equalsIgnoreCase("percent") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHAdditionalOfDeathDueTrafficAccidentRateLim") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHDeathOfTheSpouseByAccidentRateLim")&&ValSubModelo!=null && ((!code.equalsIgnoreCase("AAHPharmaceuticalMedicalAssistanceCov")&&((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%"))))||(code.equalsIgnoreCase("AAHPharmaceuticalMedicalAssistanceCov")&&((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%")))))&& ((!code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")&&((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%"))))||(code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")&&((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%"))))||((!code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov"))&& ValSubValor==null))) {
				if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("pc")) {
					//this.subModelo = "por cada siniestro";
					newSubModelo.add("por cada siniestro");
				} else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("ag")) {
					//this.subModelo = "por vigencia";
					newSubModelo.add("por vigencia");
				} else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("pd_GSS")) {
				//	this.subModelo = "por día";
					newSubModelo.add("por día");
				}  else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("ppt_GSS")) {
					//this.subModelo = "por vigencia por póliza";
					newSubModelo.add("por vigencia por póliza");
				}
			} else{
				//this.subModelo = "";
				newSubModelo.add("");
			}
			/*else if (currentDisplayValue != null && currentDisplayValue.equalsIgnoreCase("") || currentDisplayValue != null && currentDisplayValue.equalsIgnoreCase("0%") || currentDisplayValue != null && currentDisplayValue.equalsIgnoreCase("100%") ) {
				if (code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")) {
					this.subModelo = "";
				}
			}*/
		}
		this.subModelo = newSubModelo;
	}

	public void calculateSubExcl() {
		ArrayList<String> newSubExcl;
		newSubExcl = new ArrayList<String>();
		ArrayList<String> newSubModelo;
		newSubModelo = new ArrayList<String>();
		ArrayList<String> newSubValor;
		newSubValor = new ArrayList<String>();
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			String ValSubExcl = "";

			try {
				coTerm.getAggregationModelList().getAggregationModel().getCode();
			}catch (NullPointerException e) {
				ValSubExcl = null;
			}
			if (ValSubExcl != null) {
				ValSubExcl = coTerm.getAggregationModelList().getAggregationModel().getCode();
			}
			String ValSubValor = "";

			try {
				coTerm.getDisplayValue();
			}catch (NullPointerException e) {
				ValSubValor = null;
			}
			if (ValSubValor != null) {
				ValSubValor = coTerm.getDisplayValue();
			}
			if (coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && coTerm.getValueType().getCode().equalsIgnoreCase("percent") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHAdditionalOfDeathDueTrafficAccidentRateLim") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHDeathOfTheSpouseByAccidentRateLim")&&ValSubExcl != null && ((!code.equalsIgnoreCase("AAHPharmaceuticalMedicalAssistanceCov")&&((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%"))))||(code.equalsIgnoreCase("AAHPharmaceuticalMedicalAssistanceCov")&&((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%")))) && ((!code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")&&(((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%")))||(ValSubValor == null)))||(code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")&&((ValSubValor != null)&&(!coTerm.getDisplayValue().equalsIgnoreCase("0%"))&&(!coTerm.getDisplayValue().equalsIgnoreCase("100%"))))||((!code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov"))&& ValSubValor==null)))) {
			//	this.subExcl = coTerm.getAggregationModelList().getAggregationModel().getCode();
				newSubExcl.add(coTerm.getAggregationModelList().getAggregationModel().getCode());
			} else {if (code.equalsIgnoreCase("AAHPharmaceuticalMedicalAssistanceCov") && (coTerm.getDisplayValue()==null || coTerm.getDisplayValue().equalsIgnoreCase("0%"))) {
				System.out.println("EXCELLL ++++++");
			//	if (coTerm.getDisplayValue()==null || coTerm.getDisplayValue().equalsIgnoreCase("0%")) {
					System.out.println("POOOOLLL ++++++");
			//	this.subModelo = "";
					newSubModelo.add("");
				//this.subValor = "";
					newSubValor.add("");
				//this.subExcl = "se excluyen";
				newSubExcl.add("se excluyen");
				//}
			}else{
				//this.subExcl = "";
				newSubExcl.add("");
				} }/*else if (code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")) {
				if (coTerm.getDisplayValue().equalsIgnoreCase("") || coTerm.getDisplayValue().equalsIgnoreCase("0%") || coTerm.getDisplayValue().equalsIgnoreCase("100%") ) {
					this.subExcl = "";
				}
			}*/
		}
		this.subExcl = newSubExcl;
		if((newSubValor==null)&&(newSubModelo==null)){
			this.subValor = newSubValor;
			this.subModelo = newSubModelo;
		}
	}


/*
	public void calculateTipoDocum() {
		System.out.println("calculateTipoDocum");
		System.out.println(aahpartys.getAahpartys());
		try {
			for (AAHParty party : aahpartys.getAahpartys()) {
				System.out.println(party);
				for (OfficialID partyCov : party.getCoveredPerson().getOfficialIDs().getOfficialID()) {
					//partyCov.getPrimary();
					if (partyCov.getPrimary().equalsIgnoreCase("true")) {
						System.out.println(1);
						this.tipoDocum = partyCov.getType().getUnlocalizedName();
					} else {
						System.out.println(2);
						this.tipoDocum = "";
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
*/
	/*
		System.out.println("calculateTipoDocum");
		OfficialID officialID = new OfficialID();
		System.out.println(officialID.getPrimary());
		//try {
			if (officialID.getPrimary().equalsIgnoreCase("true")) {
				System.out.println(1);
				this.tipoDocum = officialID.getType().getUnlocalizedName();
			} else {
				System.out.println(2);
				this.tipoDocum = "";
			}
		//} catch(Exception e) {
			//System.out.println(e);
		//}
	}
	 */

}
