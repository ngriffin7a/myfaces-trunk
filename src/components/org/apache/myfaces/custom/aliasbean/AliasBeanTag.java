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

import javax.faces.component.UIComponent;

import org.apache.myfaces.taglib.UIComponentTagBase;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.5  2005/02/18 17:19:30  matzew
 * added release() to tag clazzes.
 *
 * Revision 1.4  2005/01/27 01:59:45  svieujot
 * AliasBean : Change sourceBean attribute for value.
 * Make it work with both beans references ( #{myBean} ), and fix strings as value.
 * Document tld.
 *
 * Revision 1.3  2004/11/23 11:03:35  svieujot
 * Get ride of the x:aliasBean "permanent" attribute.
 *
 * Revision 1.2  2004/11/23 04:46:40  svieujot
 * Add an ugly "permanent" tag to x:aliasBean to handle children events.
 *
 * Revision 1.1  2004/11/08 20:43:15  svieujot
 * Add an x:aliasBean component
 *
 */
public class AliasBeanTag extends UIComponentTagBase {

    private String _alias;
    private String _valueExpression;
    
    public void release() {
        super.release();

        _alias=null;
        _valueExpression=null;

    }
    
    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        setStringProperty(component, "alias", _alias);
        setStringProperty(component, "value", _valueExpression);
    }
    
    public String getComponentType() {
        return AliasBean.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }
    
    public void setAlias(String alias){
        _alias = alias;
    }
    
    public void setValue(String valueExpression){
        _valueExpression = valueExpression;
    }
}