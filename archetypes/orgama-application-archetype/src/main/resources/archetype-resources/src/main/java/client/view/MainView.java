package ${package}.client.view;

import ${package}.client.presenter.MainPresenter;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.orgama.client.presenter.AuthInfoWidgetPresenter;
import org.orgama.client.view.OrgamaView;

/**
 * Implementation of the main view for this orgama project
 * @author kguthrie
 */
public class MainView extends OrgamaView implements MainPresenter.Display {

	@UiField HTMLPanel pnlAuth;

	@Override
	public void renderAuthPanel(AuthInfoWidgetPresenter authPanel) {
		pnlAuth.add(authPanel.getWidget());
	}
	
}
