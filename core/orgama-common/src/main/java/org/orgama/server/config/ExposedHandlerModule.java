package org.orgama.server.config;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.server.actionvalidator.ActionValidator;
import com.gwtplatform.dispatch.server.guice.HandlerModule;
import com.gwtplatform.dispatch.shared.Action;
import com.gwtplatform.dispatch.shared.Result;

/**
 * handler module that exposes its binding methods publicly
 * @author kguthrie
 */
abstract class ExposedHandlerModule extends HandlerModule {

	/**
	 * Exposed version of the bind handler method
	 * @param <A>
	 * @param <R>
	 * @param actionClass
	 * @param handlerClass 
	 */
	<A extends Action<R>, R extends Result> void bindHandlerExp(
			Class<A> actionClass, 
			Class<? extends ActionHandler<A, R>> handlerClass) {
		super.bindHandler(actionClass, handlerClass); 
	}

	/**
	 * Exposed version of the bind handler method with action validator
	 * @param <A>
	 * @param <R>
	 * @param actionClass
	 * @param handlerClass
	 * @param actionValidator 
	 */
	<A extends Action<R>, R extends Result> void bindHandlerExp(
			Class<A> actionClass, 
			Class<? extends ActionHandler<A, R>> handlerClass, 
			Class<? extends ActionValidator> actionValidator) {
		super.bindHandler(actionClass, handlerClass, actionValidator); 
	}
	
	
	@Override
	protected abstract void configureHandlers();
}
