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

import net.sourceforge.myfaces.renderkit.RendererUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
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
    private static final Log log = LogFactory.getLog(HtmlCheckboxListRenderer.class);

    private static final String PAGE_DIRECTION = "PAGE_DIRECTION";
    private static final String LINE_DIRECTION = "LINE_DIRECTION";

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent,
                                         HtmlSelectManyCheckbox.class);

        HtmlSelectManyCheckbox selectMany = (HtmlSelectManyCheckbox)uiComponent;

        String layout = selectMany.getLayout();
        boolean pageDirectionLayout = true; //TODO: Default to PAGE_DIRECTION ?
        if (layout != null)
        {
            if (layout.equals(PAGE_DIRECTION))
            {
                pageDirectionLayout = true;
            }
            else if (layout.equals(LINE_DIRECTION))
            {
                pageDirectionLayout = false;
            }
            else
            {
                log.error("Wrong layout attribute for component " + selectMany.getClientId(facesContext) + ": " + layout);
            }
        }

        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement(HTML.TABLE_ELEM, selectMany);
        if (!pageDirectionLayout) writer.startElement(HTML.TR_ELEM, selectMany);

        Converter converter;
        List selectItemList = RendererUtils.getSelectItemList(selectMany);
        try
        {
            converter = RendererUtils.findUISelectManyConverter(facesContext, selectMany);
        }
        catch (FacesException e)
        {
            log.error("Error finding Converter for component with id " + selectMany.getClientId(facesContext));
            converter = null;
        }

        Set lookupSet = RendererUtils.getSelectedValuesAsSet(selectMany);

        for (Iterator it = selectItemList.iterator(); it.hasNext(); )
        {
            SelectItem selectItem = (SelectItem)it.next();
            Object itemValue = selectItem.getValue();
            String itemStrValue;
            if (converter == null)
            {
                itemStrValue = itemValue.toString();
            }
            else
            {
                itemStrValue = converter.getAsString(facesContext, selectMany, itemValue);
            }

            writer.write("\t\t");
            if (pageDirectionLayout) writer.startElement(HTML.TR_ELEM, selectMany);
            writer.startElement(HTML.TD_ELEM, selectMany);
            HtmlRendererUtils.renderCheckbox(facesContext,
                                           selectMany,
                                           itemStrValue,
                                           selectItem.getLabel(),
                                           lookupSet.contains(itemValue));
            writer.endElement(HTML.TD_ELEM);
            if (pageDirectionLayout) writer.endElement(HTML.TR_ELEM);
        }

        if (!pageDirectionLayout) writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent,
                                         HtmlSelectManyCheckbox.class);
        HtmlRendererUtils.decodeSelectMany(facesContext,
                                           (HtmlSelectManyCheckbox)uiComponent);
    }

}
