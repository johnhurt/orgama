// <copyright file="RootPresenter.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.client.presenter;

import org.orgama.client.config.ClientSideConstants;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.FriendlyPresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.*;
import org.orgama.client.EventBus;
import org.orgama.client.view.DisplayMode;

/**
 * In house implementation of the root level presenter
 * @author kguthrie
 */
public class RootPresenter 
        extends FriendlyPresenterWidget<RootPresenter.Display>
        implements ResetPresentersHandler
				, RevealRootContentHandler
				, RevealRootLayoutContentHandler
				, RevealRootPopupContentHandler
				, LockInteractionHandler {

    public interface Display extends View {
        public void redirect(String url);
        
        public void setMetaDataKeyPair(String name, String content);
        public void setPageDescription(String description);
        public void setPageKeywords(String keywords);
        public void setPageTitle(String title);
        public void setPageSubtitle(String subtitle);
        
        public void setDisplayMode(DisplayMode applicationMode);
        
        public void lockScreen();
        public void unlockScreen();
    }
    
    public static final Object ROOT_SLOT = new Object();
    
    private static RootPresenter instance = null;
    
    public static RootPresenter get() {
		return instance;
	}
	
	private boolean isResetting;
    
    /**
     * Creates a proxy class for a presenter that can contain tabs.
     *
     * @param eventBus The event bus.
     */
    @Inject
    public RootPresenter(final Display view, 
			com.google.gwt.event.shared.EventBus eventBus,
			ClientSideConstants clientSideConstants) {
        super(eventBus, view);
        
        if (instance == null){
            return;
        }
        instance = this;
        setVisible(true);
        
        view.setPageTitle(clientSideConstants.getAppTitle());
        view.setPageSubtitle("");
        view.setPageDescription(clientSideConstants.getAppDescription());
        view.setPageKeywords(clientSideConstants.getAppKeywords());
    }

    @Override
    protected void onBind() {
        super.onBind();

        addRegisteredHandler(ResetPresentersEvent.getType(), this);
        addRegisteredHandler(RevealRootContentEvent.getType(), this);
        addRegisteredHandler(RevealRootLayoutContentEvent.getType(), this);
        addRegisteredHandler(RevealRootPopupContentEvent.getType(), this);
        addRegisteredHandler(LockInteractionEvent.getType(), this);
    }

    @Override
    public void onResetPresenters(ResetPresentersEvent resetPresentersEvent) {
        if (!isResetting) {
            isResetting = true;
            exposedInternalReset();
            isResetting = false;
        }
    }

    @Override
    public void onRevealRootContent(
            final RevealRootContentEvent revealContentEvent) {
        getView().setDisplayMode(DisplayMode.normal);
        setInSlot(ROOT_SLOT, revealContentEvent.getContent());
    }

    @Override
    public void onRevealRootLayoutContent(
            final RevealRootLayoutContentEvent revealContentEvent) {
        getView().setDisplayMode(DisplayMode.application);
        setInSlot(ROOT_SLOT, revealContentEvent.getContent());
    }

    @Override
    public void onRevealRootPopupContent(
            final RevealRootPopupContentEvent revealContentEvent) {
        addToPopupSlot(revealContentEvent.getContent());
    }

    @Override
    public void onLockInteraction(LockInteractionEvent lockInteractionEvent) {
        if (lockInteractionEvent.shouldLock()) {
            getView().lockScreen();
        } else {
            getView().unlockScreen();
        }
    }
    
    /**
     * set the value of the last history token to be then redirect 
     * @param url
     */
    public void redirect(String url) {
        getView().redirect(url);
    }

    /**
     * sets the main title of the page.  This title will be appended along with
     * the subtitle
     * @param mainTitle
     */
    public void setPageTitle(String mainTitle) {
        getView().setPageTitle(mainTitle);
    }

    /**
     * sets the main title of the page.  This title will be appended along with
     * the subtitle
     * @param mainTitle
     */
    public void setPageSubtitle(String subtitle) {
        getView().setPageSubtitle(subtitle);
    }
    
    /**
     * sets the value of the given meta data key pair.  An Exception will be 
     * thrown if the name does not already exist
     */
    public void setMetaDataKeyPair(String name, String content) {
        getView().setMetaDataKeyPair(name, content);
    }

    /**
     * set the value of the meta tag for page description to the string given
     * @param desc
     */
    public void setPageDescription(String desc) {
        getView().setPageDescription(desc);
    }

    /**
     * sets the additional keywords for the current subpage of the site.  If
     * the default keywords variable has not been set, then the current list of
     * keywords in the dom meta element will become the default and will be
     * added to the list of keywords each time
     */
    public void setPageKeywords(String keywords) {
        getView().setPageKeywords(keywords);
    }

}
