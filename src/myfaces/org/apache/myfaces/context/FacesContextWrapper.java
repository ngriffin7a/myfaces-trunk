/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ApplicationEvent;
import javax.faces.event.FacesEvent;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.lifecycle.ViewHandler;
import javax.faces.tree.Tree;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Locale;

/**
 * Convenient class to wrap the current FacesContext.
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * @deprecated no longer used
 */
public class FacesContextWrapper
    extends FacesContext
{
    private final FacesContext _facesContext;

    public FacesContextWrapper(FacesContext facesContext)
    {
        _facesContext = facesContext;
    }

    public FacesContext getWrappedFacesContext()
    {
        return _facesContext;
    }

    //JSF.5.1.1
    public HttpSession getHttpSession()
    {
        return _facesContext.getHttpSession();
    }

    public ServletContext getServletContext()
    {
        return _facesContext.getServletContext();
    }

    public ServletRequest getServletRequest()
    {
        return _facesContext.getServletRequest();
    }

    public ServletResponse getServletResponse()
    {
        return _facesContext.getServletResponse();
    }

    //JSF.5.1.2
    public Locale getLocale()
    {
        return _facesContext.getLocale();
    }

    public void setLocale(Locale locale)
    {
        _facesContext.setLocale(locale);
    }

    //JSF.5.1.3
    public void setTree(Tree tree)
    {
        _facesContext.setTree(tree);
    }

    public Tree getTree()
    {
        return _facesContext.getTree();
    }

    public void release()
    {
        _facesContext.release();
    }

    //JSF.5.1.4
    public Iterator getApplicationEvents()
    {
        return _facesContext.getApplicationEvents();
    }

    public int getApplicationEventsCount()
    {
        return _facesContext.getApplicationEventsCount();
    }

    public void addApplicationEvent(ApplicationEvent event)
    {
        _facesContext.addApplicationEvent(event);
    }

    public void addMessage(UIComponent uicomponent, Message message)
    {
        _facesContext.addMessage(uicomponent, message);
    }

    public int getMaximumSeverity()
    {
        return _facesContext.getMaximumSeverity();
    }

    public Iterator getMessages()
    {
        return _facesContext.getMessages();
    }

    public Iterator getMessages(UIComponent uicomponent)
    {
        return _facesContext.getMessages(uicomponent);
    }

    //JSF.5.1.6
    public ApplicationHandler getApplicationHandler()
    {
        return _facesContext.getApplicationHandler();
    }

    public ViewHandler getViewHandler()
    {
        return _facesContext.getViewHandler();
    }

    //JSF.5.1.7
    public Class getModelType(String modelReference)
        throws FacesException
    {
        return _facesContext.getModelType(modelReference);
    }

    public Object getModelValue(String modelReference)
        throws FacesException
    {
        return _facesContext.getModelValue(modelReference);
    }

    public void setModelValue(String modelReference, Object value)
        throws FacesException
    {
        _facesContext.setModelValue(modelReference, value);
    }

    //JSF.5.1.8
    public Iterator getFacesEvents()
    {
        return _facesContext.getFacesEvents();
    }

    public void addFacesEvent(FacesEvent facesevent)
    {
        _facesContext.addFacesEvent(facesevent);
    }

    public ResponseStream getResponseStream()
    {
        return _facesContext.getResponseStream();
    }

    public ResponseWriter getResponseWriter()
    {
        return _facesContext.getResponseWriter();
    }

    public void setResponseStream(ResponseStream responsestream)
    {
        _facesContext.setResponseStream(responsestream);
    }

    public void setResponseWriter(ResponseWriter responsewriter)
    {
        _facesContext.setResponseWriter(responsewriter);
    }

    public void renderResponse()
    {
        _facesContext.renderResponse();
    }

    public void responseComplete()
    {
        _facesContext.responseComplete();
    }


}
