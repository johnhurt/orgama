package org.orgama.server.auth;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import java.util.List;
import org.orgama.server.Ofy;
import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.server.auth.source.IAuthServiceProvider;
import org.orgama.shared.Logger;
import org.orgama.shared.auth.AuthUtils;
import org.orgama.shared.auth.except.AuthException;
import org.orgama.shared.auth.model.AuthUser;
import org.orgama.shared.except.OrgamaCoreException;

/**
 * services specific to auth users
 * @author kguthrie
 */
public class AuthUserService implements ICoreUserService {
	
	@Inject
	public AuthUserService(IAuthServiceProvider serviceProvider) {
		Ofy.register(AuthUser.class);
	}

	@Override
	public AuthUser getUserFromEmailAddress(String emailAddress) {
		
		String sanitizedEmailAddress = 
				AuthUtils.sanitizeEmailAddress(emailAddress);
		
		//This list should only have one or zero entries
		List<AuthUser> usersWithEmailAddress = 
				Ofy.load().type(AuthUser.class)
						.filter("sanitizedEmailAddress", 
								sanitizedEmailAddress).list();
		
		if (usersWithEmailAddress == null 
				|| usersWithEmailAddress.isEmpty()) {
			return null;
		}
		
		if (usersWithEmailAddress.size() > 1) {
			
			Logger.fatal("Mutiple Users exist with email address: " + 
					emailAddress);
			
			throw new OrgamaCoreException("There was an error on the server " +
					"while getting information on the emailaddress you " +
					"entered.  Please try again later");
		}
		
		return usersWithEmailAddress.get(0);
	}

	/**
	 * Attempt to register the user using the information in their 
	 * authInitialization
	 * @param authInit
	 * @return 
	 */
	@Override
	public AuthUser registerNewUser(AuthInitialization authInit) {
		
		AuthUser result = new AuthUser();
		
		result.setEmailAddress(authInit.getEmailAddress());
		result.setAuthServiceName(authInit.getAuthServiceName());
		result.setServiceSpecificUserId(authInit.getServiceSpecificUserId());
		
		registerUser(result);
		
		return result;
	}

	/**
	 * Register the given user by inserting it into the datastore after 
	 * verifying that the email address, external user id, and auth resource
	 * name are not null.  Uniqueness checks are done by objectify, and will 
	 * throw uniqueField restriction exceptions
	 * @param user 
	 */
	void registerUser(AuthUser user) {
		
		//Could probably replace this with some @NotNulls
		if (Strings.isNullOrEmpty(user.getServiceSpecificUserId())) {
			throw new AuthException("Cannot register user with null or empty " +
					"external user id");
		}
		
		if (Strings.isNullOrEmpty(user.getSanitizedEmailAddress())) {
			throw new AuthException("Cannot register user with null or empty " +
					"email address");
		}
		
		if (Strings.isNullOrEmpty(user.getAuthServiceName())) {
			throw new AuthException("Cannot register user with null or empty " +
					"auth resource name");
		}
		
		Ofy.save().entity(user).now();
	}
	
			
	/**
	 * get the user with the given id
	 * @param userId
	 * @return 
	 */
	@Override
	public AuthUser getUserById(long userId) {
		return Ofy.load().type(AuthUser.class).id(userId).get();
	}

}
