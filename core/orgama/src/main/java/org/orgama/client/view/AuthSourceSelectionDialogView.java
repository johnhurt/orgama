/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import java.util.ArrayList;
import org.orgama.client.event.AuthSourceSelectedHandler;
import org.orgama.client.event.HasAuthSourceSelectedHandler;
import org.orgama.client.i18n.OrgamaConstants;
import org.orgama.client.presenter.AuthSourceSelectionDialogPresenter;
import org.orgama.shared.IRedirector;
import org.orgama.shared.auth.model.AuthSourceInfo;
import org.orgama.shared.auth.source.AuthServiceName;

/**
 * Real view of the auth service selection dialog
 * @author kguthrie
 */
public class AuthSourceSelectionDialogView extends ViewImpl
        implements AuthSourceSelectionDialogPresenter.Display,
        HasAuthSourceSelectedHandler {

    public interface AuthServiceSelectionDialogViewUiBinder extends
            UiBinder<Widget, AuthSourceSelectionDialogView> {
    }
    
    private final Widget widget;
    private ArrayList<AuthSourceInfo> authSources;
    private AuthSourceSelectedHandler selectionHandler;

	@UiField
	HTMLPanel pnlAuthSourceSelector;
	
    @UiField
    FocusPanel pnlGoogleAccounts;
    
    @UiField
    FocusPanel pnlFacebook;
	
	@UiField
	OrgamaConstants cnst;
    
    @Inject
    public AuthSourceSelectionDialogView(
            final AuthServiceSelectionDialogViewUiBinder uiBinder) {
        
        widget = uiBinder.createAndBindUi(this);
        
        setupHandlers();
    }
    
    @Override
    public Widget asWidget() {
        return widget;
    }

    /**
     * set up handlers for the auth source links and buttons
     */
    private void setupHandlers() {
        selectionHandler = null;
        
        pnlGoogleAccounts.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                onSelecterClick(AuthServiceName.googleAccounts);
            }
        });
        
        pnlFacebook.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                onSelecterClick(AuthServiceName.facebook);
            }
        });
    }
    
    /**
     * Handles the click of one of the auth sources.
     * @param resourceName 
     */
    public void onSelecterClick(String resourceName) {
        if (authSources == null || selectionHandler == null) {
            return;
        }
        
        for (AuthSourceInfo as : authSources) {
            if (as.getResourceName().equals(resourceName)) {
                selectionHandler.onAuthSourceSelected(as);
                return;
            }
        }
    }
    
    @Override
    public void setAuthSources(ArrayList<AuthSourceInfo> authSources) {
        this.authSources = authSources;
		
		switch(authSources.size()) {
			case 1: {
				pnlAuthSourceSelector.setWidth("93px");
				break;
			}
			case 2: {
				pnlAuthSourceSelector.setWidth("222px");
				break;
			}
		}
		
		for (AuthSourceInfo source : authSources) {
			if (AuthServiceName.facebook.equals(
					source.getResourceName())) {
				pnlFacebook.setVisible(true);
			}
			if (AuthServiceName.googleAccounts.equals(
					source.getResourceName())) {
				pnlGoogleAccounts.setVisible(true);
			}
		}
    }

    @Override
    public HasAuthSourceSelectedHandler getAuthSourceSelecter() {
        return this;
    }

    @Override
    public void addAuthSourceHandler(AuthSourceSelectedHandler assHandler) {
        selectionHandler = assHandler;
    }

}
