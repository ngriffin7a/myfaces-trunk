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

import net.sourceforge.myfaces.renderkit.attr.*;
import net.sourceforge.myfaces.renderkit.html.util.CommonAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLInputAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;
import net.sourceforge.myfaces.util.bundle.BundleUtils;
import net.sourceforge.myfaces.component.CommonComponentAttributes;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
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
    implements CommonComponentAttributes,
               CommonRendererAttributes,
               HTMLUniversalAttributes,
               HTMLEventHandlerAttributes,
               HTMLInputAttributes,
               TextRendererAttributes,
               UserRoleAttributes
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
        return uicomponent instanceof UIInput ||
               uicomponent instanceof UIOutput;
    }

    protected void initAttributeDescriptors()
    {
        addAttributeDescriptors(UIInput.TYPE, TLD_HTML_URI, "input_text", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UIInput.TYPE, TLD_HTML_URI, "input_text", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UIInput.TYPE, TLD_HTML_URI, "input_text", HTML_INPUT_ATTRIBUTES);
        addAttributeDescriptors(UIInput.TYPE, TLD_HTML_URI, "input_text", INPUT_TEXT_ATTRIBUTES);
        addAttributeDescriptors(UIInput.TYPE, TLD_HTML_URI, "input_text", USER_ROLE_ATTRIBUTES);

        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_text", HTML_UNIVERSAL_ATTRIBUTES);
        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_text", HTML_EVENT_HANDLER_ATTRIBUTES);
        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_text", OUTPUT_TEXT_ATTRIBUTES);
        addAttributeDescriptors(UIOutput.TYPE, TLD_HTML_URI, "output_text", USER_ROLE_ATTRIBUTES);
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
        String coumpoundId = uiComponent.getClientId(facesContext);
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

        CommonAttributes.renderCssClass(writer, uiComponent, INPUT_CLASS_ATTR);
        CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_UNIVERSAL_ATTRIBUTES);
        CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_EVENT_HANDLER_ATTRIBUTES);
        CommonAttributes.renderHTMLAttributes(writer, uiComponent, HTML_INPUT_ATTRIBUTES);

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
        String text;
        String key = (String)uiComponent.getAttribute(KEY_ATTR);
        if (key != null)
        {
            text = BundleUtils.getString(facesContext,
                                            (String)uiComponent.getAttribute(BUNDLE_ATTR),
                                            key);
        }
        else
        {
            text = getStringValue(facesContext, uiComponent);
        }

        writer.write(HTMLEncoder.encode(text, true, true));
        if (css != null)
        {
            writer.write("</span>");
        }
    }

}
