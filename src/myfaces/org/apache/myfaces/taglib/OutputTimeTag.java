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

import net.sourceforge.myfaces.component.UIOutput;
import net.sourceforge.myfaces.renderkit.html.TimeRenderer;

import javax.faces.component.UIComponent;


/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class OutputTimeTag
        extends MyFacesTag
{
    //MyFaces tag extensions:
    public UIComponent createComponent()
    {
        UIComponent uiComponent = new UIOutput();
        uiComponent.setConverter("TimeConverter");
        return uiComponent;
    }

    public String getRendererType()
    {
        return TimeRenderer.TYPE;
    }

    public void setOutputClass(String value)
    {
        setRendererAttribute(TimeRenderer.OUTPUT_CLASS_ATTR, value);
    }

    public void setTimeStyle(String value)
    {
        setRendererAttribute(TimeRenderer.TIME_STYLE_ATTR, value);
    }

    public void setTimezone(String value)
    {
        setRendererAttribute(TimeRenderer.TIMEZONE_ATTR, value);
    }
}
