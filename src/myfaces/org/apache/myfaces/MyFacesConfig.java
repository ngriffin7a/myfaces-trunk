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

import java.util.logging.Level;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.IOException;

/**
 * Global configuration for MyFaces.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesConfig
{
    private static final String _PROPERTY_FILE = "myfaces.properties";
    private static final String _UseJspFileCache = "UseJspFileCache";
    private static final String _UseStateEncodingOnTheFly = "UseStateEncodingOnTheFly";
    private static final String _UseStateZipping = "UseStateZipping";
    private static final String _UseAlwaysSaveComponentValue = "UseAlwaysSaveComponentValue";
    private static final String _UseFileExtensionServletMapping = "UseFileExtensionServletMapping";
    private static final String LOG_LEVEL = "log_level";
    private static final Level DEFAULT_LOG_LEVEL = Level.INFO;

    private MyFacesConfig() {}

    /**
     * MyFaces needs to parse JSP files when saving state information.
     * Setting this parameter to true, means that JSP files are cached
     * in the servlet context (= application in JSP terminology).
     * During development this should always be set to false, because
     * otherwise changes in JSPs will not be recognized by MyFaces.
     */
    public static boolean isJspInfoApplicationCaching()
    {
        return getPropertyAsBoolean(_UseJspFileCache, false);
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
     */
    public static boolean isStateEncodingOnTheFly()
    {
        return getPropertyAsBoolean(_UseStateEncodingOnTheFly, false);
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
        return getPropertyAsBoolean(_UseStateZipping, false);
    }

    /**
     * Normally MyFaces does not save a component's value when it comes from
     * the referenced model value. This assumes, that all model beans are able
     * to save and restore their state themselves.
     * If there are model beans, that are not clever enough to save their state
     * you should set this configuration option to true.
     */
    public static boolean isAlwaysSaveComponentValue()
    {
        return getPropertyAsBoolean(_UseAlwaysSaveComponentValue, true);
    }

    /**
     * @see net.sourceforge.myfaces.webapp.ServletMappingWithExtension
     * @see net.sourceforge.myfaces.webapp.ServletMappingWithVirtualPath
     */
    public static boolean isFileExtensionServletMapping()
    {
        return getPropertyAsBoolean(_UseFileExtensionServletMapping, true);
    }



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

    public static Level getLogLevel()
    {
        String logLevel = getProperty(LOG_LEVEL);
        if (logLevel == null)
        {
            return DEFAULT_LOG_LEVEL;
        }

        Level level = (Level)LOG_LEVELS.get(logLevel);
        if (level == null)
        {
            return DEFAULT_LOG_LEVEL;
        }

        return level;
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
}
