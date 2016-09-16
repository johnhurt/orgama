package org.orgama.server.config;

import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;
import com.gwtplatform.dispatch.server.guice.HandlerModule;
import java.util.List;

/**
 * Interface that provides simple access to the server side guice injections
 * for Orgama extensions
 * @author kguthrie
 */
public interface OrgamaExtensionModule {
	
	public HandlerModule getHandlerModule();
	public ServletModule getServletModule();
	public List<Module> getOtherModules();
	
}
