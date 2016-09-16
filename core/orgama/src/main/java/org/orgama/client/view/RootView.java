/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client.view;

import com.google.gwt.dom.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import org.orgama.client.presenter.RootPresenter;
import org.orgama.shared.Logger;

/**
 * In-house implementation of root view.  Instead of using the "glass" element,
 * We use the BlockUi object.  This might introduce some overhead, but it 
 * makes it a little more versatile 
 * @author kguthrie
 */
public class RootView extends ViewImpl implements RootPresenter.Display {

    private static RootView instance = null;
    
    public static RootView get() {
        return instance;
    }
    
    protected boolean usingRootLayoutPanel;
    protected DisplayMode displayMode;
    protected String title;
    protected String subtitle;
    
    /**
     * Represents the body element
     */
    protected Panel pnlRoot;
    
    /**
     * Everything 
     */
    protected Panel pnlEverything;
    
    /**
     * content panel
     */
    protected Widget content;
    
    @Inject
    public RootView(BlockUi blockUi, Logger logger) {
        if (instance != null) {
            return;
        }
        
        instance = this;
        displayMode = DisplayMode.nil;
        setDisplayMode(DisplayMode.normal);
        content = null;
        Logger.debug("Creating RootView");
    }

    @Override
    public Widget asWidget() {
        return pnlRoot;
    }
    
	/**
	 * Updates the values of the rootPnl and everything panel pointers to match
	 * the value of the display mode.  
	 */
    private void updateRootPanel() {
		
		if (pnlRoot == null) {
			pnlRoot = RootPanel.get();
			pnlEverything = new AbsolutePanel();
		}
		
		switch(displayMode) {
			case application: {
				pnlRoot.getElement().getStyle().setHeight(
						100, Style.Unit.PCT);
				pnlEverything.getElement().getStyle().setHeight(
						100, Style.Unit.PCT);
				break;
			}
			case normal: {
				pnlRoot.getElement().getStyle().clearHeight();
				pnlEverything.getElement().getStyle().clearHeight();
				break;
			}
		}
		
        pnlRoot.clear();
        pnlRoot.add(pnlEverything);
        pnlRoot.add(BlockUi.get());
    }

    @Override
    public void lockScreen() {
        BlockUi.glass();
    }

    @Override
    public void unlockScreen() {
        BlockUi.unglass();
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        assert slot == RootPresenter.ROOT_SLOT :
                "Unknown slot used in the root proxy.";
        
        this. content = content;
        
        switch (displayMode) {
			case application: {
				content.getElement().getStyle().setHeight(100, Style.Unit.PCT);
				break;
			}
			case normal: {
				content.getElement().getStyle().clearHeight();
				break;
			}
        }
        pnlEverything.clear();
        pnlEverything.add(content);
    }
    
    @Override
    public void setDisplayMode(DisplayMode displayMode) {
        if (this.displayMode == displayMode) {
            return;
        }
        
        this.displayMode = displayMode;
        
		updateRootPanel();
		
        if (pnlRoot == null) {
            return;
        }
        
        switch (displayMode) {
			case application: {
				if (content != null) {
					content.getElement().getStyle().setHeight(
							100, Style.Unit.PCT);
				}
				break;
			}
		
			case normal: {
				if (content != null) {
					content.getElement().getStyle().clearHeight();
				}
				break;
			}
		}
    }
    
    public void setBackgroundColor(String color) {
        if (pnlRoot == null) {
            return;
        }
        pnlRoot.getElement().getStyle().setBackgroundColor(color);
    }
    
    /**
     * set the value of the last history token to be then redirect 
     * @param url
     */
    @Override
    public void redirect(String url) {
        redirectNative(url);
    }

    /**
     * native javascript redirect
     * @param url
     */
    private native void redirectNative(String url)/*-{
        $wnd.location = url;
    }-*/;

    /**
     * get the content for the given meta data element name.  Null is returned
     * if no element exists with that name.
     * @param name
     * @return
     */
    private MetaElement getMetaDataKeyPair(String name) {
        NodeList<Element> metas = Document.get().getElementsByTagName("meta");
        int metaCount = metas.getLength();
        MetaElement currMeta;
        MetaElement result = null;

        for (int i = 0; i < metaCount; i++) {
            currMeta = (MetaElement)metas.getItem(i);

            if (currMeta == null) {
                continue;
            }

            if (currMeta.getName().equalsIgnoreCase(name)) {
                result = currMeta;
                break;
            }
        }

        return result;
    }

    /**
     * adds or modifies a meta data key-value pair in the dom
     * @param name
     * @param content 
     */
    @Override
    public void setMetaDataKeyPair(String name, String content) {
        
        MetaElement currMeta = getMetaDataKeyPair(name);

        if (currMeta == null) {
            addMetaDataKeyPair(name, content);
        }
        else {
            currMeta.setContent(content);
        }
    }
    
    
    /**
     * adds a meta data element with the given key pair.  No checks will be done
     * to insure no key with the given name already exists
     * @param name
     * @param content
     */
    public void addMetaDataKeyPair(String name, String content) {
        MetaElement me = Document.get().createMetaElement();
        me.setName(name);
        me.setContent(content);
        Document.get().getElementsByTagName("head").getItem(0).appendChild(me);
    }

    /**
     * set the value of the meta tag for page description to the string given
     * @param desc
     */
    @Override
    public void setPageDescription(String desc) {
        MetaElement currMeta = getMetaDataKeyPair("description");

        if (currMeta == null) {
            addMetaDataKeyPair("description", desc);
        }
        else {
            currMeta.setContent(desc);
        }
    }
    
    /**
     * sets the additional keywords for the current subpage of the site.  If
     * the default keywords variable has not been set, then the current list of
     * keywords in the dom meta element will become the default and will be
     * added to the list of keywords each time
     */
    @Override
    public void setPageKeywords(String keywords) {
        MetaElement currMeta = getMetaDataKeyPair("keywords");

        if (currMeta == null) {
            addMetaDataKeyPair("keywords", keywords);
        }
        else {
            currMeta.setContent(keywords);
        }
    }

    /**
     * sets the main title of the page.  This title will be appended along with
     * the subtitle to look like "Subtitle | Title"
     * @param mainTitle
     */
    @Override
    public void setPageTitle(String mainTitle) {
        this.title = mainTitle;
        updateTitle();
    }

    @Override
    public void setPageSubtitle(String subtitle) {
        this.subtitle = subtitle;
        updateTitle();
    }

    /**
     * Does the work of updating the page's title with the current title and
     * subtitle
     */
    private void updateTitle() {
        if (subtitle == null || subtitle.trim().length() == 0) {
            Document.get().setTitle(title);
        }
        else {
            Document.get().setTitle(subtitle + " | " + title);
        }
    }
}
