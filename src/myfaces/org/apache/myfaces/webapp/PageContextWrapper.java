package net.sourceforge.myfaces.webapp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.util.Enumeration;

public class PageContextWrapper
    extends PageContext
{
    private static final Log log = LogFactory.getLog(PageContextWrapper.class);

    private PageContext _originalPageContext;

    public PageContextWrapper(PageContext originalPageContext)
    {
        _originalPageContext = originalPageContext;
    }

    public PageContext getWrappedPageContext()
    {
        return _originalPageContext;
    }

    public void initialize(Servlet servlet, ServletRequest servletRequest, ServletResponse servletResponse, String name, boolean b, int i, boolean b1) throws IOException, IllegalStateException, IllegalArgumentException
    {
        _originalPageContext.initialize(servlet, servletRequest, servletResponse, name, b, i, b1);
    }

    public void release()
    {
        _originalPageContext.release();
    }

    public void setAttribute(String name, Object o)
    {
        _originalPageContext.setAttribute(name, o);
    }

    public void setAttribute(String name, Object o, int i)
    {
        _originalPageContext.setAttribute(name, o, i);
    }

    public Object getAttribute(String name)
    {
        return _originalPageContext.getAttribute(name);
    }

    public Object getAttribute(String name, int i)
    {
        return _originalPageContext.getAttribute(name, i);
    }

    public Object findAttribute(String name)
    {
        return _originalPageContext.findAttribute(name);
    }

    public void removeAttribute(String name)
    {
        _originalPageContext.removeAttribute(name);
    }

    public void removeAttribute(String name, int i)
    {
        _originalPageContext.removeAttribute(name, i);
    }

    public int getAttributesScope(String name)
    {
        return _originalPageContext.getAttributesScope(name);
    }

    public Enumeration getAttributeNamesInScope(int i)
    {
        return _originalPageContext.getAttributeNamesInScope(i);
    }

    public JspWriter getOut()
    {
        return _originalPageContext.getOut();
    }

    public HttpSession getSession()
    {
        return _originalPageContext.getSession();
    }

    public Object getPage()
    {
        return _originalPageContext.getPage();
    }

    public ServletRequest getRequest()
    {
        return _originalPageContext.getRequest();
    }

    public ServletResponse getResponse()
    {
        return _originalPageContext.getResponse();
    }

    public Exception getException()
    {
        return _originalPageContext.getException();
    }

    public ServletConfig getServletConfig()
    {
        return _originalPageContext.getServletConfig();
    }

    public ServletContext getServletContext()
    {
        return _originalPageContext.getServletContext();
    }

    public void forward(String name) throws ServletException, IOException
    {
        _originalPageContext.forward(name);
    }

    public void include(String name) throws ServletException, IOException
    {
        _originalPageContext.include(name);
    }

    public void handlePageException(Exception e) throws ServletException, IOException
    {
        log.error("handlePageException in " + _originalPageContext.getClass().getName(), e);
        _originalPageContext.handlePageException(e);
    }

    public void handlePageException(Throwable throwable) throws ServletException, IOException
    {
        log.error("handlePageException in " + _originalPageContext.getClass().getName(), throwable);
        _originalPageContext.handlePageException(throwable);
    }

    public BodyContent pushBody()
    {
        return _originalPageContext.pushBody();
    }

    public JspWriter popBody()
    {
        return _originalPageContext.popBody();
    }

    public int hashCode()
    {
        return _originalPageContext.hashCode();
    }

    public boolean equals(Object obj)
    {
        return _originalPageContext.equals(obj);
    }

    public String toString()
    {
        return _originalPageContext.toString();
    }
}
