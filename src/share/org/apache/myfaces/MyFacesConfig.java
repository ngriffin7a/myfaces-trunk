/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.myfaces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ExternalContext;

/**
 * Global configuration for MyFaces.
 *
 * TODO: remove legacy param names
 * TODO: optimize access by caching in a map?
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesConfig
{
    private static final Log log = LogFactory.getLog(MyFacesConfig.class);

    private static final String PARAM_allowJavascript_legacy = "myfaces_allow_javascript";
    private static final String PARAM_allowJavascript = "net.sourceforge.myfaces.ALLOW_JAVASCRIPT";
    private static final boolean DEFAULT_allowJavascript = true;

    private static final String PARAM_prettyHtml_legacy = "myfaces_pretty_html";
    private static final String PARAM_prettyHtml = "net.sourceforge.myfaces.PRETTY_HTML";
    private static final boolean DEFAULT_prettyHtml = true;


    private MyFacesConfig()
    {
        // utility class, no instances allowed
    }


    public static boolean isAllowJavascript(ExternalContext externalContext)
    {
        return getBooleanInitParameter(externalContext,
                                       PARAM_allowJavascript,
                                       DEFAULT_allowJavascript,
                                       PARAM_allowJavascript_legacy);
    }

    public static boolean isPrettyHtml(ExternalContext externalContext)
    {
        return getBooleanInitParameter(externalContext,
                                       PARAM_prettyHtml,
                                       DEFAULT_prettyHtml,
                                       PARAM_prettyHtml_legacy);
    }

    private static boolean getBooleanInitParameter(ExternalContext externalContext,
                                                   String paramName,
                                                   boolean defaultValue,
                                                   String legacyParamName)
    {
        if (externalContext == null)
        {
            if (log.isWarnEnabled()) log.warn("Could not get context init parameter '" + paramName + "' because no external context given. Using default value " + defaultValue);
            return defaultValue;
        }

        String strValue = externalContext.getInitParameter(paramName);
        if (strValue == null)
        {
            strValue = externalContext.getInitParameter(legacyParamName);
        }

        if (strValue == null)
        {
            if (log.isInfoEnabled()) log.info("No context init parameter '" + paramName + "' found, using default value " + defaultValue);
            return defaultValue;
        }
        else if (strValue.equalsIgnoreCase("true"))
        {
            return true;
        }
        else if (strValue.equalsIgnoreCase("false"))
        {
            return false;
        }
        else
        {
            if (log.isWarnEnabled()) log.warn("Wrong context init parameter '" + paramName + "' (='" + strValue + "'), using default value " + defaultValue);
            return defaultValue;
        }
    }

}
