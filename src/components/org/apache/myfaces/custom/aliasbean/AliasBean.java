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
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The aliasBean tag allows you to link a fictive bean to a real bean.
 * 
 * Let's suppose you have a subform you use often but with different beans.<br/>
 * The aliasBean allows you to design the subform with a fictive bean
 * and to include it in all the pages where you use it.
 * You just need to make an alias to the real bean named after the fictive bean before invoking the fictive bean.<br/>
 * This making it possible to have a library of reusable generic subforms.
 * 
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/11/14 15:06:36  svieujot
 * Improve AliasBean to make the alias effective only within the tag body
 *
 * Revision 1.1  2004/11/08 20:43:15  svieujot
 * Add an x:aliasBean component
 *
 */
public class AliasBean extends UIInput {
    private static final Log log = LogFactory.getLog(AliasBean.class);

    public static final String COMPONENT_TYPE = "org.apache.myfaces.AliasBean";

    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.AliasBean";

    private String _sourceBeanExpression = null;

    private String _aliasBeanExpression = null;

    public AliasBean() {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object saveState(FacesContext context) {
        log.debug("saveState");
        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = _sourceBeanExpression;
        values[2] = _aliasBeanExpression;
        return values;
    }

    public Object processSaveState(FacesContext context) {
        // This is the same code as the one from UIComponentBase
        // but it's inlined here to be sure that it matches
        // with the code of processRestoreState.
        // This ensures the component is portable to the JSF RI.

        if (context == null)
            throw new NullPointerException("context");
        if (isTransient())
            return null;
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

    public void restoreState(FacesContext context, Object state) {
        log.debug("restoreState");
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        _sourceBeanExpression = (String) values[1];
        _aliasBeanExpression = (String) values[2];
    }

    public void processValidators(FacesContext context) {
        log.debug("processValidators");
        makeAlias(context);
        super.processValidators(context);
        removeAlias(context);
    }

    public void processUpdates(FacesContext context) {
        log.debug("processUpdates");
        makeAlias(context);
        super.processUpdates(context);
        removeAlias(context);
    }

    void makeAlias(FacesContext context) {
        ValueBinding sourceBeanVB;
        if (_sourceBeanExpression == null) {
            sourceBeanVB = getValueBinding("sourceBean");
            _sourceBeanExpression = sourceBeanVB.getExpressionString();
        } else {
            sourceBeanVB = context.getApplication().createValueBinding(_sourceBeanExpression);
        }

        Object _sourceBean = sourceBeanVB.getValue(context);

        ValueBinding aliasVB;
        if (_aliasBeanExpression == null) {
            aliasVB = getValueBinding("alias");
            _aliasBeanExpression = aliasVB.getExpressionString();
        } else {
            aliasVB = context.getApplication().createValueBinding(_aliasBeanExpression);
        }

        aliasVB.setValue(context, _sourceBean);

        log.debug("makeAlias: " + _sourceBeanExpression + " = " + _aliasBeanExpression);
    }

    void removeAlias(FacesContext context) {
        log.debug("removeAlias: " + _sourceBeanExpression + " != " + _aliasBeanExpression);

        ValueBinding aliasVB = getValueBinding("alias");
        aliasVB.setValue(context, null);
    }
}