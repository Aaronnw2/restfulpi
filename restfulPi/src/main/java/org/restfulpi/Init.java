package org.restfulpi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Init {

	private static final Logger log = LogManager.getLogger();
	private static final PropertiesReader props = new PropertiesReader();

	private static final String PORT_PROPERTY_NAME = "port";
	private static final String ENDPOINTS_PACKAGE_VALUE = "org.restfulpi.endpoint";
	private static final String JERSEY_PROVIDER_PACKAGE_PROPERTY = "jersey.config.server.provider.packages";
	private static final String CORS_FILTER_PROPERTY = "com.sun.jersey.spi.container.ContainerResponseFilters";
	private static final String CORS_FILTER_VALUE = "org.webservice.endpoints.security.ResponseCORSFilter";
	private static final String JSON_MAPPING_PROPERTY = "com.sun.jersey.api.json.POJOMappingFeature";
	private static final String JSON_MAPPING_VALUE = "true";
	private static final String RESOURCE_BASE = "web/";
	private static final String WEB_CONTEXT_PATH = "/*";
	private static final String API_CONTEXT_PATH = "/api/*";

	public static void main(String[] args) {
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		Server jettyServer = null;
		
		try {
			jettyServer = new Server(Integer.parseInt(props.getProperty(PORT_PROPERTY_NAME)));
			jettyServer.setHandler(context);

			ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, API_CONTEXT_PATH);
			jerseyServlet.setInitOrder(0);
			jerseyServlet.setInitParameter(JERSEY_PROVIDER_PACKAGE_PROPERTY, ENDPOINTS_PACKAGE_VALUE);
			jerseyServlet.setInitParameter(CORS_FILTER_PROPERTY, CORS_FILTER_VALUE);
			jerseyServlet.setInitParameter(JSON_MAPPING_PROPERTY, JSON_MAPPING_VALUE);

			context.addServlet(DefaultServlet.class, WEB_CONTEXT_PATH);
			context.setResourceBase(RESOURCE_BASE);

			jettyServer.start();
			jettyServer.join();
		} catch (Exception e) {
			log.error("Error in class: " + e.getMessage(), e);
		} finally {
			jettyServer.destroy();
		}
	}

}
