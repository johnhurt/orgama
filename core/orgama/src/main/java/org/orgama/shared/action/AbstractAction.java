// <copyright file="AbstractAction.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://rookandpawn.com/

package org.orgama.shared.action;

import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;
import java.io.Serializable;
import org.orgama.client.event.IDispatchListener;

/**
 * First level abstraction of the gwt-dispatch.  This level adds unique
 * identifiers for each
 * @author Kevin Guthrie
 */
public abstract class AbstractAction<T extends AbstractResult> extends
        UnsecuredActionImpl<T> implements Serializable, IDispatchListener {

    //used for id tracking and synchronized incrementing
    private static transient long lastId = 0;

	private transient IDispatchListener disptachListener;
	
    //unique id for action
    private long id;

    /**
     * base constructor
     */
    public AbstractAction() {
        id = lastId++;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

	/**
	 * @return the disptachListener
	 */
	public IDispatchListener getDisptachListener() {
		return disptachListener;
	}

	/**
	 * @param disptachListener the disptachListener to set
	 */
	public void setDisptachListener(IDispatchListener disptachListener) {
		this.disptachListener = disptachListener;
	}

	@Override
	public void onDispatch() {
		if (disptachListener == null) {
			return;
		}
		disptachListener.onDispatch();
	}

	@Override
	public void onResponse() {
		if (disptachListener == null) {
			return;
		}
		disptachListener.onResponse();
	}
	
}
