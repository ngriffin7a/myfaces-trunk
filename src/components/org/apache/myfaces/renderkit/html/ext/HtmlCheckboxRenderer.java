/*
 * Copyright 2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.renderkit.html.ext;

import org.apache.myfaces.component.UserRoleUtils;
import org.apache.myfaces.custom.checkbox.HtmlCheckbox;
import org.apache.myfaces.renderkit.RendererUtils;
import org.apache.myfaces.renderkit.html.HtmlCheckboxRendererBase;

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
 * Revision 1.8  2005/01/18 22:43:05  svieujot
 * Fix some bugs where converter wasn't used to determine selected values.
 * This caused for examples the list, checkbox and radio based components to bug when the backing bean value type is a primitive.
 *
 * Revision 1.7  2004/10/13 11:50:59  matze
 * renamed packages to org.apache
 *
 * Revision 1.6  2004/08/13 15:47:08  manolito
 * No decode for spread checkbox or radio
 *
 * Revision 1.5  2004/07/01 21:53:06  mwessendorf
 * ASF switch
 *
 * Revision 1.4  2004/06/04 00:26:16  o_rossmueller
 * modified renderes to comply with JSF 1.1
 *
 * Revision 1.3  2004/05/18 14:31:38  manolito
 * user role support completely moved to components source tree
 *
 * Revision 1.2  2004/04/05 09:11:03  manolito
 * extended exception messages
 *
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
            renderSingleCheckbox(context, (HtmlCheckbox)component);
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


    private void renderSingleCheckbox(FacesContext facesContext, HtmlCheckbox checkbox) throws IOException
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
            throw new IllegalStateException("Could not find component '" + forAttr + "' (calling findComponent on component '" + checkbox.getClientId(facesContext) + "')");
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
        Set lookupSet = RendererUtils.getSelectedValuesAsSet(facesContext, uiComponent, converter, uiSelectMany);

        renderCheckbox(facesContext,
                       uiSelectMany,
                       itemStrValue,
                       selectItem.getLabel(),
                       lookupSet.contains(itemStrValue), true);
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


    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        if (uiComponent instanceof HtmlCheckbox)
        {
            //nothing to decode
        }
        else
        {
            super.decode(facesContext, uiComponent);
        }
    }
}
