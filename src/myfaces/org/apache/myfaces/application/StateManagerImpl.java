package net.sourceforge.myfaces.application;

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
public class StateManagerImpl
    extends StateManager
{
    private static final Log log = LogFactory.getLog(StateManagerImpl.class);

    public StateManagerImpl()
    {
        if (log.isTraceEnabled()) log.trace("New StateManager instance created");
    }

    protected Object getComponentStateToSave(FacesContext facescontext)
    {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    protected Object getTreeStructureToSave(FacesContext facescontext)
    {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    protected void restoreComponentState(FacesContext facescontext, UIViewRoot uiviewroot) throws IOException
    {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    protected UIViewRoot restoreTreeStructure(FacesContext facescontext, String s)
    {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    public UIViewRoot restoreView(FacesContext facescontext, String s)
    {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    public StateManager.SerializedView saveSerializedView(FacesContext facescontext) throws IllegalStateException
    {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    public void writeState(FacesContext facescontext, StateManager.SerializedView serializedview) throws IOException
    {
        throw new UnsupportedOperationException("not yet implemented.");
    }
}
