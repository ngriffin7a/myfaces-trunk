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
package net.sourceforge.myfaces.taglib.core;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.taglib.MyfacesComponentTag;

import javax.faces.component.UIComponent;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SelectItemTag
        extends MyfacesComponentTag
{
    //private static final Log log = LogFactory.getLog(SelectItemTag.class);

    public String getComponentType()
    {
        return "SelectItem";
    }

    protected String getDefaultRendererType()
    {
        return null;
    }

    // UISelectItem attributes
    private String _disabled;
    private String _itemDescription;
    private String _itemLabel;
    private String _itemValue;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setBooleanProperty(component, JSFAttr.DISABLED_ATTR, _disabled);
        setStringProperty(component, JSFAttr.ITEM_DESCRIPTION_ATTR, _itemDescription);
        setStringProperty(component, JSFAttr.ITEM_LABEL_ATTR, _itemLabel);
        setStringProperty(component, JSFAttr.ITEM_VALUE_ATTR, _itemValue);

        if (_itemValue == null &&
            component.getValueBinding("binding") == null &&
            component.getValueBinding("value") == null)
        {
            throw new IllegalArgumentException("SelectItem with no value");
        }
    }

    public void setDisabled(String disabled)
    {
        _disabled = disabled;
    }

    public void setItemDescription(String itemDescription)
    {
        _itemDescription = itemDescription;
    }

    public void setItemLabel(String itemLabel)
    {
        _itemLabel = itemLabel;
    }

    public void setItemValue(String itemValue)
    {
        _itemValue = itemValue;
    }

}
