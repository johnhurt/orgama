package ${package}.client.presenter;

import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.Proxy;
import org.orgama.client.annotation.DisableCodeSplit;
import org.orgama.client.annotation.NameToken;
import org.orgama.client.presenter.AuthInfoWidgetPresenter;
import org.orgama.client.presenter.OrgamaPresenter;

/**
 * Main/Initial presenter for the app.  This presenter is bound to the empty
 * name token by default, so www.your-app.com/ will load this presenter and its
 * associated view
 */
@NameToken("")
@DisableCodeSplit
public class MainPresenter extends OrgamaPresenter<MainPresenter.Display> {

	private AuthInfoWidgetPresenter authPresenter;
	
	/**
	 * Interface that the view or any mocks will need to conform to
	 */
	public interface Display extends View {
		void renderAuthPanel(AuthInfoWidgetPresenter authPanel);
	}
	
	public MainPresenter(Display view, Proxy proxy, 
			AuthInfoWidgetPresenter authPresenter) {
		super(view, proxy);
		
		this.authPresenter = authPresenter;
	}
	
	@Override
	protected void onFirstLoad() {
		getView().renderAuthPanel(authPresenter);
	}

	@Override
	protected void bindToView(Display view) {
		
	}


}
