package net.sourceforge.myfaces.webapp;

import javax.servlet.ServletContext;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface ServletMappingFactory
{
    public static final String TYPE_EXTENSION = "extension";
    public static final String TYPE_VIRTUAL_PATH = "virtual_path";

    public ServletMapping getServletMapping(ServletContext servletContext);
}
