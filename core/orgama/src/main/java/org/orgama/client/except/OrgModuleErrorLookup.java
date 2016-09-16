// <copyright file="OrgModuleErrorLookup.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.client.except;

import com.google.inject.Inject;
import org.orgama.client.i18n.OrgExceptionMessages;
import org.orgama.shared.enm.OrgErrorCodes;

/**
 * Error lookup for the mvp module. Because this class extends the base mvp
 * error lookup, it will automatically be registered in the error message
 * registry
 *
 * @author kguthrie
 */
public class OrgModuleErrorLookup extends BaseErrorMessageLookup {

    protected OrgExceptionMessages messages;

    @Inject
    public OrgModuleErrorLookup(OrgExceptionMessages messages) {
        super(OrgErrorCodes.MODULE_CODE);
        messages = this.messages;
    }

    @Override
    public String getMessageForCode(int errorNum) {

        switch (errorNum) {

            case OrgErrorCodes.DATA_ACCESS_ERROR: {
                return messages.dataAccessError();
            }
            
            case OrgErrorCodes.UNKOWN_ERROR:
            default: {
                return messages.unknownError();
            }
        }

    }
}
