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
import java.util.Iterator;

/**
 * see Javadoc of JSF Specification
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.12  2004/08/22 10:37:29  mwessendorf
 * bug #1007065
 *
 * Revision 1.11  2004/07/01 22:00:48  mwessendorf
 * ASF switch
 *
 * Revision 1.10  2004/06/08 02:37:50  o_rossmueller
 * fix #967991: remove event from queue after broadcase
 * abort event procession on AbortProcessingException
 *
 * Revision 1.9  2004/05/12 07:57:40  manolito
 * Log in javadoc header
 *
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

        boolean abort = false;

        int phaseIdOrdinal = phaseId.getOrdinal();
        for (Iterator iterator = _events.iterator(); iterator.hasNext();)
        {
            FacesEvent event = (FacesEvent) iterator.next();
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
                    // abort event processing
                    // Page 3-30 of JSF 1.1 spec: "Throw an AbortProcessingException, to tell the JSF implementation
                    //  that no further broadcast of this event, or any further events, should take place."
                    abort = true;
                    break;
                } finally {
                    iterator.remove();
                }
            }
        }

        if (abort) {
            // TODO: abort processing of any event of any phase or just of any event of the current phase???
            clearEvents();
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
        _broadcastForPhase(PhaseId.UPDATE_MODEL_VALUES);
        if (context.getRenderResponse() || context.getResponseComplete())
        {
            clearEvents();
        }
    }

    public void processApplication(FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
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
            return getLocale((String)locale);
        }
        else
        {
            throw new IllegalArgumentException("locale binding"); //TODO: not specified!?
        }
    }

    /**
     * Create Locale from String representation.
     * 
     * @see http://java.sun.com/j2se/1.4.2/docs/api/java/util/Locale.html
     * @param locale locale representation in String.
     * @return Locale instance
     */
    private static Locale getLocale(String locale){
        int cnt = 0;
        int pos = 0;
        int prev = 0;
        
        // store locale variation.
        // ex. "ja_JP_POSIX"
        //  lv[0] : language(ja)
        //  lv[1] : country(JP)
        //  lv[2] : variant(POSIX)
        String[] lv = new String[3];
        Locale l=null;

        while((pos=locale.indexOf('_',prev))!=-1){
             lv[cnt++] = locale.substring(prev,pos);
             prev = pos + 1;
        }
        
        lv[cnt++] = locale.substring(prev,locale.length());
        
        switch(cnt){
            case 1:
                // create Locale from language.
                l = new Locale(lv[0]);
                break;
            case 2:
                // create Locale from language and country.
                l = new Locale(lv[0],lv[1]);
                break;
            case 3:
                // create Locale from language, country and variant.
                l = new Locale(lv[0], lv[1], lv[2]);
                break;
        }
        return l;
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
