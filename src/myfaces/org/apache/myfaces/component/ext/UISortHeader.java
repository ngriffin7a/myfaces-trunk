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

import net.sourceforge.myfaces.component.UIComponentUtils;
import net.sourceforge.myfaces.component.UIPanel;
import net.sourceforge.myfaces.renderkit.html.ext.SortColumnRenderer;

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
    extends UIPanel
    implements ActionListener
{
    public static final String COLUMN_ATTR = "column";
    public static final String COLUMN_REFERENCE_ATTR = "columnReference";
    public static final String ASCENDING_ATTR = "ascending";
    public static final String ASCENDING_REFERENCE_ATTR = "ascendingReference";

    public UISortHeader()
    {
        super(false);
        UIComponentUtils.setTransient(this, false); //Always remember current sort column
        setValid(true); //Value is always valid (necessary for updateModel)
    }

    public boolean isAscending()
    {
        return UIComponentUtils.getBooleanAttribute(this, ASCENDING_ATTR, false);
    }

    public void setAscending(boolean ascending)
    {
        UIComponentUtils.setBooleanAttribute(this, ASCENDING_ATTR, ascending);
    }

    public boolean currentAscending(FacesContext facesContext)
    {
        Boolean asc = (Boolean)getAttribute(ASCENDING_ATTR);
        if (asc != null)
        {
            return asc.booleanValue();
        }

        String ascRef = getAscendingReference();
        asc = (Boolean)facesContext.getModelValue(ascRef);
        return asc.booleanValue();
    }


    public String getAscendingReference()
    {
        return (String)getAttribute(ASCENDING_REFERENCE_ATTR);
    }

    public void setAscendingReference(String ascendingReference)
    {
        setAttribute(ASCENDING_REFERENCE_ATTR, ascendingReference);
    }


    public void updateModel(FacesContext facesContext)
    {
        super.updateModel(facesContext);
        Boolean asc = (Boolean)getAttribute(ASCENDING_ATTR);
        if (asc != null)
        {
            String ascRef = getAscendingReference();
            facesContext.setModelValue(ascRef, asc);
            //we do not set it null, so that state saver is able to save the ascending state
            //setAttribute(ASCENDING_ATTR, null);
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
        if (source.getRendererType().equals(SortColumnRenderer.TYPE))
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String sortColumn = (String)source.currentValue(facesContext);
            String currentColumn = (String)currentValue(facesContext);
            if (sortColumn.equals(currentColumn))
            {
                setAscending(!currentAscending(facesContext));
            }
            else
            {
                setValue(sortColumn);
                setValid(true);

                Boolean defaultAscending = (Boolean)source.getAttribute(SortColumnRenderer.DEFAULT_ASCENDING_ATTR);
                if (defaultAscending != null)
                {
                    setAscending(defaultAscending.booleanValue());
                }
                else
                {
                    setAscending(true);
                }
            }
        }
    }

}
