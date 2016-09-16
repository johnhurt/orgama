// <copyright file="AbstractHandler.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.server.handler;

import org.orgama.shared.OrgActionException;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import org.orgama.shared.action.AbstractAction;
import org.orgama.shared.action.AbstractResult;
import org.orgama.shared.except.ExceptionByErrorCode;

/**
 * First level implementation of the action handler.  This implementation takes
 * care of copying the id number between the action and the result as well
 * as packaging up serializable exceptions in an OrgActionException so that it
 * can be safely passed on to the client without the dispatch system screwing it
 * up.
 * @author kguthrie
 */
public abstract class AbstractHandler<A extends AbstractAction<R>,
        R extends AbstractResult> implements ActionHandler<A, R> {

    /**
     * this is the method called by the dispatch system it will in turn call the
     * impl version that is to be filled in by the actual implementation
     * @param a
     * @param ec
     * @return
     * @throws ActionException 
     */
    @Override
    public final R execute(A a, ExecutionContext ec) throws ActionException {
        try {
            R result = execImpl(a, ec);
            
            if (result != null) {
                result.setId(a.getId());
            }
            
            return result;
        }
        catch(ExceptionByErrorCode ebe) {
            throw new OrgActionException(ebe);
        }
    }
    
    /**
     * this is the undo method actually called by the dispatch system.  This
     * will call the undo implementation and catch any exceptions that can
     * be passed to the client safely
     * @param a
     * @param r
     * @param ec
     * @throws ActionException 
     */
    @Override
    public final void undo(A a, R r, ExecutionContext ec) 
            throws ActionException {
        try {
            undoImpl(a, r, ec);
        }
        catch(ExceptionByErrorCode ebe) {
            throw new OrgActionException(ebe);
        }
    }
    
    
    public abstract R execImpl(A a, ExecutionContext ec);
    public abstract void undoImpl(A a, R r, ExecutionContext ec);
    
}
