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

/**
 * see Javadoc of JSF Specification
 * 
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UIGraphic
        extends UIComponentBase
{
    private static final String URL_PROPERTY = "url";
    private static final String VALUE_PROPERTY = "value";

    public String getUrl()
    {
        return (String)getValue();
    }

    public void setUrl(String url)
    {
        setValue(url);
    }

    public ValueBinding getValueBinding(String name)
    {
        if (URL_PROPERTY.equals(name))
        {
            return super.getValueBinding(VALUE_PROPERTY);
        }
        else
        {
            return super.getValueBinding(name);
        }
    }

    public void setValueBinding(String name,
                                ValueBinding binding)
    {
        if (URL_PROPERTY.equals(name))
        {
            super.setValueBinding(VALUE_PROPERTY, binding);
        }
        else
        {
            super.setValueBinding(name, binding);
        }
    }


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "javax.faces.Graphic";
    public static final String COMPONENT_FAMILY = "javax.faces.Graphic";
    private static final String DEFAULT_RENDERER_TYPE = "javax.faces.Image";

    private Object _value = null;

    public UIGraphic()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setValue(Object value)
    {
        _value = value;
    }

    public Object getValue()
    {
        if (_value != null) return _value;
        ValueBinding vb = getValueBinding("value");
        return vb != null ? (Object)vb.getValue(getFacesContext()) : null;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _value;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _value = (Object)values[1];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}
