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
package net.sourceforge.myfaces.component;

import net.sourceforge.myfaces.convert.ConverterUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MyFacesUISelectItem
    extends javax.faces.component.UISelectItem
{
    public static final String ITEM_DESCRIPTION_PROP = "itemDescription";
    public static final String ITEM_LABEL_PROP = "itemLabel";

    //MyFaces extension
    public static final String ITEM_KEY_PROP = "itemKey";
    public static final String ITEM_BUNDLE_PROP = "itemBundle";

    private String _itemKey;
    private String _itemBundle;

    public MyFacesUISelectItem()
    {
        //FIXME
        //setValid(true);
    }

    public boolean getRendersSelf()
    {
        return false;
    }

    /**
     * MyFaces extension: getItemValue returns the currentValue of this
     * component as a String.
     */
    public String getItemValue()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        //FIXME
        //Object v = currentValue(facesContext);
        //return ConverterUtils.getComponentValueAsString(facesContext, this, v);
        return null;
    }

    public void setItemValue(String v)
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        //FIXME
        /*
        setValue(ConverterUtils.getComponentValueAsObject(facesContext,
                                                          this,
                                                          v));
                                                          */
    }


    public String getItemKey()
    {
        return _itemKey;
    }

    public void setItemKey(String itemKey)
    {
        _itemKey = itemKey;
    }

    public String getItemBundle()
    {
        return _itemBundle;
    }

    public void setItemBundle(String itemBundle)
    {
        _itemBundle = itemBundle;
    }



//------------------------------------------------------------------------------

    public String getClientId(FacesContext context)
    {
        return UIComponentUtils.getClientId(context, this);
    }

    public void addFacet(String facetName, UIComponent facet)
    {
        //FIXME
        //super.addFacet(facetName, facet);
        UIComponentUtils.ensureComponentInNamingContainer(facet);
    }

//------------------------------------------------------------------------------
}
