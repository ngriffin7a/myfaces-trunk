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
package net.sourceforge.myfaces.application.jsp;

import net.sourceforge.myfaces.application.MyfacesStateManager;
import net.sourceforge.myfaces.application.TreeStructureManager;
import net.sourceforge.myfaces.renderkit.MyfacesResponseStateManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;
import java.io.IOException;

/**
 * Default StateManager implementation.
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 *
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Manfred Geiler
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.20  2004/08/15 15:35:26  o_rossmueller
 * fix #1008685: hold view state by viewId
 *
 * Revision 1.19  2004/08/15 15:31:09  o_rossmueller
 * fix #1008685: hold view state by viewId
 *
 * Revision 1.18  2004/08/13 13:15:20  manolito
 * FIXME comment
 *
 * Revision 1.17  2004/07/21 11:22:40  mwessendorf
 * last modification in effect of Adam Winer bug-report.
 *
 * Revision 1.16  2004/07/01 22:05:20  mwessendorf
 * ASF switch
 *
 * Revision 1.15  2004/05/18 08:29:38  manolito
 * saveSerializedView now caches the SerializedView within the request, so that multiple calls only process the saveState methods of components once.
 *
 * Revision 1.14  2004/04/13 08:08:08  manolito
 * NPE bug fix
 *
 * Revision 1.13  2004/04/06 10:26:03  royalts
 * restoreView(...) returns null if restoredViewId != viewId
 *
 * Revision 1.12  2004/04/06 10:20:27  manolito
 * no state restoring for different viewId
 *
 * Revision 1.11  2004/03/25 08:52:40  manolito
 * fixed server state saving
 */
