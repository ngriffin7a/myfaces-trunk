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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.convert.impl.StringArrayConverter;
import net.sourceforge.myfaces.renderkit.RendererUtils;
import net.sourceforge.myfaces.renderkit.html.util.SelectItemUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.component.html.HtmlSelectManyCheckboxList;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;


/**
 * DOCUMENT ME!
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 */
public class HtmlCheckboxListRenderer
extends HtmlRenderer
{
    private static final Converter STRINGARRAY_CONVERTER = new StringArrayConverter();
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent,
                                         HtmlSelectManyCheckboxList.class);

        Set selectedValuesSet
                = SelectItemUtil.getSelectedValuesAsStringSet(facesContext, (UISelectMany)uiComponent);

        boolean breakLine = false;

        for (Iterator it = SelectItemUtil.getSelectItems(facesContext, uiComponent);
                    it.hasNext();)
        {
            if (breakLine)
            {
                ResponseWriter writer = facesContext.getResponseWriter();
                writer.write(HTML.BR_ELEM);
            }
            else
            {
                breakLine = true;
            }

            SelectItem selectItem = (SelectItem) it.next();
            String selectItemStrValue = selectItem.getValue().toString();
            boolean checked = selectedValuesSet.contains(selectItemStrValue);

            HtmlRendererUtils.drawCheckbox(
                facesContext,
                uiComponent,
                selectItemStrValue,
                selectItem.getLabel(),
                checked);
        }
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent,
                                         HtmlSelectManyCheckboxList.class);
        HtmlRendererUtils.decodeSelectMany(facesContext,
                                           (HtmlSelectManyCheckboxList)uiComponent);
    }

}
