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
package javax.faces.component;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.RenderKitFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * see Javadoc of JSF Specification
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UIViewRoot
        extends UIComponentBase
{
    public static final String UNIQUE_ID_PREFIX = "_id";
    public static final int ANY_PHASE_ORDINAL = PhaseId.ANY_PHASE.getOrdinal();

    private int _uniqueIdCounter = 0;

    private String _viewId = null;
    private Locale _locale = null;
    private List _events = null;

    public String getViewId()
    {
        return _viewId;
    }

    public void setViewId(String viewId)
    {
        if (viewId == null) throw new NullPointerException("viewId");
        _viewId = viewId;
    }

    public void queueEvent(FacesEvent event)
    {
        if (event == null) throw new NullPointerException("event");
        if (_events == null)
        {
            _events = new ArrayList();
        }
        _events.add(event);
    }

    private void _broadcastForPhase(PhaseId phaseId)
    {
        if (_events == null) return;
        int phaseIdOrdinal = phaseId.getOrdinal();
        for (int i = 0; i < _events.size(); i++) //No optimization for size() call here, because List may grow during loop!
        {
            FacesEvent event = (FacesEvent)_events.get(i);
            int ordinal = event.getPhaseId().getOrdinal();
            if (ordinal == ANY_PHASE_ORDINAL ||
                ordinal == phaseIdOrdinal)
            {
                UIComponent source = event.getComponent();
                try
                {
                    source.broadcast(event);
                }
                catch (AbortProcessingException e)
                {
                    // we do not abort event processing itself
                    // javadoc: "no further processing on the _current_ event should be performed"
                }
            }
        }
    }


    private void clearEvents()
    {
        _events = null;
    }


    public void processDecodes(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        super.processDecodes(context);
        _broadcastForPhase(PhaseId.ANY_PHASE);
        _broadcastForPhase(PhaseId.APPLY_REQUEST_VALUES);
        if (context.getRenderResponse() || context.getResponseComplete())
        {
            clearEvents();
        }
    }

    public void processValidators(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        super.processValidators(context);
        _broadcastForPhase(PhaseId.ANY_PHASE);
        _broadcastForPhase(PhaseId.PROCESS_VALIDATIONS);
        if (context.getRenderResponse() || context.getResponseComplete())
        {
            clearEvents();
        }
    }

    public void processUpdates(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        super.processUpdates(context);
        _broadcastForPhase(PhaseId.ANY_PHASE);
        _broadcastForPhase(PhaseId.UPDATE_MODEL_VALUES);
        if (context.getRenderResponse() || context.getResponseComplete())
        {
            clearEvents();
        }
    }

    public void processApplication(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        _broadcastForPhase(PhaseId.ANY_PHASE);
        _broadcastForPhase(PhaseId.INVOKE_APPLICATION);
        if (context.getRenderResponse() || context.getResponseComplete())
        {
            clearEvents();
        }
    }

    public void encodeBegin(FacesContext context)
            throws java.io.IOException
    {
        _uniqueIdCounter = 0;
        clearEvents();
        super.encodeBegin(context);
    }

    public String createUniqueId()
    {
        return UNIQUE_ID_PREFIX + _uniqueIdCounter++;
    }

    public Locale getLocale()
    {
        if (_locale != null) return _locale;
        ValueBinding vb = getValueBinding("locale");
        FacesContext facesContext = getFacesContext();
        if (vb == null)
        {
            return facesContext.getApplication().getViewHandler().calculateLocale(facesContext);
        }
        Object locale = (Locale)vb.getValue(facesContext);
        if (locale == null)
        {
            return facesContext.getApplication().getViewHandler().calculateLocale(facesContext);
        }
        if (locale instanceof Locale)
        {
            return (Locale)locale;
        }
        else if (locale instanceof String)
        {
            return new Locale((String)locale); //TODO: what do they mean by create Locale from String?
        }
        else
        {
            throw new IllegalArgumentException("locale binding"); //TODO: not specified!?
        }
    }

    public void setLocale(Locale locale)
    {
        _locale = locale;
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "javax.faces.ViewRoot";
    public static final String COMPONENT_FAMILY = "javax.faces.ViewRoot";
    private static final String DEFAULT_RENDERKITID = RenderKitFactory.HTML_BASIC_RENDER_KIT;

    private String _renderKitId = null;

    public UIViewRoot()
    {
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }


    public void setRenderKitId(String renderKitId)
    {
        _renderKitId = renderKitId;
    }

    public String getRenderKitId()
    {
        if (_renderKitId != null) return _renderKitId;
        ValueBinding vb = getValueBinding("renderKitId");
        return vb != null ? (String)vb.getValue(getFacesContext()) : DEFAULT_RENDERKITID;
    }



    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[4];
        values[0] = super.saveState(context);
        values[1] = _locale;
        values[2] = _renderKitId;
        values[3] = _viewId;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _locale = (Locale)values[1];
        _renderKitId = (String)values[2];
        _viewId = (String)values[3];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}
