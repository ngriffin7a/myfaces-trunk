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
package net.sourceforge.myfaces.component.html.ext;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/03/30 17:47:32  manolito
 * Message and Messages refactored
 *
 * Revision 1.1  2004/03/30 13:27:04  manolito
 * extended Message component
 *
 */
public class HtmlMessage
    extends javax.faces.component.html.HtmlMessage
{
    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlMessage";
    public static final String COMPONENT_FAMILY = "javax.faces.Message";
    private static final String DEFAULT_RENDERER_TYPE = "javax.faces.Message";
    private static final String DEFAULT_SUMMARYDETAILSEPARATOR = ": ";

    private String _summaryDetailSeparator = null;

    public HtmlMessage()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setSummaryDetailSeparator(String summaryDetailSeparator)
    {
        _summaryDetailSeparator = summaryDetailSeparator;
    }

    public String getSummaryDetailSeparator()
    {
        if (_summaryDetailSeparator != null) return _summaryDetailSeparator;
        ValueBinding vb = getValueBinding("summaryDetailSeparator");
        return vb != null ? (String)vb.getValue(getFacesContext()) : DEFAULT_SUMMARYDETAILSEPARATOR;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = _summaryDetailSeparator;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _summaryDetailSeparator = (String)values[1];
    }
    //------------------ GENERATED CODE END ---------------------------------------
}
