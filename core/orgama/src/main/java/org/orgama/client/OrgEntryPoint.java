/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.gwtplatform.mvp.client.DelayedBindRegistry;
import org.orgama.client.config.OrgGinjector;
import org.orgama.shared.Logger;

/**
 * Orgama's entry point.  The actual entry point of the application should
 * extend this class.
 * @author kguthrie
 */
public abstract class OrgEntryPoint implements EntryPoint {

    /**
     * Gwt client entry point
     */
    @Override
    public final void onModuleLoad() {
        
		Logger.trace("Entered into Orgama entry point");
		
        try {
            beforeInjection();
        }
        catch(Exception ex) {
            Window.alert("Error in pre-injection initialization.  " + 
                    ex.getMessage());;
        }
        
        try {
            //Get the injector 
            OrgGinjector i = getInjector();

			Logger.trace("Created the ginjector");
			
            DelayedBindRegistry.bind(i);
            
			Logger.trace("Bound the delayed bind registry");
			
            try {
                afterInjection();
            }
            catch(Exception ex) {
                //error in post-injection intitialization code
                Logger.error("Error in post-injection code", ex);
            }

            i.getPlaceManager().revealCurrentPlace();
			
			Logger.trace("revealed the current place");
        }
        catch(Exception ex) {
            Window.alert("Error in injection phase of initialization.  " + 
                    ex.getMessage());
			Logger.error("Attempting to log exception", ex);
        }
        
    }

    /**
     * The applications entrypoint should provide a reference to the class
     * representing the application's injector
     * @return 
     */
    protected abstract OrgGinjector getInjector();
    
    
    /**
     * called before the injector is initialized.  
     */
    protected void beforeInjection() {
        
    }
    
    /**
     * called after the injector is initialized.
     */
    protected void afterInjection() {
        
    }
}
