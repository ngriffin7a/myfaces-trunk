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

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.RendererUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectMany;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectManyCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;


/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.5  2004/05/18 14:31:39  manolito
 * user role support completely moved to components source tree
 *
 * Revision 1.4  2004/05/05 18:03:00  o_rossmueller
 * fix #948110: render span element for styleClass
 *
 * Revision 1.3  2004/04/02 13:57:13  manolito
 * extended HtmlSelectManyCheckbox with layout "spread" and custom Checkbox component
 *
 * Revision 1.2  2004/04/02 13:37:49  manolito
 * no message
 *
 */
public class HtmlCheckboxRendererBase
        extends HtmlRenderer
{
    private static final Log log = LogFactory.getLog(HtmlCheckboxRendererBase.class);

    private static final String PAGE_DIRECTION = "pageDirection";
    private static final String LINE_DIRECTION = "lineDirection";

    private static final String EXTERNAL_TRUE_VALUE = "true";

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);
        if (uiComponent instanceof UISelectBoolean)
        {
            renderCheckbox(facesContext,
                           uiComponent,
                           EXTERNAL_TRUE_VALUE,
                           null,
                           ((UISelectBoolean)uiComponent).isSelected());
        }
        else if (uiComponent instanceof UISelectMany)
        {
            renderCheckboxList(facesContext, (UISelectMany)uiComponent);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + uiComponent.getClass().getName());
        }
    }


    public void renderCheckboxList(FacesContext facesContext, UISelectMany selectMany)
            throws IOException
    {
        String layout = getLayout(selectMany);
        boolean pageDirectionLayout = false; //Default to lineDirection
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
        String styleClass = getStyleClass(selectMany);

        if (styleClass != null && styleClass.length() > 0) {
            writer.startElement(HTML.SPAN_ELEM, selectMany);
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
        }


        writer.startElement(HTML.TABLE_ELEM, selectMany);
        if (!pageDirectionLayout) writer.startElement(HTML.TR_ELEM, selectMany);

        Converter converter;
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

        for (Iterator it = RendererUtils.getSelectItemList(selectMany).iterator(); it.hasNext(); )
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
            renderCheckbox(facesContext,
                           selectMany,
                           itemStrValue,
                           selectItem.getLabel(),
                           lookupSet.contains(itemValue));
            writer.endElement(HTML.TD_ELEM);
            if (pageDirectionLayout) writer.endElement(HTML.TR_ELEM);
        }

        if (!pageDirectionLayout) writer.endElement(HTML.TR_ELEM);
        writer.endElement(HTML.TABLE_ELEM);
        if (styleClass != null && styleClass.length() > 0) {
            writer.endElement(HTML.SPAN_ELEM);
        }
    }


    protected String getLayout(UISelectMany selectMany)
    {
        if (selectMany instanceof HtmlSelectManyCheckbox)
        {
            return ((HtmlSelectManyCheckbox)selectMany).getLayout();
        }
        else
        {
            return (String)selectMany.getAttributes().get(JSFAttr.LAYOUT_ATTR);
        }
    }

    protected String getStyleClass(UISelectMany selectMany)
    {
        if (selectMany instanceof HtmlSelectManyCheckbox)
        {
            return ((HtmlSelectManyCheckbox)selectMany).getStyleClass();
        }
        else
        {
            return (String)selectMany.getAttributes().get(JSFAttr.STYLE_CLASS_ATTR);
        }
    }


    protected void renderCheckbox(FacesContext facesContext,
                                  UIComponent uiComponent,
                                  String value,
                                  String label,
                                  boolean checked) throws IOException
    {
        String clientId = uiComponent.getClientId(facesContext);

        ResponseWriter writer = facesContext.getResponseWriter();

        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, HTML.INPUT_TYPE_CHECKBOX, null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        if (checked)
        {
            writer.writeAttribute(HTML.CHECKED_ATTR, HTML.CHECKED_ATTR, null);
        }

        if ((value != null) && (value.length() > 0))
        {
            writer.writeAttribute(HTML.VALUE_ATTR, value, null);
        }

        HtmlRendererUtils.renderHTMLAttributes(writer, uiComponent, HTML.INPUT_PASSTHROUGH_ATTRIBUTES_WITHOUT_DISABLED);
        if (isDisabled(facesContext, uiComponent))
        {
            writer.writeAttribute(HTML.DISABLED_ATTR, Boolean.TRUE, null);
        }

        if ((label != null) && (label.length() > 0))
        {
            writer.write(HTML.NBSP_ENTITY);
            writer.writeText(label, null);
        }

        writer.endElement(HTML.INPUT_ELEM);
    }


    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        //TODO: overwrite in extended HtmlCheckboxRenderer and check for enabledOnUserRole
        if (uiComponent instanceof HtmlSelectBooleanCheckbox)
        {
            return ((HtmlSelectBooleanCheckbox)uiComponent).isDisabled();
        }
        else if (uiComponent instanceof HtmlSelectManyCheckbox)
        {
            return ((HtmlSelectManyCheckbox)uiComponent).isDisabled();
        }
        else
        {
            return RendererUtils.getBooleanAttribute(uiComponent, HTML.DISABLED_ATTR, false);
        }
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);
        if (uiComponent instanceof UISelectBoolean)
        {
            HtmlRendererUtils.decodeUISelectBoolean(facesContext, uiComponent);
        }
        else if (uiComponent instanceof UISelectMany)
        {
            HtmlRendererUtils.decodeUISelectMany(facesContext, uiComponent);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + uiComponent.getClass().getName());
        }
    }


    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue) throws ConverterException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);
        if (uiComponent instanceof UISelectBoolean)
        {
            return submittedValue;
        }
        else if (uiComponent instanceof UISelectMany)
        {
            return RendererUtils.getConvertedUISelectManyValue(facesContext,
                                                               (UISelectMany)uiComponent,
                                                               submittedValue);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + uiComponent.getClass().getName());
        }
    }
}
