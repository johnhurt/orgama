/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client.view;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.*;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.orgama.client.i18n.OrgamaConstants;
import org.orgama.shared.enm.ButtonSet;
import org.orgama.shared.enm.DialogResult;

/**
 * Singleton and ui binder element that can be used to block user input from
 * a given area, element, or the entire screen.  The singleton for this object 
 * is placed at placed at the root of the application and 
 * @author kguthrie
 */
public class BlockUi extends Composite {
    
	@BindingAnnotation @Target({FIELD, PARAMETER, METHOD}) @Retention(RUNTIME)
	public static @interface BackgroundColor {}

	@BindingAnnotation @Target({FIELD, PARAMETER, METHOD}) @Retention(RUNTIME)
	public static @interface BackgroundOpacity {}
	
    public interface BlockUiUiBinder extends UiBinder<Widget, BlockUi> {}
    
    private static BlockUi instance = null;
	
	public static BlockUi get() {
		return instance;
	}
    
    private Element blockUiElement;
    private Element blockUiBackgroundElement;
    
    private String backgroundColor = "#404040";
    private double backgroundOpacity = 0.5;
    
    /** default handler for the block ui buttons.  Only unblocks the ui */
    protected static ClickHandler defaultClickHandler;
    
    protected static HandlerRegistration btnOkayHandlerRegistration = null;
    protected static HandlerRegistration btnCancelHandlerRegistration = null;
    protected static HandlerRegistration btnYesHandlerRegistration = null;
    protected static HandlerRegistration btnNoHandlerRegistration = null;
    
    @UiField HTMLPanel pnlBlockUiBackground;
    @UiField HTMLPanel pnlBlockUiContentContainer;
    @UiField HTMLPanel pnlBlockUiUserContent;
    @UiField HTMLPanel pnlBlockUiControls;
    
    @UiField Button btnOkay;
    @UiField Button btnCancel;
    @UiField Button btnYes;
    @UiField Button btnNo;
    
    
    private static Element glass;
    
    @Inject
    BlockUi(BlockUiUiBinder uiBinder) {
        
		instance = this;
        
        initWidget(uiBinder.createAndBindUi(this));
        
        initElementStyles();
        
        glass = null;
        
        defaultClickHandler = new ClickHandler() {

            @Override
            public void onClick(ClickEvent e) {
                BlockUi.unblock();
            }
        };
    }
    
	@Inject(optional = true)
	private void setBackgroundColor(@BackgroundColor String color) {
		if (!color.startsWith("#")) {
			color = "#" + color;
		}
		backgroundColor = color;
	}
	
	@Inject(optional = true)
	private void setBackgroundOpacity(@BackgroundOpacity double opacity) {
		this.backgroundOpacity = opacity;
	}
	
    /**
     * initialize the style of the block ui element
     */
    private void initElementStyles() {
        Style style;
        
        blockUiElement = this.getElement();
        blockUiBackgroundElement = pnlBlockUiBackground.getElement();
        
        style = blockUiElement.getStyle();
        
        unblock();
    }
    
    /**
     * Block the entire window with the default color and opacity
     */
    public static void blockAll() {
		instance.blockAllDefault();
    }
    
    /**
     * Block all ui including the block ui container with clear div
     */
    public static void glass() {
        if (glass == null) {
            glass = Document.get().createDivElement();

            Style style = glass.getStyle();
            style.setPosition(Style.Position.ABSOLUTE);
            style.setLeft(0, Style.Unit.PX);
            style.setTop(0, Style.Unit.PX);
            style.setRight(0, Style.Unit.PX);
            style.setBottom(0, Style.Unit.PX);
            style.setZIndex(2147483647); // Maximum z-index
        }
        
        Document.get().getBody().appendChild(glass);
    }
    
    /**
     * remove the glass
     */
    public static void unglass() {
        if (glass == null) {
            return;
        }
        Document.get().getBody().removeChild(glass);
    }
    
    /**
     * Block the entire client window with the given color and opacity of 
     * background
     */
    public void blockAllDefault() {
        Style style = instance.blockUiElement.getStyle();
        
        style.setTop(0, Style.Unit.PX);
        style.setLeft(0, Style.Unit.PX);
        
        style = blockUiBackgroundElement.getStyle();
        
        style.setBackgroundColor(backgroundColor);
        style.setOpacity(backgroundOpacity);
        
        setHeight("100%");
        setWidth("100%");
        setVisible(true);
    }
    
    /**
     * Unblocks the ui.  Sets the block ui panel to invisible
     */
    public static void unblock() {
        instance.setVisible(false);
    }
    
    /**
     * Blocks the ui and shows the given message with the default display 
     * options.  No UiElements will be placed, so the call to unblock must 
     * be done programmatically 
     * @param message 
     */
    public static void blockAllWithMessage(String message) {
        blockAllWithMessage(message, ButtonSet.nil, null);
    }
    
