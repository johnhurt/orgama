package org.orgama.client.event;

/**
 * Interface that defines methods to be called before and after dispatching
 * @author kguthrie
 */
public interface IDispatchListener {

	void onDispatch();
	
	void onResponse();
	
}
