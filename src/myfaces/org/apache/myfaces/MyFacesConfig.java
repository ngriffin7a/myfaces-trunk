/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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
