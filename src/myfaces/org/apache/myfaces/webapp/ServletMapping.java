package net.sourceforge.myfaces.webapp;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

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
     * @see net.sourceforge.myfaces.application.ViewHandlerImpl
     * @see net.sourceforge.myfaces.renderkit.html.jspinfo.JspViewParser
     */
    public String mapViewIdToFilename(ServletContext servletContext, String viewId);

    /**
     * Maps the real URL of a link or form action to an appropriate (virtual) URL that conforms
     * to the servlet mapping in web.xml.
     */
    public String encodeViewIdForURL(FacesContext facesContext, String viewId);

    /**
     * Returns treeId that corresponds to the current request.
     */
    public String getViewIdFromRequest(FacesContext facesContex);
}
