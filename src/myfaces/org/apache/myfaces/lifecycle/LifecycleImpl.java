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

import net.sourceforge.myfaces.context.FacesContextImpl;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.util.DebugUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Implements the lifecycle as described in Spec. 1.0 PRD2 Chapter 2
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class LifecycleImpl
        extends Lifecycle
{
    private static final Log log = LogFactory.getLog(LifecycleImpl.class);

    private final ApplicationFactory _applicationFactory;
//    private final RenderKitFactory _rkFactory;
    private final List _phaseListeners = new ArrayList();

    public LifecycleImpl()
    {
        _applicationFactory = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
// FIXME
//        _treeFactory = (TreeFactory)FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
//        _rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
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
     * @return true, if response is complete
     */
    private boolean restoreView(FacesContext facesContext)
        throws FacesException
    {
        if (log.isTraceEnabled()) log.trace("entering restoreView in " + LifecycleImpl.class.getName());

        // Derive view identifier
        String viewId = deriveViewId(facesContext);

        ViewHandler viewHandler


        UIViewRoot viewRoot = facesContext.getViewRoot();

        //Set locale
        Locale locale = facesContext.getExternalContext().getRequestLocale();
        if (locale != null)
        {
            facesContext.getViewRoot().setLocale(locale);
        }

        //Create tree
        Tree tree = _treeFactory.getViewRoot(facesContext, getViewId(facesContext));
        facesContext.setTree(tree);

        //Restore state
        RenderKit renderKit = _rkFactory.getRenderKit(tree.getRenderKitId());

        Renderer stateRenderer = null;
        try
        {
            stateRenderer = renderKit.getRenderer(StateRenderer.TYPE);
        }
        catch (Exception e)
        {
            //No StateRenderer
        }

        if (stateRenderer != null)
        {
            try
            {
                log.trace("StateRenderer found, calling decode.");
                stateRenderer.decode(facesContext, null);
            }
            catch (IOException e)
            {
                throw new FacesException("Error restoring state", e);
            }

            //remove messages, that were added during tree restoring
            Iterator msgIt = facesContext.getMessages();
            if (msgIt.hasNext())
            {
                if (log.isInfoEnabled())
                {
                    while (msgIt.hasNext())
                    {
                        FacesMessage msg = (FacesMessage)msgIt.next();
                        log.info("Message, added during reconstituteComponentTree: " + msg.getSummary() + " / " + msg.getDetail());
                    }
                }

                if (facesContext instanceof FacesContextImpl)
                {
                    ((FacesContextImpl)facesContext).clearMessages();
                }
                else
                {
                    log.warn("Messages were added during reconstituteComponentTree phase, but could not be removed afterwards, because current context is not instance of FacesContextImpl.");
                }
            }
        }
        else
        {
            log.info("No StateRenderer found, cannot restore tree.");
        }

        UIComponent root = facesContext.getViewRoot();
        try
        {
            root.processRestoreState(facesContext, ???);
        }
        catch (IOException e)
        {
            throw new FacesException(e);
        }

        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        ActionListener actionListener = af.getApplication().getActionListener();
        traverseAndRegisterActionListener(actionListener, root);

        if (log.isTraceEnabled()) log.trace("exiting restoreView in " + LifecycleImpl.class.getName());
    }


    /**
     * Apply Request Values (JSF.2.2.2)
     * @return true, if response is complete
     */
    private boolean applyRequestValues(FacesContext facesContext)
        throws FacesException
    {
        if (log.isTraceEnabled()) log.trace("entering applyRequestValues in " + LifecycleImpl.class.getName());

        UIComponent root = facesContext.getViewRoot();
        root.processDecodes(facesContext);

        doEventProcessing(facesContext, PhaseId.APPLY_REQUEST_VALUES);

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

        int messageCountBefore = getMessageCount(facesContext);

        UIComponent root = facesContext.getViewRoot();
        root.processValidators(facesContext);

        doEventProcessing(facesContext, PhaseId.PROCESS_VALIDATIONS);

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

        if (getMessageCount(facesContext) > messageCountBefore)
        {
            renderResponse(facesContext);
            if (log.isDebugEnabled()) log.debug("exiting processValidations in " + LifecycleImpl.class.getName() + " (after render response - because of messages during validation!)");
            return true;
        }

        if (log.isTraceEnabled()) log.trace("exiting processValidations in " + LifecycleImpl.class.getName());
        return false;
    }


    private int getMessageCount(FacesContext facesContext)
    {
        if (facesContext instanceof FacesContextImpl)
        {
            return ((FacesContextImpl)facesContext).getMessageCount();
        }
        else
        {
            int count = 0;
            Iterator it = facesContext.getMessages();
            while (it.hasNext())
            {
                it.next();
                count++;
            }
            return count;
        }
    }


    /**
     * Update Model Values (JSF.2.2.4)
     * @return true, if response is complete
     */
    private boolean updateModelValues(FacesContext facesContext) throws FacesException
    {
        if (log.isTraceEnabled()) log.trace("entering updateModelValues in " + LifecycleImpl.class.getName());

        UIComponent root = facesContext.getViewRoot();
        root.processUpdates(facesContext);

        doEventProcessing(facesContext, PhaseId.UPDATE_MODEL_VALUES);

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

        doEventProcessing(facesContext, PhaseId.INVOKE_APPLICATION);

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

        try
        {
            getViewHandler().renderView(facesContext, facesContext.getViewRoot());
        }
        catch (IOException e)
        {
            throw new FacesException(e.getMessage(), e);
        }

        if (log.isTraceEnabled()) log.trace("exiting renderResponse in " + LifecycleImpl.class.getName());
    }


    //event processing
    private void doEventProcessing(FacesContext facesContext, PhaseId phaseId)
    {
        for (Iterator it = facesContext.getFacesEvents(); it.hasNext();)
        {
            FacesEvent facesEvent = (FacesEvent)it.next();
            UIComponent comp = facesEvent.getComponent();
            try
            {
                if (!comp.broadcast(facesEvent, phaseId))
                {
                    it.remove();
                }
            }
            catch (AbortProcessingException e)
            {
                it.remove();
            }
        }
    }

    public static final String ACTION_LISTENER_FLAG = "ACTION_LISTENER_FLAG";

    private void traverseAndRegisterActionListener(ActionListener actionListener,
                                                   UIComponent uiComponent)
    {
        if (uiComponent instanceof UICommand &&
            uiComponent.getAttributes().get(ACTION_LISTENER_FLAG) == null)
        {
            ((UICommand)uiComponent).addActionListener(actionListener);
            uiComponent.getAttributes().put(ACTION_LISTENER_FLAG, Boolean.TRUE);
        }

        for (Iterator it = uiComponent.getFacetsAndChildren(); it.hasNext(); )
        {
            traverseAndRegisterActionListener(actionListener,
                                              (UIComponent)it.next());
        }
    }


    public static String deriveViewId(FacesContext facesContext)
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


    public void addPhaseListener(PhaseListener phaseListener) {
        _phaseListeners.add(phaseListener);
    }

    public PhaseListener[] getPhaseListeners() {
        return (PhaseListener[]) _phaseListeners.toArray(new PhaseListener[_phaseListeners.size()]);
    }

    public void removePhaseListener(PhaseListener phaseListener) {
        _phaseListeners.remove(phaseListener);
    }
}
