package net.sourceforge.myfaces.webapp;

import net.sourceforge.myfaces.util.logging.LogUtil;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * ServletMapping for the alternative url-pattern "*.jsf".
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class ServletMappingWithExtension
    implements ServletMapping
{
    private static final String JSP_EXTENSION = ".jsp";
    private static final String FACES_EXTENSION = ".jsf";

    public String mapTreeIdToFilename(ServletContext servletContext, String treeId)
    {
        if (treeId.toLowerCase().endsWith(FACES_EXTENSION))
        {
            return treeId.substring(0, treeId.length() - FACES_EXTENSION.length()) + JSP_EXTENSION;
        }
        else
        {
            LogUtil.getLogger().warning("TreeId has invalid extension: " + treeId);
            return treeId;
        }
    }


    public String encodeTreeIdForURL(FacesContext facesContext, String treeId)
    {
        //treeId already has .jsf extension, nothing to do
        return treeId;
        /*
        if (!isModificationNeeded(facesContext, urlToEncode))
        {
            return urlToEncode;
        }

        String lcURL = urlToEncode.toLowerCase();
        if (lcURL.endsWith(JSP_EXTENSION))
        {
            return urlToEncode.substring(0, urlToEncode.length() - JSP_EXTENSION.length()) + FACES_EXTENSION;
        }
        else
        {
            int i = lcURL.indexOf(JSP_EXTENSION + "?");
            if (i >= 0)
            {
                return urlToEncode.substring(0, i) + FACES_EXTENSION + urlToEncode.substring(i + JSP_EXTENSION.length());
            }
            else
            {
                //No JSP-Link
                return urlToEncode;
            }
        }
        */
    }



    /*
    private static boolean isModificationNeeded(FacesContext facesContext, String url)
    {
        //determine the path part of the url
        String path;
        int firstDoubleSlash = url.indexOf("//");
        if (firstDoubleSlash >= 0)
        {
            int nextSlash = url.indexOf('/', firstDoubleSlash + 2);
            if (nextSlash == -1)
            {
                //external reference (domain without path), no modification
                return false;
            }
            else
            {
                path = url.substring(nextSlash);
            }
        }
        else
        {
            int quest = url.indexOf('?');
            int slash = url.indexOf('/');
            if (slash == -1 ||
                (quest >= 0 && slash > quest))
            {
                //relative URL, always modify
                return true;
            }
            path = url;
        }

        //is this path within our current context?
        ServletRequest servletRequest = facesContext.getServletRequest();
        if (!(servletRequest instanceof HttpServletRequest))
        {
            throw new UnsupportedOperationException("only HttpServletRequest supported");
        }

        String contextPath = ((HttpServletRequest)servletRequest).getContextPath();
        if (contextPath != null && contextPath.length() > 0)
        {
            if (!path.startsWith(contextPath))
            {
                //external reference, no modification
                return false;
            }
        }

        return true;
    }
    */

    public String getTreeIdFromRequest(ServletRequest servletRequest)
    {
        if (!(servletRequest instanceof HttpServletRequest))
        {
            throw new UnsupportedOperationException("only HttpServletRequest supported");
        }
        String treeId = ((HttpServletRequest)servletRequest).getServletPath();
        return treeId;
    }
}
