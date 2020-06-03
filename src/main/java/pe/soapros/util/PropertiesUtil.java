package pe.soapros.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import pe.soapros.exception.PropertyException;

public class PropertiesUtil {

	/**
	 * LOG
	 */
	private static final Logger LOG = Logger.getLogger(PropertiesUtil.class.getName());

	/**
	 * Constant for default name each environment
	 */
	private static final String DEFAULT_PROPERTY_FILE = "/configuration.properties";

	/**
	 * att file properties loaded
	 */
	private final Properties propertiesLoaded;

	/**
	 * Instance class PropertiesUtil
	 */
	private static PropertiesUtil instance;

	/**
	 * Get instance class PropertiesUtil
	 * 
	 * @return PropertiesUtil
	 * 
	 * @throws PropertyException
	 * 
	 */
	public static final PropertiesUtil getInstance() throws PropertyException {
		if (instance == null) {
			instance = new PropertiesUtil();
		}

		return instance;
	}

	/**
	 * Get instance PropertiesUtil
	 * 
	 * @param propertyFile
	 * 
	 * @return PropertiesUtil
	 * 
	 * @throws PropertyException
	 */
	public static PropertiesUtil getInstance(final String propertyFile) throws PropertyException {
		if (instance == null) {
			instance = new PropertiesUtil(propertyFile);
		}

		return instance;
	}

	/**
	 * Constructor default
	 * 
	 * @throws PropertyException
	 */
	private PropertiesUtil() throws PropertyException {
		this.propertiesLoaded = this.loadPropertiesFile(null);
	}

	/**
	 * Constructor by name file
	 * 
	 * @param propertyFile String
	 * 
	 * @throws PropertyException
	 */
	private PropertiesUtil(final String propertyFile) throws PropertyException {
		this.propertiesLoaded = this.loadPropertiesFile(propertyFile);
	}

	/**
	 * Load properties file informed by argument . If not informed no file name will
	 * be load default file of according to the environment informed via the maven
	 * profiel during the build
	 * 
	 * @param propertyFileName
	 * 
	 * @return Properties
	 * 
	 * @throws PropertyException
	 */
	private Properties loadPropertiesFile(final String propertyFileName) throws PropertyException {
		LOG.info("Load file properties");

		final Properties prop = new Properties();
		InputStream input = null;

		try {
			if (StringUtils.isNotBlank(propertyFileName)) {
				input = new FileInputStream(propertyFileName);
			} else {
				input = getClass().getResourceAsStream(DEFAULT_PROPERTY_FILE);
			}

			prop.load(input);
		} catch (FileNotFoundException e) {
			LOG.error("", e);
			throw new PropertyException(e);
		} catch (IOException ex) {
			LOG.error("", ex);
			throw new PropertyException(ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOG.error("", e);
					throw new PropertyException(e);
				}
			}
		}

		return prop;
	}

	/**
	 * Get property in the file for the key informed by argument
	 * 
	 * @param propertyName
	 * 
	 * @return String
	 * 
	 * @throws PropertyException
	 */
	public String getProperty(final String propertyName) throws PropertyException {
		if (StringUtils.isBlank(propertyName)) {
			return null;
		}

		try {
			return this.propertiesLoaded.getProperty(propertyName);
		} catch (Exception e) {
			LOG.error("", e);
			throw new PropertyException(e);
		}
	}

	/**
	 * Obtains property in the file for key informed by argument and replace all
	 * occurrences of dynamic content '{0}{1}' etc for values contained in the array
	 * 
	 * @param propertyName
	 * 
	 * @param arrayCustomMessages
	 * 
	 * @return String
	 * 
	 * @throws PropertyException
	 */
	public String getPropertyWithCustomMessages(final String propertyName, final String[] arrayCustomMessages)
			throws PropertyException {
		if (StringUtils.isBlank(propertyName)) {
			return null;
		}

		try {
			final String value = this.propertiesLoaded.getProperty(propertyName);

			if (StringUtils.isNotBlank(value) && arrayCustomMessages != null && arrayCustomMessages.length > 0) {
				return new MessageFormat(value).format(arrayCustomMessages);
			}

			return value;
		} catch (Exception e) {
			LOG.error("", e);
			throw new PropertyException(e);
		}
	}

}
