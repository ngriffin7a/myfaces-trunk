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
package net.sourceforge.myfaces.lifecycle;

import net.sourceforge.myfaces.util.DebugUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Implements the lifecycle as described in Spec. 1.0 PFD Chapter 2
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class LifecycleImpl
        extends Lifecycle
{
    private static final Log log = LogFactory.getLog(LifecycleImpl.class);

    private List _phaseListenerList = new ArrayList();
    private PhaseListener[] _phaseListenerArray = null;

    public LifecycleImpl()
    {
        // hide from public access
    }

    public void execute(FacesContext facesContext)
        throws FacesException
    {
        if (restoreView(facesContext))
        {
            return;
        }

        if (applyRequestValues(facesContext))
        {
            return;
        }

        if (processValidations(facesContext))
        {
            return;
        }

        if (updateModelValues(facesContext))
        {
            return;
        }

        if (invokeApplication(facesContext))
        {
            return;
        }

        renderResponse(facesContext);
    }


    // Phases

    /**
     * Restore View (JSF.2.2.1)
     * @return true, if immediate rendering should occur
     */
    private boolean restoreView(FacesContext facesContext)
        throws FacesException
    {
        if (log.isTraceEnabled()) log.trace("entering restoreView in " + LifecycleImpl.class.getName());

        informPhaseListenersBefore(facesContext, PhaseId.RESTORE_VIEW);

        // Derive view identifier
        String viewId = deriveViewId(facesContext);

        Application application = facesContext.getApplication();
        ViewHandler viewHandler = application.getViewHandler();

        boolean viewCreated = false;
        UIViewRoot viewRoot = viewHandler.restoreView(facesContext, viewId);
        if (viewRoot == null)
        {
            viewRoot = viewHandler.createView(facesContext, viewId);
            facesContext.renderResponse();
            viewCreated = true;
        }

        facesContext.setViewRoot(viewRoot);

        if (log.isTraceEnabled())
        {
            DebugUtils.traceView(log, viewCreated ? "Newly created view" : "Restored view");
        }

        if (facesContext.getExternalContext().getRequestParameterMap().isEmpty())
        {
            //no POST or query parameters --> set render response flag
            facesContext.renderResponse();
        }

        recursivelyHandleComponentReferencesAndSetValid(facesContext, viewRoot);

        informPhaseListenersAfter(facesContext, PhaseId.RESTORE_VIEW);

        if (facesContext.getRenderResponse())
        {
            renderResponse(facesContext);
            if (log.isDebugEnabled()) log.debug("exiting restoreView in " + LifecycleImpl.class.getName() + " (after render response)");
            return true;
        }

        if (log.isTraceEnabled()) log.trace("exiting restoreView in " + LifecycleImpl.class.getName());
        return false;
    }


    /**
     * Apply Request Values (JSF.2.2.2)
     * @return true, if response is complete
     */
    private boolean applyRequestValues(FacesContext facesContext)
        throws FacesException
    {
        if (log.isTraceEnabled()) log.trace("entering applyRequestValues in " + LifecycleImpl.class.getName());

        informPhaseListenersBefore(facesContext, PhaseId.APPLY_REQUEST_VALUES);

        facesContext.getViewRoot().processDecodes(facesContext);

        informPhaseListenersAfter(facesContext, PhaseId.APPLY_REQUEST_VALUES);

        if (facesContext.getResponseComplete())
        {
            if (log.isDebugEnabled()) log.debug("exiting applyRequestValues in " + LifecycleImpl.class.getName() + " (response complete)");
            return true;
        }

        if (facesContext.getRenderResponse())
        {
            renderResponse(facesContext);
            if (log.isDebugEnabled()) log.debug("exiting applyRequestValues in " + LifecycleImpl.class.getName() + " (after render response)");
            return true;
        }

        if (log.isTraceEnabled()) log.trace("exiting applyRequestValues in " + LifecycleImpl.class.getName());
        return false;
    }


    /**
     * Process Validations (JSF.2.2.3)
     * @return true, if response is complete
     */
    private boolean processValidations(FacesContext facesContext) throws FacesException
    {
        if (log.isTraceEnabled()) log.trace("entering processValidations in " + LifecycleImpl.class.getName());

        informPhaseListenersBefore(facesContext, PhaseId.PROCESS_VALIDATIONS);

        //int messageCountBefore = getMessageCount(facesContext);

        facesContext.getViewRoot().processValidators(facesContext);

        informPhaseListenersAfter(facesContext, PhaseId.PROCESS_VALIDATIONS);

        if (facesContext.getResponseComplete())
        {
            if (log.isDebugEnabled()) log.debug("exiting processValidations in " + LifecycleImpl.class.getName() + " (response complete)");
            return true;
        }

        if (facesContext.getRenderResponse())
        {
            renderResponse(facesContext);
            if (log.isDebugEnabled()) log.debug("exiting processValidations in " + LifecycleImpl.class.getName() + " (after render response)");
            return true;
        }

        /*
        if (getMessageCount(facesContext) > messageCountBefore)
        {
            renderResponse(facesContext);
            if (log.isDebugEnabled()) log.debug("exiting processValidations in " + LifecycleImpl.class.getName() + " (after render response - because of messages during validation!)");
            return true;
        }
        */

        if (log.isTraceEnabled()) log.trace("exiting processValidations in " + LifecycleImpl.class.getName());
        return false;
    }


    /**
     * Update Model Values (JSF.2.2.4)
     * @return true, if response is complete
     */
    private boolean updateModelValues(FacesContext facesContext) throws FacesException
    {
        if (log.isTraceEnabled()) log.trace("entering updateModelValues in " + LifecycleImpl.class.getName());

        informPhaseListenersBefore(facesContext, PhaseId.UPDATE_MODEL_VALUES);

        facesContext.getViewRoot().processUpdates(facesContext);

        informPhaseListenersAfter(facesContext, PhaseId.UPDATE_MODEL_VALUES);

        if (facesContext.getResponseComplete())
        {
            if (log.isDebugEnabled()) log.debug("exiting updateModelValues in " + LifecycleImpl.class.getName() + " (after render response)");
            return true;
        }

        if (facesContext.getRenderResponse())
        {
            renderResponse(facesContext);
            if (log.isDebugEnabled()) log.debug("exiting updateModelValues in " + LifecycleImpl.class.getName() + " (after render response)");
            return true;
        }

        if (log.isTraceEnabled()) log.trace("exiting updateModelValues in " + LifecycleImpl.class.getName());
        return false;
    }


    /**
     * Invoke Application (JSF.2.2.5)
     * @return true, if response is complete
     */
    private boolean invokeApplication(FacesContext facesContext)
        throws FacesException
    {
        if (log.isTraceEnabled()) log.trace("entering invokeApplication in " + LifecycleImpl.class.getName());

        informPhaseListenersBefore(facesContext, PhaseId.INVOKE_APPLICATION);

        facesContext.getViewRoot().processApplication(facesContext);

        informPhaseListenersAfter(facesContext, PhaseId.INVOKE_APPLICATION);

        if (facesContext.getResponseComplete())
        {
            if (log.isDebugEnabled()) log.debug("exiting invokeApplication in " + LifecycleImpl.class.getName() + " (response complete)");
            return true;
        }

        if (log.isTraceEnabled()) log.trace("exiting invokeApplication in " + LifecycleImpl.class.getName());
        return false;
    }

    /**
     * Render Response (JSF.2.2.6)
     */
    private void renderResponse(FacesContext facesContext)
        throws FacesException
    {
        if (log.isTraceEnabled()) log.trace("entering renderResponse in " + LifecycleImpl.class.getName());

        informPhaseListenersBefore(facesContext, PhaseId.RENDER_RESPONSE);

        Application application = facesContext.getApplication();
        ViewHandler viewHandler = application.getViewHandler();
        try
        {
            viewHandler.renderView(facesContext, facesContext.getViewRoot());
        }
        catch (IOException e)
        {
            throw new FacesException(e.getMessage(), e);
        }

        informPhaseListenersAfter(facesContext, PhaseId.RENDER_RESPONSE);

        if (log.isTraceEnabled())
        {
            DebugUtils.traceView(log, "View after rendering");
        }

        if (log.isTraceEnabled()) log.trace("exiting renderResponse in " + LifecycleImpl.class.getName());
    }


    private static String deriveViewId(FacesContext facesContext)
    {
        ExternalContext externalContext = facesContext.getExternalContext();

        String viewId = externalContext.getRequestPathInfo();
        if (viewId == null)
        {
            //No extra path info found, so it is propably extension mapping
            viewId = externalContext.getRequestServletPath();
            DebugUtils.assertError(viewId != null,
                                   log, "RequestServletPath is null, cannot determine viewId of current page.");

            //TODO: JSF Spec 2.2.1 - what do they mean by "if the default ViewHandler implementation is used..." ?
            String defaultSuffix = externalContext.getInitParameter(ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
            String suffix = defaultSuffix != null ? defaultSuffix : ViewHandler.DEFAULT_SUFFIX;
            DebugUtils.assertError(suffix.charAt(0) == '.',
                                   log, "Default suffix must start with a dot!");

            int dot = viewId.lastIndexOf('.');
            if (dot == -1)
            {
                log.error("Assumed extension mapping, but there is no extension in " + viewId);
            }
            else
            {
                viewId = viewId.substring(0, dot) + suffix;
            }
        }

        return viewId;
    }


    private static void recursivelyHandleComponentReferencesAndSetValid(FacesContext facesContext,
                                                                        UIComponent root)
    {
        for (Iterator it = root.getFacetsAndChildren(); it.hasNext(); )
        {
            UIComponent component = (UIComponent)it.next();

            ValueBinding binding = component.getValueBinding("binding");    //TODO: constant
            if (binding != null)
            {
                binding.setValue(facesContext, component);
            }

            if (component instanceof UIInput)
            {
                ((UIInput)component).setValid(true);
            }

            recursivelyHandleComponentReferencesAndSetValid(facesContext, component);
        }
    }

    public void addPhaseListener(PhaseListener phaseListener)
    {
        if (_phaseListenerList == null)
        {
            _phaseListenerList = new ArrayList();
            if (_phaseListenerArray != null)
            {
                _phaseListenerList.addAll(Arrays.asList(_phaseListenerArray));
                _phaseListenerArray = null;
            }
        }
        _phaseListenerList.add(phaseListener);
    }

    public void removePhaseListener(PhaseListener phaseListener)
    {
        if (_phaseListenerList == null)
        {
            _phaseListenerList = new ArrayList();
            if (_phaseListenerArray != null)
            {
                _phaseListenerList.addAll(Arrays.asList(_phaseListenerArray));
                _phaseListenerArray = null;
            }
        }
        _phaseListenerList.remove(phaseListener);
    }

    public PhaseListener[] getPhaseListeners()
    {
        if (_phaseListenerArray == null)
        {
            if (_phaseListenerList == null)
            {
                _phaseListenerArray = new PhaseListener[0];
            }
            else
            {
                _phaseListenerArray = (PhaseListener[])_phaseListenerList.toArray(new PhaseListener[_phaseListenerList.size()]);
                _phaseListenerList = null;
            }
        }
        return _phaseListenerArray;
    }


    private void informPhaseListenersBefore(FacesContext facesContext, PhaseId phaseId)
    {
        PhaseListener[] phaseListeners = getPhaseListeners();
        for (int i = 0; i < phaseListeners.length; i++)
        {
            PhaseListener phaseListener = phaseListeners[i];
            int listenerPhaseId = phaseListener.getPhaseId().getOrdinal();
            if (listenerPhaseId == PhaseId.ANY_PHASE.getOrdinal() ||
                listenerPhaseId == phaseId.getOrdinal())
            {
                phaseListener.beforePhase(new PhaseEvent(facesContext, phaseId, this));
            }
        }

    }

    private void informPhaseListenersAfter(FacesContext facesContext, PhaseId phaseId)
    {
        PhaseListener[] phaseListeners = getPhaseListeners();
        for (int i = 0; i < phaseListeners.length; i++)
        {
            PhaseListener phaseListener = phaseListeners[i];
            int listenerPhaseId = phaseListener.getPhaseId().getOrdinal();
            if (listenerPhaseId == PhaseId.ANY_PHASE.getOrdinal() ||
                listenerPhaseId == phaseId.getOrdinal())
            {
                phaseListener.afterPhase(new PhaseEvent(facesContext, phaseId, this));
            }
        }

    }

}
