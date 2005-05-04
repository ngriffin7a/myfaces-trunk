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
package org.apache.myfaces.custom.aliasbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The aliasBean tag allows you to link a fictive bean to a real bean.
 * 
 * Let's suppose you have a subform you use often but with different beans.
 * <br/>The aliasBean allows you to design the subform with a fictive bean and
 * to include it in all the pages where you use it. You just need to make an
 * alias to the real bean named after the fictive bean before invoking the
 * fictive bean. <br/>This making it possible to have a library of reusable
 * generic subforms.
 * 
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.10  2005/03/15 00:34:17  svieujot
 * Close MYFACES-128, thanks to Mathias Broekelmann.
 *
 * Revision 1.9  2005/03/10 22:42:26  svieujot
 * Close MYFACES-125 thanks to Mathias Broekelmann.
 *
 * Revision 1.8  2005/03/10 15:11:00  svieujot
 * Fix MYFACES-125 thanks to Mathias Broekelmann.
 *
 * Revision 1.7  2005/01/27 16:00:30  svieujot
 * *** empty log message ***
 *
 * Revision 1.6  2005/01/27 01:59:45  svieujot
 * AliasBean : Change sourceBean attribute for value.
 * Make it work with both beans references ( #{myBean} ), and fix strings as value.
 * Document tld.
 *
 * Revision 1.5  2005/01/04 15:41:06  svieujot
 * new x:buffer component.
 *
 * Revision 1.4  2004/11/23 11:03:35  svieujot
 * Get ride of the x:aliasBean "permanent" attribute.
 * 
 * Revision 1.3 2004/11/23 04:46:40 svieujot Add an ugly "permanent"
 * tag to x:aliasBean to handle children events.
 * 
 * Revision 1.2 2004/11/14 15:06:36 svieujot Improve AliasBean to make the alias
 * effective only within the tag body
 * 
 * Revision 1.1 2004/11/08 20:43:15 svieujot Add an x:aliasBean component
 * 
 */
public class AliasBean extends UIComponentBase {
    private static final Log log = LogFactory.getLog(AliasBean.class);

    public static final String COMPONENT_TYPE = "org.apache.myfaces.AliasBean";
    public static final String COMPONENT_FAMILY = "javax.faces.Data";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.AliasBean";

    private String _valueExpression = null;

    private String _aliasBeanExpression = null;

    private transient FacesContext _context = null;
	private transient Object evaluatedExpression = null;

    public AliasBean() {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public void setAlias(String aliasBeanExpression){
        this._aliasBeanExpression = aliasBeanExpression;
    }
    
    public String getValue(){
        if (_valueExpression != null)
            return _valueExpression;
        ValueBinding vb = getValueBinding("value");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }
    public void setValue(String valueExpression){
        this._valueExpression = valueExpression;
    }

    public Object saveState(FacesContext context) {
        log.debug("saveState");

        _context = context;

        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = _valueExpression;
        values[2] = _aliasBeanExpression;
        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        log.debug("restoreState");

        _context = context;

        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _valueExpression = (String) values[1];
        _aliasBeanExpression = (String) values[2];
    }

    public Object processSaveState(FacesContext context) {
        if (context == null)
            throw new NullPointerException("context");
        if (isTransient())
            return null;
		
		makeAlias(context);
		
        Map facetMap = null;
        for (Iterator it = getFacets().entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            if (facetMap == null)
                facetMap = new HashMap();
            UIComponent component = (UIComponent) entry.getValue();
            if (!component.isTransient()) {
                facetMap.put(entry.getKey(), component.processSaveState(context));
            }
        }
        List childrenList = null;
        if (getChildCount() > 0) {
            for (Iterator it = getChildren().iterator(); it.hasNext();) {
                UIComponent child = (UIComponent) it.next();
                if (!child.isTransient()) {
                    if (childrenList == null)
                        childrenList = new ArrayList(getChildCount());
                    childrenList.add(child.processSaveState(context));
                }
            }
        }
		
		removeAlias(context);
		
        return new Object[] { saveState(context), facetMap, childrenList };
    }

    public void processRestoreState(FacesContext context, Object state) {
        if (context == null)
            throw new NullPointerException("context");
        Object myState = ((Object[]) state)[0];

        restoreState(context, myState);
        makeAlias(context);

        Map facetMap = (Map) ((Object[]) state)[1];
        List childrenList = (List) ((Object[]) state)[2];
        for (Iterator it = getFacets().entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            Object facetState = facetMap.get(entry.getKey());
            if (facetState != null) {
                ((UIComponent) entry.getValue()).processRestoreState(context, facetState);
            } else {
                context.getExternalContext().log("No state found to restore facet " + entry.getKey());
            }
        }
        if (getChildCount() > 0) {
            int idx = 0;
            for (Iterator it = getChildren().iterator(); it.hasNext();) {
                UIComponent child = (UIComponent) it.next();
                Object childState = childrenList.get(idx++);
                if (childState != null) {
                    child.processRestoreState(context, childState);
                } else {
                    context.getExternalContext().log("No state found to restore child of component " + getId());
                }
            }
        }

        removeAlias(context);
    }

    public void processValidators(FacesContext context) {
        log.debug("processValidators");
        makeAlias(context);
        super.processValidators(context);
        removeAlias(context);
    }

	public void processDecodes(FacesContext context) {
		log.debug("processDecodes");
		makeAlias(context);
		super.processDecodes(context);
		removeAlias(context);
	}
	
    public void processUpdates(FacesContext context) {
        log.debug("processUpdates");
        makeAlias(context);
        super.processUpdates(context);
        removeAlias(context);
    }

    public void queueEvent(FacesEvent event) {
		super.queueEvent(new FacesEventWrapper(event, this));
    }

    public void broadcast(FacesEvent event) throws AbortProcessingException {
        makeAlias();

        if (event instanceof FacesEventWrapper) {
            FacesEvent originalEvent = ((FacesEventWrapper) event).getWrappedFacesEvent();
            originalEvent.getComponent().broadcast(originalEvent);
        } else {
            super.broadcast(event);
        }

        removeAlias();
    }

    void makeAlias(FacesContext context) {
        _context = context;
        makeAlias();
    }
	
	private void computeEvaluatedExpression(){
		if( evaluatedExpression != null )
			return;
		
		ValueBinding valueVB = null;
        if (_valueExpression == null) {
            valueVB = getValueBinding("value");
            _valueExpression = valueVB.getExpressionString();
        }

        if( valueVB == null ){
            if( _valueExpression.startsWith("#{") ){
                valueVB = _context.getApplication().createValueBinding(_valueExpression);
				evaluatedExpression = valueVB.getValue(_context);
            }else{
				evaluatedExpression = _valueExpression;
            }
        }else{
			evaluatedExpression = valueVB.getValue(_context);
        }
	}

    private void makeAlias() {
        // First, compute the value or reference
		computeEvaluatedExpression();

        // Then set the alias to this value
        ValueBinding aliasVB;
        if (_aliasBeanExpression == null) {
            aliasVB = getValueBinding("alias");
            _aliasBeanExpression = aliasVB.getExpressionString();
        } else {
            aliasVB = _context.getApplication().createValueBinding(_aliasBeanExpression);
        }

        aliasVB.setValue(_context, evaluatedExpression);

        log.debug("makeAlias: " + _valueExpression + " = " + _aliasBeanExpression);
    }

    void removeAlias(FacesContext context) {
        _context = context;
        removeAlias();
    }

    private void removeAlias() {
        log.debug("removeAlias: " + _valueExpression + " != " + _aliasBeanExpression);

        ValueBinding aliasVB = getValueBinding("alias");
        aliasVB.setValue(_context, null);
    }

    private static class FacesEventWrapper extends FacesEvent {
        private FacesEvent _wrappedFacesEvent;

        public FacesEventWrapper(FacesEvent facesEvent, AliasBean redirectComponent) {
            super(redirectComponent);
            _wrappedFacesEvent = facesEvent;
        }

        public PhaseId getPhaseId() {
            return _wrappedFacesEvent.getPhaseId();
        }

        public void setPhaseId(PhaseId phaseId) {
            _wrappedFacesEvent.setPhaseId(phaseId);
        }

        public void queue() {
            _wrappedFacesEvent.queue();
        }

        public String toString() {
            return _wrappedFacesEvent.toString();
        }

        public boolean isAppropriateListener(FacesListener faceslistener) {
            return _wrappedFacesEvent.isAppropriateListener(faceslistener);
        }

        public void processListener(FacesListener faceslistener) {
            _wrappedFacesEvent.processListener(faceslistener);
        }

        public FacesEvent getWrappedFacesEvent() {
            return _wrappedFacesEvent;
        }
    }
}