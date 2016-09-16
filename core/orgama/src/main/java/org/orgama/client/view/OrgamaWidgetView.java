package org.orgama.client.view;

import com.google.gwt.user.client.ui.Widget;
/**
 * Simple implementation of the base Orgama View that assumes that a orgama
 * Widget's view does not have slots to be managed
 * @author kguthrie
 */
public abstract class OrgamaWidgetView extends OrgamaView {

	@Override
	public void addToSlot(Object slot, Widget content) {
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public void removeFromSlot(Object slot, Widget content) {
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		throw new UnsupportedOperationException("Not supported yet."); 
	}
	
}
