package net.sourceforge.myfaces.webapp.webxml;

/**
 * @author gem (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ServletMapping
{
    private String _servletName;
    private Class _servletClass;
    private String _urlPattern;

    public ServletMapping(String servletName, Class servletClass, String urlPattern)
    {
        _servletName = servletName;
        _servletClass = servletClass;
        _urlPattern = urlPattern;
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
