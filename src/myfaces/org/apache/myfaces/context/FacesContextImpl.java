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

import net.sourceforge.myfaces.util.bean.BeanUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ApplicationEvent;
import javax.faces.event.FacesEvent;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.ViewHandler;
import javax.faces.tree.Tree;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    private ServletContext _servletcontext;
    private ServletRequest _servletrequest;
    private ServletResponse _servletresponse;
    private Lifecycle _lifecycle;
    private Locale _locale = Locale.getDefault();
    private Tree _tree = null;
    private List _applicationEvents = null;
    private List _facesEvents = null;
    private List _messages = null;
    private List _messageComponents = null;
    private int _maximumSeverity = 0;
    private ResponseStream _responseStream = null;
    private ResponseWriter _responseWriter = null;
    private boolean _renderResponse = false;
    private boolean _responseComplete = false;

    public FacesContextImpl(ServletContext servletcontext,
                            ServletRequest servletrequest,
                            ServletResponse servletresponse,
                            Lifecycle lifecycle)
    {
        _servletcontext = servletcontext;
        _servletrequest = servletrequest;
        _servletresponse = servletresponse;
        _lifecycle = lifecycle;
        FacesContext.setCurrentInstance(this);
    }

    //JSF.5.1.1
    public HttpSession getHttpSession()
    {
        if (_servletrequest instanceof HttpServletRequest)
        {
            return ((HttpServletRequest)_servletrequest).getSession();
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    public HttpSession getHttpSession(boolean create)
    {
        if (_servletrequest instanceof HttpServletRequest)
        {
            return ((HttpServletRequest)_servletrequest).getSession(create);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    public ServletContext getServletContext()
    {
        return _servletcontext;
    }

    public ServletRequest getServletRequest()
    {
        return _servletrequest;
    }

    public ServletResponse getServletResponse()
    {
        return _servletresponse;
    }

    //JSF.5.1.2
    public Locale getLocale()
    {
        return _locale;
    }

    public void setLocale(Locale locale)
    {
        _locale = locale;
    }

    //JSF.5.1.3
    public void setTree(Tree tree)
    {
        _tree = tree;
    }

    public Tree getTree()
    {
        return _tree;
    }

    public void release()
    {
        //Our FacesContextFactory does no pooling yet, so no need to release anything here
    }


    //JSF.5.1.4
    public Iterator getApplicationEvents()
    {
        return _applicationEvents != null
                ? _applicationEvents.iterator()
                : Collections.EMPTY_LIST.iterator();
    }

    public int getApplicationEventsCount()
    {
        return _applicationEvents != null
                ? _applicationEvents.size()
                : 0;
    }

    public void addApplicationEvent(ApplicationEvent event)
    {
        if (_applicationEvents == null)
        {
            _applicationEvents = new ArrayList();
        }
        _applicationEvents.add(event);
    }

    //JSF.5.1.5
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

    public Iterator getMessages()
    {
        return _messages != null
                ? _messages.iterator()
                : Collections.EMPTY_LIST.iterator();
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
            UIComponent uic = (UIComponent)_messageComponents.get(i);
            if ((uicomponent == null && uic == NULL_DUMMY) ||
                uicomponent == uic)
            {
                lst.add(_messages.get(i));
            }
        }
        return lst.iterator();
    }


    //JSF.5.1.6
    public ApplicationHandler getApplicationHandler()
    {
        return _lifecycle.getApplicationHandler();
    }

    public ViewHandler getViewHandler()
    {
        return _lifecycle.getViewHandler();
    }


    //JSF.5.1.7
    public Class getModelType(String modelReference)
            throws FacesException
    {
        modelReference = BeanUtils.stripBracketsFromModelReference(modelReference);
        int i = modelReference.indexOf('.');
        if (i == -1)
        {
            return getModelInstance(modelReference).getClass();
        }
        else
        {
            String objName = modelReference.substring(0, i);
            String propName = modelReference.substring(i + 1);
            Object obj = getModelInstance(objName);
            return BeanUtils.getBeanPropertyType(obj, propName);
        }
    }

    /**
     * @throws FacesException   if model instance could not be found
     */
    public Object getModelValue(String modelReference)
            throws FacesException
    {
        modelReference = BeanUtils.stripBracketsFromModelReference(modelReference);
        int i = modelReference.indexOf('.');
        if (i == -1)
        {
            return getModelInstance(modelReference);
        }
        else
        {
            String objName = modelReference.substring(0, i);
            String propName = modelReference.substring(i + 1);
            Object obj = getModelInstance(objName);
            return BeanUtils.getBeanPropertyValue(obj, propName);
        }
    }

    public void setModelValue(String modelReference, Object value)
            throws FacesException
    {
        modelReference = BeanUtils.stripBracketsFromModelReference(modelReference);
        int i = modelReference.indexOf('.');
        if (i == -1)
        {
            setModelInstance(modelReference, value);
        }
        else
        {
            String objName = modelReference.substring(0, i);
            String propName = modelReference.substring(i + 1);
            Object obj = getModelInstance(objName);
            BeanUtils.setBeanPropertyValue(obj, propName, value);
        }
    }


    //JSF.5.1.8
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


    //JSF.5.1.9

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

    //JSF.5.1.10

    public void renderResponse()
    {
        _renderResponse = true;
    }

    public void responseComplete()
    {
        _responseComplete = true;
    }


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

    //Helpers

    /**
     * @throws  FacesException if there is no model instance with the given modelId
     */
    protected Object getModelInstance(String modelId) throws FacesException
    {
        Object obj = findModelInstance(modelId);
        if (obj == null)
        {
            throw new FacesException("No model instance '" + modelId + "' found.");
        }
        return obj;
    }


    /**
     * @return  null, if not found
     */
    private Object findModelInstance(String modelId)
    {
        return findBean(this, modelId);
    }

    private void setModelInstance(String modelId, Object modelObj)
    {
        if (_servletcontext == null || _servletrequest == null)
        {
            throw new IllegalStateException("No servlet context or request!?");
        }

        //Request context
        if (_servletrequest.getAttribute(modelId) != null)
        {
            _servletrequest.setAttribute(modelId, modelObj);
            return;
        }

        //Session context
        if (_servletrequest instanceof HttpServletRequest)
        {
            HttpSession session = ((HttpServletRequest)_servletrequest).getSession(false);
            if (session != null)
            {
                if (session.getAttribute(modelId) != null)
                {
                    session.setAttribute(modelId, modelObj);
                    return;
                }
            }
        }

        //Application context
        if (_servletcontext.getAttribute(modelId) != null)
        {
            _servletcontext.setAttribute(modelId, modelObj);
            return;
        }

        //TODO: request scope as default? - not yet specified in JSF Spec.
        _servletrequest.setAttribute(modelId, modelObj);
    }


    /**
     * @return  null, if not found
     */
    public static Object findBean(FacesContext facesContext, String beanId)
    {
        Object obj;

        //Request context
        ServletRequest servletrequest = facesContext.getServletRequest();
        obj = servletrequest.getAttribute(beanId);
        if (obj != null)
        {
            return obj;
        }

        //Session context
        if (servletrequest instanceof HttpServletRequest)
        {
            HttpSession session = ((HttpServletRequest)servletrequest).getSession(false);
            if (session != null)
            {
                obj = session.getAttribute(beanId);
                if (obj != null)
                {
                    return obj;
                }
            }
        }

        //Application context
        ServletContext servletcontext = facesContext.getServletContext();
        obj = servletcontext.getAttribute(beanId);
        if (obj != null)
        {
            return obj;
        }

        return null;
    }


}
