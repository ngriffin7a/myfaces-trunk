package net.sourceforge.myfaces.config;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ExternalContext;

/**
 * Holds all configuration init parameters (from web.xml) that are independent
 * from the core implementation. The parameters in this class are available to
 * all shared, component and implementation classes.
 * See RuntimeConfig for configuration infos that come from the faces-config
 * files and are needed by the core implementation.
 */
public class MyfacesConfig
{
    private static final Log log = LogFactory.getLog(MyfacesConfig.class);

    private static final String APPLICATION_MAP_PARAM_NAME = MyfacesConfig.class.getName();

    private static final String  INIT_PARAM_PRETTY_HTML = "net.sourceforge.myfaces.PRETTY_HTML";
    private static final boolean INIT_PARAM_PRETTY_HTML_DEFAULT = true;

    private static final String  INIT_PARAM_ALLOW_JAVASCRIPT = "net.sourceforge.myfaces.ALLOW_JAVASCRIPT";
    private static final boolean INIT_PARAM_ALLOW_JAVASCRIPT_DEFAULT = true;

    private static final String  INIT_PARAM_DETECT_JAVASCRIPT = "net.sourceforge.myfaces.DETECT_JAVASCRIPT";
    private static final boolean INIT_PARAM_DETECT_JAVASCRIPT_DEFAULT = false;

    private static final String  INIT_PARAM_AUTO_SCROLL = "net.sourceforge.myfaces.AUTO_SCROLL";
    private static final boolean INIT_PARAM_AUTO_SCROLL_DEFAULT = false;

    private boolean _prettyHtml;
    private boolean _detectJavascript;
    private boolean _allowJavascript;
    private boolean _autoScroll;

    public static MyfacesConfig getCurrentInstance(ExternalContext extCtx)
    {
        MyfacesConfig myfacesConfig = (MyfacesConfig)extCtx
                                        .getApplicationMap().get(APPLICATION_MAP_PARAM_NAME);
        if (myfacesConfig != null) return myfacesConfig;

        myfacesConfig = new MyfacesConfig();
        extCtx.getApplicationMap().put(APPLICATION_MAP_PARAM_NAME, myfacesConfig);

        myfacesConfig.setPrettyHtml(getBooleanInitParameter(extCtx, INIT_PARAM_PRETTY_HTML,
                                                                    INIT_PARAM_PRETTY_HTML_DEFAULT));
        myfacesConfig.setAllowJavascript(getBooleanInitParameter(extCtx, INIT_PARAM_ALLOW_JAVASCRIPT,
                                                                         INIT_PARAM_ALLOW_JAVASCRIPT_DEFAULT));
        myfacesConfig.setDetectJavascript(getBooleanInitParameter(extCtx, INIT_PARAM_DETECT_JAVASCRIPT,
                                                                          INIT_PARAM_DETECT_JAVASCRIPT_DEFAULT));
        myfacesConfig.setAutoScroll(getBooleanInitParameter(extCtx, INIT_PARAM_AUTO_SCROLL,
                                                                    INIT_PARAM_AUTO_SCROLL_DEFAULT));
        return myfacesConfig;
    }

    private static boolean getBooleanInitParameter(ExternalContext externalContext,
                                                   String paramName,
                                                   boolean defaultValue)
    {
        String strValue = externalContext.getInitParameter(paramName);
        if (strValue == null)
        {
            if (log.isInfoEnabled()) log.info("No context init parameter '" + paramName + "' found, using default value " + defaultValue);
            return defaultValue;
        }
        else if (strValue.equalsIgnoreCase("true") || strValue.equalsIgnoreCase("on") || strValue.equalsIgnoreCase("yes"))
        {
            return true;
        }
        else if (strValue.equalsIgnoreCase("false") || strValue.equalsIgnoreCase("off") || strValue.equalsIgnoreCase("no"))
        {
            return false;
        }
        else
        {
            if (log.isWarnEnabled()) log.warn("Wrong context init parameter '" + paramName + "' (='" + strValue + "'), using default value " + defaultValue);
            return defaultValue;
        }
    }

    public boolean isPrettyHtml()
    {
        return _prettyHtml;
    }

    public void setPrettyHtml(boolean prettyHtml)
    {
        _prettyHtml = prettyHtml;
    }

    public boolean isDetectJavascript()
    {
        return _detectJavascript;
    }

    public void setDetectJavascript(boolean detectJavascript)
    {
        _detectJavascript = detectJavascript;
    }

    /**
     * Do not use this in renderers directly!
     * You should use {@link net.sourceforge.myfaces.renderkit.html.util.JavascriptUtils#isJavascriptAllowed}
     * to determine if javascript is allowed or not.
     */
    public boolean isAllowJavascript()
    {
        return _allowJavascript;
    }

    public void setAllowJavascript(boolean allowJavascript)
    {
        _allowJavascript = allowJavascript;
    }

    public boolean isAutoScroll()
    {
        return _autoScroll;
    }

    public void setAutoScroll(boolean autoScroll)
    {
        _autoScroll = autoScroll;
    }
}
