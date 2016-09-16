package org.orgama.server;

import com.google.inject.Injector;
import java.util.logging.Logger;
import javax.servlet.jsp.PageContext;
import org.orgama.server.auth.AuthBootstrapper;
import org.orgama.server.auth.model.CompleteAuthState;
/**
 * Entry point for jsp to call into the Orgama application core.
 * @author kguthrie
 */
public class JspInterop {

	private static final Logger logger = Logger.getLogger("JspInterop");
	
	/**
	 * called by the jsp page when it is first loaded
	 * @param context 
	 */
	public static void onLoad(PageContext pageContext) {
		Injector injector = 
				(Injector)pageContext.getServletContext().getAttribute(
						Injector.class.getName());

		AuthBootstrapper authBootstrapper = injector.getInstance(
				AuthBootstrapper.class);

		CompleteAuthState authState = authBootstrapper.bootstrap();
		
		try {
			pageContext.getOut().write(String.format(
					"<script language='javascript' type='text/javascript'>" + 
							"var ___authState___=\"%s\";" +
							"var ___authServiceName___=%s;" +
					"</script>", 
					authState.getAuthState().toString(), 
					authState.getAuthServiceName() == null
							? "null" 
							: "\"" + authState.getAuthServiceName() + "\""));
		}
		catch(Exception ex) {
			logger.severe("Error writing auth state to jsp.  " + 
					ex.getMessage());
		}
	}
	
}
