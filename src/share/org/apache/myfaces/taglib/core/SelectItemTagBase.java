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
package org.apache.myfaces.taglib.core;

import org.apache.myfaces.taglib.UIComponentTagBase;
import org.apache.myfaces.renderkit.JSFAttr;

import javax.faces.component.UIComponent;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class SelectItemTagBase
    extends UIComponentTagBase
{
    //private static final Log log = LogFactory.getLog(SelectItemTag.class);

    public String getComponentType()
    {
        return "javax.faces.SelectItem";
    }

    public String getRendererType()
    {
        return null;
    }

    // UISelectItem attributes
    private String _itemDisabled;
    private String _itemDescription;
    private String _itemLabel;
    private String _itemValue;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setBooleanProperty(component, JSFAttr.ITEM_DISABLED_ATTR, _itemDisabled);
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

    public void setItemDisabled(String itemDisabled)
    {
        _itemDisabled = itemDisabled;
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
