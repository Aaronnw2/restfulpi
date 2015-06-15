package org.restfulpi;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.format;
import static org.restfulpi.PropertiesReader.AUTH_REALM_PROPERTIES_PROPERTY_NAME;
import static org.restfulpi.PropertiesReader.BASIC_AUTH_PROPERTY_NAME;
import static org.restfulpi.PropertiesReader.CORS_HEADERS_PROPERTY_NAME;
import static org.restfulpi.PropertiesReader.DEFAULT_PORT_PROPERTY_NAME;
import static org.restfulpi.PropertiesReader.SSL_KEYSTORE_FILE_AND_PATH_PROPERTY;
import static org.restfulpi.PropertiesReader.SSL_KEYSTORE_PASSWORD_PROPERTY;
import static org.restfulpi.PropertiesReader.SSL_PROPERTY_NAME;
import static org.restfulpi.PropertiesReader.WEB_DIRECTORY_PROPERTY_NAME;
import static org.restfulpi.PropertiesReader.fileExists;
import static org.restfulpi.PropertiesReader.getInstance;

import java.util.Collections;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.servlet.ServletContainer;
import org.restfulpi.gpio.GPIORequestHandler;

public class RestfulPiServer {

	private static final Logger log = LogManager.getLogger();
	private static final PropertiesReader props = getInstance();

	private ServletContextHandler context;
	private Server jettyServer;
	
	private static final String ENDPOINTS_PACKAGE_VALUE = "org.restfulpi.endpoint";
	private static final String JERSEY_PROVIDER_PACKAGE_PROPERTY = "jersey.config.server.provider.packages";
	private static final String CORS_FILTER_PROPERTY = "com.sun.jersey.spi.container.ContainerResponseFilters";
	private static final String CORS_FILTER_VALUE = "org.restfulpi.endpoint.CORSResponseFilter";
	private static final String JSON_MAPPING_PROPERTY = "com.sun.jersey.api.json.POJOMappingFeature";
	private static final String JSON_MAPPING_VALUE = "true";
	private static final String WEB_CONTEXT_PATH = "/*";
	private static final String API_CONTEXT_PATH = "/pins/*";

	public RestfulPiServer() {
		setupContextAndServer();
	}

	public RestfulPiServer(Properties customProperties) {
		props.setProperties(customProperties);
		setupContextAndServer();
	}
	
	public RestfulPiServer(Properties customProperties, String jerseyClass) {
		//TODO: set the extra api classes / packages. Or maybe just have any other API endpoint classes in the same package?
		props.setProperties(customProperties);
		setupContextAndServer();
	}
	

	public void start() {
		try {
			GPIORequestHandler.getInstance();

			jettyServer.start();
			jettyServer.join();
		} catch (Exception e) {
			log.error(format("Error in server initialization: %d", e.getMessage()), e);
		} finally {
			jettyServer.destroy();
		}
	}
	
	public static void main(String[] args) {
		RestfulPiServer server = new RestfulPiServer();
		server.start();
	}
	
	private void setupContextAndServer() {
		context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		int port = Integer.parseInt(props.getProperty(DEFAULT_PORT_PROPERTY_NAME));

		if(parseBoolean(props.getProperty(SSL_PROPERTY_NAME))) {jettyServer = setSSLConnector(port);}
		else jettyServer = new Server(port);

		if(parseBoolean(props.getProperty(BASIC_AUTH_PROPERTY_NAME))) startServerWithAuth(jettyServer, context);
		else jettyServer.setHandler(context);

		ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, API_CONTEXT_PATH);
		jerseyServlet.setInitOrder(0);
		if(parseBoolean(props.getProperty(CORS_HEADERS_PROPERTY_NAME))) 
			jerseyServlet.setInitParameter(CORS_FILTER_PROPERTY, CORS_FILTER_VALUE);
		jerseyServlet.setInitParameter(JERSEY_PROVIDER_PACKAGE_PROPERTY, ENDPOINTS_PACKAGE_VALUE);
		jerseyServlet.setInitParameter(JSON_MAPPING_PROPERTY, JSON_MAPPING_VALUE);

		context.addServlet(DefaultServlet.class, WEB_CONTEXT_PATH);
		context.setResourceBase(props.getProperty(WEB_DIRECTORY_PROPERTY_NAME));
	}

	private static Server setSSLConnector(int port) {
		String keystorePathAndFile = props.getProperty(SSL_KEYSTORE_FILE_AND_PATH_PROPERTY);
		String keystorePassword = props.getProperty(SSL_KEYSTORE_PASSWORD_PROPERTY);
		if(keystorePathAndFile.equals("") || !fileExists(keystorePathAndFile)) {
			log.error("To use SSL the keystore property must be set, and Keystore file must exist at that location");
			return new Server(port);
		}
		if(keystorePassword.equals("")) {
			log.error("To use SSL the keystore_password property must be set");
			return new Server(port);
		}

		Server server = new Server();
		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme("https");
		http_config.setSecurePort(port);
		http_config.setOutputBufferSize(32768);

		SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStorePath(keystorePathAndFile);
		sslContextFactory.setKeyStorePassword(keystorePassword);
		sslContextFactory.setKeyManagerPassword(keystorePassword);

		HttpConfiguration https_config = new HttpConfiguration(http_config);
		https_config.addCustomizer(new SecureRequestCustomizer());
		ServerConnector https = new ServerConnector(server,
				new SslConnectionFactory(sslContextFactory,HttpVersion.HTTP_1_1.asString()),
				new HttpConnectionFactory(https_config));
		https.setPort(port);
		https.setIdleTimeout(500000);
		server.setConnectors(new Connector[] { https });
		return server;
	}

	private static void startServerWithAuth(Server jettyServer, ServletContextHandler context) {
		String authPropertiesPathAndFile = props.getProperty(AUTH_REALM_PROPERTIES_PROPERTY_NAME);
		if(!fileExists(authPropertiesPathAndFile)) {
			log.error("The property auth_realm_properties must be set in order to use authentication");
			jettyServer.setHandler(context);
			return;
		}
		log.info(format("Loading realm information from %s", authPropertiesPathAndFile));
		LoginService loginService = new HashLoginService("JettyRealm", authPropertiesPathAndFile);
		jettyServer.addBean(loginService);
		ConstraintSecurityHandler security = new ConstraintSecurityHandler();
		jettyServer.setHandler(security);
		Constraint constraint = new Constraint();
		constraint.setName("auth");
		constraint.setAuthenticate(true);
		constraint.setRoles(new String[] { "apiuser" });
		ConstraintMapping mapping = new ConstraintMapping();
		mapping.setPathSpec("/*");
		mapping.setConstraint(constraint);
		security.setConstraintMappings(Collections.singletonList(mapping));
		security.setAuthenticator(new BasicAuthenticator());
		security.setLoginService(loginService);

		security.setHandler(context);
	}
}
