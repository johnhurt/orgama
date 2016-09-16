package org.orgama.client.presenter;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import org.orgama.client.EventBus;

/**
 * Widget presenter specific to Orgama
 * @author kguthrie
 */
public class OrgamaWidgetPresenter<V extends View> extends PresenterWidget<V> {

	public OrgamaWidgetPresenter(V view) {
		super(EventBus.get(), view);
	}

}
