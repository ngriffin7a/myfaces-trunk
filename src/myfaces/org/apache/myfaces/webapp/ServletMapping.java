package net.sourceforge.myfaces.webapp;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public interface ServletMapping
{
    /**
     * Converts the given (response) treeId, so that it can be used as the forward
     * URL by the ViewHandler or as a filename to parse the current JSP.
     * @see net.sourceforge.myfaces.lifecycle.ViewHandlerJspImpl
     * @see net.sourceforge.myfaces.renderkit.html.jspinfo.JspTreeParser
     */
    public String mapTreeIdToFilename(ServletContext servletContext, String treeId);

    /**
     * Maps the real URL of a link or form action to an appropriate (virtual) URL that conforms
     * to the servlet mapping in web.xml.
     */
    public String encodeTreeIdForURL(FacesContext facesContext, String treeId);

    /**
     * Returns treeId that corresponds to the current request.
     */
    public String getTreeIdFromRequest(ServletRequest request);
}
