package ${package}.client.config;

import ${package}.client.NameTokens;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import org.orgama.client.annotation.DefaultPlace;
import org.orgama.client.config.ClientSideConstants;
		
import static org.orgama.client.config.ClientSideConstants.*;

/**
 * This is where the injected dependencies are defined.
 * @author kguthrie
 */
public class AppConfig extends AbstractPresenterModule {

    @Override
    public void configure() {
		bindConstants();
    }
	
	/**
	 * Bind constants here.  This method is only here to improve organization
	 */
	private void bindConstants() {
		
        bind(ClientSideConstants.class).asEagerSingleton();
        
        //String Constants
        bindConstant().annotatedWith(
				DefaultPlace.class).to(NameTokens.landingPage);
        
		bindConstant().annotatedWith(
				AppTitle.class).to(
				"${artifactId}");
        
		bindConstant().annotatedWith(
				AppDescription.class).to(
				"Orgama application for ${artifactId}");
		
        bindConstant().annotatedWith(
				AppKeywords.class).to(
				"example keywords awesome");
	}

}