public class JspStateManagerImpl
    extends MyfacesStateManager
{
    private static final Log log = LogFactory.getLog(JspStateManagerImpl.class);
    private static final String SERIALIZED_VIEW_SESSION_ATTR
            = JspStateManagerImpl.class.getName() + ".SERIALIZED_VIEW";
    private static final String SERIALIZED_VIEW_REQUEST_ATTR
            = JspStateManagerImpl.class.getName() + ".SERIALIZED_VIEW";

    private RenderKitFactory _renderKitFactory = null;

    public JspStateManagerImpl()
    {
        if (log.isTraceEnabled()) log.trace("New JspStateManagerImpl instance created");
    }


    protected Object getComponentStateToSave(FacesContext facesContext)
    {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (viewRoot.isTransient())
        {
            return null;
        }

        Object serializedComponentStates = viewRoot.processSaveState(facesContext);
        //Locale is a state attribute of UIViewRoot and need not be saved explicitly
        return serializedComponentStates;
    }

    protected Object getTreeStructureToSave(FacesContext facesContext)
    {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (viewRoot.isTransient())
        {
            return null;
        }
        TreeStructureManager tsm = new TreeStructureManager();
        return tsm.buildTreeStructureToSave(viewRoot);
    }

    protected void restoreComponentState(FacesContext facesContext,
                                         UIViewRoot uiViewRoot,
                                         String renderKitId)
    {
        Object serializedComponentStates;
        if (isSavingStateInClient(facesContext))
        {
            RenderKit renderKit = getRenderKitFactory().getRenderKit(facesContext, renderKitId);
            ResponseStateManager responseStateManager = renderKit.getResponseStateManager();
            serializedComponentStates = responseStateManager.getComponentStateToRestore(facesContext);
            if (serializedComponentStates == null)
            {
                log.error("No serialized component state found in client request!");
                // mark UIViewRoot invalid by resetting view id
                uiViewRoot.setViewId(null);
                return;
            }
        }
        else
        {
            SerializedView serializedView = getSerializedViewFromServletSession(facesContext.getExternalContext(),
                                                                                uiViewRoot.getViewId());
            if (serializedView == null)
            {
                log.error("No serialized view found in server session!");
                // mark UIViewRoot invalid by resetting view id
                uiViewRoot.setViewId(null);
                return;
            }
            serializedComponentStates = serializedView.getState();
            if (serializedComponentStates == null)
            {
                log.error("No serialized component state found in server session!");
                return;
            }
        }

        if (uiViewRoot.getRenderKitId() == null)
        {
            //Just to be sure...
            uiViewRoot.setRenderKitId(renderKitId);
        }

        uiViewRoot.processRestoreState(facesContext, serializedComponentStates);
    }

    protected UIViewRoot restoreTreeStructure(FacesContext facesContext,
                                              String viewId,
                                              String renderKitId)
    {
        UIViewRoot uiViewRoot = null;
        if (isSavingStateInClient(facesContext))
        {
            //reconstruct tree structure from request
            RenderKit rk = getRenderKitFactory().getRenderKit(facesContext, renderKitId);
            ResponseStateManager responseStateManager = rk.getResponseStateManager();
            Object treeStructure = responseStateManager.getTreeStructureToRestore(facesContext, viewId);
            if (treeStructure == null)
            {
                if (log.isDebugEnabled()) log.debug("No tree structure state found in client request");
                return null;
            }

            TreeStructureManager tsm = new TreeStructureManager();
            uiViewRoot = tsm.restoreTreeStructure((TreeStructureManager.TreeStructComponent)treeStructure);
            if (log.isTraceEnabled()) log.trace("Tree structure restored from client request");
        }
        else
        {
            //reconstruct tree structure from ServletSession
            SerializedView serializedView = getSerializedViewFromServletSession(facesContext.getExternalContext(),
                                                                                viewId);
            if (serializedView == null)
            {
                if (log.isDebugEnabled()) log.debug("No serialized view found in server session!");
                return null;
            }

            Object treeStructure = serializedView.getStructure();
            if (treeStructure == null)
            {
                if (log.isDebugEnabled()) log.debug("No tree structure state found in server session, former UIViewRoot must have been transient");
                return null;
            }

            TreeStructureManager tsm = new TreeStructureManager();
            uiViewRoot = tsm.restoreTreeStructure((TreeStructureManager.TreeStructComponent)serializedView.getStructure());
            if (log.isTraceEnabled()) log.trace("Tree structure restored from server session");
        }

        return uiViewRoot;
    }

    public UIViewRoot restoreView(FacesContext facescontext, String viewId, String renderKitId)
    {
        UIViewRoot uiViewRoot = restoreTreeStructure(facescontext, viewId, renderKitId);
        if (uiViewRoot != null)
        {
            uiViewRoot.setViewId(viewId);
            restoreComponentState(facescontext, uiViewRoot, renderKitId);
            String restoredViewId = uiViewRoot.getViewId();
            if (restoredViewId == null || !(restoredViewId.equals(viewId)))
            {
                return null;
            }

            if (!isSavingStateInClient(facescontext))
            {
                removeSerializedViewFromServletSession(facescontext.getExternalContext(), viewId);
            }
        }
        return uiViewRoot;
    }

    public SerializedView saveSerializedView(FacesContext facesContext) throws IllegalStateException
    {
        //removeTransientComponents(facesContext.getViewRoot());

        ExternalContext externalContext = facesContext.getExternalContext();

        // SerializedView already created before within this request?
        SerializedView serializedView = (SerializedView)externalContext.getRequestMap()
                                                            .get(SERIALIZED_VIEW_REQUEST_ATTR);
        if (serializedView == null)
        {
            // first call to saveSerializedView --> create SerializedView
            Object treeStruct = getTreeStructureToSave(facesContext);
            Object compStates = getComponentStateToSave(facesContext);
            serializedView = new StateManager.SerializedView(treeStruct, compStates);
            externalContext.getRequestMap().put(SERIALIZED_VIEW_REQUEST_ATTR,
                                                serializedView);
        }

        if (isSavingStateInClient(facesContext))
        {
            return serializedView;
        }
        else
        {
            //save state in server session
            saveSerializedViewInServletSession(externalContext,
                                               facesContext.getViewRoot().getViewId(),
                                               serializedView);
            return null;    //return null to signal that saving is done
        }
    }

    public void writeState(FacesContext facesContext,
                           SerializedView serializedView) throws IOException
    {
        if (!isSavingStateInClient(facesContext))
        {
            throw new IllegalStateException("Must not be called in server state saving mode");
        }

        UIViewRoot uiViewRoot = facesContext.getViewRoot();
        //save state in response (client)
        RenderKit renderKit = getRenderKitFactory().getRenderKit(facesContext, uiViewRoot.getRenderKitId());
        renderKit.getResponseStateManager().writeState(facesContext, serializedView);
    }

    /**
     * MyFaces extension
     * @param facesContext
     * @param serializedView
     * @throws IOException
     */
    public void writeStateAsUrlParams(FacesContext facesContext,
                                      SerializedView serializedView) throws IOException
    {
        if (!isSavingStateInClient(facesContext))
        {
            throw new IllegalStateException("Must not be called in server state saving mode");
        }

        UIViewRoot uiViewRoot = facesContext.getViewRoot();
        //save state in response (client)
        RenderKit renderKit = getRenderKitFactory().getRenderKit(facesContext, uiViewRoot.getRenderKitId());
        ResponseStateManager responseStateManager = renderKit.getResponseStateManager();
        if (responseStateManager instanceof MyfacesResponseStateManager)
        {
            ((MyfacesResponseStateManager)responseStateManager).writeStateAsUrlParams(facesContext,
                                                                                      serializedView);
        }
        else
        {
            log.error("ResponseStateManager of render kit " + uiViewRoot.getRenderKitId() + " is no MyfacesResponseStateManager and does not support saving state in url parameters.");
        }
    }



    //helpers

    protected RenderKitFactory getRenderKitFactory()
    {
        if (_renderKitFactory == null)
        {
            _renderKitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        }
        return _renderKitFactory;
    }
    
    protected void saveSerializedViewInServletSession(ExternalContext externalContext,
                                                      String viewId,
                                                      SerializedView serializedView)
    {
        // TODO: What, if user has more than one browser window open on the same page?!
        // only the state of the latest accessed window will be stored at the moment
        externalContext.getSessionMap().put(SERIALIZED_VIEW_SESSION_ATTR + "-" + viewId,
                                            new Object[] {viewId, serializedView});
    }
    
    protected SerializedView getSerializedViewFromServletSession(ExternalContext externalContext,
                                                                 String viewId)
    {
        Object[] ar = (Object[])externalContext.getSessionMap().get(SERIALIZED_VIEW_SESSION_ATTR + "-" + viewId);
        if (ar == null) return null;    // no state information in session
        String savedViewId = (String)ar[0];
        if (viewId == null || viewId.equals(savedViewId))
        {
            return (SerializedView)ar[1];
        }
        else
        {
            //saved state applies to different viewId
            return null;
        }
    }

    protected void removeSerializedViewFromServletSession(ExternalContext externalContext, String viewId)
    {
        externalContext.getSessionMap().remove(SERIALIZED_VIEW_SESSION_ATTR + "-" + viewId);
    }

    /*
    protected Map getSessionViewMap(ExternalContext externalContext)
    {
        Map sessionMap = externalContext.getSessionMap();
        Map viewMap = (Map)sessionMap.get(VIEW_MAP_SESSION_PARAM);
        if (viewMap == null)
        {
            viewMap = new HashMap();
            sessionMap.put(VIEW_MAP_SESSION_PARAM, viewMap);
        }
        return viewMap;
    }
    */


    /*
    private void removeTransientComponents(UIComponent root)
    {
        if (root.getChildCount() > 0)
        {
            for (Iterator it = root.getChildren().iterator(); it.hasNext(); )
            {
                UIComponent child = (UIComponent)it.next();
                if (child.isTransient())
                {
                    it.remove();
                }
            }
        }

        for (Iterator it = root.getFacets().values().iterator(); it.hasNext(); )
        {
            UIComponent facet = (UIComponent)it.next();
            if (facet.isTransient())
            {
                it.remove();
            }
        }
    }
    */
}
