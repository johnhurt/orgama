package org.orgama.server.config;

import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.server.actionvalidator.ActionValidator;
import com.gwtplatform.dispatch.server.guice.HandlerModule;
import com.gwtplatform.dispatch.shared.Action;
import com.gwtplatform.dispatch.shared.Result;
import java.util.LinkedList;
import java.util.List;

/**
 * 1st level implementation of a basic orgama extension module. This is an
 * abstract class that provides a simple interface to add handler and servlet
 * bindings
 *
 * @author kguthrie
 */
public abstract class BaseExtensionModule extends ServletModule
		implements OrgamaExtensionModule {

	private final ExposedHandlerModule handlerModule;
	private final List<Module> otherModules;

	public BaseExtensionModule() {
		handlerModule = new ExposedHandlerModule() {
			@Override
			protected void configureHandlers() {
				addHandlerBindings();
			}
		};

		otherModules = new LinkedList<Module>();
	}

	@Override
	protected void configureServlets() {
		addServerBindings();
	}

	@Override
	public HandlerModule getHandlerModule() {
		return handlerModule;
	}

	@Override
	public ServletModule getServletModule() {
		return this;
	}

	@Override
	public List<Module> getOtherModules() {
		return otherModules;
	}

	protected void addModule(Module module) {
		otherModules.add(module);
	}

	/**
	 * This is how a handler is registered to handle actions of the given type.
	 * @param <A> Type of {@link Action}
	 * @param <R> Type of {@link Result}
	 * @param actionClass Implementation of {@link Action} to link and bind
	 * @param handlerClass Implementation of {@link ActionHandler} to link and
	 * bind
	 */
	protected <A extends Action<R>, R extends Result> void bindHandler(
			Class<A> actionClass,
			Class<? extends ActionHandler<A, R>> handlerClass) {
		handlerModule.bindHandlerExp(actionClass, handlerClass);
	}

	/**
	 * Bind the given handler to handle the given action type and validate that
	 * action type with the given action validator
	 * @param <A> Type of {@link Action}
	 * @param <R> Type of {@link Result}
	 * @param actionClass Implementation of {@link Action} to link and bind
	 * @param handlerClass Implementation of {@link ActionHandler} to link and
	 * bind
	 * @param actionValidator Implementation of {@link ActionValidator} to link
	 * and bind
	 */
	protected <A extends Action<R>, R extends Result> void bindHandler(
			Class<A> actionClass, 
			Class<? extends ActionHandler<A, R>> handlerClass,
			Class<? extends ActionValidator> actionValidator) {
		handlerModule.bindHandlerExp(
				actionClass, handlerClass, actionValidator);
	}

	protected abstract void addHandlerBindings();

	protected abstract void addServerBindings();
}
