/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.config;

import com.google.appengine.api.utils.SystemProperty;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import org.orgama.server.Orgama;

/**
 * this is an abstract class for creating a context listener for Guice.  In
 * order to use guice servlet with this library, you need to extend this class
 * and add the following lines to your web.xml
 *
 * <listener>
 *       <listener-class>
 *           package.of.your.GuiceServletContextListener
 *       </listener-class>
 *   </listener>
 *
 *  <filter>
 *       <filter-name>guiceFilter</filter-name>
 *       <filter-class>com.google.inject.servlet.GuiceFilter</filter-class>
 *   </filter>
 *
 *   <filter-mapping>
 *       <filter-name>guiceFilter</filter-name>
 *       <url-pattern>/*</url-pattern>
 *   </filter-mapping>
 *
 * @author kguthrie
 */
public abstract class BaseServletContextListener
        extends com.google.inject.servlet.GuiceServletContextListener {

    protected static final Logger logger = Logger.getLogger(
            "ContextListener");

	private final List<Module> modules;
	
	
	public BaseServletContextListener() {
		modules = new LinkedList<Module>();
	}
	
    @Override
    protected Injector getInjector() {
		long getInjectorStart = System.currentTimeMillis();
		
		add(new OrgamaServerCoreModule());
		addExtensions();
		
		//Add the development mode extension if this is development mode 
		if (!Orgama.isProduction()) {
			addDevelopmentModeExtension();
		}
		else {
			//otherwise add the production mode extension
			add(new ProductionModeExtension());
		}
		
		long createInjectorStart = System.currentTimeMillis();
		
		Injector result = Guice.createInjector(
				SystemProperty.Environment.Environment
						.environment.value().equals(
								SystemProperty.Environment.Value.Production) ?
										Stage.PRODUCTION :
										Stage.DEVELOPMENT, modules);
		
		long finishTime = System.currentTimeMillis();
		
		logger.info(String.format(
				"Module Gathering:%dms Injector Creation:%dms", 
				createInjectorStart - getInjectorStart, 
				finishTime - createInjectorStart));
		
		return result;
    }

	/**
	 * Called when the server initializes a context
	 * @param servletContextEvent 
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
	    // No call to super as it also calls getInjector()
	    ServletContext sc = servletContextEvent.getServletContext();
		sc.setAttribute(Injector.class.getName(), getInjector());
	}
	
	/**
	 * Called when the server destroys a context
	 * @param servletContextEvent 
	 */
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext sc = servletContextEvent.getServletContext();
        sc.removeAttribute(Injector.class.getName());
		super.contextDestroyed(servletContextEvent); 
	}

	/**
	 * Add a single extension module.  This will add all the individual modules
	 * provided by the extension to the guice servlet injector
	 * @param extensionModule 
	 */
	public void add(OrgamaExtensionModule extension) {
		
		logger.log(Level.INFO, "Adding extension: {0}", 
				extension.getClass().getSimpleName());
		
		if (extension.getServletModule() != null) {
			modules.add(extension.getServletModule());
		}

		if (extension.getHandlerModule() != null) {
			modules.add(extension.getHandlerModule());
		}
		
		List<Module> otherExtensionModules = extension.getOtherModules();

		if (otherExtensionModules != null) {
			modules.addAll(otherExtensionModules);
		}
	}
	
	/**
	 * Use reflection to find the local runtime extension and add an instance of
	 * it to the list of extensions to be loaded
	 */
	private void addDevelopmentModeExtension() {
		try {
			Class<?> devModeExtensionClass = Class.forName(
					"org.orgama.server.config.DevModeExtension");
			add((BaseExtensionModule)devModeExtensionClass.newInstance());
		}
		catch(Exception ex) {
			throw new RuntimeException("Failed to add the development mode " +
					"extension");
		}
	}
	
    /**
     * This is called to add the extension modules
     * @return
     */
    protected abstract void addExtensions();
	
	
}
