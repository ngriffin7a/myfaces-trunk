package net.sourceforge.myfaces.application.jsp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * DOCUMENT ME!
 * 
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class JspStateManagerImpl
    extends StateManager
{
    private static final Log log = LogFactory.getLog(JspStateManagerImpl.class);
    private static final String VIEW_MAP_SESSION_PARAM
            = JspStateManagerImpl.class.getName() + ".VIEW_MAP";

    private RenderKitFactory _renderKitFactory = null;

    public JspStateManagerImpl()
    {
        if (log.isTraceEnabled()) log.trace("New JspStateManagerImpl instance created");
    }


    protected Object getTreeStructureToSave(FacesContext facescontext)
    {
        Object treeStructure = null; // TODO: implement
        return treeStructure;
    }

    protected Object getComponentStateToSave(FacesContext facesContext)
    {
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Object serializedComponentStates = viewRoot.processSaveState(facesContext);
        //Locale is a state attribute of UIViewRoot and need not be saved explicitly
        return serializedComponentStates;
    }

    public StateManager.SerializedView saveSerializedView(FacesContext facesContext) throws IllegalStateException
    {
        if (isSavingStateInClient(facesContext))
        {
            Object treeStruct = getTreeStructureToSave(facesContext);
            Object compStates = getComponentStateToSave(facesContext);
            return new StateManager.SerializedView(treeStruct, compStates);
        }
        else
        {
            return null;
        }
    }

    public void writeState(FacesContext facesContext,
                           StateManager.SerializedView serializedView) throws IOException
    {
        ExternalContext externalContext = facesContext.getExternalContext();
        UIViewRoot uiViewRoot = facesContext.getViewRoot();
        if (isSavingStateInClient(facesContext))
        {
            //save state in response (client)
            RenderKit renderKit = getRenderKitFactory().getRenderKit(uiViewRoot.getRenderKitId(), facesContext);
            renderKit.getResponseStateManager().writeState(facesContext, serializedView);
        }
        else
        {
            //save state in server session
            Map viewMap = getViewMap(externalContext);
            viewMap.put(uiViewRoot.getViewId(), uiViewRoot);
        }
    }



    protected UIViewRoot restoreTreeStructure(FacesContext facesContext, String viewId)
    {
        //TODO: We do not know the former renderKitId !?
        RenderKit rk = getRenderKitFactory().getRenderKit(RenderKitFactory.DEFAULT_RENDER_KIT, facesContext);
        ResponseStateManager responseStateManager = rk.getResponseStateManager();
        Object treeStructure = responseStateManager.getTreeStructureToRestore(facesContext, viewId);
        if (treeStructure == null)
        {
            return null;
        }
        else
        {
            UIViewRoot uiViewRoot = null;   //TODO: implement
            return uiViewRoot;
        }
    }

    protected void restoreComponentState(FacesContext facesContext, UIViewRoot uiViewRoot) throws IOException
    {
        ResponseStateManager responseStateManager = getResponseStateManager(facesContext, uiViewRoot);
        Object serializedComponentStates = responseStateManager.getComponentStateToRestore(facesContext);
        uiViewRoot.processRestoreState(facesContext, serializedComponentStates);
    }

    public UIViewRoot restoreView(FacesContext facescontext, String viewId)
    {
        UIViewRoot uiViewRoot = restoreTreeStructure(facescontext, viewId);
        try
        {
            restoreComponentState(facescontext, uiViewRoot);
        }
        catch (IOException e)
        {
            throw new FacesException(e);
        }
        return uiViewRoot;
    }



    //helpers

    protected ResponseStateManager getResponseStateManager(FacesContext facesContext,
                                                           UIViewRoot uiViewRoot)
    {
        RenderKit renderKit = getRenderKitFactory().getRenderKit(uiViewRoot.getRenderKitId(), facesContext);
        return renderKit.getResponseStateManager();
    }

    protected RenderKitFactory getRenderKitFactory()
    {
        if (_renderKitFactory == null)
        {
            _renderKitFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        }
        return _renderKitFactory;
    }

    protected Map getViewMap(ExternalContext externalContext)
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
}
