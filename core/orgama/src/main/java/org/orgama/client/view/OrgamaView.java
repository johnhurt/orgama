package org.orgama.client.view;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.View;

/**
 * Base implementation of View for for Orgama Presenter-View combinations
 * @author kguthrie
 */
public abstract class OrgamaView implements View {

	private Widget widget;
	
	protected <V extends OrgamaView, 
			T extends UiBinder<Widget, V>> void bind(T binder) {
		this.widget = binder.createAndBindUi((V)this);
		onBind();
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void addToSlot(Object slot, Widget content) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void removeFromSlot(Object slot, Widget content) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	/**
	 * This method is called after the widget has been bound to this view.  
	 * Automatically injected UiFields will not be available until this method
	 * is called.
	 */
	protected void onBind() {}

}
