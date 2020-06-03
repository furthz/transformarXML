package pe.soapros.log;

import org.apache.log4j.Logger;

public class Log extends Logger{

	/**
	 * LOG4J.
	 */
	public static Logger LOG4j = null;
	
	/**
	 * Prefix in the message log
	 */
	public static final String PREFIX_LOG_PB = "Stand Alone process - ";
	
	/**
	 * instancia.
	 */
	private static Log instance;

	/**
	 * Contrutor.
	 * 
	 * @param name
	 * 
	 */
	protected Log(final String name) {
		super(name);
		LOG4j = Logger.getLogger(name);
	}
	
	public void info(final Object message) {
		LOG4j.info(PREFIX_LOG_PB + message);
	}
	
	public void info(final Object message, final Throwable error) {
		LOG4j.info(PREFIX_LOG_PB + message, error);
	}
	
	public void debug(final Object message) {
		LOG4j.debug(PREFIX_LOG_PB + message);
	}

	public void debug(final Object message, final Throwable t) {
		LOG4j.debug(PREFIX_LOG_PB + message, t);
	}

	public void error(final Object message) {
		LOG4j.error(PREFIX_LOG_PB + message);
	}

	public void error(final Object message, final Throwable t) {
		LOG4j.error(PREFIX_LOG_PB + message, t);
	}
	    
	public void fatal(final Object message) {
		LOG4j.fatal(PREFIX_LOG_PB + message); 
	}

	public void fatal(final Object message, final Throwable t) {
		LOG4j.fatal(PREFIX_LOG_PB + message, t);
	}
	
    public void warn(final Object message) {
    	LOG4j.warn(PREFIX_LOG_PB + message);
    }

    public void warn(final Object message, final Throwable t) {
    	LOG4j.warn(PREFIX_LOG_PB + message, t);
    }

    public void trace(Object message) {
    	LOG4j.trace(PREFIX_LOG_PB + message);
    }

    public void trace(Object message, Throwable t) {
    	LOG4j.trace(PREFIX_LOG_PB + message);
    }

	
	/**
	 * get instance of LogSoaPros.
	 * 
	 * @param name
	 * 
	 * @return LogSoaPros
	 * 
	 */
	public static Log getInstance(final String name) {
		if (instance == null) {
			instance = new Log(name);
		}
		
		return instance;
	}
	
	/**
	 * get instance of LogSoaPros.
	 * 
	 * @param <T>
	 * 
	 * @param name
	 * 
	 * @return LogSoaPros
	 * 
	 */
	public static Log getInstance(@SuppressWarnings("rawtypes") final Class clazz) {
		if (instance == null) {
			instance = new Log(clazz.getName());
		}
		
		return instance;
	}
	
}
