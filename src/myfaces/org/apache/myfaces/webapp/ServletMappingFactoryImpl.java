package net.sourceforge.myfaces.webapp;

import net.sourceforge.myfaces.MyFacesConfig;

import javax.servlet.ServletContext;

/**
 * TODO: description
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ServletMappingFactoryImpl
    implements ServletMappingFactory
{
    private ServletMapping _servletMapping = null;

    public ServletMapping getServletMapping(ServletContext servletContext)
    {
        if (_servletMapping == null)
        {
            if (MyFacesConfig.isFileExtensionServletMapping())
            {
                _servletMapping = new ServletMappingWithExtension();
            }
            else
            {
                _servletMapping = new ServletMappingWithVirtualPath();
            }
        }
        return _servletMapping;
    }
}
