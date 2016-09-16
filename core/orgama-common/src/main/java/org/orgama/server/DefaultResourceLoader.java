package org.orgama.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.orgama.shared.Logger;

/**
 * Default Resource Loader
 * @author kguthrie
 */
public class DefaultResourceLoader implements IResourceLoader {

	private final Provider<HttpServletRequest> requestProvider;
	private ServletContext servletContext;
	
	@Inject
	public DefaultResourceLoader(
			Provider<HttpServletRequest> requestProvider) {
		this.requestProvider = requestProvider;
		servletContext = null;
	}
	
	/**
	 * Load content from a file in the class path
	 * @param file
	 * @param properties
	 * @return 
	 */
	@Override
	public String loadFromClassPathFile(String file, 
			Map<String, String> properties) {
		
		InputStream fileStream;
		ClassLoader classLoader = Orgama.class.getClassLoader();
		
		if (file == null) {
			Logger.error("null file requested from classpath");
			return null;
		}
		
		fileStream = classLoader.getResourceAsStream(file);
		
		if (fileStream == null) {
			Logger.error("File not found in classpath " + file);
			return null;
		}
		
		return loadFromStream(fileStream, properties);
	}

	/**
	 * Load content from a file located in the web application root folder
	 * @param file
	 * @param properties
	 * @return 
	 */
	@Override
	public String loadFromAppRootFile(String file, 
			Map<String, String> properties) {
		InputStream fileStream;
		
		if (file == null) {
			Logger.error("null file requested from app root");
			return null;
		}
		
		if (!file.startsWith("/")) {
			file = "/" + file;
		}
		
		fileStream = getServetContext().getResourceAsStream(file);
		
		if (fileStream == null) {
			Logger.error("File not found in app root " + file);
			return null;
		}
		
		return loadFromStream(fileStream, properties);
	}

	/**
	 * Load the content from the given url
	 * @param url
	 * @param properties
	 * @return 
	 */
	@Override
	public String loadFromUrl(String url, Map<String, String> properties) {
		InputStream urlStream;
		
		try {
			urlStream = new URL(url).openStream();
		}
		catch(Exception ex) {
			Logger.error("Failed to open Stream to url");
			return null;
		}
		
		return loadFromStream(urlStream, properties);
	}

	/**
	 * Get the servlet context for the application
	 * @return 
	 */
	private ServletContext getServetContext() {
		if (servletContext == null) {
			synchronized (this) {
				if (servletContext == null) {
					servletContext = requestProvider.get().getSession()
							.getServletContext();
				}
			}
		}
		
		return servletContext;
	}
	
	
	/**
	 * Load the content in the given stream as a string, and replace tokens
	 * in the properties map
	 * @param stream
	 * @param properties
	 * @return 
	 */
	protected static String loadFromStream(InputStream stream, 
			Map<String, String> properties) {
		
		String result = null;
		
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(stream));
			
			StringBuilder resultBuilder = new StringBuilder();

			String sLine;

			int line = 0;

			while ((sLine = br.readLine()) != null) {
				if (line++ > 0) {
					resultBuilder.append("\n");
				}
				resultBuilder.append(sLine);
			}

			br.close();
			
			result = resultBuilder.toString();
		}
		catch(Exception ex) {
			Logger.error("Failed to get contents of file stream", ex);
			return null;
		}
		
		return applyProperties(result, properties);
	}
	
	/**
	 * find and replace properties in the string and return the result
	 * @param originalContent
	 * @param properties
	 * @return 
	 */
	protected static String applyProperties(String originalContent, 
			Map<String, String> properties) {
		
		if (originalContent != null && properties != null) {
			//Apply all the properties 
			for (Map.Entry<String, String> entry : properties.entrySet()) {
				originalContent = originalContent.replace(
						String.format("${%s}", entry.getKey()), 
						entry.getValue());
			}
		}
		
		return originalContent;
	}
}
