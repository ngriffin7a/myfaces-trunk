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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ExternalContext;
import javax.faces.render.RenderKit;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.sourceforge.myfaces.util.FacesUtils;


/**
 * DOCUMENT ME!
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class FacesContextImpl
    extends FacesContext
{
    //~ Static fields/initializers -----------------------------------------------------------------

    protected static final Object NULL_DUMMY        = new Object();

    //~ Instance fields ----------------------------------------------------------------------------

    List                          _messageClientIds = null;
    List                          _messages         = null;
    private Application           _application;
    private ExternalContext       _externalContext;
    private ResponseStream        _responseStream   = null;
    private ResponseWriter        _responseWriter   = null;
    private FacesMessage.Severity _maximumSeverity  = FacesMessage.SEVERITY_INFO;
    private UIViewRoot            _viewRoot;
    private boolean               _renderResponse   = false;
    private boolean               _responseComplete = false;

    //~ Constructors -------------------------------------------------------------------------------

    public FacesContextImpl(
        ServletContext servletContext, ServletRequest servletRequest,
        ServletResponse servletResponse)
    {
        _application         = FacesUtils.getApplication();
        _externalContext     = new ExternalContextImpl(
                servletContext, servletRequest, servletResponse);
        FacesContext.setCurrentInstance(this);
    }

    //~ Methods ------------------------------------------------------------------------------------

    public ExternalContext getExternalContext()
    {
        return _externalContext;
    }

    public FacesMessage.Severity getMaximumSeverity()
    {
        return _maximumSeverity;
    }

    /**
     * MyFaces extension.
     */
    public int getMessageCount()
    {
        return (_messages == null) ? 0 : _messages.size();
    }

    public Iterator getMessages()
    {
        return (_messages != null) ? _messages.iterator() : Collections.EMPTY_LIST.iterator();
    }

    public Application getApplication()
    {
        return _application;
    }

    public Iterator getClientIdsWithMessages()
    {
        if (_messages.isEmpty())
        {
            return Collections.EMPTY_LIST.iterator();
        }

        return new Iterator()
            {
                private int _next;
                boolean     _nextFound;

                public void remove()
                {
                    throw new UnsupportedOperationException();
                }

                public boolean hasNext()
                {
                    if (!_nextFound)
                    {
                        for (; _next < _messageClientIds.size(); _next++)
                        {
                            if (_messageClientIds.get(_next) != NULL_DUMMY)
                            {
                                _nextFound = true;
                                break;
                            }
                        }
                    }
                    return _nextFound;
                }

                public Object next()
                {
                    if (hasNext())
                    {
                        _nextFound = false;
                        return _messages.get(_next++);
                    }

                    throw new NoSuchElementException();
                }
            };
    }

    public Iterator getMessages(String clientId)
    {
        if (_messages == null)
        {
            return Collections.EMPTY_LIST.iterator();
        }

        List lst = new ArrayList();
        for (int i = 0; i < _messages.size(); i++)
        {
            String savedClientId = (String) _messageClientIds.get(i);
            if (
                ((savedClientId == NULL_DUMMY) && (clientId == null))
                        || savedClientId.equals(clientId))
            {
                lst.add(_messages.get(i));
            }
        }
        return lst.iterator();
    }

    public RenderKit getRenderKit()
    {
        //return ???;
    }

    public boolean getRenderResponse()
    {
        return _renderResponse;
    }

    public boolean getResponseComplete()
    {
        return _responseComplete;
    }

    public void setResponseStream(ResponseStream responseStream)
    {
        if (responseStream == null)
        {
            throw new NullPointerException("responseStream");
        }
        _responseStream = responseStream;
    }

    public ResponseStream getResponseStream()
    {
        return _responseStream;
    }

    public void setResponseWriter(ResponseWriter responseWriter)
    {
        if (responseWriter == null)
        {
            throw new NullPointerException("responseWriter");
        }
        _responseWriter = responseWriter;
    }

    public ResponseWriter getResponseWriter()
    {
        return _responseWriter;
    }

    public void setViewRoot(UIViewRoot viewRoot)
    {
        if (viewRoot == null)
        {
            throw new NullPointerException("viewRoot");
        }
        _viewRoot = viewRoot;
    }

    public UIViewRoot getViewRoot()
    {
        return _viewRoot;
    }

    public void addMessage(String clientId, FacesMessage message)
    {
        if (message == null)
        {
            throw new NullPointerException("message");
        }

        if (_messages == null)
        {
            _messages             = new ArrayList();
            _messageClientIds     = new ArrayList();
        }
        _messages.add(message);
        _messageClientIds.add((clientId != null) ? clientId : NULL_DUMMY);
        if (message.getSeverity().compareTo(_maximumSeverity) > 0)
        {
            _maximumSeverity = message.getSeverity();
        }
    }

    /**
     * MyFaces extension.
     */
    public void clearMessages()
    {
        // TODO: not called from anywhere, should remove
        _messages             = null;
        _messageClientIds     = null;
        _maximumSeverity      = FacesMessage.SEVERITY_INFO;
    }

    public void release()
    {
        _messages             = null;
        _messageClientIds     = null;
        _maximumSeverity      = FacesMessage.SEVERITY_INFO;
        _responseStream       = null;
        _responseWriter       = null;
        _renderResponse       = false;
        _responseComplete     = false;
        FacesContext.setCurrentInstance(null);
    }

    public void renderResponse()
    {
        _renderResponse = true;
    }

    public void responseComplete()
    {
        _responseComplete = true;
    }
}