    /**
     * Block the ui and show the message with the default style and shows an 
     * Okay button that will unblock the ui
     * @param message
     */
    public static void blockAllWithMessage(String message, ButtonSet buttons, 
            final DialogResponseHandler responseHandler) {
        
        HTML messageContent = new HTML(SafeHtmlUtils.fromString(message));
        messageContent.setWidth("500px");
        blockAllWithWidget(messageContent, buttons, responseHandler);
    }
    
    /**
     * block the entire page with the given (simple) widget.  This method will
     * also add a set of buttons and use the given response handler to handle 
     * the buttons
     */
    public static void blockAllWithWidget(Widget widget, ButtonSet buttons, 
            final DialogResponseHandler responseHandler) {
        
        blockAll();
        instance.pnlBlockUiContentContainer.setVisible(true);
        
        instance.pnlBlockUiUserContent.clear();
        instance.pnlBlockUiUserContent.add(widget);
        
        // if there are no buttons to be shown, then return
        if (buttons == ButtonSet.nil) {
            instance.pnlBlockUiControls.setVisible(false);
            return;
        }
        
        switch (buttons) {
            case okay: {
                instance.pnlBlockUiControls.setVisible(true);
                instance.btnOkay.setVisible(true);
                instance.btnCancel.setVisible(false);
                instance.btnYes.setVisible(false);
                instance.btnNo.setVisible(false);
                addResponseHandlerToButton(DialogResult.okay, responseHandler);
                
                break;
            }
            case cancel: {
                instance.pnlBlockUiControls.setVisible(true);
                instance.btnOkay.setVisible(false);
                instance.btnCancel.setVisible(true);
                instance.btnYes.setVisible(false);
                instance.btnNo.setVisible(false);
                addResponseHandlerToButton(DialogResult.cancel, 
                        responseHandler);
                
                break;
            }
            case okayCancel: {
                instance.pnlBlockUiControls.setVisible(true);
                instance.btnOkay.setVisible(true);
                instance.btnCancel.setVisible(true);
                instance.btnYes.setVisible(false);
                instance.btnNo.setVisible(false);
                addResponseHandlerToButton(DialogResult.okay, responseHandler);
                addResponseHandlerToButton(DialogResult.cancel, 
                        responseHandler);
                
                break;
            }
            case yesNo: {
                instance.pnlBlockUiControls.setVisible(true);
                instance.btnOkay.setVisible(false);
                instance.btnCancel.setVisible(false);
                instance.btnYes.setVisible(true);
                instance.btnNo.setVisible(true);
                addResponseHandlerToButton(DialogResult.yes, responseHandler);
                addResponseHandlerToButton(DialogResult.no, 
                        responseHandler);
                break;
            }
        }
        
        //Focus on the appropriate button
        if (buttons == ButtonSet.yesNo) {
            instance.btnYes.getElement().focus();
        }
        else {
            instance.btnOkay.getElement().focus();
        }
    }
    
    /**
     * add the given handler o the button corresponding to the dialog result 
     * that was given.  If the result is null, then the default click handler 
     * is added to the button instead
     * @param dResult
     * @param handler 
     */
    protected static void addResponseHandlerToButton(final DialogResult dResult, 
            final DialogResponseHandler handler) {
        ClickHandler clickHandler;
        /**
         * nil dialog result means do nothing
         */
        if (dResult == DialogResult.nil) {
            return;
        }
        
        if (handler == null) {
            clickHandler = defaultClickHandler;
        }
        else {
            clickHandler = new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    handler.handleResponse(dResult);
                }
            };
        }
        
        switch (dResult) {
            case okay: {
                if (btnOkayHandlerRegistration != null) {
                    btnOkayHandlerRegistration.removeHandler();
                }
                btnOkayHandlerRegistration = 
                        instance.btnOkay.addClickHandler(clickHandler);
                break;
            }
            case cancel: {
                if (btnCancelHandlerRegistration != null) {
                    btnCancelHandlerRegistration.removeHandler();
                }
                btnCancelHandlerRegistration = 
                        instance.btnCancel.addClickHandler(clickHandler);
                break;
            }
            case yes: {
                if (btnYesHandlerRegistration != null) {
                    btnYesHandlerRegistration.removeHandler();
                }
                btnYesHandlerRegistration = 
                        instance.btnYes.addClickHandler(clickHandler);
                break;
            }
            case no: {
                if (btnNoHandlerRegistration != null) {
                    btnNoHandlerRegistration.removeHandler();
                }
                btnNoHandlerRegistration = 
                        instance.btnNo.addClickHandler(clickHandler);
                break;
            }
        }
    }
}
