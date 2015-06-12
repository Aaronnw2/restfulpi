package org.restfulpi;

import static java.lang.Boolean.parseBoolean;
import static java.lang.String.format;
import static org.restfulpi.PropertiesReader.AUTH_REALM_PROPERTIES_PROPERTY_NAME;
import static org.restfulpi.PropertiesReader.BASIC_AUTH_PROPERTY_NAME;
import static org.restfulpi.PropertiesReader.CORS_HEADERS_PROPERTY_NAME;
import static org.restfulpi.PropertiesReader.DEFAULT_PORT_PROPERTY_NAME;
import static org.restfulpi.PropertiesReader.WEB_DIRECTORY_PROPERTY_NAME;
import static org.restfulpi.PropertiesReader.fileExists;
import static org.restfulpi.PropertiesReader.getInstance;

import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.glassfish.jersey.servlet.ServletContainer;
import org.restfulpi.gpio.GPIORequestHandler;

public class InitializationController {

	private static final Logger log = LogManager.getLogger();
	private static final PropertiesReader props = getInstance();

	private static final String ENDPOINTS_PACKAGE_VALUE = "org.restfulpi.endpoint";
	private static final String JERSEY_PROVIDER_PACKAGE_PROPERTY = "jersey.config.server.provider.packages";
	private static final String CORS_FILTER_PROPERTY = "com.sun.jersey.spi.container.ContainerResponseFilters";
	private static final String CORS_FILTER_VALUE = "org.webservice.endpoints.security.ResponseCORSFilter";
	private static final String JSON_MAPPING_PROPERTY = "com.sun.jersey.api.json.POJOMappingFeature";
	private static final String JSON_MAPPING_VALUE = "true";
	private static final String WEB_CONTEXT_PATH = "/*";
	private static final String API_CONTEXT_PATH = "/pins/*";

	public static void main(String[] args) {
		//TODO: move this to its own method, and add constructor to use as library
		//TODO: SSL
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		Server jettyServer = null;
		
		try {
			jettyServer = new Server(Integer.parseInt(props.getProperty(DEFAULT_PORT_PROPERTY_NAME)));

			if(parseBoolean(props.getProperty(BASIC_AUTH_PROPERTY_NAME))) startServerWithAuth(jettyServer, context);
			else jettyServer.setHandler(context);
	        
			ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, API_CONTEXT_PATH);
			jerseyServlet.setInitOrder(0);
			if(parseBoolean(props.getProperty(CORS_HEADERS_PROPERTY_NAME))) jerseyServlet.setInitParameter(CORS_FILTER_PROPERTY, CORS_FILTER_VALUE);
			jerseyServlet.setInitParameter(JERSEY_PROVIDER_PACKAGE_PROPERTY, ENDPOINTS_PACKAGE_VALUE);
			jerseyServlet.setInitParameter(JSON_MAPPING_PROPERTY, JSON_MAPPING_VALUE);

			context.addServlet(DefaultServlet.class, WEB_CONTEXT_PATH);
			context.setResourceBase(props.getProperty(WEB_DIRECTORY_PROPERTY_NAME));
			
			GPIORequestHandler.getInstance();
			
			jettyServer.start();
			jettyServer.join();
		} catch (Exception e) {
			log.error(format("Error in server initialization: %d", e.getMessage()), e);
		} finally {
			jettyServer.destroy();
		}
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
