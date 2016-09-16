package org.orgama.client.presenter;

import com.google.gwt.event.shared.GwtEvent;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import org.orgama.client.EventBus;

/**
 * Base presenter specific to orgama applications.
 * @author kguthrie
 */
public abstract class OrgamaPresenter<V extends View> 
		extends Presenter<V, Proxy<?>> {

	private boolean firstLoadHappened;
	private final GwtEvent.Type<RevealContentHandler<?>> parentSlot;
	
	/**
	 * Create a new OrgamaPresenter as a base to another presenter with a parent 
	 * slot defined. 
	 * @param view
	 * @param proxy
	 * @param parentSlot 
	 */
	public OrgamaPresenter(V view, Proxy<?> proxy, 
			GwtEvent.Type<RevealContentHandler<?>> parentSlot) {
		super(EventBus.get(), view, proxy);
		firstLoadHappened = false;
		this.parentSlot = parentSlot;
	}
	
	/**
	 * Create a new OrgamaPresenter as a base to another presenter with no 
	 * parent.  The extending presenter that uses this constructor will be 
	 * placed directly inside the root content presenter
	 * @param view
	 * @param proxy 
	 */
	public OrgamaPresenter(V view, Proxy<?> proxy) {
		this(view, proxy, null);
	}
	
	/**
	 * This is called to reveal this presenter (rather its view) in the parent
	 * presenter.
	 */
	@Override
	protected final void revealInParent() {
		
		//If there is a parent slot defined, fire an event to place this 
		//presenter within in.
		if (parentSlot != null) { 
			RevealContentEvent revealEvent = 
					new RevealContentEvent(parentSlot, this);
			EventBus.fireEvent(revealEvent);
		}
		else {
			//Otherwise, place this presenter in the root presenter
			RevealRootContentEvent.fire(EventBus.get(), this);
		}
		
		if (!firstLoadHappened) {
			firstLoadHappened = true;
			onFirstLoad();
		}
	}
	
	/**
	 * Called automatically when it is time to bind this presenter to its 
	 * associated view.
	 */
	@Override
	protected final void onBind() {
		bindToView(getView());
	}
	
	/**
	 * returns whether or not this presenter has been loaded for the first time
	 * @return 
	 */
	public boolean hasFirstLoadHappened() {
		return firstLoadHappened;
	}
	
	/**
	 * This method is called when this presenter is loaded for the first time.
	 */
	protected abstract void onFirstLoad();

	/**
	 * This method is called automatically.  The body of this method should be
	 * where this presenter should be bound to the given view. 
	 * @param view 
	 */
	protected abstract void bindToView(V view);
}
