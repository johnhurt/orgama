/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client.widget;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.TextBox;

/**
 *
 * @author kguthrie
 */
public class WatermarkedTextBox extends TextBox 
        implements BlurHandler, FocusHandler {
	
    String watermark;
    HandlerRegistration blurHandler;
    HandlerRegistration focusHandler;
    String naturalColor;
	boolean useNative;

	public WatermarkedTextBox() {
		this("");
	}
	
    public WatermarkedTextBox(String defaultValue) {
        super();
        setText(defaultValue);
		useNative = true;
    }

    public WatermarkedTextBox(String defaultValue, String watermark) {
        this(defaultValue);
        setWatermark(watermark);
    }

    public WatermarkedTextBox(String watermark, 
            double fontSize, Style.Unit units) {
        this("", watermark);
        setFontSize(fontSize, units);
    }
    
    /**
     * Adds a watermark if the parameter is not NULL or EMPTY
     *
     * @param watermark
     */
    public final void setWatermark(final String watermark) {
        this.watermark = watermark;
		
        if ((watermark != null) && 
			(!watermark.trim().isEmpty()) &&
			(!setNativeIfSupported(watermark))) {
            blurHandler = addBlurHandler(this);
            focusHandler = addFocusHandler(this);
            enableWatermark();
        } else {
            // Remove handlers
			if (blurHandler != null) {
				blurHandler.removeHandler();
				blurHandler = null;
			}
			
			if (focusHandler != null) {
				focusHandler.removeHandler();
				focusHandler = null;
			}
        }
    }

	/**
	 * Checks to see if placeholder is supported natively.
	 * @return 
	 */
	private boolean setNativeIfSupported(String watermark) {
		if (!useNative) {
			return false;
		}
		
		try {
			this.getElement().setPropertyString("placeholder", watermark);
		}
		catch(Exception ex) {
			useNative = false;
		}
		
		return useNative;
	}
	
	/**
	 * Set the placeholder for the text box.  This is synonymous with the 
	 * watermark
	 * @param placeholder 
	 */
	public void setPlaceholder(String placeholder) {
		setWatermark(placeholder);
	}
	
	/**
	 * get the placeholder.  This is synonymous with getting the watermark
	 * @return 
	 */
	public String getPlaceholder() {
		return watermark;
	}
	
    @Override
    public void onBlur(BlurEvent event) {
        enableWatermark();
    }

    void enableWatermark() {
        String text = getText();
        if ((text.length() == 0) || (text.equalsIgnoreCase(watermark))) {
            // Show watermark
            setText(watermark);
            this.naturalColor = this.getElement().getStyle().getColor();
            this.getElement().getStyle().setColor("DarkGray");
        }
    }

    /**
     * Set the font size
     * @param fontSize 
     */
    private void setFontSize(double fontSize, Style.Unit units) {
        getElement().getStyle().setFontSize(fontSize, units);
    }
    
    @Override
    public void onFocus(FocusEvent event) {
        if (getText().equalsIgnoreCase(watermark)) {
            // Hide watermark
            setText("");
            this.getElement().getStyle().setColor(naturalColor);
        }
    }
}
