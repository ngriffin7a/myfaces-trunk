package net.sourceforge.myfaces.webapp;

import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;

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


    public String mapTreeIdToFilename(ServletContext servletContext, String treeId)
    {
        //treeId is already without virtual path ("/faces"), so nothing to do.
        return treeId;
    }

    public String encodeTreeIdForURL(FacesContext facesContext, String treeId)
    {
        return VIRTUAL_PATH + treeId;
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

        ServletRequest servletRequest = facesContext.getServletRequest();
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

    public String getTreeIdFromRequest(ServletRequest servletRequest)
    {
        if (!(servletRequest instanceof HttpServletRequest))
        {
            throw new UnsupportedOperationException("only HttpServletRequest supported");
        }
        return ((HttpServletRequest)servletRequest).getPathInfo();
    }
}
