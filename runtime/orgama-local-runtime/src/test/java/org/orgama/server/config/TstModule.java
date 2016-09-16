package org.orgama.server.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jukito.JukitoModule;
import org.jukito.TestEagerSingleton;
import org.jukito.TestSingleton;
import org.orgama.server.Orgama;
import org.orgama.server.auth.DevModeFacebookAuthService;
import org.orgama.server.mock.MockServletRequestResponse;

/**
 *
 * @author kguthrie
 */
public class TstModule extends JukitoModule {

	@Override
    protected void configureTest() {
		bind(Orgama.class).in(TestEagerSingleton.class);
		bind(DevModeFacebookAuthService.class).in(TestSingleton.class);

        bind(MockServletRequestResponse.class)
                .in(TestSingleton.class);

        bind(HttpServletRequest.class)
                .to(MockServletRequestResponse.Request.class)
                .in(TestSingleton.class);

        bind(HttpServletResponse.class)
                .to(MockServletRequestResponse.Response.class)
                .in(TestSingleton.class);
	}

}
