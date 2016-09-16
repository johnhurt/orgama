/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.auth;

/**
 * Interface for a class that detects the first time new code is run, and
 * runs initialization code.
 * @author kguthrie
 */
public interface IStartupHandler {
    public boolean shouldRunStartup();
    public void run();
}
