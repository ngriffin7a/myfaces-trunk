package net.sourceforge.myfaces.webapp;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 * Default ServletMapping for the url-pattern "/faces/*".
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ServletMappingWithVirtualPath
    implements ServletMapping
{
    /**
     * This path must match the "url-pattern" attribute in the "servlet-mapping" of the
     * corresponding web.xml.
     * The virtual path for the default mapping "/faces/*" is "/faces".
     */
    private static final String VIRTUAL_PATH = "/faces";


    public String mapViewIdToFilename(ServletContext servletContext, String viewId)
    {
        //treeId is already without virtual path ("/faces"), so nothing to do.
        return viewId;
    }

    public String encodeViewIdForURL(FacesContext facesContext, String viewId)
    {
        return VIRTUAL_PATH + viewId;
        /*
        String protocolHostPort;
        String absolutePath;
        String facesPath;
        int firstDoubleSlash = urlToEncode.indexOf("//");
        if (firstDoubleSlash >= 0)
        {
            int nextSlash = urlToEncode.indexOf('/', firstDoubleSlash + 2);
            if (nextSlash == -1)
            {
                protocolHostPort = urlToEncode;
                absolutePath = "";
            }
            else
            {
                protocolHostPort = urlToEncode.substring(0, nextSlash);
                absolutePath = urlToEncode.substring(nextSlash);
            }
        }
        else
        {
            if (!urlToEncode.startsWith("/"))
            {
                //relative paths need no modification
                return urlToEncode;
            }
            protocolHostPort = "";
            absolutePath = urlToEncode;
        }

        ServletRequest servletRequest = facesContext.getExternalContext().getRequest();
        if (!(servletRequest instanceof HttpServletRequest))
        {
            throw new UnsupportedOperationException("only HttpServletRequest supported");
        }

        String contextPath = ((HttpServletRequest)servletRequest).getContextPath();
         if (contextPath != null && contextPath.length() > 0)
        {
            if (!absolutePath.startsWith(contextPath))
            {
                //external reference, no modification needed (allowed)
                return urlToEncode;
            }
            facesPath = protocolHostPort + contextPath + VIRTUAL_PATH + absolutePath.substring(contextPath.length());
        }
        else
        {
            facesPath = protocolHostPort + VIRTUAL_PATH + absolutePath;
        }

        return facesPath;
        */
    }

    public String getViewIdFromRequest(FacesContext facesContext)
    {
        return facesContext.getExternalContext().getRequestPathInfo();
    }
}
