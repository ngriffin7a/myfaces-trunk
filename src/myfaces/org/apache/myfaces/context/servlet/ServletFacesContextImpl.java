/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.15  2004/07/01 22:05:04  mwessendorf
 * ASF switch
 *
 * Revision 1.14  2004/04/16 13:56:59  manolito
 * Bug #922317 - ClassCastException in action handler after adding message
 *
 * Revision 1.13  2004/03/31 11:58:38  manolito
 * custom component refactoring
 *
 */
public class ServletFacesContextImpl
    extends FacesContext
{
    //~ Static fields/initializers -----------------------------------------------------------------

    protected static final Object NULL_DUMMY        = new Object();

    //~ Instance fields ----------------------------------------------------------------------------

    List                                _messageClientIds = null;
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
                        for (int len = _messageClientIds.size(); _next < len; _next++)
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
            Object savedClientId = _messageClientIds.get(i);
            if (clientId == null)
            {
                if (savedClientId == NULL_DUMMY) lst.add(_messages.get(i));
            }
            else
            {
                if (clientId.equals(savedClientId)) lst.add(_messages.get(i));
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
        if (_externalContext != null)
        {
            _externalContext.release();
            _externalContext = null;
        }

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
