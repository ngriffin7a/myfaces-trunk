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
package net.sourceforge.myfaces.renderkit.html.ext;

import net.sourceforge.myfaces.custom.checkbox.HtmlCheckbox;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HtmlCheckboxRendererBase;
import net.sourceforge.myfaces.renderkit.html.HtmlRendererUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;
import java.util.Set;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.1  2004/04/02 13:57:11  manolito
 * extended HtmlSelectManyCheckbox with layout "spread" and custom Checkbox component
 *
 */
public class HtmlCheckboxRenderer
        extends HtmlCheckboxRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlRadioRenderer.class);

    private static final String LAYOUT_SPREAD = "spread";

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        if (context == null) throw new NullPointerException("context");
        if (component == null) throw new NullPointerException("component");

        if (component instanceof HtmlCheckbox)
        {
            renderCheckbox(context, (HtmlCheckbox)component);
        }
        else if (component instanceof UISelectMany)
        {
            String layout = getLayout((UISelectMany)component);
            if (layout != null && layout.equals(LAYOUT_SPREAD))
            {
                return; //checkbox inputs are rendered by spread checkbox components
            }
            else
            {
                super.encodeEnd(context, component);
            }
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
        }
    }


    private void renderCheckbox(FacesContext facesContext, HtmlCheckbox checkbox) throws IOException
    {
        String forAttr = checkbox.getFor();
        if (forAttr == null)
        {
            throw new IllegalStateException("mandatory attribute 'for'");
        }
        int index = checkbox.getIndex();
        if (index < 0)
        {
            throw new IllegalStateException("positive index must be given");
        }

        UIComponent uiComponent = checkbox.findComponent(forAttr);
        if (uiComponent == null)
        {
            throw new IllegalStateException("Could not find component '" + forAttr + "'");
        }
        if (!(uiComponent instanceof UISelectMany))
        {
            throw new IllegalStateException("UISelectMany expected");
        }

        UISelectMany uiSelectMany = (UISelectMany)uiComponent;
        Converter converter;
        List selectItemList = RendererUtils.getSelectItemList(uiSelectMany);
        if (index >= selectItemList.size())
        {
            throw new IndexOutOfBoundsException("index " + index + " >= " + selectItemList.size());
        }

        try
        {
            converter = RendererUtils.findUISelectManyConverter(facesContext, uiSelectMany);
        }
        catch (FacesException e)
        {
            converter = null;
        }

        SelectItem selectItem = (SelectItem)selectItemList.get(index);
        Object itemValue = selectItem.getValue();
        String itemStrValue;
        if (converter == null)
        {
            itemStrValue = itemValue.toString();
        }
        else
        {
            itemStrValue = converter.getAsString(facesContext, uiSelectMany, itemValue);
        }

        //TODO: we must cache this Set!
        Set lookupSet = RendererUtils.getSelectedValuesAsSet(uiSelectMany);

        HtmlRendererUtils.renderCheckbox(facesContext,
                                         uiSelectMany,
                                         itemStrValue,
                                         selectItem.getLabel(),
                                         lookupSet.contains(itemValue));
    }
}
