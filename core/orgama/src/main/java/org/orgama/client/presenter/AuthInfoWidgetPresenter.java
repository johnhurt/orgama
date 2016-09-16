package org.orgama.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.mvp.client.View;
import org.orgama.client.Dispatch;
import org.orgama.client.event.IDispatchListener;
import org.orgama.client.view.BlockUi;
import org.orgama.client.view.DialogResponseHandler;
import org.orgama.shared.Logger;
import org.orgama.shared.auth.AuthUtils;
import org.orgama.shared.auth.action.SignOut;
import org.orgama.shared.auth.action.SignOutResult;
import org.orgama.shared.auth.action.ValidateEmailAddress;
import org.orgama.shared.auth.action.ValidateEmailAddressResult;
import org.orgama.shared.auth.model.ICompleteAuthState;
import org.orgama.shared.enm.ButtonSet;
import org.orgama.shared.enm.DialogResult;

/**
 * First cut of a widget that controls the authentication process.  
 * @author kguthrie
 */
public class AuthInfoWidgetPresenter 
		extends OrgamaWidgetPresenter<AuthInfoWidgetPresenter.Display> {

	public interface Display extends View {
		
		void setStatus(String statusMessage);
		void setEmailAddressInputVisible(boolean visible);
		void setRegisterSignInButtonVisible(boolean visible);
		void setSignOutButtonVisible(boolean visible);
		
		HasClickHandlers getSignInOrRegisterButton();
		HasClickHandlers getSignOutButton();
        HasKeyDownHandlers getEmailAddressBoxKeyHandler();
		
		String getEmailAddress();
		void saveInputs();
		void setBusy(boolean busy);
	}
	
	private ICompleteAuthState authState;
	private final Provider<AuthSourceSelectionDialogPresenter> assPresenterProv;
	private final Provider<SignOutDialogPresenter> signOutPresenterProvider;
	
	@Inject
	public AuthInfoWidgetPresenter(Display view, 
			ICompleteAuthState authState, 
			Provider<AuthSourceSelectionDialogPresenter> assPresenterProv, 
			Provider<SignOutDialogPresenter> signOutPresenterProvider) {
		super(view);
		this.assPresenterProv = assPresenterProv;
		this.authState = authState;
		this.signOutPresenterProvider = signOutPresenterProvider;
	}

	@Override
	protected void onBind() {
		updateStatus();
		bindHandlers();
	}
	
	private void bindHandlers() {
		getView().getSignInOrRegisterButton().addClickHandler(
				new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onSignInOrRegisterClick();
			}
		});
		
        //bind the press of a key inside the email address box 
        getView().getEmailAddressBoxKeyHandler().addKeyDownHandler(
                new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                onEmailAddressKeyDown(event.getNativeKeyCode());
            }
        });
		
		//bind the sign out button click event
		getView().getSignOutButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onSignOutClick();
			}
		});
	}
	
    /**
     * handles the press of a key when in the email address text box.  This
     * is used to detect when enter is pressed and simulate the click of 
     * the submit/register button
     * @param keyCode 
     */
    protected void onEmailAddressKeyDown(int keyCode) {
        
        //Simulate the click of the register/login button
        if (keyCode == KeyCodes.KEY_ENTER) {
            onSignInOrRegisterClick();
        }
    }
	
	/**
	 * This is called when the user clicks the sign in or register button
	 */
	private void onSignInOrRegisterClick() {
		String emailAddress = getView().getEmailAddress();
		
		if (!AuthUtils.validateEmailAddress(emailAddress)) {
			return;
		}
		
		ValidateEmailAddress vem = new ValidateEmailAddress(emailAddress);
		vem.setDisptachListener(new IDispatchListener() {

			@Override
			public void onDispatch() {
				getView().setBusy(true);
			}

			@Override
			public void onResponse() {
				getView().setBusy(false);
			}
		});
		
		Dispatch.dispatch(vem, new AsyncCallback<ValidateEmailAddressResult>() {

			@Override
			public void onFailure(Throwable ex) {
				Logger.error(ex.getMessage(), ex);
			}

			@Override
			public void onSuccess(ValidateEmailAddressResult result) {
				switch (result.getResponseCode()) {
					case unknownEmailAddress: {
						AuthSourceSelectionDialogPresenter assPresenter = 
								assPresenterProv.get();
						assPresenter.setEmailAddress(result.getEmailAddress());
						assPresenter.setAuthSources(result.getAuthSourceList());
						BlockUi.blockAllWithWidget(assPresenter.getWidget(),
								ButtonSet.cancel, null);
						break;
					}
				}
			}
		});
		
		getView().saveInputs();
	}
	
	/**
	 * Handles the click of the sign out button
	 */
	private void onSignOutClick() {
		switch(authState.getAuthState()) {
			case authenticated: {
				final SignOutDialogPresenter signOutPresenter = 
						signOutPresenterProvider.get();
				BlockUi.blockAllWithWidget(signOutPresenter.getWidget(), 
						ButtonSet.yesNo, new DialogResponseHandler() {

					@Override
					public void handleResponse(DialogResult result) {
						boolean logOutOfExternal = 
								signOutPresenter.getNeedsExternalLogOut();
						
						switch(result) {
							case yes: {
								dispatchSignOut(true, logOutOfExternal);
								break;
							}
						}
						
						BlockUi.unblock();
					}
				});
				break;
			}
			case externalUserIdMismatch: {
				dispatchSignOut(false, true);
				break;
			}
			default: {
				BlockUi.blockAllWithMessage(
						"You shouldn't be able to do that");
				break;
			}
		}
	}
	
	/**
	 * This method is here just to prevent having to call into the dispatcher
	 * multiple times for the same action.  This method will dispatch the given
	 * sign out action.  If there is an error, it will be logger.  Otherwise
	 * nothing will happen because sign out always redirects somewhere
	 * @param signout 
	 */
	private void dispatchSignOut(boolean signOutOfApp, 
			boolean signOutOfExternalService) {
		
		final SignOut signOut = new SignOut();
		
		signOut.setSignOutOfApp(signOutOfApp);
		signOut.setSignOutOfExternalService(signOutOfExternalService);
		
		if (signOutOfExternalService && !(signOutOfApp)) {
			signOut.setAuthServiceName(authState.getAuthServiceName());
		}
		
		signOut.setDisptachListener(new IDispatchListener() {

			@Override
			public void onDispatch() {
				getView().setBusy(true);
			}

			@Override
			public void onResponse() {
				getView().setBusy(false);
			}
		});
		
		Dispatch.dispatch(signOut, 
				new AsyncCallback<SignOutResult>() {

			@Override
			public void onFailure(Throwable caught) {
				Logger.error("Error logging out", caught);
			}

			@Override
			public void onSuccess(SignOutResult result) {
				//Shouldn't get here due to auto redirect
			}
		});
	}
	
	/**
	 * update the display of the status
	 */
	private void updateStatus() {
		
		String statusMessage = "Unknown Server error, try again later.";
		boolean emailAddressInputVisible = false;
		boolean signInOrRegisterVisible = false;
		boolean signOutButtonVisible = false;
		
		switch(authState.getAuthState()) {
			case nil: {
				statusMessage = "You are not currently signed in";
				emailAddressInputVisible = true;
				signInOrRegisterVisible = true;
				break;
			}
			case authenticated: {
				statusMessage = "You are logged in";
				signOutButtonVisible = true;
				break;
			}
			case emailAddressTaken: {
				statusMessage = "Sorry, the email address you entered is " +
						"not available";
				emailAddressInputVisible = true;
				signInOrRegisterVisible = true;
				break;
			}
			case externalAccountClaimed: {
				statusMessage = "Sorry, the external account you are " +
						"authenticated with is already bound to another user";
				emailAddressInputVisible = true;
				signInOrRegisterVisible = true;
				break;
			}
			case externalAuthenticationFailed: {
				statusMessage = "Sorry, the external authentication failed. " +
						"Please try again.";
				emailAddressInputVisible = true;
				signInOrRegisterVisible = true;
				break;
			}
			case externalUserIdMismatch: {
				statusMessage = "Sorry, the external account you are " +
						"authenticated with does not match the one " +
						"registered with the email address you entered.  " +
						"Please sign out of the current external account and " +
						"try again";
				signOutButtonVisible = true;
				break;
			}
				
		}
		
		getView().setStatus(statusMessage);
		getView().setEmailAddressInputVisible(emailAddressInputVisible);
		getView().setRegisterSignInButtonVisible(signInOrRegisterVisible);
		getView().setSignOutButtonVisible(signOutButtonVisible);
	}
	
}
