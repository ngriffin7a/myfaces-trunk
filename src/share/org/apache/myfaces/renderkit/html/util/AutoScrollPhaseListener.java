package org.apache.myfaces.renderkit.html.util;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * This Phase listener is necessary for the autoscroll feature.
 * Its only purpose is to determine the former viewId and store it in request scope
 * so that we can later determine if a navigation has happened during rendering.
 */
public class AutoScrollPhaseListener
        implements PhaseListener
{
    //private static final Log log = LogFactory.getLog(AutoScrollPhaseListener.class);

    public PhaseId getPhaseId()
    {
        return PhaseId.RESTORE_VIEW;
    }

    public void beforePhase(PhaseEvent event)
    {
    }

    public void afterPhase(PhaseEvent event)
    {
        FacesContext facesContext = event.getFacesContext();
        String viewId = facesContext.getViewRoot().getViewId();
        if (viewId != null)
        {
            JavascriptUtils.setOldViewId(facesContext.getExternalContext(), viewId);
        }
    }

}
