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

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ExternalContext;
import javax.faces.component.UIComponent;
import javax.faces.tree.Tree;
import javax.faces.event.FacesEvent;
import javax.faces.application.Message;
import java.util.Iterator;
import java.util.Locale;

/**
 * TODO: liven up this class
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FacesContextMockImpl
    extends FacesContext
{
    public Iterator getFacesEvents()
    {
        return null;
    }

    public Locale getLocale()
    {
        return null;
    }

    public void setLocale(Locale locale)
    {
    }

    public int getMaximumSeverity()
    {
        return 0;
    }

    public Iterator getMessages()
    {
        return null;
    }

    public Iterator getMessages(UIComponent uicomponent)
    {
        return null;
    }

    public ResponseStream getResponseStream()
    {
        return null;
    }

    public void setResponseStream(ResponseStream responsestream)
    {
    }

    public ResponseWriter getResponseWriter()
    {
        return null;
    }

    public void setResponseWriter(ResponseWriter responsewriter)
    {
    }

    public Tree getTree()
    {
        return null;
    }

    public void setTree(Tree tree)
    {
    }

    public void addFacesEvent(FacesEvent facesevent)
    {
    }

    public void addMessage(UIComponent uicomponent, Message message)
    {
    }

    public void release()
    {
    }

    public void renderResponse()
    {
    }

    public void responseComplete()
    {
    }

    public ExternalContext getExternalContext()
    {
        return null;
    }
}
