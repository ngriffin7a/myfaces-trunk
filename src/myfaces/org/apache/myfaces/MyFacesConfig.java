/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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

import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Global configuration for MyFaces.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesConfig
{
    //private static final String _PROPERTY_FILE = "myfaces.properties";

    private static final String PARAM_checkJspModification = "myfaces_CheckJspModification";
    private static final boolean DEFAULT_checkJspModification = true;

    private static final String PARAM_servletMappingMode = "myfaces_ServletMappingMode";
    private static final String DEFAULT_servletMappingMode = "virtual_path";

    private static final String PARAM_logLevel = "myfaces_LogLevel";
    private static final Level  DEFAULT_logLevel = Level.INFO;
    private static final Map LOG_LEVELS = new HashMap();
    static
    {
        LOG_LEVELS.put("FINEST", Level.FINEST);
        LOG_LEVELS.put("FINER", Level.FINER);
        LOG_LEVELS.put("FINE", Level.FINE);
        LOG_LEVELS.put("INFO", Level.INFO);
        LOG_LEVELS.put("WARNING", Level.WARNING);
        LOG_LEVELS.put("SEVERE", Level.SEVERE);
    }

    public static final int STATE_SAVING_MODE__SERVER_SESSION = 1;
    public static final int STATE_SAVING_MODE__CLIENT_SERIALIZED = 2;
    public static final int STATE_SAVING_MODE__CLIENT_MINIMIZED = 3;
    public static final int STATE_SAVING_MODE__CLIENT_MINIMIZED_ZIPPED = 4;
    private static final String PARAM_stateSavingMode = "myfaces_StateSavingMode";
    private static final int DEFAULT_stateSavingMode = STATE_SAVING_MODE__CLIENT_MINIMIZED;

    private static final String PARAM_autoCreateRequestScopeBeans = "myfaces_AutoCreateRequestScopeBeans";
    private static final boolean DEFAULT_autoCreateRequestScopeBeans = true;

    private static final String PARAM_discardInternalAttributes = "myfaces_DiscardInternalAttributes";
    private static final boolean DEFAULT_discardInternalAttributes = true;

    private static final String PARAM_disableJspParser = "myfaces_DisableJspParser";
    private static final boolean DEFAULT_disableJspParser = false;

    private static final String PARAM_defaultLanguage = "myfaces_DefaultLanguage";
    private static final String DEFAULT_defaultLanguage = "en";

    private static final String CONFIG_MAP_ATTR = MyFacesConfig.class.getName() + ".MAP";

    private static final String PARAM_wrapPageContext = "myfaces_WrapPageContext";
    private static final boolean DEFAULT_wrapPageContext = true;


    private MyFacesConfig() {}

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
     * @see net.sourceforge.myfaces.webapp.ServletMappingWithExtension
     * @see net.sourceforge.myfaces.webapp.ServletMappingWithVirtualPath
     */
    public static String getServletMappingMode(ServletContext servletContext)
    {
        return getStringInitParameter(servletContext,
                                      PARAM_servletMappingMode,
                                      DEFAULT_servletMappingMode);
    }

    /**
     * Get the log level, that should be used by the {@link net.sourceforge.myfaces.util.logging.MyFacesLogger MyFacesLogger}.
     */
    public static Level getLogLevel(ServletContext servletContext)
    {
        Level level = (Level)findInitParameterInMap(servletContext,
                                                    PARAM_logLevel);
        if (level != null)
        {
            return level;
        }

        String strValue = servletContext.getInitParameter(PARAM_logLevel);
        if (strValue == null)
        {
            level = DEFAULT_logLevel;
            LogUtil.getLogger().info("No context init parameter '" + PARAM_logLevel + "' found, using default value " + DEFAULT_logLevel);
        }
        else
        {
            level = (Level)LOG_LEVELS.get(strValue);
            if (level == null)
            {
                level = DEFAULT_logLevel;
                LogUtil.getLogger().info("Wrong context init parameter '" + PARAM_logLevel + "' (='" + strValue + "'), using default value " + level);
            }
        }

        saveInitParameterInMap(servletContext, PARAM_logLevel, level);
        return level;
    }

    /**
     * See web.xml in the examples webapp for documentation!
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
            LogUtil.getLogger().info("No context init parameter '" + PARAM_stateSavingMode + "' found, using default value " + mode);
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
            LogUtil.getLogger().info("Wrong context init parameter '" + PARAM_stateSavingMode + "' (='" + strValue + "'), using default value " + mode);
        }

        saveInitParameterInMap(servletContext, PARAM_stateSavingMode, mode);
        return mode.intValue();
    }

    /**
     * See web.xml in the examples webapp for documentation!
     */
    public static boolean isAutoCreateRequestScopeBeans(ServletContext servletContext)
    {
        return getBooleanInitParameter(servletContext,
                                       PARAM_autoCreateRequestScopeBeans,
                                       DEFAULT_autoCreateRequestScopeBeans);
    }

    /**
     * See web.xml in the examples webapp for documentation!
     */
    public static boolean isDiscardInternalAttributes(ServletContext servletContext)
    {
        return getBooleanInitParameter(servletContext,
                                       PARAM_discardInternalAttributes,
                                       DEFAULT_discardInternalAttributes);
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

    /**
     * See web.xml in the examples webapp for documentation!
     */
    public static boolean isWrapPageContext(ServletContext servletContext)
    {
        return getBooleanInitParameter(servletContext,
                                       PARAM_wrapPageContext,
                                       DEFAULT_wrapPageContext);
    }

    /**
     * See web.xml in the examples webapp for documentation!
     */
    public static String getDefaultLanguage(ServletContext servletContext)
    {
        return getStringInitParameter(servletContext,
                                      PARAM_defaultLanguage,
                                      DEFAULT_defaultLanguage);
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
            LogUtil.getLogger().info("No context init parameter '" + paramName + "' found, using default value " + paramValue);
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
            LogUtil.getLogger().info("No context init parameter '" + paramName + "' found, using default value " + bParam);
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
            LogUtil.getLogger().info("Wrong context init parameter '" + paramName + "' (='" + strValue + "'), using default value " + bParam);
        }

        saveInitParameterInMap(servletContext, paramName, bParam);
        return bParam.booleanValue();
    }

}
