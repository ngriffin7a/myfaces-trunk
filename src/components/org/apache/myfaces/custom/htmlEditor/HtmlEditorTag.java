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
package org.apache.myfaces.custom.htmlEditor;

import javax.faces.component.UIComponent;

import org.apache.myfaces.taglib.UIComponentTagBase;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/12/02 22:28:30  svieujot
 * Add an x:htmlEditor based on the Kupu library.
 *
 *
 */
public class HtmlEditorTag extends UIComponentTagBase {
    
    private String _value;
    
    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        setStringProperty(component, "value", _value);
    }
    
    public String getComponentType() {
        return HtmlEditor.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.apache.myfaces.HtmlEditor";
    }
    
    public void setValue(String value){
        _value = value;
    }
}