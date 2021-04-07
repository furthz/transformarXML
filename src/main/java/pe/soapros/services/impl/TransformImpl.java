package pe.soapros.services.impl;

import java.io.File;
import java.net.URI;
import java.util.Map;


import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

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
		
			// Se deja solo OfficialId con value true
			document.getContent().getPolicyPeriod().getaAHLine().getaAHPartys().descartarOfficialID();
			document.getContent().getPolicyPeriod().getPrimaryNamedInsured().getContact().descartarOfficialID();
			// Se dejan solo los coverageTerms con valores amount_limit_GSS y Detuctible en el nodo Code
			document.getContent().getPolicyPeriod().getaAHLine().getaAHPartys().descartarCoverageTerms();
			// En los casos que se tenga Detuctible y valor distinto a 0, se coloca el valor un par de jerarquias mayores
			document.getContent().getPolicyPeriod().getaAHLine().getaAHPartys().calcularDetuctibleValue();
			// hacer la transformación usando el bean ya cargado
			document.getContent().getPolicyPeriod().getaAHLine().getaAHPartys().generateNominados();
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
