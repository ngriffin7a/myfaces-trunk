package net.sourceforge.myfaces.application.jsp;

import net.sourceforge.myfaces.application.MyfacesStateManager;
import net.sourceforge.myfaces.application.TreeStructureManager;
import net.sourceforge.myfaces.renderkit.MyfacesResponseStateManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * DOCUMENT ME!
 * 
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JspStateManagerImpl
    extends MyfacesStateManager
{
    private static final Log log = LogFactory.getLog(JspStateManagerImpl.class);
    private static final String VIEW_MAP_SESSION_PARAM
            = JspStateManagerImpl.class.getName() + ".VIEW_MAP";

    private RenderKitFactory _renderKitFactory = null;

    public JspStateManagerImpl()
    {
        if (log.isTraceEnabled()) log.trace("New JspStateManagerImpl instance created");
    }


    protected Object getComponentStateToSave(FacesContext facesContext)
    {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (viewRoot.isTransient() || !isSavingStateInClient(facesContext))
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
                return;
            }
        }
        else
        {
            Map sessionViewMap = getSessionViewMap(facesContext.getExternalContext());
            SerializedView serializedView = (SerializedView)sessionViewMap.get(uiViewRoot.getViewId());
            if (serializedView != null)
            {
                serializedComponentStates = serializedView.getState();
                if (serializedComponentStates == null)
                {
                    log.error("No serialized component state found in server session!");
                    return;
                }
            }
            else
            {
                log.error("No serialized view found in server session!");
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
            if (treeStructure != null)
            {
                TreeStructureManager tsm = new TreeStructureManager();
                uiViewRoot = tsm.restoreTreeStructure((TreeStructureManager.TreeStructComponent)treeStructure);
            }
            if (log.isDebugEnabled()) log.debug("No tree structure state found in client request!");
        }
        else
        {
            //get component tree from server session
            Map sessionViewMap = getSessionViewMap(facesContext.getExternalContext());
            SerializedView serializedView = (SerializedView)sessionViewMap.get(viewId);
            if (serializedView != null && serializedView.getStructure() != null)
            {
                TreeStructureManager tsm = new TreeStructureManager();
                uiViewRoot = tsm.restoreTreeStructure((TreeStructureManager.TreeStructComponent)serializedView.getStructure());
            }
            if (log.isDebugEnabled()) log.debug("No tree structure state found in server session!");
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
        }
        return uiViewRoot;
    }

    public SerializedView saveSerializedView(FacesContext facesContext) throws IllegalStateException
    {
        //Sun's UIComponentBase has a bug:
        //Component state saving and restoring does not work right if there
        //are transient components. So, we must remove transient components
        //before calling viewRoots processSaveState method.
        removeTransientComponents(facesContext.getViewRoot()); //TODO: still necessary?

        Object treeStruct = getTreeStructureToSave(facesContext);
        Object compStates = getComponentStateToSave(facesContext);
        SerializedView serializedView = new SerializedView(treeStruct, compStates);
        if (isSavingStateInClient(facesContext))
        {
            return serializedView;
        }
        else
        {
            //save state in server session
            ExternalContext externalContext = facesContext.getExternalContext();
            Map sessionViewMap = getSessionViewMap(externalContext);
            sessionViewMap.put(facesContext.getViewRoot().getViewId(), serializedView);
            return null;
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


    protected void removeTransientComponents(UIComponent root)
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
}
