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
package net.sourceforge.myfaces.renderkit.html;

import net.sourceforge.myfaces.component.UIInput;
import net.sourceforge.myfaces.renderkit.attr.TextRendererAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UITextEntry;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * DOCUMENT ME!
 * @author Manfred Geiler (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class TextRenderer
        extends HTMLRenderer
        implements TextRendererAttributes
{
    public static final String TYPE = "Text";

    public String getRendererType()
    {
        return TYPE;
    }

    public boolean supportsComponentType(String s)
    {
        return s.equals(UIInput.TYPE) || s.equals(UIOutput.TYPE);
    }

    public boolean supportsComponentType(UIComponent uicomponent)
    {
        return uicomponent instanceof UITextEntry ||   //FINAL: javax.faces.component.UIInput instead!
               uicomponent instanceof UIOutput;
    }



    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        if (uiComponent.getComponentType().equals(UIInput.TYPE))
        {
            renderInput(facesContext, uiComponent);
        }
        else
        {
            renderOutput(facesContext, uiComponent);
        }
    }

    public void renderInput(FacesContext facesContext, UIComponent uiComponent)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<input type=\"text\"");
        String coumpoundId = uiComponent.getCompoundId();
        writer.write(" name=\"");
        writer.write(coumpoundId);
        writer.write("\"");
        writer.write(" id=\"");
        writer.write(coumpoundId);
        writer.write("\"");
        String currentValue = getStringValue(facesContext, uiComponent);
        if (currentValue != null)
        {
            writer.write(" value=\"");
            writer.write(HTMLEncoder.encode(currentValue, false, false));
            writer.write("\"");
        }
        String size = (String)uiComponent.getAttribute(SIZE_ATTR);
        if (size != null)
        {
            writer.write(" size=\"");
            writer.write(size);
            writer.write("\"");
        }
        String maxLength = (String)uiComponent.getAttribute(MAX_LENGTH_ATTR);
        if (maxLength != null)
        {
            writer.write(" maxlength=\"");
            writer.write(maxLength);
            writer.write("\"");
        }
        String css = (String)uiComponent.getAttribute(INPUT_CLASS_ATTR);
        if (css != null)
        {
            writer.write("<span class=\"");
            writer.write(css);
            writer.write("\">");
        }

        writer.write(">");
    }


    public void renderOutput(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        String css = (String)uiComponent.getAttribute(OUTPUT_CLASS_ATTR);
        if (css != null)
        {
            writer.write("<span class=\"");
            writer.write(css);
            writer.write("\">");
        }
        writer.write(HTMLEncoder.encode(getStringValue(facesContext, uiComponent), true, true));
        if (css != null)
        {
            writer.write("</span>");
        }
    }

}
