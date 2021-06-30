package pe.soapros.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

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

	@JsonProperty("LimNombre")
	private String limNombre;

	@JsonProperty("DedValor")
	private String dedValor;

	@JsonProperty("LimValorDV")
	private String limValorDV;

	@JsonProperty("LimValorDA")
	private String limValorDA;

	@JsonProperty("LimValor")
	private String limValor;

	@JsonProperty("DedNombre")
	private String dedNombre;

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

	@JsonProperty("DedModelo")
	private String dedModelo;

	@JsonProperty("LimModelo")
	private String limModelo;

	@JsonProperty("SubNombre")
	private String subNombre;

	@JsonProperty("SubValor")
	private String subValor;

	@JsonProperty("SubModelo")
	private String subModelo;

	@JsonProperty("SubExcl")
	private String subExcl;


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

	public void calculateLimNombre() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			// System.out.println("coTerm.getModelType().getCode() "+coTerm.getModelType().getCode());
			// System.out.println("coTerm.getDisplayValue() "+coTerm.getDisplayValue());
				if(coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && !coTerm.getValueType().getCode().equalsIgnoreCase("percent")) {
				this.limNombre = coTerm.getDisplayName();
			}
		}
	}

	public void calculateDedValor() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {

			String currentDedValor = "";

			try {
				coTerm.getValueType().getCode();

			} catch (NullPointerException e) {
				currentDedValor = null;
			}
			if (currentDedValor != null) {
				currentDedValor	 = coTerm.getValueType().getCode();
			}
			//System.out.println("coTerm.getModelType().getCode() "+coTerm.getModelType().getCode());
			//System.out.println("currentDedValor "+currentDedValor);
			//System.out.println("--------------------------------------------------");
			if(coTerm.getModelType().getCode().equalsIgnoreCase("Deductible") && currentDedValor != null && currentDedValor.equalsIgnoreCase("percent")) {
				this.dedValor = coTerm.getDisplayValue();
			}
		}
	}

	public void calculateLimValorDV() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			if(coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && !coTerm.getValueType().getCode().equalsIgnoreCase("percent")) {
				this.limValorDV = coTerm.getDisplayValue();
			}
		}
	}

	public void calculateLimValorDA() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			if(coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && !coTerm.getValueType().getCode().equalsIgnoreCase("percent")) {
				this.limValorDA = coTerm.getDisplayAmount();
			}
		}
	}

	public void calculateLimValor() {
		//try {
			for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
				if (coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && !coTerm.getValueType().getCode().equalsIgnoreCase("percent") && coTerm.getValueType().getCode().equalsIgnoreCase("money")) {
					this.limValor = limValorDA;
				} else if (limValor != limValorDA){
					this.limValor = limValorDV;
				}
			}
		//} catch(Exception e) {
		//	System.out.println(e);
		//}
	}

	public void calculateDedNombre() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			String currentDedNombre = "";

			try {
				coTerm.getValueType().getCode();

			} catch (NullPointerException e) {
				currentDedNombre = null;
			}
			if (currentDedNombre != null) {
				currentDedNombre = coTerm.getValueType().getCode();
			}
			//System.out.println("coTerm.getModelType().getCode() "+coTerm.getModelType().getCode());
			//System.out.println("currentDedValor "+currentDedNombre);
			//System.out.println("--------------------------------------------------");
			if (coTerm.getModelType().getCode().equalsIgnoreCase("Deductible") && currentDedNombre != null && currentDedNombre.equalsIgnoreCase("percent")) {
				this.dedNombre = coTerm.getDisplayName();
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
					this.suma = coTerm.getDisplayAmount();
				}
			}
		}
	}

	public void calculateEdadMinCant() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			if (coTerm.getModelType().getCode().equalsIgnoreCase("min_age_limit_GSS")) {
				this.edadMinCant = coTerm.getDisplayValue();
			}
		}
	}

	public void calculateEdadMinUnid() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			if (coTerm.getModelType().getCode().equalsIgnoreCase("min_age_type_limit_GSS")) {
				this.edadMinUnid = coTerm.getDisplayValue();
			}
		}
	}

	public void calculateEdadMaxIng() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			if (coTerm.getModelType().getCode().equalsIgnoreCase("max_age_limit_GSS")) {
				this.edadMaxIng = coTerm.getDisplayValue() + " años";
			}
		}
	}

	public void calculateEdadMaxPerm() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			if (coTerm.getModelType().getCode().equalsIgnoreCase("max_perm_age_limit_GSS")) {
				this.edadMaxPerm = coTerm.getDisplayValue() + " años";
			}
		}
	}

	public void calculateDedModelo() {

		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			if (coTerm.getModelType().getCode().equalsIgnoreCase("Deductible") && coTerm.getValueType().getCode().equalsIgnoreCase("percent")) {
				if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("pc")) {
					this.dedModelo = " por cada siniestro";
				} else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("ag")) {
					this.dedModelo = " por vigencia";
				} else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("pd_GSS")) {
					this.dedModelo = " por día";
				}  else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("ppt_GSS")) {
					this.dedModelo = " por vigencia por póliza";
				}
			}
		}

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
	}

	public void calculateSubNombre() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			String currentDisplayValue = "";

			try {
				coTerm.getDisplayValue();
			}catch (NullPointerException e) {
				currentDisplayValue = null;
			}
			if (currentDisplayValue != null) {
				currentDisplayValue = coTerm.getDisplayValue();
			}
			//System.out.println("currentDisplayValue "+currentDisplayValue);
			//System.out.println("--------------------------------------------------");
			if (coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && coTerm.getValueType().getCode().equalsIgnoreCase("percent") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHAdditionalOfDeathDueTrafficAccidentRateLim")) {
				this.subNombre = coTerm.getDisplayName();
			} else if ( currentDisplayValue != null && currentDisplayValue.equalsIgnoreCase("") || currentDisplayValue != null && currentDisplayValue.equalsIgnoreCase("0%") || currentDisplayValue != null && currentDisplayValue.equalsIgnoreCase("100%") ) {
					if (code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")) {
						this.subNombre = "";
					}
			}
		}
	}

	public void calculateSubValor() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			if (coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && coTerm.getValueType().getCode().equalsIgnoreCase("percent") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHAdditionalOfDeathDueTrafficAccidentRateLim") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHDeathOfTheSpouseByAccidentRateLim")) {
				this.subValor = coTerm.getDisplayName();
			} else if (!code.equalsIgnoreCase("AAHPharmaceuticalMedicalAssistanceCov")) {
				this.subValor = "";
			}
		}
	}

	public void calculateSubModelo() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			String currentDisplayValue = "";

			try {
				coTerm.getDisplayValue();
			}catch (NullPointerException e) {
				currentDisplayValue = null;
			}
			if (currentDisplayValue != null) {
				currentDisplayValue = coTerm.getDisplayValue();
			}
			//System.out.println("currentDisplayValue "+currentDisplayValue);
			//System.out.println("--------------------------------------------------");

			if (coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && coTerm.getValueType().getCode().equalsIgnoreCase("percent") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHAdditionalOfDeathDueTrafficAccidentRateLim") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHDeathOfTheSpouseByAccidentRateLim")) {
				if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("pc")) {
					this.subModelo = " por cada siniestro";
				} else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("ag")) {
					this.subModelo = " por vigencia";
				} else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("pd_GSS")) {
					this.subModelo = " por día";
				}  else if (coTerm.getAggregationModelList().getAggregationModel().getCode().equalsIgnoreCase("ppt_GSS")) {
					this.subModelo = " por vigencia por póliza";
				}
			} else if (currentDisplayValue != null && currentDisplayValue.equalsIgnoreCase("") || currentDisplayValue != null && currentDisplayValue.equalsIgnoreCase("0%") || currentDisplayValue != null && currentDisplayValue.equalsIgnoreCase("100%") ) {
				if (code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")) {
					this.subModelo = "";
				}
			}
		}
	}

	public void calculateSubExcl() {
		for (CoverageTerm coTerm : coverageTerms.getCoverageTerm()) {
			if (coTerm.getModelType().getCode().equalsIgnoreCase("Limit") && coTerm.getValueType().getCode().equalsIgnoreCase("percent") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHAdditionalOfDeathDueTrafficAccidentRateLim") && !coTerm.getCodeIdentifier().equalsIgnoreCase("AAHDeathOfTheSpouseByAccidentRateLim")) {
				this.subExcl = coTerm.getAggregationModelList().getAggregationModel().getCode();
			} else if (code.equalsIgnoreCase("AAHPharmaceuticalMedicalAssistanceCov")) {
				if (coTerm.getDisplayValue().equalsIgnoreCase("") || coTerm.getDisplayValue().equalsIgnoreCase("0%")) {
				this.subModelo = "";
				this.subValor = "";
				this.subExcl = "se excluyen";
				}
			} else if (code.equalsIgnoreCase("AHRefundExpensesPharmaceuticalMedicalAssistanceCov")) {
				if (coTerm.getDisplayValue().equalsIgnoreCase("") || coTerm.getDisplayValue().equalsIgnoreCase("0%") || coTerm.getDisplayValue().equalsIgnoreCase("100%") ) {
					this.subExcl = "";
				}
			}
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
