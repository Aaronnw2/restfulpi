package org.restfulpi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertiesReader {

	private static final String DEFAULT_PORT = "8080";
	private static final String DEFAULT_PORT_PROPERTY_NAME = "port";
	private static final String REST_PROPERTIES_NAME = "restProperties";
	private static final String DEFAULT_FILE_LOCATION = "/home/pi/rest.properties";
	private Properties properties;

	private static final Logger log = LogManager.getLogger();

	public PropertiesReader() {
		properties = new Properties();
		String javaOption = System.getProperty(REST_PROPERTIES_NAME);
		if(isNotNullOrEmpty(javaOption) && fileExists(javaOption)) {
			log.debug("Loading initial configuration from " + javaOption);
			loadProperties(javaOption);
		} else if(fileExists(DEFAULT_FILE_LOCATION)) {
			log.debug("Loading initial configuration from " + DEFAULT_FILE_LOCATION);	
			loadProperties(DEFAULT_FILE_LOCATION);
		} else {
			log.debug("Java option not set, and no default file found. No initial configuration used");
			loadDefaultOptions();
		}
	}

	private void loadDefaultOptions() {
		properties.put(DEFAULT_PORT_PROPERTY_NAME, DEFAULT_PORT);
	}

	public String getProperty(String key) throws Exception {
		String value = properties.getProperty(key);
		if(isNotNullOrEmpty(value)) return value;
		else throw new Exception("Property \"" + key + "\" not found");
	}

	public Properties getProperties() {
		return properties;
	}

	private void loadProperties(String propFileAndPath) {
		FileInputStream in;
		try {
			in = new FileInputStream(propFileAndPath);
			properties.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			log.error("Properties file " + propFileAndPath + " not found.", e);
		} catch (IOException e) {
			log.error("I/O exception for Properties file " + propFileAndPath, e);
		}
	}

	private boolean isNotNullOrEmpty(String property) {
		if(property == null) return false;
		if(property.equals("")) return false;
		return true;
	}

	private boolean fileExists(String fileAndPath) {
		File f = new File(fileAndPath);
		if(f.exists() && !f.isDirectory()) return true;
		log.error("Properties file " + fileAndPath + " does not exist");
		return false;
	}
}
