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

import net.sourceforge.myfaces.component.CommonComponentAttributes;
import net.sourceforge.myfaces.renderkit.attr.CommonRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.TextRendererAttributes;
import net.sourceforge.myfaces.renderkit.attr.UserRoleAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLEventHandlerAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLInputAttributes;
import net.sourceforge.myfaces.renderkit.html.attr.HTMLUniversalAttributes;
import net.sourceforge.myfaces.renderkit.html.util.HTMLEncoder;
import net.sourceforge.myfaces.renderkit.html.util.HTMLUtil;
import net.sourceforge.myfaces.util.bundle.BundleUtils;

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

    /*
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
    */




    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
        throws IOException
    {
        if (uiComponent instanceof UIInput)
        {
            renderInput(facesContext, (UIInput)uiComponent);
        }
        else
        {
            renderOutput(facesContext, (UIOutput)uiComponent);
        }
    }

    public void renderInput(FacesContext facesContext, UIInput uiInput)
            throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.write("<input type=\"text\"");
        String coumpoundId = uiInput.getClientId(facesContext);
        writer.write(" name=\"");
        writer.write(coumpoundId);
        writer.write("\"");
        writer.write(" id=\"");
        writer.write(coumpoundId);
        writer.write("\"");
        String currentValue = getStringValue(facesContext, uiInput);
        if (currentValue != null)
        {
            writer.write(" value=\"");
            writer.write(HTMLEncoder.encode(currentValue, false, false));
            writer.write("\"");
        }

        HTMLUtil.renderCssClass(writer, uiInput, INPUT_CLASS_ATTR);
        HTMLUtil.renderHTMLAttributes(writer, uiInput, HTML_UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiInput, HTML_EVENT_HANDLER_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiInput, HTML_INPUT_ATTRIBUTES);
        HTMLUtil.renderDisabledOnUserRole(facesContext, uiInput);

        writer.write(">");
    }


    public void renderOutput(FacesContext facesContext, UIOutput uiOutput)
        throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        // TODO: don't render span when empty
        writer.write("<span");
        HTMLUtil.renderCssClass(writer, uiOutput, OUTPUT_CLASS_ATTR);
        HTMLUtil.renderHTMLAttributes(writer, uiOutput, HTML_UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiOutput, HTML_EVENT_HANDLER_ATTRIBUTES);
        writer.write(">");

        String text;
        String key = (String)uiOutput.getAttribute(KEY_ATTR);
        if (key != null)
        {
            text = BundleUtils.getString(facesContext,
                                            (String)uiOutput.getAttribute(BUNDLE_ATTR),
                                            key);
        }
        else
        {
            text = getStringValue(facesContext, uiOutput);
        }

        writer.write(HTMLEncoder.encode(text, true, true));

        writer.write("</span>");
    }

}
