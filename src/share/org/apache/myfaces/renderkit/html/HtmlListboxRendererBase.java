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

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.component.html.HtmlSelectManyListbox;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import java.io.IOException;


/**
 * @author Thomas Spiegl (latest modification by $Author$)
 * @author Anton Koinov
 * @version $Revision$ $Date$
 * $Log$
 * Revision 1.2  2004/05/26 11:10:12  o_rossmueller
 * fix #959926: styleClass support for selectOneRadio, selectOneList, selectManyList
 *
 * Revision 1.1  2004/05/18 14:31:39  manolito
 * user role support completely moved to components source tree
 *
 */
public class HtmlListboxRendererBase
        extends HtmlRenderer
{
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        RendererUtils.checkParamValidity(facesContext, uiComponent, null);
        String styleClass;

        if (uiComponent instanceof UISelectMany)
        {
            int size;
            if (uiComponent instanceof HtmlSelectManyListbox)
            {
                size = ((HtmlSelectManyListbox)uiComponent).getSize();
                styleClass = ((HtmlSelectManyListbox)uiComponent).getStyleClass();
            }
            else
            {
                Integer i = (Integer)uiComponent.getAttributes().get(JSFAttr.SIZE_ATTR);
                size = i != null ? i.intValue() : 0;
                styleClass = (String)uiComponent.getAttributes().get(JSFAttr.STYLE_CLASS_ATTR);
            }
            ResponseWriter writer = facesContext.getResponseWriter();

            if (styleClass != null && styleClass.length() > 0) {

                writer.startElement(HTML.SPAN_ELEM, uiComponent);
                writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
            }
            HtmlRendererUtils.renderListbox(facesContext,
                                            (UISelectMany)uiComponent,
                                            isDisabled(facesContext, uiComponent),
                                            size);
            if (styleClass != null && styleClass.length() > 0) {
                writer.endElement(HTML.SPAN_ELEM);
            }
        }
        else if (uiComponent instanceof HtmlSelectOneListbox)
        {
            int size;
            if (uiComponent instanceof HtmlSelectOneListbox)
            {
                size = ((HtmlSelectOneListbox)uiComponent).getSize();
                styleClass = ((HtmlSelectOneListbox)uiComponent).getStyleClass();
            }
            else
            {
                Integer i = (Integer)uiComponent.getAttributes().get(JSFAttr.SIZE_ATTR);
                size = i != null ? i.intValue() : 0;
                styleClass = (String)uiComponent.getAttributes().get(JSFAttr.STYLE_CLASS_ATTR);
            }
            ResponseWriter writer = facesContext.getResponseWriter();

            if (styleClass != null && styleClass.length() > 0) {

                writer.startElement(HTML.SPAN_ELEM, uiComponent);
                writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
            }
            HtmlRendererUtils.renderListbox(facesContext,
                                            (UISelectOne)uiComponent,
                                            isDisabled(facesContext, uiComponent),
                                            size);
            if (styleClass != null && styleClass.length() > 0) {
                writer.endElement(HTML.SPAN_ELEM);
            }
        }
        else
        {
            throw new IllegalArgumentException("Unsupported component class " + uiComponent.getClass().getName());
        }
    }
    
    
    protected boolean isDisabled(FacesContext facesContext, UIComponent uiComponent)
    {
        //TODO: overwrite in extended HtmlListboxRenderer and check for enabledOnUserRole
        if (uiComponent instanceof HtmlSelectManyListbox)
        {
            return ((HtmlSelectManyListbox)uiComponent).isDisabled();
        }
        else if (uiComponent instanceof HtmlSelectOneListbox)
        {
            return ((HtmlSelectOneListbox)uiComponent).isDisabled();
        }
        else
        {
            return RendererUtils.getBooleanAttribute(uiComponent, HTML.DISABLED_ATTR, false);
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
