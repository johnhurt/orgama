package org.orgama.client.auth;

import com.google.inject.Inject;
import org.orgama.shared.auth.model.AuthState;
import org.orgama.shared.auth.model.ICompleteAuthState;

/**
 * Client side implementation of the IComplete auth state object.  This object
 * will natively pull the values for the auth state from the data stored as
 * javascript strings in the jsp page.
 * @author kguthrie
 */
public class CompleteAuthState implements ICompleteAuthState {

	@Inject
	public CompleteAuthState() {}
	
	@Override
	public String getAuthServiceName() {
		return getAuthServiceNameNative();
	}

	private native String getAuthServiceNameNative() /*-{
	    return $wnd.___authServiceName___;
	}-*/;
	
	@Override
	public AuthState getAuthState() {
		return Enum.valueOf(AuthState.class, getAuthStateNative());
	}
	
	private native String getAuthStateNative() /*-{
	    return $wnd.___authState___;
	}-*/;
	
}
