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
package net.sourceforge.myfaces.taglib.html;

import net.sourceforge.myfaces.renderkit.JSFAttr;

import javax.faces.component.UIComponent;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlOutputMessageTag
        extends HtmlComponentTag
{
    //private static final Log log = LogFactory.getLog(HtmlOutputMessageTag.class);

    public String getComponentType()
    {
        return "OutputMessage";
    }

    public String getDefaultRendererType()
    {
        return "Message";
    }

    // UIComponent attributes --> already implemented in MyfacesComponentTag

    // user role attributes --> already implemented in MyfacesComponentTag

    // HTML universal attributes --> already implemented in HtmlComponentTag

    // HTML event handler attributes --> already implemented in HtmlComponentTag

    // UIOutput attributes
    // value and converterId --> already implemented in MyfacesComponentTag

    // HtmlOutputMessage attributes
    private String _escape;

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setBooleanProperty(component, JSFAttr.ESCAPE_ATTR, _escape);
    }

    public void setEscape(String escape)
    {
        _escape = escape;
    }
}
