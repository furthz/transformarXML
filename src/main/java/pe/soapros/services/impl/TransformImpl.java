package pe.soapros.services.impl;

import java.io.File;
import java.net.URI;
import java.util.Map;


import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import pe.soapros.constants.Constants;
import pe.soapros.log.Log;
import pe.soapros.services.Transform;
import pe.soapros.bean.Document;

@Component(value = "TransformImpl")
public class TransformImpl implements Transform {

	private static final Log log = Log.getInstance(TransformImpl.class);

	@Override
	public void createParallelProcess(Map<String, Object> parameters) throws Exception {
		log.debug("Creando un proceso concurrente para transformar el xml");

		String rutaXML = String.valueOf(parameters.get("inputXML"));
		System.out.println(rutaXML);
		rutaXML=rutaXML.replace("\\","/");
		
		URI url = new URI(rutaXML);
		
		String path = url.resolve(".").toString();
		System.out.println(path);
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		File xml = new File(rutaXML);
		
		try {
			Document document = xmlMapper.readValue(xml, Document.class);
			System.out.println(xmlMapper.valueToTree(document));
			// Se deja solo OfficialId con value true
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().descartarOfficialID();
			document.getContent().getPolicyPeriod().getPrimaryNamedInsured().getContact().descartarOfficialID();
			// Se dejan solo los coverageTerms con valores amount_limit_GSS y Detuctible en el nodo Code
			//document.getContent().getPolicyPeriod().getaAHLine().getaAHPartys().descartarCoverageTerms();
			// En los casos que se tenga Detuctible y valor distinto a 0, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularDetuctibleValue();

			// En los casos que se tenga Limit y valor igual a percent, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularLimNombre();
			// En los casos que se tenga Detuctible y valor igual a percent, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularDedValor();
			// En los casos que se tenga Limit y valor distinto a percent, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularLimValorDV();
			// En los casos que se tenga Limit y valor distinto a percent, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularLimValorDA();
			// En los casos que se tenga Limit y valor distinto a percent, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularLimValor();
			// En los casos que se tenga Limit y valor distinto a percent, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularDedNombre();
			// En los casos que se tenga amount_limit_GSS, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularSuma();
			// En los casos que se tenga min_age_limit_GSS, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularEdadMinCant();
			// En los casos que se tenga min_age_type_limit_GSS, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularEdadMinUnid();
			// En los casos que se tenga max_age_limit_GSS, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularEdadMaxIng();
			// En los casos que se tenga max_perm_age_limit_GSS, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularEdadMaxPerm();

			// En los casos que se tenga Deductible y valor igual a percent, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularDedModelo();
			// En los casos que se tenga Limit y valor distinto a percent, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularLimModelo();
			// En los casos que se tenga Limit y valor distinto a percent, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularSubNombre();
			// En los casos que se tenga Limit y valor distinto a percent, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularSubValor();
			// En los casos que se tenga Limit y valor distinto a percent, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularSubModelo();
			// En los casos que se tenga Limit y valor distinto a percent, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularSubExcl();
			// En los casos que se tenga Limit y valor distinto a percent, se coloca el valor un par de jerarquias mayores
			// document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calcularTipoDocum();

			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calculateTipoDocum();

			// hacer la transformación usando el bean ya cargado
			document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().generateNominados();

			// Validacion solo para los documentos PC70: "ARG_GW_PC_CotizacionAP_Colectiva"
			if (document.getHeader().getThemeID().equals(Constants.PC70)) {
				System.out.println("DOCUMENTO PC70: "+Constants.PC70 );
				document.getContent().getPolicyPeriod().getAAHLine().getAAHPartys().calculateActiv();
			}
			// Obtener conteo de AAHPartys
			document.getContent().getPolicyPeriod().sumAlllParties();
			
			xmlMapper.writeValue(new File(path + "\\resultante.xml"), document);
			
			System.out.println(document);		
			
			
		} catch (Exception e) {
			log.error(e);
			throw e;
		}

	}

}
