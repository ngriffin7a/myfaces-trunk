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
package net.sourceforge.myfaces.custom.date;

import java.util.Date;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Sylvain Vieujot (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlInputDate extends UIInput {
    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlInputDate";
    public static final String COMPONENT_FAMILY = "javax.faces.Input";
    private static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.Date";
    
    /**
     * Same as for f:convertDateTime 
     * Specifies what contents the string value will be formatted to include, or parsed expecting.
     * Valid values are "date", "time", and "both". Default value is "date".
     */
    private String _type = null;
    
    public HtmlInputDate() {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    /**
     * Just to make things easier
     */
    public Date getDate() {
        return (Date) getValue();
    }
    
	public String getType() {
		if (_type != null) return _type;
		ValueBinding vb = getValueBinding("type");
		return vb != null ? (String)vb.getValue(getFacesContext()) : "date";

	}

	public void setType(String string) {
		_type = string;
	}
	
    public Object saveState(FacesContext context) {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _type;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _type = (String)values[1];
    }
}