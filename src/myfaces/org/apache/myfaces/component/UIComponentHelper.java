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
package net.sourceforge.myfaces.component;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class UIComponentHelper
    implements CommonComponentAttributes
{
    private UIComponent _uiComponent;

    public UIComponentHelper(UIComponent uiComponent)
    {
        _uiComponent = uiComponent;
    }

    public String getClientId(FacesContext context)
    {
        return UIComponentUtils.getClientId(context, _uiComponent);
    }

    public void addFacet(String facetName, UIComponent facet)
    {
        NamingContainer namingContainer;
        if (_uiComponent instanceof NamingContainer)
        {
            namingContainer = (NamingContainer)_uiComponent;
        }
        else
        {
            namingContainer = UIComponentUtils.findNamingContainer(_uiComponent);
        }
        if (facet.getComponentId() == null)
        {
            facet.setComponentId(namingContainer.generateClientId());
        }
        namingContainer.addComponentToNamespace(facet);
    }

    public UIComponent getParent(UIComponent superGetParent)
    {
        UIComponent parent = superGetParent;
        return parent != null
                ? parent
                : (UIComponent)_uiComponent.getAttribute(FACET_PARENT_ATTR);
    }

}
