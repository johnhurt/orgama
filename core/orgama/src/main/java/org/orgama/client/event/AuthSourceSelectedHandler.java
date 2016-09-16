/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client.event;

import org.orgama.shared.auth.model.AuthSourceInfo;


/**
 * interface for any class that can handle an auth source being selected
 * @author kguthrie
 */
public interface AuthSourceSelectedHandler {
    public void onAuthSourceSelected(AuthSourceInfo authSource);
}
