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
package net.sourceforge.myfaces.taglib.ext;

import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.ext.NavigationSeparatorRenderer;
import net.sourceforge.myfaces.taglib.MyFacesTag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

/**
 * see "navigation_item" tag in myfaces_ext.tld
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class NavigationSeparatorTag
    extends MyFacesTag
    implements CommonRendererAttributes

{
    public String getComponentType()
    {
        return "Output";
    }

    public String getRendererType()
    {
        return NavigationSeparatorRenderer.TYPE;
    }

    public void overrideProperties(UIComponent uiComponent)
    {
        super.overrideProperties(uiComponent);
        if (((UIOutput)uiComponent).getValue() == null)
        {
            ((UIOutput)uiComponent).setValue(" ");
        }
    }

    // user role attributes --> already implemented in MyFacesTag

    // UIComponent attributes --> already implemented in MyFacesTag

    // UIOutput attributes

    public void setValue(Object value)
    {
        super.setValue(value);
    }

    public void setOutputClass(String value)
    {
        setRendererAttributeString(OUTPUT_CLASS_ATTR, value);
    }

    // HTML universal attributes --> already implemented in MyFacesTag

    // HTML event handler attributes --> already implemented in MyFacesTag

    // user role attributes --> already implemented in MyFacesTag

}
