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
package net.sourceforge.myfaces.component.ext;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlDataTableScroller
        extends UICommand
{
    //private static final Log log = LogFactory.getLog(HtmlPanelTabbedPane.class);

    private static final String FIRST_FACET_NAME    = "first";
    private static final String LAST_FACET_NAME     = "last";
    private static final String NEXT_FACET_NAME     = "next";
    private static final String PREVIOUS_FACET_NAME = "previous";

    public void setFirst(UIComponent header)
    {
        getFacets().put(FIRST_FACET_NAME, header);
    }

    public UIComponent getFirst()
    {
        return (UIComponent)getFacets().get(FIRST_FACET_NAME);
    }

    public void setLast(UIComponent header)
    {
        getFacets().put(LAST_FACET_NAME, header);
    }

    public UIComponent getLast()
    {
        return (UIComponent)getFacets().get(LAST_FACET_NAME);
    }

    public void setNext(UIComponent header)
    {
        getFacets().put(NEXT_FACET_NAME, header);
    }

    public UIComponent getNext()
    {
        return (UIComponent)getFacets().get(NEXT_FACET_NAME);
    }

    public void setPrevoius(UIComponent header)
    {
        getFacets().put(PREVIOUS_FACET_NAME, header);
    }

    public UIComponent getPrevious()
    {
        return (UIComponent)getFacets().get(PREVIOUS_FACET_NAME);
    }

    public boolean getRendersChildren()
    {
        return true;
    }


    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlDataTableScroller";
    public static final String COMPONENT_FAMILY = "javax.faces.Command";
    public static final String DEFAULT_RENDERER_TYPE = "net.sourceforge.myfaces.TableScroller";

    private String _for = null;

    public HtmlDataTableScroller()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setFor(String forValue)
    {
        _for = forValue;
    }

    public String getFor()
    {
        if (_for != null) return _for;
        ValueBinding vb = getValueBinding("for");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _for;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _for = (String)values[1];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}
