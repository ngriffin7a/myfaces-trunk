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

import net.sourceforge.myfaces.component.UserRoleUtils;
import net.sourceforge.myfaces.custom.radio.HtmlRadio;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.HtmlRadioRendererBase;
import net.sourceforge.myfaces.renderkit.html.HTML;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;


/**
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.6  2004/06/04 00:26:16  o_rossmueller
 * modified renderes to comply with JSF 1.1
 *
 * Revision 1.5  2004/05/18 14:31:38  manolito
 * user role support completely moved to components source tree
 *
 * Revision 1.4  2004/04/05 09:11:03  manolito
 * extended exception messages
 *
 * Revision 1.3  2004/04/02 13:57:11  manolito
 * extended HtmlSelectManyCheckbox with layout "spread" and custom Checkbox component
 *
 * Revision 1.2  2004/03/31 15:15:59  royalts
 * no message
 *
 * Revision 1.1  2004/03/31 13:26:08  manolito
 * extended radio renderer
 *
 */
public class HtmlRadioRenderer
        extends HtmlRadioRendererBase
{
    //private static final Log log = LogFactory.getLog(HtmlRadioRenderer.class);

    private static final String LAYOUT_SPREAD = "spread";

    public void encodeEnd(FacesContext context, UIComponent component) throws IOException
    {
        if (context == null) throw new NullPointerException("context");
        if (component == null) throw new NullPointerException("component");

        if (component instanceof HtmlRadio)
        {
            renderRadio(context, (HtmlRadio)component);
        }
        else if (component instanceof UISelectOne)
        {
            String layout = getLayout(component);
            if (layout != null && layout.equals(LAYOUT_SPREAD))
            {
                return; //radio inputs are rendered by spread radio components
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


    private void renderRadio(FacesContext facesContext, HtmlRadio radio) throws IOException
    {
        String forAttr = radio.getFor();
        if (forAttr == null)
        {
            throw new IllegalStateException("mandatory attribute 'for'");
        }
        int index = radio.getIndex();
        if (index < 0)
        {
            throw new IllegalStateException("positive index must be given");
        }

        UIComponent uiComponent = radio.findComponent(forAttr);
        if (uiComponent == null)
        {
            throw new IllegalStateException("Could not find component '" + forAttr + "' (calling findComponent on component '" + radio.getClientId(facesContext) + "')");
        }
        if (!(uiComponent instanceof UISelectOne))
        {
            throw new IllegalStateException("UISelectOne expected");
        }

        UISelectOne uiSelectOne = (UISelectOne)uiComponent;
        Converter converter;
        List selectItemList = RendererUtils.getSelectItemList(uiSelectOne);
        if (index >= selectItemList.size())
        {
            throw new IndexOutOfBoundsException("index " + index + " >= " + selectItemList.size());
        }

        try
        {
            converter = RendererUtils.findUIOutputConverter(facesContext, uiSelectOne);
        }
        catch (FacesException e)
        {
            converter = null;
        }

        Object currentValue = uiSelectOne.getValue();
        SelectItem selectItem = (SelectItem)selectItemList.get(index);
        Object itemValue = selectItem.getValue();
        String itemStrValue;
        if (converter == null)
        {
            itemStrValue = itemValue.toString();
        }
        else
        {
            itemStrValue = converter.getAsString(facesContext, uiSelectOne, itemValue);
        }
        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.LABEL_ELEM, uiSelectOne);

        renderRadio(facesContext,
                    uiSelectOne,
                    itemStrValue,
                    selectItem.getLabel(),
                    currentValue == null && itemValue == null ||
                    currentValue != null && currentValue.equals(itemValue), false);
        writer.endElement(HTML.LABEL_ELEM);        
    }


    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        if (!UserRoleUtils.isEnabledOnUserRole(uiComponent))
        {
            return false;
        }
        else
        {
            return super.isDisabled(facesContext, uiComponent);
        }
    }

}
