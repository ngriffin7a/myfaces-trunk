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
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Global configuration for MyFaces.
 *
 * TODO: rename params to "net.sourceforge.myfaces.XXX" style
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesConfig
{
    private static final Log log = LogFactory.getLog(MyFacesConfig.class);

    private static final String PARAM_allowJavascript = "myfaces_allow_javascript";
    private static final boolean DEFAULT_allowJavascript = true;

    private static final String PARAM_prettyHtml = "myfaces_pretty_html";
    private static final boolean DEFAULT_prettyHtml = true;

    private static final String PARAM_checkJspModification = "myfaces_CheckJspModification";
    private static final boolean DEFAULT_checkJspModification = true;
    
    private static final String PARAM_allowDesignMode = "myfaces_allow_designmode";
    private static final boolean DEFAULT_allowDesignMode = false;

    public static final int STATE_SAVING_MODE__SERVER_SESSION = 1;
    public static final int STATE_SAVING_MODE__CLIENT_SERIALIZED = 2;
    public static final int STATE_SAVING_MODE__CLIENT_MINIMIZED = 3;
    public static final int STATE_SAVING_MODE__CLIENT_MINIMIZED_ZIPPED = 4;
    private static final String PARAM_stateSavingMode = "myfaces_StateSavingMode";
    private static final int DEFAULT_stateSavingMode = STATE_SAVING_MODE__CLIENT_MINIMIZED;

    private static final String PARAM_disableJspParser = "myfaces_DisableJspParser";
    private static final boolean DEFAULT_disableJspParser = false;

    private static final String CONFIG_MAP_ATTR = MyFacesConfig.class.getName() + ".MAP";


    private MyFacesConfig() 
    {
        // utility class, no instances allowed
    }


    public static boolean isAllowJavascript(ExternalContext externalContext)
    {
        return getBooleanInitParameter(externalContext,
                                       PARAM_allowJavascript,
                                       DEFAULT_allowJavascript);
    }

    public static boolean isPrettyHtml(ExternalContext externalContext)
    {
        return getBooleanInitParameter(externalContext,
                                       PARAM_prettyHtml,
                                       DEFAULT_prettyHtml);
    }
    
    public static boolean isAllowDesignMode(ExternalContext externalContext)
    {
        return true;
        /*
        return getBooleanInitParameter(externalContext,
                                       PARAM_allowDesignMode,
                                       DEFAULT_allowDesignMode);
                                       */
    }

    protected static boolean getBooleanInitParameter(ExternalContext externalContext,
                                                     String paramName,
                                                     boolean defaultValue)
    {
        String strValue = (externalContext != null)
            ? externalContext.getInitParameter(paramName) : null;
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









    /**
     * MyFaces needs to parse JSP files when doing the "minimizing state
     * saving" strategy. The parsed infos are (of course) cached in memory.
     * Setting this parameter to true, means that JSP files are checked
     * for modification whenever a parsed info is needed. If the JSP was
     * modified (i.e. has a newer file date than when it was parsed) it is
     * parsed again.
     * In a development environment you should always set this parameter to
     * true. Setting it to false in production might enhance performance.
     */
    public static boolean isCheckJspModification(ServletContext servletContext)
    {
        return getBooleanInitParameter(servletContext,
                                       PARAM_checkJspModification,
                                       DEFAULT_checkJspModification);
    }

    /**
     * See web.xml in the examples webapp for documentation!
     * @deprecated remove, if new state saving is implemented
     */
    public static int getStateSavingMode(ServletContext servletContext)
    {
        Integer mode = (Integer)findInitParameterInMap(servletContext,
                                                       PARAM_stateSavingMode);
        if (mode != null)
        {
            return mode.intValue();
        }

        String strValue = servletContext.getInitParameter(PARAM_stateSavingMode);
        if (strValue == null)
        {
            mode = new Integer(DEFAULT_stateSavingMode);
            if (log.isInfoEnabled()) log.info("No context init parameter '" + PARAM_stateSavingMode + "' found, using default value " + mode);
        }
        else if (strValue.equalsIgnoreCase("server_session"))
        {
            mode = new Integer(STATE_SAVING_MODE__SERVER_SESSION);
        }
        else if (strValue.equalsIgnoreCase("client_serialized"))
        {
            mode = new Integer(STATE_SAVING_MODE__CLIENT_SERIALIZED);
        }
        else if (strValue.equalsIgnoreCase("client_minimized"))
        {
            mode = new Integer(STATE_SAVING_MODE__CLIENT_MINIMIZED);
        }
        else if (strValue.equalsIgnoreCase("client_minimized_zipped"))
        {
            mode = new Integer(STATE_SAVING_MODE__CLIENT_MINIMIZED_ZIPPED);
        }
        else
        {
            mode = new Integer(DEFAULT_stateSavingMode);
            if (log.isWarnEnabled()) log.warn("Wrong context init parameter '" + PARAM_stateSavingMode + "' (='" + strValue + "'), using default value " + mode);
        }

        saveInitParameterInMap(servletContext, PARAM_stateSavingMode, mode);
        return mode.intValue();
    }


    /**
     * See web.xml in the examples webapp for documentation!
     */
    public static boolean isDisableJspParser(ServletContext servletContext)
    {
        return getBooleanInitParameter(servletContext,
                                       PARAM_disableJspParser,
                                       DEFAULT_disableJspParser);
    }



    protected static Object findInitParameterInMap(ServletContext servletContext,
                                            String paramName)
    {
        Map configMap = (Map)servletContext.getAttribute(CONFIG_MAP_ATTR);
        if (configMap == null)
        {
            configMap = new HashMap();
            servletContext.setAttribute(CONFIG_MAP_ATTR, configMap);
            return null;
        }
        else
        {
            return configMap.get(paramName);
        }
    }

    protected static void saveInitParameterInMap(ServletContext servletContext,
                                          String paramName,
                                          Object paramValue)
    {
        Map configMap = (Map)servletContext.getAttribute(CONFIG_MAP_ATTR);
        if (configMap == null)
        {
            configMap = new HashMap();
            servletContext.setAttribute(CONFIG_MAP_ATTR, configMap);
        }
        configMap.put(paramName, paramValue);
    }

    protected static String getStringInitParameter(ServletContext servletContext,
                                                   String paramName,
                                                   String defaultValue)
    {
        String paramValue = (String)findInitParameterInMap(servletContext,
                                                           paramName);
        if (paramValue != null)
        {
            return paramValue;
        }

        paramValue = servletContext.getInitParameter(paramName);
        if (paramValue == null)
        {
            paramValue = defaultValue;
            if (log.isInfoEnabled()) log.info("No context init parameter '" + paramName + "' found, using default value " + paramValue);
        }

        saveInitParameterInMap(servletContext, paramName, paramValue);
        return paramValue;
    }

    protected static boolean getBooleanInitParameter(ServletContext servletContext,
                                                     String paramName,
                                                     boolean defaultValue)
    {
        Boolean bParam = (Boolean)findInitParameterInMap(servletContext,
                                                         paramName);
        if (bParam != null)
        {
            return bParam.booleanValue();
        }

        String strValue = servletContext.getInitParameter(paramName);
        if (strValue == null)
        {
            bParam = defaultValue ? Boolean.TRUE : Boolean.FALSE;
            if (log.isInfoEnabled()) log.info("No context init parameter '" + paramName + "' found, using default value " + bParam);
        }
        else if (strValue.equalsIgnoreCase("true"))
        {
            bParam = Boolean.TRUE;
        }
        else if (strValue.equalsIgnoreCase("false"))
        {
            bParam = Boolean.FALSE;
        }
        else
        {
            bParam = defaultValue ? Boolean.TRUE : Boolean.FALSE;
            if (log.isWarnEnabled()) log.warn("Wrong context init parameter '" + paramName + "' (='" + strValue + "'), using default value " + bParam);
        }

        saveInitParameterInMap(servletContext, paramName, bParam);
        return bParam.booleanValue();
    }

}
