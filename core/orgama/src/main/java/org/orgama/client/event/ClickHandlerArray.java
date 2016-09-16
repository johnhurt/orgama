package org.orgama.client.event;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import java.util.LinkedList;

/**
 * 
 * @author kguthrie
 */
public class ClickHandlerArray implements HasClickHandlers {

    private LinkedList<ClickHandler> handlers;

    public ClickHandlerArray() {
        handlers = new LinkedList<ClickHandler>();
    }

    @Override
    public HandlerRegistration addClickHandler(final ClickHandler handler) {
        HandlerRegistration result = new HandlerRegistration() {

            @Override
            public void removeHandler() {
                handlers.remove(handler);
            }
        };

        handlers.add(handler);

        return result;
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        for (ClickHandler c : handlers) {
            c.onClick((ClickEvent)event);
        }
    }

}
