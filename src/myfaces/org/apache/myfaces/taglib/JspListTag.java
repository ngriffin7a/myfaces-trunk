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

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.UIPanel;
import net.sourceforge.myfaces.renderkit.html.JspListRenderer;

import javax.faces.component.UIComponent;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by Author)
 * @version $Revision$ $Date$
 */
public class JspListTag
    extends MyFacesTag
{
    public UIComponent createComponent()
    {
        UIPanel panel = new UIPanel()
        {
            public boolean getRendersChildren()
            {
                return false;
            }
        };
        // donot save State and set JspListRenderer
        UIComponentUtils.setTransient(panel, true);
        return panel;
    }

    public String getRendererType()
    {
        return JspListRenderer.TYPE;
    }

    public void setStyle(String value)
    {
        setRendererAttribute(JspListRenderer.PANEL_CLASS_ATTR, value);
    }

    public void setColumnClasses(String value)
    {
        setRendererAttribute(JspListRenderer.COLUMN_CLASSES_ATTR, value);
    }

    public void setRowClasses(String value)
    {
        setRendererAttribute(JspListRenderer.ROW_CLASSES_ATTR, value);
    }

    public void setFooterClass(String value)
    {
        setRendererAttribute(JspListRenderer.FOOTER_CLASS_ATTR, value);
    }

    public void setHeaderClass(String value)
    {
        setRendererAttribute(JspListRenderer.HEADER_CLASS_ATTR, value);
    }

}
