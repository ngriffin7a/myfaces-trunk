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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.renderkit.RendererUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.component.html.HtmlSelectManyMenu;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import java.io.IOException;

/**
 * X-CHECKED: tlddoc of h:selectManyListbox
 *
 * @author Manfred Geiler (latest modification by $Author$)
 * @author Thomas Spiegl
 * @version $Revision$ $Date$
 */
public class HtmlMenuRenderer
        extends HtmlRenderer
{
    //private static final Log log = LogFactory.getLog(HtmlMenuRenderer.class);

    public void encodeEnd(FacesContext facesContext, UIComponent component)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, component, null);

        ResponseWriter writer = facesContext.getResponseWriter();

        if (component instanceof UISelectMany)
        {
            String styleClass = getStyleClass((UISelectMany)component);
            if (styleClass != null)
            {
                writer.startElement(HTML.SPAN_ELEM, component);
                writer.writeAttribute(HTML.CLASS_ATTR, styleClass, HTML.STYLE_CLASS_ATTR);
            }

            HtmlRendererUtils.renderMenu(facesContext, (UISelectMany)component);

            if (styleClass != null)
            {
                writer.endElement(HTML.SPAN_ELEM);
            }
        }
        else if (component instanceof UISelectOne)
        {
            String styleClass = getStyleClass((UISelectOne)component);
            if (styleClass != null)
            {
                writer.startElement(HTML.SPAN_ELEM, component);
                writer.writeAttribute(HTML.CLASS_ATTR, styleClass, HTML.STYLE_CLASS_ATTR);
            }

            HtmlRendererUtils.renderMenu(facesContext, (UISelectOne)component);

            if (styleClass != null)
            {
                writer.endElement(HTML.SPAN_ELEM);
            }
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + component.getClass().getName());
        }
    }

    private String getStyleClass(UISelectMany component)
    {
        if (component instanceof HtmlSelectManyMenu)
        {
            return ((HtmlSelectManyMenu)component).getStyleClass();
        }
        else
        {
            return (String)component.getAttributes().get(HTML.STYLE_CLASS_ATTR);
        }
    }

    private String getStyleClass(UISelectOne component)
    {
        if (component instanceof HtmlSelectOneMenu)
        {
            return ((HtmlSelectOneMenu)component).getStyleClass();
        }
        else
        {
            return (String)component.getAttributes().get(HTML.STYLE_CLASS_ATTR);
        }
    }


    public void decode(FacesContext facesContext, UIComponent uiComponent)
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);

        if (uiComponent instanceof UISelectMany)
        {
            HtmlRendererUtils.decodeUISelectMany(facesContext, uiComponent);
        }
        else if (uiComponent instanceof UISelectOne)
        {
            HtmlRendererUtils.decodeUISelectOne(facesContext, uiComponent);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + uiComponent.getClass().getName());
        }
    }

    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue) throws ConverterException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);

        if (uiComponent instanceof UISelectMany)
        {
            return RendererUtils.getConvertedUISelectManyValue(facesContext,
                                                               (UISelectMany)uiComponent,
                                                               submittedValue);
        }
        else if (uiComponent instanceof UISelectOne)
        {
            return RendererUtils.getConvertedUIOutputValue(facesContext,
                                                           (UISelectOne)uiComponent,
                                                           submittedValue);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + uiComponent.getClass().getName());
        }
    }

}
