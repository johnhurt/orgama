package org.orgama.server;

import com.google.appengine.api.utils.SystemProperty;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * Provides information about the context in which the application/request is
 * running.
 * @author kguthrie
 */
public final class Orgama {

	private static Orgama instance = null;
	
	private static Provider<HttpServletRequest> requestProvider;
	private static IResourceLoader resourceLoader;

	@Inject
	public Orgama(Provider<HttpServletRequest> requestProvider, 
			IResourceLoader resourceLoader) {
		
		Orgama.requestProvider = requestProvider;
		Orgama.resourceLoader = resourceLoader;
	}
	
	/**
	 * returns the base domain of the application based on the current request.
	 * @return 
	 */
	public static String getDomain() {
		HttpServletRequest request = instance.requestProvider.get();
		String requestUrl = request.getRequestURL().toString();
		String requestUri = request.getRequestURI();
		String result = requestUrl.replace(requestUri, "");
		return result;
	}
	
	/**
	 * Returns true if the application is running in the production environment
	 * @return 
	 */
	public static boolean isProduction() {
		return SystemProperty.environment.value() == 
				SystemProperty.Environment.Value.Production;
	}
	
	/**
	 * Load the file with the given path from within the classpath (to be 
	 * specific this is using the classpath of the web application and not that
	 * of the webserver).  
	 * @param file
	 * @param properties
	 * @return
	 * @throws Exception 
	 */
	public static String loadClassPathFile(String file) {
		return loadClassPathFile(file, null);
	}
	
	/**
	 * Load the file with the given path from within the classpath (to be 
	 * specific this is using the classpath of the web application and not that
	 * of the webserver).  Once the content of the file is loaded, the 
	 * properties map is used to replace tokens like ${name} with the value 
	 * for the entry "value" in the map
	 * @param file
	 * @param properties
	 * @return content or null if the file is not found or there is an error
	 * @throws Exception 
	 */
	public static String loadClassPathFile(
			String file, Map<String, String> properties) {
		return resourceLoader.loadFromClassPathFile(file, properties);
	}
	
	/**
	 * Load the given file from the root of the application as a String.  After
	 * the contents are loaded, apply property replacement from ${name} to
	 * value
	 * @param file
	 * @param properties
	 * @return file contents or null if file is not found or error
	 */
	public static String loadFileFromAppRoot(String file) {
		return loadFileFromAppRoot(file, null);
	}
	
	/**
	 * Load the given file from the root of the application as a String.  After
	 * the contents are loaded, apply property replacement from ${name} to
	 * value
	 * @param file
	 * @param properties
	 * @return 
	 */
	public static String loadFileFromAppRoot(String file, 
			Map<String, String> properties) {
		return resourceLoader.loadFromAppRootFile(file, properties);
	}

		
	/**
	 * Load the content from the given url.  
	 * @param url
	 * @param properties
	 * @return 
	 */
	public static String loadFromUrl(String url) {
		return loadFromUrl(url, null);
	}

	
	/**
	 * Load the content from the given url.  
	 * After the contents are loaded, apply property replacement from ${name} to
	 * value
	 * @param url
	 * @param properties
	 * @return 
	 */
	public static String loadFromUrl(String url, 
			Map<String, String> properties) {
		return resourceLoader.loadFromUrl(url, properties);
	}

	
}
