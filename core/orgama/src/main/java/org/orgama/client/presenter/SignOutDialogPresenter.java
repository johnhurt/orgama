/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client.presenter;

import com.google.gwt.user.client.ui.HasValue;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import org.orgama.client.EventBus;
import org.orgama.client.i18n.OrgamaConstants;
import org.orgama.shared.auth.model.ICompleteAuthState;
import org.orgama.shared.auth.source.AuthServiceName;

/**
 * Presenter for log out message in the block ui screen
 * @author kguthrie
 */
public class SignOutDialogPresenter extends 
        PresenterWidget<SignOutDialogPresenter.Display> {
    
    /**
     * Interface for the view of this widget presenter
     */
    public interface Display extends View {
        HasValue<Boolean> shouldLogOutOfExternalAuthSite();
        void setExternalAuthCheckMessage(String message);
        void setExternalAuthCheckVisible(boolean visible);
    }
    
    private final OrgamaConstants constants;
	
    @Inject
    public SignOutDialogPresenter(Display view, OrgamaConstants constants, 
			ICompleteAuthState authState) {
        super(EventBus.get(), view);
        this.constants = constants;
		this.setAuthServiceName(authState.getAuthServiceName());
    }
 
    
    public boolean getNeedsExternalLogOut() {
        return getView().shouldLogOutOfExternalAuthSite().getValue();
    }
    
    /**
     * Set the auth source that will be logged out of using the auth source's
     * res name in the session
     */
    private void setAuthServiceName(String authServiceName) {
        
        if (authServiceName == null) {
            return;
        }
        
        if (authServiceName.equals(
                AuthServiceName.googleAccounts)) {
            getView().setExternalAuthCheckVisible(true);
            getView().setExternalAuthCheckMessage(
                    constants.alsoLogOutOfGoogleAccounts());
        }
        else if(authServiceName.equals(
                AuthServiceName.facebook)) {
            getView().setExternalAuthCheckVisible(true);
            getView().setExternalAuthCheckMessage(
                    constants.alsoLogOutOfFacebook());
        }
    }
}
