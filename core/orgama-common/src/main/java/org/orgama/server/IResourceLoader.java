package org.orgama.server;

import java.util.Map;

/**
 * Interface defining an object that can load resources from different sources
 * @author kguthrie
 */
public interface IResourceLoader {

	String loadFromClassPathFile(String file, Map<String, String> properties);
	
	String loadFromAppRootFile(String file, Map<String, String> properties);
	
	String loadFromUrl(String url, Map<String, String> properties);
	
}
