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

import javax.faces.application.Message;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.event.FacesEvent;
import javax.faces.tree.Tree;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.*;

/**
 * DOCUMENT ME!
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class FacesContextImpl
    extends FacesContext
{
    private ExternalContext _externalContext;
    private Locale _locale = null;
    private Tree _tree = null;
    private List _facesEvents = null;
    private List _messages = null;
    private List _messageComponents = null;
    private int _maximumSeverity = 0;
    private ResponseStream _responseStream = null;
    private ResponseWriter _responseWriter = null;
    private boolean _renderResponse = false;
    private boolean _responseComplete = false;

    public FacesContextImpl(ServletContext servletContext,
                            ServletRequest servletRequest,
                            ServletResponse servletResponse)
    {
        _externalContext = new ExternalContextImpl(servletContext,
                                                   servletRequest,
                                                   servletResponse);
        FacesContext.setCurrentInstance(this);
    }

    public void release()
    {
        _locale = null;
        _tree = null;
        _facesEvents = null;
        _messages = null;
        _messageComponents = null;
        _maximumSeverity = 0;
        _responseStream = null;
        _responseWriter = null;
        _renderResponse = false;
        _responseComplete = false;
        FacesContext.setCurrentInstance(null);
    }




    //JSF 6.1.1
    public ExternalContext getExternalContext()
    {
        return _externalContext;
    }


    //JSF.6.1.2
    public Locale getLocale()
    {
        return _locale == null
                ? Locale.getDefault()
                : _locale;
    }

    public void setLocale(Locale locale)
    {
        _locale = locale;
    }

    //JSF.6.1.3
    public Tree getTree()
    {
        return _tree;
    }

    public void setTree(Tree tree)
    {
        _tree = tree;
    }

    //JSF.6.1.4
    private static final Object NULL_DUMMY = new Object();
    public void addMessage(UIComponent uicomponent, Message message)
    {
        if (_messages == null)
        {
            _messages = new ArrayList();
            _messageComponents = new ArrayList();
        }
        _messages.add(message);
        _messageComponents.add(uicomponent != null ? uicomponent : NULL_DUMMY);
        if (message.getSeverity() > _maximumSeverity)
        {
            _maximumSeverity = message.getSeverity();
        }
    }

    public int getMaximumSeverity()
    {
        return _maximumSeverity;
    }

    public Iterator getMessages(UIComponent uicomponent)
    {
        if (_messages == null)
        {
            return Collections.EMPTY_LIST.iterator();
        }
        List lst = new ArrayList();
        for (int i = 0; i < _messages.size(); i++)
        {
            Object msgComp = _messageComponents.get(i);
            if ((msgComp == NULL_DUMMY && uicomponent == null) ||
                 msgComp != NULL_DUMMY && uicomponent == (UIComponent)msgComp)
            {
                lst.add(_messages.get(i));
            }
        }
        return lst.iterator();
    }

    public Iterator getMessages()
    {
        return _messages != null
                ? _messages.iterator()
                : Collections.EMPTY_LIST.iterator();
    }

    /**
     * MyFaces extension.
     */
    public int getMessageCount()
    {
        return _messages == null
                ? 0
                : _messages.size();
    }

    /**
     * MyFaces extension.
     */
    public void clearMessages()
    {
        _messages = null;
        _messageComponents = null;
        _maximumSeverity = 0;
    }

    //JSF.6.1.5
    public Iterator getFacesEvents()
    {
        return _facesEvents != null
                ? _facesEvents.iterator()
                : Collections.EMPTY_LIST.iterator();
    }

    public void addFacesEvent(FacesEvent facesevent)
    {
        if (_facesEvents == null)
        {
            _facesEvents = new ArrayList();
        }
        _facesEvents.add(facesevent);
    }

    //JSF.6.1.6
    public ResponseStream getResponseStream()
    {
        return _responseStream;
    }

    public ResponseWriter getResponseWriter()
    {
        return _responseWriter;
    }

    public void setResponseStream(ResponseStream responsestream)
    {
        _responseStream = responsestream;
    }

    public void setResponseWriter(ResponseWriter responsewriter)
    {
        _responseWriter = responsewriter;
    }

    //JSF.6.1.7
    public void renderResponse()
    {
        _renderResponse = true;
    }

    public void responseComplete()
    {
        _responseComplete = true;
    }

    /**
     * MyFaces utility extension.
     */
    public static boolean isRenderResponse(FacesContext facesContext)
    {
        if (facesContext instanceof FacesContextImpl)
        {
            return ((FacesContextImpl)facesContext)._renderResponse;
        }
        else
        {
            throw new IllegalArgumentException("FacesContext of class " + facesContext.getClass().getName() + " is not supported.");
        }
    }

    /**
     * MyFaces utility extension.
     */
    public static boolean isResponseComplete(FacesContext facesContext)
    {
        if (facesContext instanceof FacesContextImpl)
        {
            return ((FacesContextImpl)facesContext)._responseComplete;
        }
        else
        {
            throw new IllegalArgumentException("FacesContext of class " + facesContext.getClass().getName() + " is not supported.");
        }
    }


}
