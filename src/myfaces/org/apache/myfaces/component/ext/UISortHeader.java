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
import net.sourceforge.myfaces.component.UIComponentHelper;
import net.sourceforge.myfaces.renderkit.html.ext.SortColumnRenderer;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
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
    extends UIComponentBase
    implements ActionListener
{
    public static final String ASCENDING_ATTR = "ascending";
    public static final String ASCENDING_REFERENCE_ATTR = "ascendingReference";

    public String getComponentType()
    {
        return UIPanel.TYPE;
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
        String column = (String)getValue();
        if (column != null)
        {
            String modelRef = getModelReference();
            facesContext.setModelValue(modelRef, column);
        }

        Boolean asc = (Boolean)getAttribute(ASCENDING_ATTR);
        if (asc != null)
        {
            String ascRef = getAscendingReference();
            facesContext.setModelValue(ascRef, asc);
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



//------------------------------------------------------------------------------
// UIComponentHelper Delegation
// HACK: Delegation, because UIComponentBase does not support Facets properly.
//       (getClientId crashes, etc.)

    private UIComponentHelper _uiComponentHelper = new UIComponentHelper(this);

    public String getClientId(FacesContext context)
    {
        return _uiComponentHelper.getClientId(context);
    }

    public void addFacet(String facetName, UIComponent facet)
    {
        super.addFacet(facetName, facet);
        _uiComponentHelper.addFacet(facetName, facet);
    }

    public UIComponent getParent()
    {
        return _uiComponentHelper.getParent(super.getParent());
    }
//------------------------------------------------------------------------------
}
