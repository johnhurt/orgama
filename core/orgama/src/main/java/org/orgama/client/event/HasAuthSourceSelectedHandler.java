/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client.event;

/**
 * interface for any class that has auth source handlers
 * @author kguthrie
 */
public interface HasAuthSourceSelectedHandler {
    public void addAuthSourceHandler(AuthSourceSelectedHandler assHandler);
}
