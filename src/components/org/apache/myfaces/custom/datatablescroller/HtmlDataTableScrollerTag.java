/**
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
package net.sourceforge.myfaces.custom.datatablescroller;

import net.sourceforge.myfaces.taglib.html.HtmlComponentBodyTagBase;

import javax.faces.component.UIComponent;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/03/31 12:15:26  manolito
 * custom component refactoring
 *
 */
public class HtmlDataTableScrollerTag
    extends HtmlComponentBodyTagBase
{
    //private static final Log log = LogFactory.getLog(HtmlDataTableScrollerTag.class);

    private String _for;


    public String getComponentType()
    {
        return HtmlDataTableScroller.COMPONENT_TYPE;
    }


    protected String getDefaultRendererType()
    {
        return "net.sourceforge.myfaces.TableScroller";
    }

    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        setStringProperty(component, "for", _for);
    }

    public void setFor(String aFor)
    {
        _for = aFor;
    }

}
