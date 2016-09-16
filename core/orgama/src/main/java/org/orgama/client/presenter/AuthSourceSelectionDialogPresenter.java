/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client.presenter;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import java.util.ArrayList;
import org.orgama.client.Dispatch;
import org.orgama.client.EventBus;
import org.orgama.client.OrgAsyncCallback;
import org.orgama.client.event.AuthSourceSelectedHandler;
import org.orgama.client.event.HasAuthSourceSelectedHandler;
import org.orgama.client.event.IDispatchListener;
import org.orgama.client.except.ClientSideException;
import org.orgama.client.view.BlockUi;
import org.orgama.shared.Logger;
import org.orgama.shared.auth.action.InitiateRegistration;
import org.orgama.shared.auth.action.InitiateRegistrationResult;
import org.orgama.shared.auth.model.AuthSourceInfo;

/**
 * Presenter for the dialog asking the user to pick an auth service to 
 * authenticate to
 * @author kguthrie
 */
public class AuthSourceSelectionDialogPresenter extends 
        PresenterWidget<AuthSourceSelectionDialogPresenter.Display>
        implements AuthSourceSelectedHandler {

    /**
     * Interface for the view of this widget presenter
     */
    public interface Display extends View {
        public void setAuthSources(ArrayList<AuthSourceInfo> authSources);
        public HasAuthSourceSelectedHandler getAuthSourceSelecter();
    }
    
    private ArrayList<AuthSourceInfo> authSources;
	private String emailAddress;
    
    @Inject
    public AuthSourceSelectionDialogPresenter(Display view) {
        super(EventBus.get(), view);
    }
 
    @Override
    public void onBind() {
        getView().getAuthSourceSelecter().addAuthSourceHandler(this);
    }
    
    /**
     * @return the authSources
     */
    public ArrayList<AuthSourceInfo> getAuthSources() {
        return authSources;
    }

    /**
     * @param authSources the authSources to set
     */
    public void setAuthSources(ArrayList<AuthSourceInfo> authSources) {
        this.authSources = authSources;
        getView().setAuthSources(authSources);
    }
    
    /**
     * Called by the view when an auth source is selected
     * @param authSource 
     */
    @Override
    public void onAuthSourceSelected(AuthSourceInfo authSource) {
        
		final InitiateRegistration registration = new InitiateRegistration();
		
		registration.setAuthResourceName(authSource.getResourceName());
		registration.setEmailAddress(emailAddress);
		
		registration.setDisptachListener(new IDispatchListener() {

			@Override
			public void onDispatch() {
				BlockUi.blockAllWithMessage("Registering...");
			}

			@Override
			public void onResponse() {
			}
		});
		
		Dispatch.dispatch(registration, 
				new OrgAsyncCallback<InitiateRegistrationResult>() {

			@Override
			public void onFailure(ClientSideException ex) {
				Logger.error("Error registering with " + 
						registration.getAuthResourceName(), ex);
			}

			@Override
			public void onSuccess(InitiateRegistrationResult result) {
				//Should never get here
			}
		});
    }

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
