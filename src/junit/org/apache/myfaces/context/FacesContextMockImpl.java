/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003, 2004  The MyFaces Team (http://myfaces.sourceforge.net)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.sourceforge.myfaces.context;

import java.util.Iterator;
import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ExternalContext;
import javax.faces.event.FacesEvent;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.render.RenderKit;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * TODO: liven up this class
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FacesContextMockImpl
    extends FacesContext
{
    private ServletContext _servletContext;
    private HttpServletRequest _httpServletRequest;
    private HttpServletResponse _httpServletResponse;
    private Lifecycle _lifecycle;
    private ExternalContext _externalContext;

    //~ Methods ------------------------------------------------------------------------------------

    public FacesContextMockImpl(ServletContext context, HttpServletRequest servletRequest, HttpServletResponse servletResponse, Lifecycle lifecycle)
    {
        _servletContext = context;
        _httpServletRequest = servletRequest;
        _httpServletResponse = servletResponse;
        _lifecycle = lifecycle;
        _externalContext = new ExternalContextMockImpl();
    }

    public FacesContextMockImpl()
    {
        this(null, null, null, null);
    }

    public Application getApplication()
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getClientIdsWithMessages()
    {
        throw new UnsupportedOperationException();
    }

    public ExternalContext getExternalContext()
    {
        return _externalContext;
    }

    public Iterator getFacesEvents()
    {
        throw new UnsupportedOperationException();
    }

    public void setLocale(Locale locale)
    {
        throw new UnsupportedOperationException();
    }

    public Locale getLocale()
    {
        throw new UnsupportedOperationException();
    }

    public Severity getMaximumSeverity()
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getMessages()
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getMessages(UIComponent uicomponent)
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getMessages(String arg0)
    {
        throw new UnsupportedOperationException();
    }

    public RenderKit getRenderKit()
    {
        throw new UnsupportedOperationException();
    }

    public boolean getRenderResponse()
    {
        throw new UnsupportedOperationException();
    }

    public boolean getResponseComplete()
    {
        throw new UnsupportedOperationException();
    }

    public void setResponseStream(ResponseStream responsestream)
    {
        throw new UnsupportedOperationException();
    }

    public ResponseStream getResponseStream()
    {
        throw new UnsupportedOperationException();
    }

    public void setResponseWriter(ResponseWriter responsewriter)
    {
        throw new UnsupportedOperationException();
    }

    public ResponseWriter getResponseWriter()
    {
        throw new UnsupportedOperationException();
    }

    public void setViewRoot(UIViewRoot arg0)
    {
        throw new UnsupportedOperationException();
    }

    public UIViewRoot getViewRoot()
    {
        throw new UnsupportedOperationException();
    }

    public void addFacesEvent(FacesEvent facesevent)
    {
        throw new UnsupportedOperationException();
    }

    public void addMessage(String arg0, FacesMessage arg1)
    {
        throw new UnsupportedOperationException();
    }

    public void release()
    {
        throw new UnsupportedOperationException();
    }

    public void renderResponse()
    {
        throw new UnsupportedOperationException();
    }

    public void responseComplete()
    {
        throw new UnsupportedOperationException();
    }
}
