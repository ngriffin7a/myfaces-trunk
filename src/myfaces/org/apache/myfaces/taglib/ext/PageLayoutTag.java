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

import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.UIPanel;
import net.sourceforge.myfaces.renderkit.html.ext.LayoutRenderer;
import net.sourceforge.myfaces.taglib.MyFacesBodyTag;

import javax.faces.component.UIComponent;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class PageLayoutTag
        extends MyFacesBodyTag
{
    public UIComponent createComponent()
    {
        UIComponent comp = new UIPanel(false);
        UIComponentUtils.setTransient(comp, true);
        comp.setValid(true);
        return comp;
    }

    public String getRendererType()
    {
        return LayoutRenderer.TYPE;
    }

    public void setLayout(String value)
    {
        setComponentProperty(CommonComponentAttributes.VALUE_ATTR, value);
    }

    public void setLayoutReference(String value)
    {
        setComponentProperty(CommonComponentAttributes.MODEL_REFERENCE_ATTR, value);
    }

    public void setCssClass(String value)
    {
        setRendererAttribute(LayoutRenderer.PANEL_CLASS_ATTR, value);
    }

}
