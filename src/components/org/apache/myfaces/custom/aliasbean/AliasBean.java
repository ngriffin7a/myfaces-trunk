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

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

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
 * Revision 1.1  2004/11/08 20:43:15  svieujot
 * Add an x:aliasBean component
 *
 */
public class AliasBean extends UIInput {
    public static final String COMPONENT_TYPE = "org.apache.myfaces.AliasBean";
    private static final String DEFAULT_RENDERER_TYPE = "org.apache.myfaces.AliasBean";
    
    private String _sourceBeanExpression = null;
    private String _aliasBeanExpression = null;
    
    private transient boolean _aliasMade = false;
    
    public AliasBean(){
        setRendererType(DEFAULT_RENDERER_TYPE);
    }
    
    public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = _sourceBeanExpression;
		values[2] = _aliasBeanExpression;
		return values;
    }

    public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[])state;
		super.restoreState(context, values[0]);
		_sourceBeanExpression = (String) values[1];
		_aliasBeanExpression = (String) values[2];

		makeAlias(context);
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    void makeAlias(FacesContext context){
		if( _aliasMade )
		    return;

		ValueBinding sourceBeanVB;
		if( _sourceBeanExpression == null ){
		    sourceBeanVB = getValueBinding("sourceBean");
            _sourceBeanExpression = sourceBeanVB.getExpressionString();
        }else{
            sourceBeanVB = context.getApplication().createValueBinding(_sourceBeanExpression);
        }

        Object _sourceBean = sourceBeanVB.getValue(context);

		ValueBinding aliasVB;
		if( _aliasBeanExpression == null ){
		    aliasVB = getValueBinding("alias");
		    _aliasBeanExpression = aliasVB.getExpressionString(); 
		}else{
		    aliasVB = context.getApplication().createValueBinding(_aliasBeanExpression);
		}
		
		aliasVB.setValue(context, _sourceBean);
            
        _aliasMade = true;
    }
}