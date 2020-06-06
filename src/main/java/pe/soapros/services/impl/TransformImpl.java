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
		
		URI url = new URI(rutaXML);
		
		String path = url.resolve(".").toString();
		
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		File xml = new File(rutaXML);
		
		try {
			Document document = xmlMapper.readValue(xml, Document.class);
		
			// hacer la transformación usando el bean ya cargado
			document.getContent().getPolicyPeriod().getaAHLine().getaAHPartys().generateNominados();
			document.getContent().getPolicyPeriod().sumAlllParties();

			
			xmlMapper.writeValue(new File(path + "\\resultante.xml"), document);
			
			System.out.println(document);		
			
			
		} catch (Exception e) {
			log.error(e);
			throw e;
		}

	}

}
