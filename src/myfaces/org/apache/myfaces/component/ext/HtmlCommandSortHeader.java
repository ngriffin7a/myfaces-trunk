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

import net.sourceforge.myfaces.component.html.MyFacesHtmlCommandLink;
import net.sourceforge.myfaces.component.html.MyFacesHtmlDataTable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class HtmlCommandSortHeader
        extends MyFacesHtmlCommandLink
{
    private static final Log log = LogFactory.getLog(HtmlCommandSortHeader.class);

    public boolean isImmediate()
    {
        return true;
    }

    public void broadcast(FacesEvent event) throws AbortProcessingException
    {
        super.broadcast(event);

        if (event instanceof ActionEvent &&
            event.getPhaseId().equals(PhaseId.APPLY_REQUEST_VALUES))
        {
            MyFacesHtmlDataTable dataTable = findParentDataTable();
            if (dataTable == null)
            {
                log.error("CommandSortHeader has no MyFacesHtmlDataTable parent");
            }
            else
            {
                String colName = getColumnName();
                String currentSortColumn = dataTable.getSortColumn();
                boolean currentAscending = dataTable.isSortAscending();
                if (colName.equals(currentSortColumn))
                {
                    dataTable.setSortAscending(!currentAscending);
                }
                else
                {
                    dataTable.setSortColumn(getColumnName());
                    dataTable.setSortAscending(true);
                }
            }
        }
    }


    public MyFacesHtmlDataTable findParentDataTable()
    {
        UIComponent parent = getParent();
        while (parent != null)
        {
            if (parent instanceof MyFacesHtmlDataTable)
            {
                return (MyFacesHtmlDataTable)parent;
            }
            parent = parent.getParent();
        }
        return null;
    }


    public Object saveState(FacesContext context)
    {
        Object values[] = new Object[3];
        values[0] = super.saveState(context);
        values[1] = _columnName;
        values[2] = _arrow;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state)
    {
        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        _columnName = (String)values[1];
        _arrow      = (Boolean)values[2];
    }

    //------------------ GENERATED CODE BEGIN (do not modify!) --------------------

    public static final String COMPONENT_TYPE = "net.sourceforge.myfaces.HtmlCommandSortHeader";
    public static final String COMPONENT_FAMILY = "javax.faces.Command";
    private static final String DEFAULT_RENDERER_TYPE = "javax.faces.Link";

    private String _columnName = null;
    private Boolean _arrow = null;

    public HtmlCommandSortHeader()
    {
        setRendererType(DEFAULT_RENDERER_TYPE);
    }

    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }

    public void setColumnName(String columnName)
    {
        _columnName = columnName;
    }

    public String getColumnName()
    {
        if (_columnName != null) return _columnName;
        ValueBinding vb = getValueBinding("columnName");
        return vb != null ? (String)vb.getValue(getFacesContext()) : null;
    }

    public void setArrow(boolean arrow)
    {
        _arrow = Boolean.valueOf(arrow);
    }

    public boolean isArrow()
    {
        if (_arrow != null) return _arrow.booleanValue();
        ValueBinding vb = getValueBinding("arrow");
        Boolean v = vb != null ? (Boolean)vb.getValue(getFacesContext()) : null;
        return v != null ? v.booleanValue() : false;
    }


    //------------------ GENERATED CODE END ---------------------------------------
}
