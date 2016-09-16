package org.orgama.server.auth;

import com.google.inject.BindingAnnotation;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * interface defining a method to get the information about the current user 
 * from the facebook oauth 2 service
 * @author kguthrie
 */
public interface IFacebookUserService {
	
	@BindingAnnotation @Target({ PARAMETER}) @Retention(RUNTIME)
	public static @interface ApplictionId {}
	
	@BindingAnnotation @Target({ PARAMETER}) @Retention(RUNTIME)
	public static @interface ApplictionSecretId {}
	
	public String getSignInUrl(String redirectUri);
	
	public String getSignOutUrl(String redirectUri, String accessToken);
	
	public String getAccessToken();
	
	public String getUserId(String accessToken);
	
}
