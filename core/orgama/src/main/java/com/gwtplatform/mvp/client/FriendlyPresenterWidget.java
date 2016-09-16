// <copyright file="FriendlyPresenterWidget.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package com.gwtplatform.mvp.client;

import com.google.gwt.event.shared.EventBus;

/**
 * extension of the gwtp widget presenter that exposes a few package scope 
 * methods and members publicly
 * @author kguthrie
 */
public class FriendlyPresenterWidget<I extends View> extends 
        PresenterWidget<I> {
    
    public FriendlyPresenterWidget(EventBus eventBus, I view) {
        super(eventBus, view);
    }
    
    /**
     * Set the visibility of the widget
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    /**
     * Expose the internal reset method
     */
    public void exposedInternalReset() {
        this.internalReset();
    }
}
