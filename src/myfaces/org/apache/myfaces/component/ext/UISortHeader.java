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
package net.sourceforge.myfaces.component.ext;

import net.sourceforge.myfaces.component.MyFacesUIPanel;
import net.sourceforge.myfaces.renderkit.html.ext.SortColumnRenderer;

import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UISortHeader
    extends MyFacesUIPanel
    implements ActionListener
{
    public static final String COLUMN_PROP = "column";
    public static final String COLUMN_REF_PROP = "columnRef";
    public static final String ASCENDING_PROP = "ascending";
    public static final String ASCENDING_REF_PROP = "ascendingRef";

    private Boolean _ascending;
    private String _ascendingRef;

    public String getColumn()
    {
        return (String)getValue();
    }

    public void setColumn(String column)
    {
        setValue(column);
    }

    public String getColumnRef()
    {
        return getValueRef();
    }

    public void setColumnRef(String columnRef)
    {
        setValueRef(columnRef);
    }

    public String currentColumn(FacesContext facesContext)
    {
        return (String)currentValue(facesContext);
    }

    public Boolean getAscending()
    {
        return _ascending;
    }

    public void setAscending(Boolean ascending)
    {
        _ascending = ascending;
    }

    public String getAscendingRef()
    {
        return _ascendingRef;
    }

    public void setAscendingRef(String ascendingRef)
    {
        _ascendingRef = ascendingRef;
    }

    public Boolean currentAscending(FacesContext facesContext)
    {
        Boolean asc = getAscending();
        if (asc != null)
        {
            return asc;
        }

        String ascRef = getAscendingRef();
        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        asc = (Boolean)af.getApplication().getValueBinding(ascRef).getValue(facesContext);
        return asc;
    }


    public void updateModel(FacesContext facesContext)
    {
        ApplicationFactory af = (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);

        String column = getColumn();
        if (column != null)
        {
            String columnRef = getColumnRef();
            if (columnRef != null)
            {
                af.getApplication().getValueBinding(columnRef).setValue(facesContext, column);
            }
        }

        Boolean asc = getAscending();
        if (asc != null)
        {
            String ascRef = getAscendingRef();
            if (ascRef != null)
            {
                af.getApplication().getValueBinding(ascRef).setValue(facesContext, asc);
            }
        }
    }



    public PhaseId getPhaseId()
    {
        return PhaseId.APPLY_REQUEST_VALUES;
    }

    public void processAction(ActionEvent actionevent)
        throws AbortProcessingException
    {
        UIComponent source = actionevent.getComponent();
        if (source instanceof UISortColumn)
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String sortColumn = ((UISortColumn)source).getColumn();
            String currentColumn = currentColumn(facesContext);
            if (sortColumn.equals(currentColumn))
            {
                boolean currAsc = currentAscending(facesContext).booleanValue();
                setAscending(Boolean.valueOf(!currAsc));
            }
            else
            {
                setColumn(sortColumn);

                Boolean defaultAscending = (Boolean)source.getAttribute(SortColumnRenderer.DEFAULT_ASCENDING_ATTR);
                if (defaultAscending != null)
                {
                    setAscending(defaultAscending);
                }
                else
                {
                    setAscending(Boolean.TRUE);
                }
            }
        }
    }
}
