/*
 * Copyright 2005 The Apache Software Foundation.
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
package org.apache.myfaces.custom.tree2;


import org.apache.myfaces.taglib.UIComponentTagBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;


/**
 * @author <a href="mailto:oliver@rossmueller.com">Oliver Rossmueller </a>
 * @author Sean Schofield
 * @version $Revision$ $Date$
 */
public class TreeTag extends UIComponentTagBase //UIComponentBodyTagBase
{

    private String _value;
    private String _var;
    private String _varNodeToggler;


    public String getComponentType()
    {
        return "org.apache.myfaces.HtmlTree2";
    }


    public String getRendererType()
    {
        return "org.apache.myfaces.HtmlTree2";
    }


    public void setValue(String value)
    {
        _value = value;
    }


    /**
     * @param var The var to set.
     */
    public void setVar(String var)
    {
        _var = var;
    }


    public void setVarNodeToggler(String varNodeToggler)
    {
        _varNodeToggler = varNodeToggler;
    }


    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        FacesContext context = getFacesContext();
        if (_value != null)
        {
            ValueBinding vb = context.getApplication().createValueBinding(_value);
            component.setValueBinding("value", vb);
        }

        if (_var != null)
        {
            ((HtmlTree) component).setVar(_var);
        }

        if (_varNodeToggler != null)
        {
            ((HtmlTree) component).setVarNodeToggler(_varNodeToggler);
        }
    }
}
