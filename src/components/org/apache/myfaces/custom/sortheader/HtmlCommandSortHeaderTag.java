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
package net.sourceforge.myfaces.custom.sortheader;


import net.sourceforge.myfaces.taglib.html.ext.HtmlCommandLinkTag;

import javax.faces.component.UIComponent;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/04/01 12:57:41  manolito
 * additional extended component classes for user role support
 *
 * Revision 1.1  2004/03/31 12:15:27  manolito
 * custom component refactoring
 *
 */
public class HtmlCommandSortHeaderTag
        extends HtmlCommandLinkTag
{
    //private static final Log log = LogFactory.getLog(HtmlCommandSortHeaderTag.class);

    public String getComponentType()
    {
        return HtmlCommandSortHeader.COMPONENT_TYPE;
    }

    protected String getDefaultRendererType()
    {
        return "net.sourceforge.myfaces.SortHeader";
    }

    private String _columnName;
    private String _arrow;

    // User Role support --> already handled by HtmlPanelGroupTag

    
    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, "columnName", _columnName);
        setBooleanProperty(component, "arrow", _arrow);
    }

    public void setColumnName(String columnName)
    {
        _columnName = columnName;
    }

    public void setArrow(String arrow)
    {
        _arrow = arrow;
    }
}
