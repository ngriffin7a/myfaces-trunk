/*
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

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ExternalContext;
import javax.faces.render.RenderKit;


/**
 * Convenient class to wrap the current FacesContext.
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class FacesContextWrapper
    extends FacesContext
{
    //~ Instance fields ----------------------------------------------------------------------------

    private final FacesContext _facesContext;

    //~ Constructors -------------------------------------------------------------------------------

    public FacesContextWrapper(FacesContext facesContext)
    {
        _facesContext = facesContext;
    }

    //~ Methods ------------------------------------------------------------------------------------

    public Application getApplication()
    {
        return _facesContext.getApplication();
    }

    public Iterator getClientIdsWithMessages()
    {
        return _facesContext.getClientIdsWithMessages();
    }

    public ExternalContext getExternalContext()
    {
        return _facesContext.getExternalContext();
    }

    public Severity getMaximumSeverity()
    {
        return _facesContext.getMaximumSeverity();
    }

    public Iterator getMessages()
    {
        return _facesContext.getMessages();
    }

    public Iterator getMessages(String clientId)
    {
        return _facesContext.getMessages(clientId);
    }

    public RenderKit getRenderKit()
    {
        return _facesContext.getRenderKit();
    }

    public boolean getRenderResponse()
    {
        return _facesContext.getRenderResponse();
    }

    public boolean getResponseComplete()
    {
        return _facesContext.getResponseComplete();
    }

    public void setResponseStream(ResponseStream responsestream)
    {
        _facesContext.setResponseStream(responsestream);
    }

    public ResponseStream getResponseStream()
    {
        return _facesContext.getResponseStream();
    }

    public void setResponseWriter(ResponseWriter responsewriter)
    {
        _facesContext.setResponseWriter(responsewriter);
    }

    public ResponseWriter getResponseWriter()
    {
        return _facesContext.getResponseWriter();
    }

    public void setViewRoot(UIViewRoot viewRoot)
    {
        _facesContext.setViewRoot(viewRoot);
    }

    public UIViewRoot getViewRoot()
    {
        return _facesContext.getViewRoot();
    }

    public void addMessage(String clientId, FacesMessage message)
    {
        _facesContext.addMessage(clientId, message);
    }

    public void release()
    {
        _facesContext.release();
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
