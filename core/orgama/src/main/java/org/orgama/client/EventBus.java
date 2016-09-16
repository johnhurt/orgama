package org.orgama.client;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import java.util.HashMap;


/**
 * Event hub for the entire application
 * @author kguthrie
 */
public class EventBus {
	
	private static final HashMap<String, Timer> timersByChannel = 
			new HashMap<String, Timer>();
	
	/**
	 * Class for providing the static instance of the contained event bus
	 */
	public static class Provider implements
			com.google.inject.Provider<com.google.gwt.event.shared.EventBus> {

		@Override
		public com.google.gwt.event.shared.EventBus get() {
			return EventBus.get();
		}
		
	}
	
    private static final com.google.gwt.event.shared.EventBus eventBus = 
			new com.google.gwt.event.shared.SimpleEventBus();

    /**
     * fire the given event
     * @param type
     * @param event
     */
    public static void fireEvent(GwtEvent<?> event) {
        eventBus.fireEvent(event);
    }

	/**
	 * fire the given event asynchronously after the given delay in milliseconds
	 * has passed
	 * @param event
	 * @param millisecondsDelay 
	 */
	public static void fireEventWithDelay(final GwtEvent<?> event, 
			int millisecondsDelay) {
		Timer t = new Timer() {

			@Override
			public void run() {
				fireEvent(event);
			}
		};
		t.schedule(millisecondsDelay);
	}
	
	/**
	 * fire the given event asynchronously after the given delay in milliseconds
	 * has passed unless another event is fired with same override channel 
	 * before the delay given here expires; in this case, the event fired here
	 * is overridden and not fired at all.
	 * @param event
	 * @param millisecondsDelay 
	 */
	public static void fireEventWithOverride(final GwtEvent<?> event, 
			String overrideChannel, int millisecondsDelay) {
		
		Timer t = new Timer() {

			@Override
			public void run() {
				fireEvent(event);
			}
		};
		
		t.schedule(millisecondsDelay);
		
		Timer oldTimer = timersByChannel.put(overrideChannel, t);
		
		if (oldTimer != null) {
			oldTimer.cancel();
		}
	}
	
    /**
     * fire the given event from the given source
     * @param event
     * @param source
     */
    public static void fireEvent(GwtEvent<?> event, Object source) {
        eventBus.fireEventFromSource(event, source);
    }

	/**
	 * fire the given event asynchronously from the given source after the 
	 * given delay in milliseconds has passed
	 * @param event
	 * @param millisecondsDelay 
	 */
	public static void fireEventWithDelay(final GwtEvent<?> event, 
			final Object source, int millisecondsDelay) {
		Timer t = new Timer() {

			@Override
			public void run() {
				fireEvent(event, source);
			}
		};
		t.schedule(millisecondsDelay);
	}
	
	/**
	 * fire the given event asynchronously after the given delay in milliseconds
	 * has passed unless another event is fired with same override channel 
	 * before the delay given here expires; in this case, the event fired here
	 * is overridden and not fired at all.
	 * @param event
	 * @param millisecondsDelay 
	 */
	public static void fireEventWithOverride(final GwtEvent<?> event, 
			final Object source, String overrideChannel, 
			int millisecondsDelay) {
		
		Timer t = new Timer() {

			@Override
			public void run() {
				fireEvent(event, source);
			}
		};
		
		t.schedule(millisecondsDelay);
		
		Timer oldTimer = timersByChannel.put(overrideChannel, t);
		
		if (oldTimer != null) {
			oldTimer.cancel();
		}
	}
	
    /**
     * add a handler for the given event type
     * @param <T>
     * @param type
     * @param handler
     */
    public static <T extends EventHandler> HandlerRegistration addHandler(
            GwtEvent.Type<T> type, T handler) {
        return eventBus.addHandler(type, handler);
    }

    /**
     * add an event handler
     * @param <T>
     * @param type
     * @param handler
     * @param sender
     */
    public static <T extends EventHandler> HandlerRegistration addHandler(
            GwtEvent.Type<T> type, T handler, Object source) {
        return eventBus.addHandlerToSource(type, source, handler);
    }
    
    /**
     * gets the singleton instance of the event bus for classes that need 
     * actual instances of it
     * @return 
     */
    public static com.google.gwt.event.shared.EventBus get() {
        return eventBus;
    }
}
