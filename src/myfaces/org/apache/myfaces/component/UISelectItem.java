/**
 * MyFaces - the free JSF implementation
 * Copyright (C) 2003  The MyFaces Team (http://myfaces.sourceforge.net)
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
package net.sourceforge.myfaces.component;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UISelectItem
    extends javax.faces.component.UISelectItem
    implements CommonComponentAttributes
{
    public static final String ITEM_DESCRIPTION_ATTR = "itemDescription";
    public static final String ITEM_LABEL_ATTR = "itemLabel";

    //MyFaces eextension
    public static final String SELECTED_ATTR = "selected";
    public static final String ITEM_KEY_ATTR = "itemKey";
    public static final String ITEM_BUNDLE_ATTR = "itemBundle";

    public UISelectItem()
    {
        setValid(true);
    }

    public boolean getRendersSelf()
    {
        return false;
    }

    public String getItemDescription()
    {
        return (String)getAttribute(ITEM_DESCRIPTION_ATTR);
    }

    public void setItemDescription(String itemDescription)
    {
        setAttribute(ITEM_DESCRIPTION_ATTR, itemDescription);
    }

    public String getItemLabel()
    {
        return (String)getAttribute(ITEM_LABEL_ATTR);
    }

    public void setItemLabel(String itemLabel)
    {
        setAttribute(ITEM_LABEL_ATTR, itemLabel);
    }

    public String getItemValue()
    {
        return (String)getAttribute(VALUE_ATTR);
    }

    public void setItemValue(String itemValue)
    {
        setAttribute(VALUE_ATTR, itemValue);
    }

    public String getItemKey()
    {
        return (String)getAttribute(ITEM_KEY_ATTR);
    }

    public void setItemKey(String itemValue)
    {
        setAttribute(ITEM_KEY_ATTR, itemValue);
    }

    public String getItemBundle()
    {
        return (String)getAttribute(ITEM_BUNDLE_ATTR);
    }

    public void setItemBundle(String itemValue)
    {
        setAttribute(ITEM_BUNDLE_ATTR, itemValue);
    }

}
