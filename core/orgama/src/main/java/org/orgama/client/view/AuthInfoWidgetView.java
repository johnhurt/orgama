package org.orgama.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import org.orgama.client.presenter.AuthInfoWidgetPresenter;
import org.orgama.client.view.res.OrgamaImages;
import org.orgama.client.widget.WatermarkedTextBox;

/**
 * First cut of the view implemented for the widget that displays and controls
 * the auth state and process respectively 
 * @author kguthrie
 */
public class AuthInfoWidgetView 
		extends OrgamaWidgetView 
		implements AuthInfoWidgetPresenter.Display {

    public interface AuthInfoWidgetViewUiBinder 
			extends UiBinder<Widget, AuthInfoWidgetView> {}
	
	@UiField Label lblStatus;
	@UiField FormPanel frmEmailAddress;
	@UiField WatermarkedTextBox txtEmailAddress;
	@UiField Button btnSignInOrRegistrer;
	@UiField Button btnSignOut;
	@UiField Image imgNextToButton;
	private final OrgamaImages images;
	
	@Inject
	public AuthInfoWidgetView(
			AuthInfoWidgetViewUiBinder uiBinder, 
			OrgamaImages images) {
		this.images = images;
		bind(uiBinder);
		btnSignInOrRegistrer.setText("Sign In/Register");
		btnSignOut.setText("Sign Out");
	}
	
	@Override
	public void setStatus(String message) {
		lblStatus.setText(message);
	}

	@Override
	public void setEmailAddressInputVisible(boolean visible) {
		txtEmailAddress.setVisible(visible);
	}

	@Override
	public void setRegisterSignInButtonVisible(boolean visible) {
		this.btnSignInOrRegistrer.setVisible(visible);
	}

	@Override
	public HasClickHandlers getSignInOrRegisterButton() {
		return btnSignInOrRegistrer;
	}

	@Override
	public String getEmailAddress() {
		return txtEmailAddress.getText();
	}
	
	@Override
	public HasKeyDownHandlers getEmailAddressBoxKeyHandler() {
		return txtEmailAddress;
	}
	
	/**
	 * Saves the inputs like email address so that the *can* be filled in via
	 * auto-complete.
	 */
	@Override
	public void saveInputs() {
		frmEmailAddress.submit();
	}
	
	@Override
	public void setSignOutButtonVisible(boolean visible) {
		btnSignOut.setVisible(visible);
	}

	@Override
	public HasClickHandlers getSignOutButton() {
		return btnSignOut;
	}

	@Override
	public void setBusy(boolean busy) {
		if (busy) {
			imgNextToButton.setUrl(images.spinner_16x16().getSafeUri());
			txtEmailAddress.setEnabled(false);
			btnSignInOrRegistrer.setEnabled(false);
			btnSignOut.setEnabled(false);
		}
		else {
			imgNextToButton.setUrl("favicon.ico");
			txtEmailAddress.setEnabled(true);
			btnSignInOrRegistrer.setEnabled(true);
			btnSignOut.setEnabled(true);
		}
	}

}
