package net.sourceforge.myfaces.application.jsp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
    private static final String VIEW_STATE_SESSION_PARAM
            = JspStateManagerImpl.class.getName() + ".VIEW_STATE";

    public JspStateManagerImpl()
    {
        if (log.isTraceEnabled()) log.trace("New StateManager instance created");
    }

    protected Object getComponentStateToSave(FacesContext facescontext)
    {
        // TODO: implement
        throw new UnsupportedOperationException("not yet implemented.");
    }

    protected Object getTreeStructureToSave(FacesContext facescontext)
    {
        // TODO: implement
        throw new UnsupportedOperationException("not yet implemented.");
    }

    protected void restoreComponentState(FacesContext facescontext, UIViewRoot uiviewroot) throws IOException
    {
        // TODO: implement
        throw new UnsupportedOperationException("not yet implemented.");
    }

    protected UIViewRoot restoreTreeStructure(FacesContext facescontext, String s)
    {
        // TODO: implement
        throw new UnsupportedOperationException("not yet implemented.");
    }

    public UIViewRoot restoreView(FacesContext facescontext, String s)
    {
        // TODO: implement
        throw new UnsupportedOperationException("not yet implemented.");
    }

    public StateManager.SerializedView saveSerializedView(FacesContext facescontext) throws IllegalStateException
    {
        // TODO: implement
        throw new UnsupportedOperationException("not yet implemented.");
    }

    public void writeState(FacesContext facesContext,
                           StateManager.SerializedView serializedView) throws IOException
    {
        ExternalContext externalContext = facesContext.getExternalContext();
        String stateSavingMethod = externalContext.getInitParameter(StateManager.STATE_SAVING_METHOD_PARAM_NAME);
        if (stateSavingMethod.equals(StateManager.STATE_SAVING_METHOD_CLIENT))
        {
            //save state in response (client)
            UIViewRoot uiViewRoot = facesContext.getViewRoot();
            RenderKitFactory rkFactory = (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
            RenderKit renderKit = rkFactory.getRenderKit(uiViewRoot.getRenderKitId(), facesContext);
            renderKit.getResponseStateManager().writeState(facesContext, serializedView);
        }
        else //TODO: Is it ok to default to server saving?
        {
            //save state in server session
            //JSP specific class, so we may cast:
            HttpSession session = (HttpSession)externalContext.getSession(true);
            session.setAttribute(VIEW_STATE_SESSION_PARAM, serializedView);
        }
    }
}
