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
package net.sourceforge.myfaces.taglib;

import net.sourceforge.myfaces.component.UIInput;
import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.renderkit.html.HiddenRenderer;
import net.sourceforge.myfaces.renderkit.attr.*;

import javax.faces.component.UIComponent;

/**
 * see "input_hidden" tag in myfaces_html.tld
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class InputHiddenTag
    extends MyFacesTag
    implements CommonComponentAttributes,
               CommonRendererAttributes,
               UserRoleAttributes,
               HiddenRendererAttributes
{
    public UIComponent createComponent()
    {
        return new UIInput();
    }

    public String getRendererType()
    {
        return HiddenRenderer.TYPE;
    }

    // UIComponent attributes --> already implemented in MyFacesTag

    // UIInput attributes

    public void setValue(Object value)
    {
        super.setValue(value);
    }

    public void setInputClass(String v)
    {
        setRendererAttribute(INPUT_CLASS_ATTR, v);
    }

    // converter attribute --> already implemented in MyFacesTag

    // user role attributes --> already implemented in MyFacesTag

}
