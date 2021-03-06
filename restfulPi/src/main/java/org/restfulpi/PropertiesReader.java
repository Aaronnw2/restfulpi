package org.restfulpi;

import static java.lang.String.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertiesReader {

	public static final String BASIC_AUTH_PROPERTY_NAME = "basic_auth_on";
	public static final String WEB_DIRECTORY_PROPERTY_NAME = "web_directory";
	public static final String SSL_PROPERTY_NAME = "ssl_on";
	public static final String CORS_HEADERS_PROPERTY_NAME = "CORS_headers";
	public static final String INPUT_PINS_PROPERTY_NAME = "input_pins";
	public static final String OUTPUT_PINS_PROPERTY_NAME = "output_pins";
	public static final String DEFAULT_PORT = "8080";
	public static final String DEFAULT_PORT_PROPERTY_NAME = "port";
	public static final String AUTH_REALM_PROPERTIES_PROPERTY_NAME = "auth_realm_properties";
	public static final String SSL_KEYSTORE_FILE_AND_PATH_PROPERTY = "keystore";
	public static final String SSL_KEYSTORE_PASSWORD_PROPERTY = "keystore_password";
	
	private static final String EMPTY_STRING = "";
	private static final String FALSE_STRING = "false";
	private static final String REST_PROPERTIES_SYSTEM_OPTION = "restProperties";
	private static final String DEFAULT_FILE_LOCATION = "/home/pi/rest.properties";
	
	private Properties properties;

	private static PropertiesReader PROPERTIES_READER;
	
	private static final Logger log = LogManager.getLogger();

	public static PropertiesReader getInstance() {
		if(PROPERTIES_READER == null) PROPERTIES_READER = new PropertiesReader();
		return PROPERTIES_READER;
	}
	
	protected PropertiesReader() {
		properties = new Properties();
		String javaOption = System.getProperty(REST_PROPERTIES_SYSTEM_OPTION);
		if(isNotNullOrEmpty(javaOption) && fileExists(javaOption)) {
			log.info(format("Loading initial configuration from %s", javaOption));
			loadProperties(javaOption);
		} else if(fileExists(DEFAULT_FILE_LOCATION)) {
			log.info(format("Loading initial configuration from %s", DEFAULT_FILE_LOCATION));	
			loadProperties(DEFAULT_FILE_LOCATION);
		} else {
			log.info("Java option for properties file not set, and no default file found. No initial configuration used");
			loadDefaultOptions();
		}
	}

	private void loadDefaultOptions() {
		properties.put(DEFAULT_PORT_PROPERTY_NAME, DEFAULT_PORT);
		properties.put(INPUT_PINS_PROPERTY_NAME, EMPTY_STRING);
		properties.put(OUTPUT_PINS_PROPERTY_NAME, EMPTY_STRING);
		properties.put(WEB_DIRECTORY_PROPERTY_NAME, EMPTY_STRING);
		properties.put(CORS_HEADERS_PROPERTY_NAME, FALSE_STRING);
		properties.put(SSL_PROPERTY_NAME, FALSE_STRING);
		properties.put(BASIC_AUTH_PROPERTY_NAME, FALSE_STRING);
	}

	public String getProperty(String key) {
		String value = properties.getProperty(key);
		if(value != null) return value;
		log.error(format("A value for \"%s\" was not found", key));
		return EMPTY_STRING;
	}

	public Properties getProperties() {
		return properties;
	}

	private void loadProperties(String propFileAndPath) {
		loadDefaultOptions();
		FileInputStream in;
		try {
			in = new FileInputStream(propFileAndPath);
			properties.load(in);
			in.close();
		} catch (FileNotFoundException e) {
			log.error(format("Properties file %s not found.", propFileAndPath), e);
		} catch (IOException e) {
			log.error(format("I/O exception for Properties file %s", propFileAndPath), e);
		}
	}
	
	public void setProperties(Properties inProperties) {
		properties = inProperties;
	}

	private boolean isNotNullOrEmpty(String property) {
		if(property == null) return false;
		if(property.equals(EMPTY_STRING)) return false;
		return true;
	}

	public static boolean fileExists(String fileAndPath) {
		if(fileAndPath.equals("")) return false;
		File f = new File(fileAndPath);
		if(f.exists() && !f.isDirectory()) return true;
		log.debug(format("Properties file %s does not exist", fileAndPath));
		return false;
	}
}
