package net.sourceforge.myfaces.application.jsp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
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

    public void writeState(FacesContext facescontext, StateManager.SerializedView serializedview) throws IOException
    {
        // TODO: implement
        throw new UnsupportedOperationException("not yet implemented.");
    }
}
