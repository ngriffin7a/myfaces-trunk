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
 * $Log$
 * Revision 1.40  2004/08/25 13:54:02  manolito
 * Log cvs keyword
 *
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
            //Note: DebugUtils Logger must also be in trace level
            DebugUtils.traceView(viewCreated ? "Newly created view" : "Restored view");
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
            if (log.isDebugEnabled()) log.debug("exiting restoreView in " + LifecycleImpl.class.getName() + " (--> render response)");
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
            if (log.isDebugEnabled()) log.debug("exiting applyRequestValues in " + LifecycleImpl.class.getName() + " (--> render response)");
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

        facesContext.getViewRoot().processValidators(facesContext);

        informPhaseListenersAfter(facesContext, PhaseId.PROCESS_VALIDATIONS);

        if (facesContext.getResponseComplete())
        {
            if (log.isDebugEnabled()) log.debug("exiting processValidations in " + LifecycleImpl.class.getName() + " (response complete)");
            return true;
        }

        if (facesContext.getRenderResponse())
        {
            if (log.isDebugEnabled()) log.debug("exiting processValidations in " + LifecycleImpl.class.getName() + " (--> render response)");
            return true;
        }

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
            if (log.isDebugEnabled()) log.debug("exiting updateModelValues in " + LifecycleImpl.class.getName() + " (response complete)");
            return true;
        }

        if (facesContext.getRenderResponse())
        {
            if (log.isDebugEnabled()) log.debug("exiting updateModelValues in " + LifecycleImpl.class.getName() + " (--> render response)");
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


    public void render(FacesContext facesContext) throws FacesException
    {
        if (log.isTraceEnabled()) log.trace("entering renderResponse in " + LifecycleImpl.class.getName());

        if (facesContext.getResponseComplete())
        {
            if (log.isDebugEnabled()) log.debug("exiting renderResponse in " + LifecycleImpl.class.getName() + " (response complete)");
            return;
        }

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
            //Note: DebugUtils Logger must also be in trace level
            DebugUtils.traceView("View after rendering");
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
            if (binding != null && !binding.isReadOnly(facesContext))
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
