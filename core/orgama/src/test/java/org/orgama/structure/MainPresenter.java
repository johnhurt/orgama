package org.orgama.structure;

import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.proxy.Proxy;
import org.orgama.client.annotation.DisableCodeSplit;
import org.orgama.client.presenter.OrgamaPresenter;

/**
 * Main/Initial presenter for the app.  This presenter is bound to the empty
 * name token by default, so www.your-app.com/ will load this presenter and its
 * associated view
 */
@NameToken("")
@DisableCodeSplit
public class MainPresenter extends OrgamaPresenter<MainPresenter.Display> {

	/**
	 * Interface that the view or any mocks will need to conform to
	 */
	public interface Display extends View {
		
		
	}
	
	public MainPresenter(MainPresenter.Display view, Proxy proxy) {
		super(view, proxy);
	}
	
	@Override
	protected void onFirstLoad() {
		
	}

	@Override
	protected void bindToView(MainPresenter.Display view) {
		
	}

}