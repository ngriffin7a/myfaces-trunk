package net.sourceforge.myfaces.webapp.webxml;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ServletMapping
{
    private String _servletName;
    private Class _servletClass;
    private String _urlPattern;
    private boolean _isExtensionMapping = false;

    public ServletMapping(String servletName,
                          Class servletClass,
                          String urlPattern)
    {
        _servletName = servletName;
        _servletClass = servletClass;
        _urlPattern = urlPattern;
        if (_urlPattern != null)
        {
            if (_urlPattern.startsWith("*."))
            {
                _isExtensionMapping = true;
            }
        }
    }

    public boolean isExtensionMapping()
    {
        return _isExtensionMapping;
    }

    public String getServletName()
    {
        return _servletName;
    }

    public Class getServletClass()
    {
        return _servletClass;
    }

    public String getUrlPattern()
    {
        return _urlPattern;
    }
}
