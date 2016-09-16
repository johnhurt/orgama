package org.orgama.server.mock;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.orgama.server.DefaultResourceLoader;

/**
 * Resource loader that can be used in testing and returns configurable content
 * for configurable urls
 * @author kguthrie
 */
public class MockResourceLoader extends DefaultResourceLoader {

	private final Map<String, String> urlContent;
	private final Map<String, String> classPathFileContent;
	private final Map<String, String> appRootFileContent;
	
	@Inject
	public MockResourceLoader(Provider<HttpServletRequest> requestProvider) {
		super(requestProvider);
		urlContent = new HashMap<String, String>();
		classPathFileContent = new HashMap<String, String>();
		appRootFileContent = new HashMap<String, String>();
	}

	/**
	 * Load content from a fake classpath file
	 * @param file
	 * @param properties
	 * @return 
	 */
	@Override
	public String loadFromClassPathFile(String file, 
			Map<String, String> properties) {
		return applyProperties(classPathFileContent.get(file), properties);
	}

	/**
	 * load content from a fake file and apply properties
	 * @param file
	 * @param properties
	 * @return 
	 */
	@Override
	public String loadFromAppRootFile(String file, 
			Map<String, String> properties) {
		return applyProperties(appRootFileContent.get(file), properties);
	}

	/**
	 * load content from the fake url and apply properties
	 * @param url
	 * @param properties
	 * @return 
	 */
	@Override
	public String loadFromUrl(String url, Map<String, String> properties) {
		return applyProperties(urlContent.get(url), properties);
	}
	
	/**
	 * Add some fake url content 
	 * @param url
	 * @param content 
	 */
	public void addUrlContent(String url, String content) {
		urlContent.put(url, content);
	}
	
	/**
	 * add some fake app root content
	 * @param file
	 * @param content 
	 */
	public void addAppRootContent(String file, String content) {
		appRootFileContent.put(file, content);
	}
	
	/**
	 * add some fake class path file content
	 * @param file
	 * @param content 
	 */
	public void addClassPathContent(String file, String content) {
		classPathFileContent.put(file, content);
	}
}
