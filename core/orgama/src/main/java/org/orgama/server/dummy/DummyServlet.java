/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.orgama.server.dummy;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Singleton;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * dummy service for dummies
 * @author kguthrie
 */
@Path("/dummy")
@Singleton
public class DummyServlet extends RemoteServiceServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
		resp.getWriter().println("Thanks");
        resp.getWriter().flush();
        resp.getWriter().close();
    }
    
    @Path("/")
    @GET
    @Produces("text/plain")
    public String helloDummy() {
        return "Hello";
    }
    
    @Path("/")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String postDummy() {
        return "Thanks";
    }
}