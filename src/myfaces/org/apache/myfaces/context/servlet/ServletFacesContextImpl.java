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
package net.sourceforge.myfaces.context.servlet;

import net.sourceforge.myfaces.util.NullIterator;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.*;


/**
 * DOCUMENT ME!
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class ServletFacesContextImpl
    extends FacesContext
{
    //~ Static fields/initializers -----------------------------------------------------------------

    protected static final Object NULL_DUMMY        = new Object();

    //~ Instance fields ----------------------------------------------------------------------------

    private List                        _messageClientIds = null;
    private List                        _messages         = null;
    private Application                 _application;
    private ServletExternalContextImpl  _externalContext;
    private ResponseStream              _responseStream   = null;
    private ResponseWriter              _responseWriter   = null;
    private FacesMessage.Severity       _maximumSeverity  = FacesMessage.SEVERITY_INFO;
    private UIViewRoot                  _viewRoot;
    private boolean                     _renderResponse   = false;
    private boolean                     _responseComplete = false;

    //~ Constructors -------------------------------------------------------------------------------

    public ServletFacesContextImpl(ServletContext servletContext,
                                   ServletRequest servletRequest,
                                   ServletResponse servletResponse)
    {
        _application = ((ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY))
                            .getApplication();
        _externalContext = new ServletExternalContextImpl(servletContext,
                                                          servletRequest,
                                                          servletResponse);
        FacesContext.setCurrentInstance(this);  //protected method, therefore must be called from here
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
        if (_messages == null || _messages.isEmpty())
        {
            return NullIterator.instance();
        }

        return new Iterator()
            {
                private int _next;
                boolean     _nextFound;

                public void remove()
                {
                    throw new UnsupportedOperationException(this.getClass().getName() + " UnsupportedOperationException");
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
                        return _messageClientIds.get(_next++);
                    }

                    throw new NoSuchElementException();
                }
            };
    }

    public Iterator getMessages(String clientId)
    {
        if (_messages == null)
        {
            return NullIterator.instance();
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
        //FIXME
        throw new UnsupportedOperationException("not yet implemented");
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
        FacesMessage.Severity serSeverity =  message.getSeverity();
        if (serSeverity != null && serSeverity.compareTo(_maximumSeverity) > 0)
        {
            _maximumSeverity = message.getSeverity();
        }
    }

    public void release()
    {
        _externalContext.release();
        _externalContext = null;

        _messageClientIds     = null;
        _messages             = null;
        _application          = null;
        _responseStream       = null;
        _responseWriter       = null;
        _viewRoot             = null;

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
