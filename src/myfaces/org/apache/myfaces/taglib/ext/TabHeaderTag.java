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

import net.sourceforge.myfaces.renderkit.attr.ButtonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.ButtonRenderer;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLAnchorAttributes;
import net.sourceforge.myfaces.taglib.MyFacesBodyTag;
import net.sourceforge.myfaces.component.MyFacesUICommand;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class TabHeaderTag
    extends MyFacesBodyTag
    implements CommonRendererAttributes,
               HTMLAnchorAttributes,
    ButtonRendererAttributes

{
    public String getComponentType()
    {
        return "TabHeader";
    }

    public String getRendererType()
    {
        return ButtonRenderer.TYPE;
    }

    // user role attributes --> already implemented in MyFacesTag

    // UIComponent attributes --> already implemented in MyFacesTag

    // UICommand attributes

    public void setCommandName(String v)
    {
        setComponentPropertyString(MyFacesUICommand.COMMAND_NAME_PROP, v);
    }

    public void setCommandClass(String v)
    {
        setRendererAttributeString(COMMAND_CLASS_ATTR, v);
    }

    public void setAction(String v)
    {
        setComponentPropertyString(MyFacesUICommand.ACTION_PROP, v);
    }

    public void setActionRef(String v)
    {
        setComponentPropertyString(MyFacesUICommand.ACTION_REF_PROP, v);
    }



    // HTML universal attributes --> already implemented in MyFacesTag

    // HTML event handler attributes --> already implemented in MyFacesTag



    // key and bundle attributes --> already implemented in MyFacesTag

    // user role attributes --> already implemented in MyFacesTag


    // Button Renderer attributes

    public void setLabel(String v)
    {
        setRendererAttributeString(LABEL_ATTR, v);
    }


}
