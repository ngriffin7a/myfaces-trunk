package net.sourceforge.myfaces.webapp;

import net.sourceforge.myfaces.MyFacesConfig;

import javax.servlet.ServletContext;

/**
 * DOCUMENT ME!
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
            String mapping = MyFacesConfig.getServletMappingType(servletContext);
            if (mapping.equals(TYPE_EXTENSION))
            {
                _servletMapping = new ServletMappingWithExtension();
            }
            else // (mapping.equals(TYPE_VIRTUAL_PATH))
            {
                _servletMapping = new ServletMappingWithVirtualPath();
            }
        }
        return _servletMapping;
    }
}
