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
package net.sourceforge.myfaces.lifecycle;

import net.sourceforge.myfaces.MyFacesFactoryFinder;
import net.sourceforge.myfaces.context.FacesContextImpl;
import net.sourceforge.myfaces.renderkit.html.state.StateRenderer;
import net.sourceforge.myfaces.util.logging.LogUtil;
import net.sourceforge.myfaces.webapp.ServletMapping;
import net.sourceforge.myfaces.webapp.ServletMappingFactory;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Message;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.lifecycle.ApplicationHandler;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.Phase;
import javax.faces.lifecycle.ViewHandler;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.tree.Tree;
import javax.faces.tree.TreeFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class LifecycleImpl
        extends Lifecycle
{
    private ApplicationHandler _applicationHandler = null;
    private ViewHandler _viewHandler = null;

    private TreeFactory _treeFactory;
    private RenderKitFactory _rkFactory;

    public LifecycleImpl()
    {
        //initPhases();
        _treeFactory = (TreeFactory)FactoryFinder.getFactory(FactoryFinder.TREE_FACTORY);
        _rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    }


    public int executePhase(FacesContext facescontext, Phase phase)
            throws FacesException
    {
        throw new UnsupportedOperationException("deprecated!");
    }

    public ApplicationHandler getApplicationHandler()
    {
        return _applicationHandler;
    }

    public ViewHandler getViewHandler()
    {
        if (_viewHandler == null)
        {
            _viewHandler = new ViewHandlerJspImpl();
        }
        return _viewHandler;
    }

    public void setApplicationHandler(ApplicationHandler applicationhandler)
    {
        _applicationHandler = applicationhandler;
    }

    public void setViewHandler(ViewHandler viewhandler)
    {
        _viewHandler = viewhandler;
    }




    public void execute(FacesContext facesContext)
        throws FacesException
    {
        reconstituteComponentTree(facesContext);

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
     * Reconstitute Component Tree (JSF.2.2.1)
     */
    private void reconstituteComponentTree(FacesContext facesContext)
        throws FacesException
    {
        LogUtil.getLogger().entering();

        //Set locale
        HttpServletRequest request = (HttpServletRequest)facesContext.getServletRequest();
        if (request.getLocale() != null)
        {
            facesContext.setLocale(request.getLocale());
        }
        else
        {
            facesContext.setLocale(Locale.getDefault());
        }

        //Create tree
        Tree tree = _treeFactory.getTree(facesContext, getTreeId(facesContext));
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
                LogUtil.getLogger().finest("StateRenderer found, calling decode.");
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
                if (LogUtil.getLogger().getLevel().intValue() <= Level.INFO.intValue())
                {
                    while (msgIt.hasNext())
                    {
                        Message msg = (Message)msgIt.next();
                        LogUtil.getLogger().info("Message, added during reconstituteComponentTree: " + msg.getSummary() + " / " + msg.getDetail());
                    }
                }

                if (facesContext instanceof FacesContextImpl)
                {
                    ((FacesContextImpl)facesContext).clearMessages();
                }
                else
                {
                    LogUtil.getLogger().warning("Messages were added during reconstituteComponentTree phase, but could not be removed afterwards, because current context is not instance of FacesContextImpl.");
                }
            }
        }
        else
        {
            LogUtil.getLogger().info("No StateRenderer found, cannot restore tree.");
        }

        LogUtil.getLogger().exiting();
    }


    /**
     * Apply Request Values (JSF.2.2.2)
     * @return true, if response is complete
     */
    private boolean applyRequestValues(FacesContext facesContext)
        throws FacesException
    {
        LogUtil.getLogger().entering();

        UIComponent root = facesContext.getTree().getRoot();
        try
        {
            root.processDecodes(facesContext);
        }
        catch (IOException e)
        {
            throw new FacesException(e);
        }

        doEventProcessing(facesContext, PhaseId.APPLY_REQUEST_VALUES);

        if (FacesContextImpl.isResponseComplete(facesContext))
        {
            LogUtil.getLogger().exiting("response complete", Level.INFO);
            return true;
        }

        if (FacesContextImpl.isRenderResponse(facesContext))
        {
            renderResponse(facesContext);
            LogUtil.getLogger().exiting("after render response", Level.INFO);
            return true;
        }

        LogUtil.getLogger().exiting();
        return false;
    }


    /**
     * Process Validations (JSF.2.2.3)
     * @return true, if response is complete
     */
    private boolean processValidations(FacesContext facesContext) throws FacesException
    {
        LogUtil.getLogger().entering();

        int messageCountBefore = getMessageCount(facesContext);

        UIComponent root = facesContext.getTree().getRoot();
        root.processValidators(facesContext);

        doEventProcessing(facesContext, PhaseId.PROCESS_VALIDATIONS);

        if (FacesContextImpl.isResponseComplete(facesContext))
        {
            LogUtil.getLogger().exiting("response complete", Level.INFO);
            return true;
        }

        if (FacesContextImpl.isRenderResponse(facesContext))
        {
            renderResponse(facesContext);
            LogUtil.getLogger().exiting("after render response", Level.INFO);
            return true;
        }

        if (getMessageCount(facesContext) > messageCountBefore)
        {
            renderResponse(facesContext);
            LogUtil.getLogger().exiting("after render response (because of messages during validation)", Level.INFO);
            return true;
        }

        LogUtil.getLogger().exiting();
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
        LogUtil.getLogger().entering();

        UIComponent root = facesContext.getTree().getRoot();
        root.processUpdates(facesContext);

        doEventProcessing(facesContext, PhaseId.UPDATE_MODEL_VALUES);

        if (FacesContextImpl.isResponseComplete(facesContext))
        {
            LogUtil.getLogger().exiting("response complete", Level.INFO);
            return true;
        }

        if (FacesContextImpl.isRenderResponse(facesContext))
        {
            renderResponse(facesContext);
            LogUtil.getLogger().exiting("after render response", Level.INFO);
            return true;
        }

        LogUtil.getLogger().exiting();
        return false;
    }


    /**
     * Invoke Application (JSF.2.2.5)
     * @return true, if response is complete
     */
    private boolean invokeApplication(FacesContext facesContext)
        throws FacesException
    {
        LogUtil.getLogger().entering();

        int eventsCount = facesContext.getApplicationEventsCount();
        if (eventsCount > 0)
        {
            ApplicationHandler handler = facesContext.getApplicationHandler();
            if (handler == null)
            {
                throw new FacesException("No application handler!");
            }

            for (Iterator events = facesContext.getApplicationEvents(); events.hasNext();)
            {
                if (handler.processEvent(facesContext, (FacesEvent)events.next()))
                {
                    renderResponse(facesContext);
                    LogUtil.getLogger().exiting("after render response (because event handler returned true)", Level.INFO);
                    return true;
                }
            }
        }

        if (FacesContextImpl.isResponseComplete(facesContext))
        {
            LogUtil.getLogger().exiting("response complete", Level.INFO);
            return true;
        }

        LogUtil.getLogger().exiting();
        return false;
    }

    /**
     * Render Response (JSF.2.2.6)
     */
    private void renderResponse(FacesContext facesContext)
        throws FacesException
    {
        LogUtil.getLogger().entering();

        try
        {
            getViewHandler().renderView(facesContext);
        }
        catch (IOException e)
        {
            throw new FacesException(e.getMessage(), e);
        }
        catch (ServletException e)
        {
            throw new FacesException(e.getMessage(), e);
        }

        LogUtil.getLogger().exiting();
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



    public static String getTreeId(FacesContext facesContext)
    {
        ServletRequest request = facesContext.getServletRequest();
        ServletContext servletContext = facesContext.getServletContext();
        ServletMappingFactory servletMappingFactory = MyFacesFactoryFinder.getServletMappingFactory(servletContext);
        ServletMapping sm = servletMappingFactory.getServletMapping(servletContext);
        return sm.getTreeIdFromRequest(request);
    }

}
