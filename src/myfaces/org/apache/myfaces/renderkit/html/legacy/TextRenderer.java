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
package net.sourceforge.myfaces.renderkit.html.legacy;

import net.sourceforge.myfaces.renderkit.JSFAttr;
import net.sourceforge.myfaces.renderkit.html.HTML;
import net.sourceforge.myfaces.renderkit.html.HTMLRenderer;
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
 * @author Anton Koinov
 * @version $Revision$ $Date$
 *
 * @deprecated use HtmlTextRenderer
 */
public class TextRenderer
extends HTMLRenderer
{
    //~ Static fields/initializers -----------------------------------------------------------------

    public static final String TYPE = "Text";

    //~ Methods ------------------------------------------------------------------------------------

    public String getRendererType()
    {
        return TYPE;
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException
    {
        if (uiComponent instanceof UIInput)
        {
            renderInput(facesContext, (UIInput) uiComponent);
        }
        else
        {
            renderOutput(facesContext, (UIOutput) uiComponent);
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
        writer.write("\" id=\"");
        writer.write(coumpoundId);
        writer.write('"');

        String currentValue = getStringValue(facesContext, uiInput);

        if (currentValue != null)
        {
            writer.write(" value=\"");
            writer.write(HTMLEncoder.encode(currentValue, false, false));
            writer.write('"');
        }

        HTMLUtil.renderStyleClass(writer, uiInput);
        HTMLUtil.renderHTMLAttributes(writer, uiInput, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiInput, HTML.EVENT_HANDLER_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(writer, uiInput, HTML.INPUT_ATTRIBUTES);
        HTMLUtil.renderHTMLAttribute(writer, uiInput, JSFAttr.MAXLENGTH_ATTR, "maxlength");
        HTMLUtil.renderDisabledOnUserRole(facesContext, uiInput);

        writer.write('>');
    }

    public void renderOutput(FacesContext facesContext, UIOutput uiOutput)
    throws IOException
    {
        ResponseWriter writer = facesContext.getResponseWriter();

        /* FIXME: see HtmlTextRenderer
        StringBuffer   buf = new StringBuffer();
        HTMLUtil.renderStyleClass(buf, uiOutput);
        HTMLUtil.renderHTMLAttributes(buf, uiOutput, HTML.UNIVERSAL_ATTRIBUTES);
        HTMLUtil.renderHTMLAttributes(buf, uiOutput, HTML.EVENT_HANDLER_ATTRIBUTES);

        if (buf.length() > 0)
        {
            writer.write("<span");
            writer.write(buf.toString());
            writer.write('>');
        }
        */

        String text;
        String key = (String) uiOutput.getAttributes().get(JSFAttr.KEY_ATTR);

        if (key != null)
        {
            text =
                BundleUtils.getString(
                    facesContext, (String) uiOutput.getAttributes().get(JSFAttr.BUNDLE_ATTR), key);
        }
        else
        {
            text = getStringValue(facesContext, uiOutput);
        }

        writer.write(HTMLEncoder.encode(text, true, true));

        /* FIXME
        if (buf.length() > 0)
        {
            writer.write("</span>");
        }
        */
    }
}
