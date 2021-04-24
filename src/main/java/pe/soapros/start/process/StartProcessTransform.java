package pe.soapros.start.process;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import pe.soapros.log.Log;

public class StartProcessTransform {

	private static final Log log = Log.getInstance(StartProcessTransform.class);
	
	public static void main(String[] args) {
		log.debug("Se inicia la transformación del XML");
		
		final String[] batchJobConfig = { "/spring/batch/config/context.xml", "/spring/batch/jobs/job-report.xml" };
		final ApplicationContext context = new ClassPathXmlApplicationContext(batchJobConfig);
		
		try {
			
			final JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
			
			final Job job = (Job) context.getBean("transformXmlJob");
			
			final Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
					
			parameters.put("inputXML", new JobParameter("D:\\Workspace\\java_apps\\transformarXML\\src\\main\\resources\\aah_quotation_colectiva_GSS-0007135665.xml"));
			
			final JobExecution jobExecution = jobLauncher.run(job, new JobParameters(parameters));
			
			log.info("Exit Status: " + jobExecution.getStatus());
			
			
		} catch (Exception e) {
			log.error("Error during execution batch", e);
			log.info("Exit code: ");
		} finally {
			// fecha recursos
			if (context != null) {
				((ConfigurableApplicationContext) context).close();
			}

			System.exit(0);
		}

		
	}
	
}
