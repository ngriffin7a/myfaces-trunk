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
package javax.faces.context;

import javax.faces.application.FacesMessage;
import java.util.Iterator;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public abstract class FacesContext
{
    public abstract javax.faces.application.Application getApplication();

    public abstract Iterator getClientIdsWithMessages();

    public abstract javax.faces.context.ExternalContext getExternalContext();

    public abstract FacesMessage.Severity getMaximumSeverity();

    public abstract Iterator getMessages();

    public abstract Iterator getMessages(String clientId);

    public abstract javax.faces.render.RenderKit getRenderKit();

    public abstract boolean getRenderResponse();

    public abstract boolean getResponseComplete();

    public abstract javax.faces.context.ResponseStream getResponseStream();

    public abstract void setResponseStream(javax.faces.context.ResponseStream responseStream);

    public abstract javax.faces.context.ResponseWriter getResponseWriter();

    public abstract void setResponseWriter(javax.faces.context.ResponseWriter responseWriter);

    public abstract javax.faces.component.UIViewRoot getViewRoot();

    public abstract void setViewRoot(javax.faces.component.UIViewRoot root);

    public abstract void addMessage(String clientId,
                                    javax.faces.application.FacesMessage message);

    public abstract void release();

    public abstract void renderResponse();

    public abstract void responseComplete();


    private static ThreadLocal _currentInstance = new ThreadLocal()
    {
        protected Object initialValue()
        {
            return null;
        }
    };

    public static javax.faces.context.FacesContext getCurrentInstance()
    {
        FacesContext facesContext = (FacesContext)_currentInstance.get();
        if (facesContext == null)
        {
            System.err.println("Fatal Error: No current FacesContext instance");
        }
        return facesContext;
    }

    protected static void setCurrentInstance(javax.faces.context.FacesContext context)
    {
        _currentInstance.set(context);

    }
}
