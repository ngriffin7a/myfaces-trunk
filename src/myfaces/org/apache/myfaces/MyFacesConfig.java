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
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Global configuration for MyFaces.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesConfig
{
    private static final String _PROPERTY_FILE = "myfaces.properties";

    private static final String PARAM_JspInfoCaching = "jspInfoCaching";
    private static final String PARAM_ServletMappingType = "servletMappingType";
    private static final String DEFAULT_ServletMappingType = "virtual_path";
    private static final String PARAM_logLevel = "logLevel";
    private static final Level  DEFAULT_logLevel = Level.INFO;


    //private static final String _StateEncodingOnTheFly = "StateEncodingOnTheFly";
    private static final String _StateZipping = "StateZipping";
    private static final String _ComponentsTransientByDefault = "ComponentsTransientByDefault";

    private static final String CONFIG_MAP_ATTR = MyFacesConfig.class.getName() + ".MAP";

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


    private MyFacesConfig() {}

    /**
     * MyFaces needs to parse JSP files when saving state information.
     * Setting this parameter to true, means that JSP files are cached
     * in the servlet context (= application in JSP terminology).
     * During development this should always be set to false, because
     * otherwise changes in JSPs will not be recognized by MyFaces.
     */
    public static boolean isJspInfoCaching(ServletContext servletContext)
    {
        return getBooleanInitParameter(servletContext,
                                       PARAM_JspInfoCaching,
                                       false);
    }

    /**
     * @see net.sourceforge.myfaces.webapp.ServletMappingWithExtension
     * @see net.sourceforge.myfaces.webapp.ServletMappingWithVirtualPath
     */
    public static String getServletMappingType(ServletContext servletContext)
    {
        return getStringInitParameter(servletContext,
                                      PARAM_ServletMappingType,
                                      DEFAULT_ServletMappingType);
    }

    /**
     *
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
     * MyFaces supports two ways of saving state information:
     * 1. "On the fly"
     *    State info is encoded within HREFs and FORMs at once.
     *    Drawback: The state of dynamic request time attributes that
     *              are calculated during JSP processing will not be
     *              saved correctly. Also the state of dynamically added
     *              or removed UIComponents will not be saved correctly.
     * 2. "Tokens"
     *    State info is encoded within HREFs and FORMs as tokens (placeholders)
     *    that are replaced by the real state info after the JSP processing
     *    of MyFaces tags has finished.
     *    Drawback: The usefaces tag must buffer all produced HTML code
     *              within it's body (see javax.servlet.jsp.tagext.BodyContent)
     *              and must do a search'n'replace before writing the
     *              content out to the response stream.
     *
     * @deprecated
     */
    public static boolean isStateEncodingOnTheFly()
    {
        //return getPropertyAsBoolean(_StateEncodingOnTheFly, false);
        return false;
    }

    /**
     * MyFaces supports two methods of encoding the state information:
     * 1. "Normal"
     *    All state values are encoded as normal URL parameters.
     *    i.e. Query parameters in HREFs and hidden inputs in FORMs
     * 2. "Zipped"
     *    All state values are encoded to a String of key/value pairs
     *    that is zipped (GZIP) and encoded to allowed characters (Base64).
     *    This String is then written as one query parameter or hidden
     *    form input.
     */
    public static boolean isStateZipping()
    {
        return getPropertyAsBoolean(_StateZipping, false);
    }

    public static boolean isComponentsTransientByDefault()
    {
        return getPropertyAsBoolean(_ComponentsTransientByDefault, true);
    }







    private static final String _TRUE = "true";
    private static boolean getPropertyAsBoolean(String name, boolean defaultValue)
    {
        String value = getProperty(name);
        if (value == null)
        {
            return defaultValue;
        }
        if(value.equals(_TRUE))
        {
            return true;
        }
        return false;
    }

    private static Properties _myfacesProperties = null;
    private static synchronized String getProperty(String name)
    {
        if (_myfacesProperties == null)
        {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(_PROPERTY_FILE);
            if (inputStream != null)
            {
                _myfacesProperties = new Properties();
                try
                {
                    _myfacesProperties.load(inputStream);
                }
                catch (IOException e)
                {
                }
            }
        }
        return _myfacesProperties.getProperty(name);
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
