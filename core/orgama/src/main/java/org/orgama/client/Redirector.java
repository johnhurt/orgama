package org.orgama.client;

import org.orgama.shared.IRedirector;

/**
 * performs a native javascript redirect
 * @author kguthrie
 */
public class Redirector implements IRedirector {

	@Override
	public native void redirect(String url) /*-{
	    $wnd.location.href=url;
	}-*/;
}
