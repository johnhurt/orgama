// <copyright file="BetterTokenFormatter.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.client;

import org.orgama.shared.Logger;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatException;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * token formatter that requires places to have a complete path to their 
 * location as their name token, and requires presenter parameters to be 
 * be separated between the path with a ?, parameters separated with a & and
 * the name and value to be separated with a =
 * @author kguthrie
 */
public class BetterTokenFormatter implements TokenFormatter {

    /**
     * make a string from a list of history tokens.  In this case only the first
     * place request will be used
     * @param placeRequestHierarchy
     * @return
     * @throws TokenFormatException 
     */
    @Override
    public String toHistoryToken(List<PlaceRequest> placeRequestHierarchy)
            throws TokenFormatException {

        if (placeRequestHierarchy.size() == 0) {
            return "";
        }

        return toPlaceToken(placeRequestHierarchy.get(0));
    }

    @Override
    public PlaceRequest toPlaceRequest(String placeToken)
            throws TokenFormatException {
        PlaceRequest result = null;
        int qMarkLocations;
        int ampLocation;
        String[] paramParts;
        String paramPart;

        Logger.trace("getting place request for " + placeToken);
        
        try {
            qMarkLocations = placeToken.indexOf('?');

            if (qMarkLocations < 0) {
                ampLocation = placeToken.indexOf('&');
                if (ampLocation >= 0) {
                    placeToken = placeToken.substring(0, ampLocation);
                }
                result = new PlaceRequest(placeToken);
            } else {
                result = new PlaceRequest(
                        placeToken.substring(0, qMarkLocations));
                paramPart = placeToken.substring(qMarkLocations + 1);
                paramParts = paramPart.split("&");
                result = addParamsToRequest(result, paramParts);
            }

        } catch (Exception ex) {
            throw new TokenFormatException("Error formatting place token - "
                    + ex.getClass().getName() + ": " + ex.getMessage());
        }

        return result;
    }

    /**
     * adds the params from the array to the request
     * @param request
     * @param params 
     */
    private PlaceRequest addParamsToRequest(PlaceRequest request, 
            String[] params) throws TokenFormatException {
        int equalsIndex;
        String paramName;
        String paramValue;

        for (String paramString : params) {
            if (paramString.length() == 0) {
                continue;
            }

            equalsIndex = paramString.indexOf('=');

            if (equalsIndex < 0) {
                paramName = paramString;
                paramValue = "";
                request = request.with(paramName, paramValue);
                continue;
            }
            if (equalsIndex == 0) {
                throw new TokenFormatException("an empty string is not a "
                        + "valid name for a parameter name");
            }

            paramName = paramString.substring(0, equalsIndex);

            if (equalsIndex == (paramString.length() - 1)) {
                paramValue = "";
            } else {
                paramValue = paramString.substring(equalsIndex + 1);
            }

            request = request.with(paramName, paramValue);
        }
        
        return request;
    }

    @Override
    public List<PlaceRequest> toPlaceRequestHierarchy(String historyToken)
            throws TokenFormatException {

		if (historyToken != null && historyToken.endsWith("_=_")) {
			historyToken = historyToken.substring(0, historyToken.length() - 3);
		}
		
        List<PlaceRequest> result = new ArrayList<PlaceRequest>();

        result.add(toPlaceRequest(historyToken));
        
        return result;
    }

    @Override
    public String toPlaceToken(PlaceRequest request)
            throws TokenFormatException {
        
        StringBuilder result = new StringBuilder();
        
        String paramValue;
        int paramCount = 0;
        
        result.append(request.getNameToken());
        
        if (request.getParameterNames().isEmpty()) {
            return result.toString();
        }
        
        result.append("?");
        
        for (String paramName : request.getParameterNames()) {
            if (paramName.length() == 0) {
                continue;
            }
            if (paramCount++ > 0) {
                result.append("&");
            }
            paramValue = request.getParameter(paramName, "");
            result.append(paramName);
            
            if (paramValue == null || paramValue.trim().length() <= 0) {
                continue;
            }
            
            result.append("=");
            result.append(paramValue);
        }
        
        Logger.debug("produced place token " + result);
        
        return result.toString();
    }
}
