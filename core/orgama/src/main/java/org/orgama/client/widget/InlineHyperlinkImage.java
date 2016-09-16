/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client.widget;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHyperlink;

/**
 *
 * @author kguthrie
 */
public class InlineHyperlinkImage extends InlineHyperlink {

    public InlineHyperlinkImage() {
    }

    public void setResource(ImageResource imageResource) {
        Image img = new Image(imageResource);
        getElement().insertFirst(img.getElement());
    }
}
