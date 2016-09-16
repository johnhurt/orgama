package org.orgama.client.view;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import org.orgama.client.i18n.OrgamaConstants;
import org.orgama.client.presenter.SignOutDialogPresenter;

/**
 * Real view of the log out dialog view
 * @author kguthrie
 */
public class SignOutDialogView extends ViewImpl
        implements SignOutDialogPresenter.Display {

    public interface LogOutDialogViewUiBinder extends
            UiBinder<Widget, SignOutDialogView> {}
    
    private final Widget widget;
    
    @UiField CheckBox cbxExternalLogOut;
    @UiField HTMLPanel pnlAdditionalCheck;
	
	@UiField
	OrgamaConstants constants;
	
    @Inject
    public SignOutDialogView(final LogOutDialogViewUiBinder uiBinder) {
        widget = uiBinder.createAndBindUi(this);
    }
    
    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public HasValue<Boolean> shouldLogOutOfExternalAuthSite() {
        return cbxExternalLogOut;
    }

    @Override
    public void setExternalAuthCheckMessage(String message) {
        cbxExternalLogOut.setText(message);
    }

    @Override
    public void setExternalAuthCheckVisible(boolean visible) {
        pnlAdditionalCheck.setVisible(visible);
    }

}
