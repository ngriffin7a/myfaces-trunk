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
public class UISelectItem
        extends UIComponentBase
{
    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "javax.faces.SelectItem";
    public static final String COMPONENT_FAMILY = "javax.faces.SelectItem";
    private static final boolean DEFAULT_ITEMDISABLED = false;

    private String _itemDescription = null;
    private Boolean _itemDisabled = null;
    private String _itemLabel = null;
    private Object _itemValue = null;
    private Object _value = null;

    public UISelectItem()
    {
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setItemDescription(String itemDescription)
    {
        _itemDescription = itemDescription;
    }

    public String getItemDescription()
    {
        if (_itemDescription != null) return _itemDescription;
        ValueBinding vb = getValueBinding("itemDescription");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setItemDisabled(boolean itemDisabled)
    {
        _itemDisabled = Boolean.valueOf(itemDisabled);
    }

    public boolean isItemDisabled()
    {
        if (_itemDisabled != null) return _itemDisabled.booleanValue();
        ValueBinding vb = getValueBinding("itemDisabled");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : DEFAULT_ITEMDISABLED;
    }

    public void setItemLabel(String itemLabel)
    {
        _itemLabel = itemLabel;
    }

    public String getItemLabel()
    {
        if (_itemLabel != null) return _itemLabel;
        ValueBinding vb = getValueBinding("itemLabel");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setItemValue(Object itemValue)
    {
        _itemValue = itemValue;
    }

    public Object getItemValue()
    {
        if (_itemValue != null) return _itemValue;
        ValueBinding vb = getValueBinding("itemValue");
        return vb != null ? (Object)vb.getValue(getFacesContext()) : null;
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
        Object values[] = new Object[5];
        values[0] = super.saveState(context);
        values[1] = _itemDescription;
        values[2] = _itemDisabled;
        values[3] = _itemLabel;
        values[4] = _itemValue;
        values[5] = _value;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _itemDescription = (String)values[1];
        _itemDisabled = (Boolean)values[2];
        _itemLabel = (String)values[3];
        _itemValue = (Object)values[4];
        _value = (Object)values[5];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}
